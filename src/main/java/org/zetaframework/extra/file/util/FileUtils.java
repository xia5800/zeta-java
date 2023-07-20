package org.zetaframework.extra.file.util;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileTypeUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IORuntimeException;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.StrUtil;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedInputStream;
import java.io.IOException;

/**
 * 文件工具类
 *
 * @author gcc
 */
public class FileUtils {

    /**
     * 获取唯一文件名
     *
     * 说明：
     * 利用uuid生成一个唯一的文件名
     * @param file
     */
    public static String getUniqueFileName(MultipartFile file) {
        String simpleUUID = UUID.randomUUID(false).toString(true);

        // 获取文件后缀
        String suffix = getSuffix(file);
        if (StrUtil.isNotBlank(suffix)) {
            suffix = "." + suffix;
        }
        // "${simpleUUID}${suffix}"
        return StrUtil.format("{}{}", simpleUUID, suffix);
    }

    /**
     * 获取文件名后缀
     *
     * 说明：
     * 1.不存在后缀则返回空字符串
     * 2.返回的后缀不带“.”
     * @param file MultipartFile
     */
    public static String getSuffix(MultipartFile file) {
        return FileUtil.getSuffix(file.getOriginalFilename());
    }


    /**
     * 生成文件路径
     * 规则： bizType/年/月/日/uniqueFileName
     *
     * @param bizType        业务类型
     * @param uniqueFileName 唯一文件名
     */
    public static String generatePath(String bizType, String uniqueFileName) {
        String dateFormat = DateUtil.format(new DateTime(), "yyyy/MM/dd");
        String module = StrUtil.isNotBlank(bizType) ? StrUtil.format("{}/", bizType) : "";

        // "${module}${dateFormat}/${uniqueFileName}"
        return StrUtil.format("{}{}/{}", module, dateFormat, uniqueFileName);
    }

    /**
     * 根据文件流的头部信息获得文件类型
     *
     * @param file MultipartFile
     */
    public static String getFileType(MultipartFile file) {
        String fileType = "";
        try {
            fileType = FileTypeUtil.getType(file.getInputStream(), file.getOriginalFilename());
        } catch (IORuntimeException | IOException e) {
            e.printStackTrace();
        }
        return fileType;
    }


    /**
     * 存储文件
     *
     * @param file MultipartFile
     * @param absolutePath web服务器存放文件的绝对路径
     */
    public static void writeFile(MultipartFile file, String absolutePath) {
        try {
            FileUtil.writeBytes(file.getBytes(), FileUtil.file(absolutePath));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 删除文件
     *
     * 注意：
     * 1.删除文件夹时不会判断文件夹是否为空
     * 2.如果文件不存在或已被删除 返回true
     * @param absolutePath web服务器存放文件的绝对路径
     */
    public static Boolean deleteFile(String absolutePath) {
        return FileUtil.del(absolutePath);
    }

    /**
     * 获取本地文件
     *
     * @param absolutePath web服务器存放文件的绝对路径
     */
    public static BufferedInputStream getFile(String absolutePath) {
        return FileUtil.getInputStream(absolutePath);
    }


}
