package org.zetaframework.extra.file.strategy;

import org.springframework.web.multipart.MultipartFile;
import org.zetaframework.core.exception.ArgumentException;
import org.zetaframework.extra.file.model.FileDeleteParam;
import org.zetaframework.extra.file.model.FileInfo;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 使用策略的客户端
 *
 * 说明：
 * 其实就是对FileStrategy做了简单的封装。
 * 实际业务中直接@Autowried FileStrategy也是Ok。原因见：[FileConfiguration]
 * @author gcc
 */
public class FileContext {

    /** OSS支持的最大文件上传大小:  5GB */
    private static final Long MAX_FILE_SIZE = 5 * 1024 * 1024 * 1024L;

    private FileStrategy fileStrategy;
    public FileContext(FileStrategy fileStrategy) {
        this.fileStrategy = fileStrategy;
    }


    /**
     * 文件上传
     *
     * @param file 文件对象
     */
    public FileInfo upload(MultipartFile file) {
        return upload(file, null);
    }

    /**
     * 文件上传
     *
     * @param file 文件对象
     * @param bizType 业务类型 例如：order、user_avatar等 会影响文件url的值
     */
    public FileInfo upload(MultipartFile file, String bizType) {
        // 最大不能超过5GB
        if (file.getSize() > MAX_FILE_SIZE) {
            throw new ArgumentException("上传的文件过大");
        }

        // 调用具体策略的上传文件方法
        return fileStrategy.upload(file, bizType);
    }

    /**
     * 批量上传
     *
     * 说明：
     * 考虑到可能某些OSS不提供批量上传的接口。
     * 所以统一使用forEach + 单文件上传方式处理。如果你有更好的方案欢迎pr
     *
     * @param files
     * @param bizType 业务类型
     */
    public List<FileInfo> batchUpload(MultipartFile[] files, String bizType) {
        List<FileInfo> result = new ArrayList<>();
        // 排除空文件、大于5G的文件，依次上传
        Arrays.stream(files).filter(it -> !it.isEmpty() && it.getSize() < MAX_FILE_SIZE).forEach(it -> {
            result.add(upload(it, bizType));
        });
        return result;
    }

    /**
     * 获取文件输入流
     *
     * @param path
     */
    public InputStream getFileInputStream(String path) {
        return fileStrategy.getObject(path);
    }

    /**
     * 删除文件
     *
     * @param param 文件删除参数
     */
    public Boolean delete(FileDeleteParam param) {
        // 调用具体策略的删除文件方法
        return fileStrategy.delete(param);
    }

    /**
     * 批量删除
     *
     * 说明：
     * 考虑到可能某些OSS不提供批量删除的接口。
     * 所以统一使用forEach + 单文件删除方式处理。如果你有更好的方案欢迎pr
     * @param params 文件删除参数
     */
    public void batchDelete(List<FileDeleteParam> params) {
        params.forEach(this::delete);
    }

    /**
     * 获取文件管理策略
     * @return
     */
    public FileStrategy getFileStrategy() {
        return fileStrategy;
    }
}
