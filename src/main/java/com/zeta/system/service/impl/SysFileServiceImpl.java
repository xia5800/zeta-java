package com.zeta.system.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zeta.system.dao.SysFileMapper;
import com.zeta.system.model.entity.SysFile;
import com.zeta.system.service.ISysFileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.zetaframework.core.exception.BusinessException;
import org.zetaframework.extra.file.model.FileDeleteParam;
import org.zetaframework.extra.file.model.FileInfo;
import org.zetaframework.extra.file.strategy.FileContext;

import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 系统文件 服务实现类
 *
 * @author AutoGenerator
 * @date 2023-07-17 14:47:29
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class SysFileServiceImpl extends ServiceImpl<SysFileMapper, SysFile> implements ISysFileService {

    private final FileContext fileContext;

    /**
     * 上传文件
     *
     * @param file    文件对象
     * @param bizType 业务类型 例如：order、user_avatar等 会影响文件url的值
     */
    @Override
    public SysFile upload(MultipartFile file, String bizType) {
        // 调用具体策略(配置文件里面配置的那个，没配置默认上传到本地)上传文件
        FileInfo fileInfo = fileContext.upload(file, bizType);

        SysFile model = BeanUtil.toBean(fileInfo, SysFile.class);
        if (!this.save(model)) {
            throw new BusinessException("文件保存失败");
        }
        return model;
    }

    /**
     * 下载文件
     *
     * @param id
     * @param response
     */
    @Override
    public void download(Long id, HttpServletResponse response) {
        SysFile sysFile = this.getById(id);
        if (sysFile == null) {
            throw new BusinessException("文件不存在或已被删除");
        }

        // 获取文件输入流
        try (InputStream inputStream = fileContext.getFileInputStream(sysFile.getPath())) {
            // 设置响应头
            response.setContentType("application/octet-stream; charset=utf-8");
            response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment;fileName=" + sysFile.getUniqueFileName());

            // 将文件流写入到输出中去
            IoUtil.copy(inputStream, response.getOutputStream());
        } catch (Exception e) {
            log.warn("文件下载失败", e);
        }
    }

    /**
     * 删除文件
     *
     * @param id
     */
    @Override
    public boolean delete(Long id) {
        SysFile sysFile = this.getById(id);
        if (sysFile == null) return true;

        // 先删除文件
        FileDeleteParam param = new FileDeleteParam();
        param.setPath(sysFile.getPath());
        fileContext.delete(param);

        // 再删除数据
        return this.removeById(id);
    }

    /**
     * 批量删除文件
     *
     * @param ids
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean batchDelete(List<Long> ids) {
        // 批量查询文件
        List<SysFile> listFile = this.listByIds(ids);
        if (listFile.isEmpty()) return true;

        // 排除相对路径为空的文件，构造批量删除参数
        List<FileDeleteParam> params = listFile.stream()
                .filter(it -> StrUtil.isNotBlank(it.getPath()))
                .map(it -> {
                    FileDeleteParam param = new FileDeleteParam();
                    param.setPath(it.getPath());
                    return param;
                }).collect(Collectors.toList());

        // 批量删除， params不用判空
        fileContext.batchDelete(params);

        // 删除数据
        return this.removeByIds(ids);
    }
}
