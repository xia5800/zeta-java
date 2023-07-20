package org.zetaframework.extra.file.model;

/**
 * 上传文件的信息
 *
 * @author gcc
 */
public class FileInfo {

    /** 原始文件名 */
    private String originalFileName;

    /** 唯一文件名 */
    private String uniqueFileName;

    /** 内容类型 */
    private String contentType;

    /** 文件大小 */
    private Long size;

    /** 后缀 */
    private String suffix;

    /** 文件类型 */
    private String fileType;

    /** 文件访问地址 */
    private String url;

    /** 文件相对地址 */
    private String path;

    /** 存储类型 */
    private String storageType;

    /**
     * 桶
     * 说明：
     * 存储类型为本地的时候，对应的是文件夹的名称
     * 存储类型为oss的时候，对应的是oss的bucket名称
     */
    private String bucket;

    /**
     * 业务类型
     *
     * 说明：
     * 1.影响文件相对路径和文件的访问地址
     * 2.值尽量用英文吧
     *
     * 举例：
     * 你上传的是用户头像，那么bizType就可以是user_avatar、avatar等
     * 上传的是订单模块的商品图片，那么bizType就可以是order、order_goods
     */
    private String bizType;

    public String getOriginalFileName() {
        return originalFileName;
    }

    public void setOriginalFileName(String originalFileName) {
        this.originalFileName = originalFileName;
    }

    public String getUniqueFileName() {
        return uniqueFileName;
    }

    public void setUniqueFileName(String uniqueFileName) {
        this.uniqueFileName = uniqueFileName;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getStorageType() {
        return storageType;
    }

    public void setStorageType(String storageType) {
        this.storageType = storageType;
    }

    public String getBucket() {
        return bucket;
    }

    public void setBucket(String bucket) {
        this.bucket = bucket;
    }

    public String getBizType() {
        return bizType;
    }

    public void setBizType(String bizType) {
        this.bizType = bizType;
    }
}
