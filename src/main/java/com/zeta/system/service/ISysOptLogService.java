package com.zeta.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zeta.system.model.dto.sysOptLog.SysOptLogTableDTO;
import com.zeta.system.model.entity.SysOptLog;
import com.zeta.system.model.param.SysOptLogQueryParam;
import org.zetaframework.base.param.PageParam;
import org.zetaframework.base.result.PageResult;
import org.zetaframework.core.log.annotation.SysLog;
import org.zetaframework.core.log.model.SysLogDTO;

/**
 * 操作日志 服务类
 *
 * @author AutoGenerator
 * @date 2023-07-17 14:49:01
 */
public interface ISysOptLogService extends IService<SysOptLog> {

    /**
     * 保存系统用户操作日志
     *
     * 说明：
     * {@link SysLog}注解的业务实现
     * @param sysLogDTO {@link org.zetaframework.core.log.model.SysLogDTO}
     */
    void save(SysLogDTO sysLogDTO);

    /**
     * 分页查询 前端数据表格用
     * @param param PageParam<SysOptLogQueryParam>
     */
    PageResult<SysOptLogTableDTO> pageTable(PageParam<SysOptLogQueryParam> param);

}
