package com.zeta.system.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zeta.system.dao.SysOptLogMapper;
import com.zeta.system.model.dto.sysOptLog.SysOptLogTableDTO;
import com.zeta.system.model.entity.SysOptLog;
import com.zeta.system.model.param.SysOptLogQueryParam;
import com.zeta.system.service.ISysOptLogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zetaframework.base.param.PageParam;
import org.zetaframework.base.result.PageResult;
import org.zetaframework.core.log.annotation.SysLog;
import org.zetaframework.core.log.model.LogDTO;

import java.util.List;
import java.util.Optional;

/**
 * 操作日志 服务实现类
 *
 * @author AutoGenerator
 * @date 2023-07-17 14:49:01
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class SysOptLogServiceImpl extends ServiceImpl<SysOptLogMapper, SysOptLog> implements ISysOptLogService {

    /**
     * 保存系统用户操作日志
     * <p>
     * 说明：
     * {@link SysLog}注解的业务实现
     *
     * @param logDTO {@link LogDTO}
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void save(LogDTO logDTO) {
        SysOptLog optLog = BeanUtil.toBean(logDTO, SysOptLog.class);
        this.save(optLog);
    }

    /**
     * 分页查询 前端数据表格用
     *
     * @param param PageParam<SysOptLogQueryParam>
     * @return PageResult<SysOptLogTableDTO>
     */
    @Override
    public PageResult<SysOptLogTableDTO> pageTable(PageParam<SysOptLogQueryParam> param) {
        IPage<SysOptLogTableDTO> page = param.buildPage();

        // 分页查询
        SysOptLogQueryParam model = Optional.ofNullable(param.getModel())
                .orElse(new SysOptLogQueryParam());
        List<SysOptLogTableDTO> optLogList = baseMapper.pageTable(page, model);
        return new PageResult<>(optLogList, page.getTotal());
    }
}
