package com.zeta.system.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zeta.system.dao.SysLoginLogMapper;
import com.zeta.system.model.entity.SysLoginLog;
import com.zeta.system.service.ISysLoginLogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zetaframework.core.log.model.LoginLogDTO;

/**
 * 登录日志 服务实现类
 *
 * @author AutoGenerator
 * @date 2023-07-17 14:49:00
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class SysLoginLogServiceImpl extends ServiceImpl<SysLoginLogMapper, SysLoginLog> implements ISysLoginLogService {

    /**
     * 保存用户登录日志
     *
     * @param loginLogDTO {@link LoginLogDTO}
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void save(LoginLogDTO loginLogDTO) {
        SysLoginLog loginLog = BeanUtil.toBean(loginLogDTO, SysLoginLog.class);
        loginLog.setCreatedBy(loginLogDTO.getUserId());
        this.save(loginLog);
    }
}
