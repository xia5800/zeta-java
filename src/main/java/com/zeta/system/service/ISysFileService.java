package com.zeta.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zeta.system.model.entity.SysFile;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 系统文件 服务类
 *
 * @author AutoGenerator
 * @date 2023-07-17 14:47:29
 */
public interface ISysFileService extends IService<SysFile> {

    /**
     * 上传文件
     *
     * @param file 文件对象
     * @param bizType 业务类型 例如：order、user_avatar等 会影响文件url的值
     */
    SysFile upload(MultipartFile file, String bizType);

    /**
     * 下载文件
     */
    void download(Long id, HttpServletResponse response);

    /**
     * 删除文件
     *
     * @param id
     */
    boolean delete(Long id);

    /**
     * 批量删除文件
     *
     * @param ids
     */
    boolean batchDelete(List<Long> ids);
}
