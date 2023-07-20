package org.zetaframework.extra.file.properties;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.zetaframework.extra.file.enums.FileStorageType;

/**
 * 文件存储配置
 *
 * @author gcc
 */
@ConfigurationProperties(prefix = FileProperties.PREFIX)
public class FileProperties {
    public static final String PREFIX = "zeta.file";

    /** 文件存储策略，如不配置则默认为上传文件到本地 */
    FileStorageType storageType = FileStorageType.LOCAL;

    /** 本地存储配置 */
    private Local local = new Local();

    /** minio存储配置 */
    private Minio minio = new Minio();

    /** 阿里云存储配置 */
    private Ali ali = new Ali();

    /** 可以继续扩展，想接入什么oss就仿造着写 */




    /**
     * 本地存储
     */
    public class Local {
        /** 根文件夹 */
        private String bucket;
        /**
         * 本地访问地址+端口号
         * eg: http://127.0.0.1
         * 说明：
         * 本地存储策略的endpoint值，仅用于拼接得到文件的访问地址
         * 不能直接访问该地址获取文件，需要配置nginx才行
         */
        private String endpoint;
        /** 文件存放路径 */
        private String storagePath;

        /**
         * 获取文件访问URL前缀
         */
        public String getUrlPrefix() {
            Assert.notBlank(endpoint, "请配置本地文件访问域名endpoint");
            Assert.notBlank(bucket, "请配置本地文件根目录bucket");

            // 去除endpoint末尾的“/”
            if (endpoint.endsWith("/")) {
                endpoint = endpoint.substring(0, endpoint.length() - 1);
            }

            String urlPrefix = StrUtil.format("{}/{}", endpoint, bucket);
            // 如果没有以“/”结尾则添加上去
            if (!urlPrefix.endsWith("/")) {
                urlPrefix += "/";
            }
            return urlPrefix;
        }

        public String getBucket() {
            return bucket;
        }

        public void setBucket(String bucket) {
            this.bucket = bucket;
        }

        public String getEndpoint() {
            return endpoint;
        }

        public void setEndpoint(String endpoint) {
            this.endpoint = endpoint;
        }

        public String getStoragePath() {
            return storagePath;
        }

        public void setStoragePath(String storagePath) {
            this.storagePath = storagePath;
        }
    }

    /**
     * minio存储
     */
    public class Minio {
        /** 桶 */
        private String bucket;
        /** minio地址+端口号 eg: https://play.min.io */
        private String endpoint;
        /** minio用户名 */
        private String accessKey;
        /** minio密码 */
        private String secretKey;

        /**
         * 获取文件访问URL前缀
         */
        public String getUrlPrefix() {
            Assert.notBlank(bucket, "请配置Minio存储桶bucket");
            Assert.notBlank(endpoint, "请配置Minio访问域名endpoint");

            // 去除endpoint末尾的“/”
            if (endpoint.endsWith("/")) {
                endpoint = endpoint.substring(0, endpoint.length() - 1);
            }

            String urlPrefix = StrUtil.format("{}/{}", endpoint, bucket);
            // 如果没有以“/”结尾则添加上去
            if (!urlPrefix.endsWith("/")) {
                urlPrefix += "/";
            }
            return urlPrefix;
        }

        public String getBucket() {
            return bucket;
        }

        public void setBucket(String bucket) {
            this.bucket = bucket;
        }

        public String getEndpoint() {
            return endpoint;
        }

        public void setEndpoint(String endpoint) {
            this.endpoint = endpoint;
        }

        public String getAccessKey() {
            return accessKey;
        }

        public void setAccessKey(String accessKey) {
            this.accessKey = accessKey;
        }

        public String getSecretKey() {
            return secretKey;
        }

        public void setSecretKey(String secretKey) {
            this.secretKey = secretKey;
        }
    }


    /**
     * 阿里云存储
     *
     * 说明：
     * 阿里云账号AccessKey拥有所有API的访问权限，风险很高。
     * 强烈建议您创建并使用RAM用户进行API访问或日常运维
     */
    public class Ali {
        /** 桶 */
        private String bucket;
        /** 阿里云oss访问域名 */
        private String endpoint;
        /** 阿里云ak */
        private String accessKeyId;
        /** 阿里云sk */
        private String accessKeySecret;

        /**
         * 获取文件访问URL前缀
         *
         * 说明：
         * URL的格式为https://BucketName.Endpoint/ObjectName
         * 前缀为https://BucketName.Endpoint/
         */
        public String getUrlPrefix() {
            Assert.notBlank(bucket, "请配置阿里云oss存储桶bucket");
            Assert.notBlank(endpoint, "请配置阿里云oss访问域名endpoint");

            String urlPrefix = StrUtil.format("https://{}.{}", bucket, endpoint);
            // 如果没有以“/”结尾则添加上去
            if (!urlPrefix.endsWith("/")) {
                urlPrefix += "/";
            }
            return urlPrefix;
        }

        public String getBucket() {
            return bucket;
        }

        public void setBucket(String bucket) {
            this.bucket = bucket;
        }

        public String getEndpoint() {
            return endpoint;
        }

        public void setEndpoint(String endpoint) {
            this.endpoint = endpoint;
        }

        public String getAccessKeyId() {
            return accessKeyId;
        }

        public void setAccessKeyId(String accessKeyId) {
            this.accessKeyId = accessKeyId;
        }

        public String getAccessKeySecret() {
            return accessKeySecret;
        }

        public void setAccessKeySecret(String accessKeySecret) {
            this.accessKeySecret = accessKeySecret;
        }
    }

    public FileStorageType getStorageType() {
        return storageType;
    }

    public void setStorageType(FileStorageType storageType) {
        this.storageType = storageType;
    }

    public Local getLocal() {
        return local;
    }

    public void setLocal(Local local) {
        this.local = local;
    }

    public Minio getMinio() {
        return minio;
    }

    public void setMinio(Minio minio) {
        this.minio = minio;
    }

    public Ali getAli() {
        return ali;
    }

    public void setAli(Ali ali) {
        this.ali = ali;
    }
}
