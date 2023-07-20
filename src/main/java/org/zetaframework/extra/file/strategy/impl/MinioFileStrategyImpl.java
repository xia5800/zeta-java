package org.zetaframework.extra.file.strategy.impl;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import io.minio.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.zetaframework.extra.file.enums.FileStorageType;
import org.zetaframework.extra.file.model.FileDeleteParam;
import org.zetaframework.extra.file.model.FileInfo;
import org.zetaframework.extra.file.properties.FileProperties;

import java.io.InputStream;

/**
 * Minio 文件管理策略实现
 * @author gcc
 */
@Component("MINIO")
public class MinioFileStrategyImpl extends AbstractFileStrategy {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Lazy
    @Autowired
    private MinioClient minioClient;

    private final FileProperties fileProperties;
    public MinioFileStrategyImpl(FileProperties fileProperties) {
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
        FileProperties.Minio minio = fileProperties.getMinio();
        // 可以注释掉这个判断，因为配置minio客户端的时候已经判断过minio.bucket是否为空了
        Assert.notBlank(minio.getBucket(), "请配置Minio存储桶bucket");

        // 文件桶不存在则创建
        try {
            if (!minioClient.bucketExists(BucketExistsArgs.builder().bucket(minio.getBucket()).build())) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(minio.getBucket()).build());
            }
        } catch (Exception e) {
            logger.error("minio创建存储桶失败", e);
            return;
        }

        // 文件上传
        try {
            minioClient.putObject(PutObjectArgs.builder()
                    .bucket(minio.getBucket())
                    .contentType(file.getContentType())
                    .object(fileInfo.getPath())
                    .stream(file.getInputStream(), file.getSize(), PutObjectArgs.MIN_MULTIPART_SIZE)
                    .build());
            logger.info("minio文件上传成功");
        } catch (Exception e) {
            logger.error("mino文件上传失败", e);
        }

        // 设置文件详情
        String url = StrUtil.format("{}{}", minio.getUrlPrefix(), fileInfo.getPath());
        fileInfo.setBucket(minio.getBucket());
        fileInfo.setUrl(url);
        fileInfo.setStorageType(FileStorageType.MINIO.name());
    }

    /**
     * 获取文件输入流
     *
     * @param path
     */
    @Override
    public InputStream getObject(String path) {
        FileProperties.Minio minio = fileProperties.getMinio();
        try {
            // 获取文件
            GetObjectArgs getObjectArgs = GetObjectArgs.builder()
                    .bucket(minio.getBucket())
                    .object(path)
                    .build();
            return minioClient.getObject(getObjectArgs);
        } catch (Exception e) {
            logger.error("minio获取文件异常:", e);
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
        FileProperties.Minio minio = fileProperties.getMinio();
        try {
            // 删除文件
            RemoveObjectArgs removeObjectArgs = RemoveObjectArgs.builder()
                    .bucket(minio.getBucket())
                    .object(param.getPath())
                    .build();
            minioClient.removeObject(removeObjectArgs);
            return true;
        } catch (Exception e) {
            logger.error("minio删除文件异常:", e);
        }
        return false;
    }

}
