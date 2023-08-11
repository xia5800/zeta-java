package com.zeta.system.controller;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.StrUtil;
import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import com.wf.captcha.SpecCaptcha;
import com.zeta.common.cacheKey.CaptchaStringCacheKey;
import com.zeta.system.model.entity.SysUser;
import com.zeta.system.model.enums.UserStateEnum;
import com.zeta.system.model.param.LoginParam;
import com.zeta.system.model.result.CaptchaResult;
import com.zeta.system.model.result.LoginResult;
import com.zeta.system.service.ISysUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.zetaframework.base.controller.SuperSimpleController;
import org.zetaframework.base.result.ApiResult;
import org.zetaframework.core.log.enums.LoginStateEnum;
import org.zetaframework.core.log.event.LoginEvent;
import org.zetaframework.core.log.model.LoginLogDTO;
import org.zetaframework.core.redis.annotation.Limit;
import org.zetaframework.core.utils.ContextUtil;
import org.zetaframework.extra.crypto.helper.AESHelper;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

/**
 * 登录认证
 * @author gcc
 */
@RequiredArgsConstructor
@ApiSupport(order = 1)
@Api(tags = "登录认证")
@RestController
@RequestMapping("/api")
public class MainController extends SuperSimpleController<ISysUserService, SysUser> {

    private final ApplicationContext applicationContext;
    private final CaptchaStringCacheKey captchaCacheKey;
    private final AESHelper aesHelper;

    @Value("${spring.profiles.active:prod}")
    private String env;

    /**
     * 用户登录
     *
     * @param param 登录参数
     * @param request HttpServletRequest
     * @return ApiResult<LoginResult>
     */
    @ApiOperation(value = "登录")
    @PostMapping("/login")
    public ApiResult<LoginResult> login(@RequestBody @Validated LoginParam param, HttpServletRequest request) {
        // 验证验证码
        String verifyCode = captchaCacheKey.get(param.getKey());
        if (StrUtil.isBlank(verifyCode)) return fail("验证码过期");
        if (!param.getCode().equalsIgnoreCase(verifyCode)) {
            return fail("验证码错误");
        }
        captchaCacheKey.delete(param.getKey());

        // 查询用户
        SysUser user = service.getByAccount(param.getAccount());
        if (user == null) return fail("用户不存在");
        // 设置用户id，方便记录日志的时候设置创建人
        ContextUtil.setUserId(user.getId());

        // 密码解密
        String password = "";
        try {
            password = aesHelper.decryptStr(param.getPassword());
        } catch (Exception e) {
            logger.warn("登录密码解密失败，pwd:{}", param.getPassword());
        }

        // 比较密码
        if (!service.comparePassword(password, user.getPassword())) {
            LoginEvent event = new LoginEvent(LoginLogDTO.loginFail(param.getAccount(), LoginStateEnum.ERROR_PWD, request));
            applicationContext.publishEvent(event);
            // 密码不正确
            return fail(LoginStateEnum.ERROR_PWD.getDesc());
        }
        // 判断用户状态
        if (Objects.equals(user.getState(), UserStateEnum.FORBIDDEN.getCode())) {
            LoginEvent event = new LoginEvent(LoginLogDTO.loginFail(param.getAccount(), LoginStateEnum.FAIL, "用户被禁用，无法登录", request));
            applicationContext.publishEvent(event);
            return fail("用户被禁用，无法登录");
        }

        // 踢人下线并登录
        StpUtil.kickout(user.getId());
        StpUtil.login(user.getId());

        // 登录日志
        applicationContext.publishEvent(new LoginEvent(LoginLogDTO.loginSuccess(
                param.getAccount(), request
        )));

        // 构造登录返回结果
        return success(new LoginResult(StpUtil.getTokenName(), StpUtil.getTokenValue()));
    }

    /**
     * 注销登录
     *
     * @param request HttpServletRequest
     * @return ApiResult<Boolean>
     */
    @ApiOperation(value = "注销登录")
    @GetMapping("/logout")
    public ApiResult<Boolean> logout(HttpServletRequest request) {
        SysUser user = service.getById(StpUtil.getLoginIdAsLong());
        if (user == null) return fail("用户异常");

        applicationContext.publishEvent(new LoginEvent(LoginLogDTO.loginFail(
                user.getAccount(), LoginStateEnum.LOGOUT, request
        )));

        // 注销登录
        StpUtil.logout();
        return success(true);
    }

    /**
     * 图形验证码
     *
     * 说明：
     * 限流规则一分钟十次调用
     */
    @Limit(name = "验证码接口限流", count = 10, describe = "您的操作过于频繁，请稍后再试")
    @ApiOperation(value = "图形验证码")
    @GetMapping("/captcha")
    public ApiResult<CaptchaResult> captcha() {
        long key = System.currentTimeMillis();

        // 验证码值缓存到redis, 5分钟有效(ps: 缓存有效时间请看CaptchaStringCacheKey#getExpire)
        SpecCaptcha specCaptcha = new SpecCaptcha(120, 40, 5);
        captchaCacheKey.set(key, specCaptcha.text());

        // 如果生产环境，不返回验证码的值
        return "prod".equals(env)
                ? success(new CaptchaResult(key, specCaptcha.toBase64()))
                : success(new CaptchaResult(key, specCaptcha.toBase64(), specCaptcha.text()));
    }

}
