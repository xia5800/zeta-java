package org.zetaframework.core.log.model;

import org.zetaframework.core.log.enums.LogTypeEnum;

/**
 * 系统日志
 *
 * @author gcc
 */
public class SysLogDTO {

    /**  日志类型 see: {@link LogTypeEnum} */
    private String type;

    /** 操作描述 */
    private String description;

    /** 请求地址 */
    private String url;

    /** 请求方式 */
    private String httpMethod;

    /** 类路径 */
    private String classPath;

    /** 请求参数 */
    private String params;

    /** 返回值 */
    private String result;

    /** 异常描述 */
    private String exception;

    /** 操作耗时 */
    private Long spendTime;

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


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getHttpMethod() {
        return httpMethod;
    }

    public void setHttpMethod(String httpMethod) {
        this.httpMethod = httpMethod;
    }

    public String getClassPath() {
        return classPath;
    }

    public void setClassPath(String classPath) {
        this.classPath = classPath;
    }

    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getException() {
        return exception;
    }

    public void setException(String exception) {
        this.exception = exception;
    }

    public Long getSpendTime() {
        return spendTime;
    }

    public void setSpendTime(Long spendTime) {
        this.spendTime = spendTime;
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
}
