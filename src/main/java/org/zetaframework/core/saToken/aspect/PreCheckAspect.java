package org.zetaframework.core.satoken.aspect;

import cn.dev33.satoken.exception.NotPermissionException;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaFoxUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.zetaframework.core.satoken.annotation.PreAuth;
import org.zetaframework.core.satoken.annotation.PreCheckPermission;
import org.zetaframework.core.satoken.annotation.PreCheckRole;
import org.zetaframework.core.satoken.annotation.PreMode;
import org.zetaframework.core.satoken.properties.IgnoreProperties;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * 权限认证 切面
 *
 * @author gcc
 */
@Aspect
@Component
public class PreCheckAspect {
    private final AntPathMatcher ANT_PATH_MATCHER = new AntPathMatcher();
    private static final String POINTCUT_SIGN =
            "@within(org.zetaframework.core.satoken.annotation.PreCheckPermission) || " +
            "@annotation(org.zetaframework.core.satoken.annotation.PreCheckPermission) || " +
            "@within(org.zetaframework.core.satoken.annotation.PreCheckRole) || " +
            "@annotation(org.zetaframework.core.satoken.annotation.PreCheckRole)";

    @Autowired
    private IgnoreProperties ignoreProperties;

    @Around(POINTCUT_SIGN)
    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        String replace = "";
        boolean enabled = true;

        // 获取父类上的@PreAuth注解
        Class<?> declaringClass = method.getDeclaringClass();
        if (declaringClass.isAnnotationPresent(PreAuth.class)) {
            PreAuth declaringAnnotation = declaringClass.getAnnotation(PreAuth.class);
            enabled = declaringAnnotation.enabled();
            replace = declaringAnnotation.replace();
        }

        // 获取类上面的@PreAuth注解
        Class<?> clazz = joinPoint.getTarget().getClass();
        if (clazz.isAnnotationPresent(PreAuth.class)) {
            PreAuth clazzAnnotation = clazz.getAnnotation(PreAuth.class);
            enabled = clazzAnnotation.enabled();
            replace = clazzAnnotation.replace();
        }

        // 判断路由是否放行路由
        if (isIgnoreToken()) {
            enabled = false;
        }

        // 鉴权
        if (enabled) {
            checkMethodAnnotation(replace, method);
        }

        return joinPoint.proceed();
    }


    /**
     * 检查方法上的注解
     * @param method
     */
    private void checkMethodAnnotation(String replace, Method method) {
        // 先校验 Method 所属 Class 上的注解
        validateAnnotation(replace, method.getDeclaringClass());
        // 再校验 Method 上的注解
        validateAnnotation(replace, method);
    }

    /**
     * 校验注解
     * @param replace String
     * @param element AnnotatedElement
     */
    private void validateAnnotation(String replace, AnnotatedElement element) {
        PreCheckPermission checkPermission = element.getAnnotation(PreCheckPermission.class);
        if (checkPermission != null && checkPermission.enabled()) {
            checkByAnnotation(replace, checkPermission);
        }
        PreCheckRole checkRole = element.getAnnotation(PreCheckRole.class);
        if (checkRole != null && checkRole.enabled()) {
            checkByAnnotation(checkRole);
        }
    }


    /**
     * 校验PreCheckPermission注解
     *
     * @param replace String
     * @param annotation PreCheckPermission
     */
    private void checkByAnnotation(String replace, PreCheckPermission annotation) {
        String[] value = annotation.value();
        value = handleValue(replace, value);
        try {
            if (annotation.mode() == PreMode.AND) {
                StpUtil.checkPermissionAnd(value);
            } else {
                StpUtil.checkPermissionOr(value);
            }
        } catch (NotPermissionException e) {
            // 权限认证未通过，再开始角色认证
            if (ArrayUtil.isNotEmpty(annotation.orRole())) {
                for (String role : annotation.orRole()) {
                    String[] roleArray = SaFoxUtil.convertStringToArray(role);
                    // 某一项role认证通过，则可以提前退出了，代表通过
                    if (StpUtil.hasRoleAnd(roleArray)) {
                        return;
                    }
                }
            }
            throw e;
        }
    }

    /**
     * 校验PreCheckRole注解
     *
     * @param annotation PreCheckRole
     */
    private void checkByAnnotation(PreCheckRole annotation) {
        String[] value = annotation.value();
        if (annotation.mode() == PreMode.AND) {
            StpUtil.checkRoleAnd(value);
        } else {
            StpUtil.checkRoleOr(value);
        }
    }

    /**
     * 处理权限码
     *
     * 例如：["{}:add", "{}:save"] => ["sys:user:add", "sys:user:save"]
     * @param replace String
     * @param value String[]
     * @return String[]
     */
    private String[] handleValue(String replace, String[] value) {
        String[] result = value.clone();
        if (StrUtil.isNotBlank(replace)) {
            // ["{}:add", "{}:save"] => ["sys:user:add", "sys:user:save"]
            result = Arrays.stream(result)
                    .map(it -> StrUtil.format(it, replace))
                    .collect(Collectors.toList())
                    .toArray(new String[value.length]);
        }
        return result;
    }

    /**
     * 是否是saToken放行路由
     *
     * @return boolean
     */
    private Boolean isIgnoreToken() {
        // 获取当前访问的路由。获取不到直接return false
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) return false;

        HttpServletRequest request = attributes.getRequest();
        String path = request.getRequestURI();

        // 判断当前访问的路由，是否是saToken放行路由.
        return ignoreProperties.getNotMatchUrl().stream().anyMatch(url -> ANT_PATH_MATCHER.match(url, path));
    }

}
