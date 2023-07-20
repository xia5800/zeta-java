package org.zetaframework.core.swagger.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * swagger配置参数
 *
 * @author gcc
 */
@ConfigurationProperties(prefix = SwaggerProperties.PREFIX)
public class SwaggerProperties {
    public static final String PREFIX = "zeta.swagger";

    /** 标题 */
    private String title = "在线文档";

    /** 自定义组名 */
    private String group = "default";

    /** 描述 */
    private String description = "";

    /** 版本 */
    private String version = "1.0";

    /** 作者信息 */
    private Contact contact = new Contact();

    /** swagger会解析的包路径 */
    private String basePackage = "com.zeta";

    /** 许可证  */
    private String license = "LICENSE";

    /** 许可证URL */
    private String licenseUrl = "http://swagger.io";

    /** 服务URL */
    private String termsOfServiceUrl = "";

    /** host */
    private String host = "";


    public class Contact {
        /** 联系人 */
        private String name = "";
        /** 联系人url */
        private String url = "";
        /** 联系人email */
        private String email = "";

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Contact getContact() {
        return contact;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }

    public String getBasePackage() {
        return basePackage;
    }

    public void setBasePackage(String basePackage) {
        this.basePackage = basePackage;
    }

    public String getLicense() {
        return license;
    }

    public void setLicense(String license) {
        this.license = license;
    }

    public String getLicenseUrl() {
        return licenseUrl;
    }

    public void setLicenseUrl(String licenseUrl) {
        this.licenseUrl = licenseUrl;
    }

    public String getTermsOfServiceUrl() {
        return termsOfServiceUrl;
    }

    public void setTermsOfServiceUrl(String termsOfServiceUrl) {
        this.termsOfServiceUrl = termsOfServiceUrl;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }
}
