package com.zeta.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zeta.system.model.entity.SysLoginLog;
import org.zetaframework.core.log.model.SysLoginLogDTO;

/**
 * 登录日志 服务类
 *
 * @author AutoGenerator
 * @date 2023-07-17 14:49:00
 */
public interface ISysLoginLogService extends IService<SysLoginLog> {

    /**
     * 保存用户登录日志
     *
     * @param loginLogDTO {@link org.zetaframework.core.log.model.SysLoginLogDTO}
     */
    void save(SysLoginLogDTO loginLogDTO);

}
