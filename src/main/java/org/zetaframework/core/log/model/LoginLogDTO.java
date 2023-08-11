package org.zetaframework.core.log.model;

import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.servlet.ServletUtil;
import cn.hutool.http.useragent.UserAgent;
import cn.hutool.http.useragent.UserAgentUtil;
import org.zetaframework.core.log.enums.LoginStateEnum;
import org.zetaframework.core.utils.ContextUtil;
import org.zetaframework.core.utils.IpAddressUtil;

import javax.servlet.http.HttpServletRequest;

/**
 * 登录日志
 *
 * @author gcc
 */
public class LoginLogDTO {

    /** 状态 {@link LoginStateEnum} */
    private String state;

    /** 用户id */
    Long userId;

    /** 账号 */
    private String account;

    /** 操作系统 */
    private String os;

    /** 设备名称 */
    private String device;

    /** 浏览器类型 */
    private String browser;

    /** ip地址 */
    private String ip;

    /** ip所在地区 */
    private String ipRegion;

    /** 备注 */
    private String comments;


    /**
     * 构造登录日志
     * @param account 账号
     * @param state 状态
     * @param comments 备注
     * @param request HttpServletRequest
     * @return LoginLogDTO
     */
    public static LoginLogDTO build(String account, String state, String comments, HttpServletRequest request) {
        UserAgent ua = UserAgentUtil.parse(ServletUtil.getHeaderIgnoreCase(request, "User-Agent"));
        String ip = ServletUtil.getClientIP(request);
        String ipRegion = StrUtil.EMPTY;
        if (StrUtil.isNotEmpty(ip)) {
            ipRegion = IpAddressUtil.search(ip);
        }

        LoginLogDTO loginLogDTO = new LoginLogDTO();
        loginLogDTO.setState(state);
        loginLogDTO.setUserId(ContextUtil.getUserId());
        loginLogDTO.setAccount(account);
        loginLogDTO.setOs(ua.getPlatform().getName());
        loginLogDTO.setDevice(ua.getOs().getName());
        loginLogDTO.setBrowser(ua.getBrowser().getName());
        loginLogDTO.setIp(ip);
        loginLogDTO.setIpRegion(ipRegion);
        loginLogDTO.setComments(comments);
        return loginLogDTO;
    }

    /**
     * 构造登录成功日志
     *
     * @param account 账号
     * @param request HttpServletRequest
     * @return LoginLogDTO
     */
    public static LoginLogDTO loginSuccess(String account, HttpServletRequest request) {
        return build(account, LoginStateEnum.SUCCESS.name(), LoginStateEnum.SUCCESS.getDesc(), request);
    }

    /**
     * 构造登录成功日志
     *
     * @param account 账号
     * @param comments 备注
     * @param request HttpServletRequest
     * @return LoginLogDTO
     */
    public static LoginLogDTO loginSuccess(String account, String comments, HttpServletRequest request) {
        return build(account, LoginStateEnum.SUCCESS.name(), comments, request);
    }

    /**
     * 构造登录失败日志
     *
     * @param account 账号
     * @param state 状态 {@link LoginStateEnum}
     * @param request HttpServletRequest
     * @return LoginLogDTO
     */
    public static LoginLogDTO loginFail(String account, LoginStateEnum state, HttpServletRequest request) {
        return build(account, state.name(), state.getDesc(), request);
    }

    /**
     * 构造登录失败日志
     *
     * @param account 账号
     * @param state 状态  {@link LoginStateEnum}
     * @param comments 备注
     * @param request HttpServletRequest
     * @return LoginLogDTO
     */
    public static LoginLogDTO loginFail(String account, LoginStateEnum state, String comments, HttpServletRequest request) {
        return build(account, state.name(), comments, request);
    }


    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public String getBrowser() {
        return browser;
    }

    public void setBrowser(String browser) {
        this.browser = browser;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getIpRegion() {
        return ipRegion;
    }

    public void setIpRegion(String ipRegion) {
        this.ipRegion = ipRegion;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    @Override
    public String toString() {
        return "LoginLogDTO{" +
                "state='" + state + '\'' +
                ", userId=" + userId +
                ", account='" + account + '\'' +
                ", os='" + os + '\'' +
                ", device='" + device + '\'' +
                ", browser='" + browser + '\'' +
                ", ip='" + ip + '\'' +
                ", ipRegion='" + ipRegion + '\'' +
                ", comments='" + comments + '\'' +
                '}';
    }
}
