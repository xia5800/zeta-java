package org.zetaframework.extra.file.strategy.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;
import org.zetaframework.core.exception.BusinessException;
import org.zetaframework.extra.file.model.FileInfo;
import org.zetaframework.extra.file.strategy.FileStrategy;
import org.zetaframework.extra.file.util.FileUtils;

/**
 * 文件抽象策略 处理类
 *
 * 说明：
 * 为什么要有这么一层抽象
 * 因为各个具体的策略实现类都有一些重复的操作。所以将这些重复的操作提取出来
 * @author gcc
 */
public abstract class AbstractFileStrategy implements FileStrategy {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 上传文件
     *
     * @param file    MultipartFile 文件对象
     * @param bizType 业务类型 影响文件相对路径和文件的访问地址
     * @return FileInfo 上传文件的详细信息
     */
    @Override
    public FileInfo upload(MultipartFile file, String bizType) {
        // 处理文件存放路径
        String uniqueFileName = FileUtils.getUniqueFileName(file);
        String path = FileUtils.generatePath(bizType, uniqueFileName);

        // 上传文件的信息。
        FileInfo fileInfo  = new FileInfo();
        fileInfo.setOriginalFileName(file.getOriginalFilename());
        fileInfo.setContentType(file.getContentType());
        fileInfo.setSize(file.getSize());
        fileInfo.setSuffix(FileUtils.getSuffix(file));
        fileInfo.setFileType(FileUtils.getFileType(file));
        fileInfo.setBizType(bizType);
        fileInfo.setUniqueFileName(uniqueFileName);
        fileInfo.setPath(path);

        try {
            // 交给对应的策略去实现文件上传
            onUpload(file, fileInfo);
        } catch (Exception e) {
            logger.error("文件上传失败:", e);
            throw new BusinessException("文件上传失败");
        }

        return fileInfo;
    }

    /**
     * 策略类实现该方法进行文件上传
     *
     * @param file     文件对象
     * @param fileInfo 文件详情
     */
    protected abstract void onUpload(MultipartFile file, FileInfo fileInfo);
}
