package org.zetaframework.extra.file.strategy.impl;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.zetaframework.extra.file.enums.FileStorageType;
import org.zetaframework.extra.file.model.FileDeleteParam;
import org.zetaframework.extra.file.model.FileInfo;
import org.zetaframework.extra.file.properties.FileProperties;
import org.zetaframework.extra.file.util.FileUtils;

import java.io.InputStream;
import java.nio.file.Paths;

/**
 * 本地 文件管理策略实现
 *
 * @author gcc
 */
@Component("LOCAL")
public class LocalFileStrategyImpl extends AbstractFileStrategy {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final FileProperties fileProperties;
    public LocalFileStrategyImpl(FileProperties fileProperties) {
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
        FileProperties.Local local = fileProperties.getLocal();
        Assert.notBlank(local.getBucket(), "请配置本地文件根目录bucket");
        Assert.notBlank(local.getEndpoint(), "请配置本地文件访问域名endpoint");
        Assert.notBlank(local.getStoragePath(), "请配置本地文件存放路径storagePath");

        // 获取文件在服务器上的绝对路径
        String absolutePath = Paths.get(local.getStoragePath(), local.getBucket(), fileInfo.getPath()).toString();

        // 存储文件
        FileUtils.writeFile(file, absolutePath);
        logger.info("上传本地文件成功");

        // 设置文件详情
        String url = StrUtil.format("{}{}", local.getUrlPrefix(), fileInfo.getPath());
        fileInfo.setBucket(local.getBucket());
        fileInfo.setUrl(url);
        fileInfo.setStorageType(FileStorageType.LOCAL.name());
    }

    /**
     * 获取文件输入流
     *
     * @param path
     */
    @Override
    public InputStream getObject(String path) {
        // 获取文件在服务器上的绝对路径
        FileProperties.Local local = fileProperties.getLocal();
        String absolutePath = Paths.get(local.getStoragePath(), local.getBucket(), path).toString();

        try {
            // 获取本地文件
            return FileUtils.getFile(absolutePath);
        } catch (Exception e) {
            logger.error("获取本地文件异常：", e);
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
        // 获取文件在服务器上的绝对路径
        FileProperties.Local local = fileProperties.getLocal();
        String absolutePath = Paths.get(local.getStoragePath(), local.getBucket(), param.getPath()).toString();

        try {
            // 获取本地文件
            return FileUtils.deleteFile(absolutePath);
        } catch (Exception e) {
            logger.error("删除本地文件异常：", e);
        }
        return false;
    }

}
