package org.zetaframework.extra.file.strategy.impl;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.OSSObject;
import com.aliyun.oss.model.PutObjectRequest;
import com.aliyun.oss.model.PutObjectResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.zetaframework.core.utils.JSONUtil;
import org.zetaframework.extra.file.enums.FileStorageType;
import org.zetaframework.extra.file.model.FileDeleteParam;
import org.zetaframework.extra.file.model.FileInfo;
import org.zetaframework.extra.file.properties.FileProperties;

import java.io.InputStream;

/**
 * 阿里云OSS 文件管理策略实现
 *
 * 参考文档：[阿里云对象存储 OSS](https://help.aliyun.com/document_detail/52834.html)
 * @author gcc
 */
@Component("ALI_OSS")
public class AliFileStrategyImpl extends AbstractFileStrategy {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final FileProperties fileProperties;
    public AliFileStrategyImpl(FileProperties fileProperties) {
        this.fileProperties = fileProperties;
    }

    /**
     * 策略类实现该方法进行文件上传
     *
     * @param file     文件对象
     * @param fileInfo 文件详情
     */
    @Override
    protected void onUpload(MultipartFile file, FileInfo fileInfo) {
        FileProperties.Ali ali = fileProperties.getAli();
        Assert.notBlank(ali.getBucket(), "请配置阿里云oss存储桶bucket");
        Assert.notBlank(ali.getEndpoint(), "请配置阿里云oss访问域名endpoint");
        Assert.notBlank(ali.getAccessKeyId(), "请配置阿里云accessKeyId");
        Assert.notBlank(ali.getAccessKeySecret(), "请配置阿里云accessKeySecret");

        // 创建阿里oss客户端
        OSS ossClient = new OSSClientBuilder().build(ali.getEndpoint(), ali.getAccessKeyId(), ali.getAccessKeySecret());

        // 文件桶不存在则创建
        if (!ossClient.doesBucketExist(ali.getBucket())) {
            ossClient.createBucket(ali.getBucket());
        }

        // 文件上传
        try {
            PutObjectRequest request = new PutObjectRequest(ali.getBucket(), fileInfo.getPath(), file.getInputStream());
            PutObjectResult response = ossClient.putObject(request);
            logger.info("阿里云oss文件上传结果：{}", JSONUtil.toJsonStr(response));
        } catch (Exception e) {
            logger.error("阿里云oss文件上传失败", e);
        }

        ossClient.shutdown();

        // 设置文件详情
        String url = StrUtil.format("{}{}", ali.getUrlPrefix(), fileInfo.getPath());
        fileInfo.setBucket(ali.getBucket());
        fileInfo.setUrl(url);
        fileInfo.setStorageType(FileStorageType.ALI_OSS.name());
    }

    /**
     * 获取文件输入流
     *
     * @param path
     */
    @Override
    public InputStream getObject(String path) {
        FileProperties.Ali ali = fileProperties.getAli();

        OSS ossClient = new OSSClientBuilder().build(ali.getEndpoint(), ali.getAccessKeyId(), ali.getAccessKeySecret());
        try {
            // 获取文件
            OSSObject ossObject = ossClient.getObject(ali.getBucket(), path);

            return ossObject.getObjectContent();
        } catch (Exception e) {
            ossClient.shutdown();
            logger.error("阿里云oss获取文件异常：", e);
        }
        return null;
    }

    /**
     * 删除文件
     *
     * @param param 文件删除参数
     */
    @Override
    public Boolean delete(FileDeleteParam param) {
        FileProperties.Ali ali = fileProperties.getAli();
        try {
            // 获取文件
            OSS ossClient = new OSSClientBuilder().build(ali.getEndpoint(), ali.getAccessKeyId(), ali.getAccessKeySecret());
            ossClient.deleteObject(ali.getBucket(), param.getPath());
            ossClient.shutdown();

            return true;
        } catch (Exception e) {
            logger.error("阿里云oss删除文件异常：", e);
        }
        return false;
    }

}
