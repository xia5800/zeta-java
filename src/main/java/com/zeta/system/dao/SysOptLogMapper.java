package com.zeta.system.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zeta.system.model.dto.sysOptLog.SysOptLogTableDTO;
import com.zeta.system.model.entity.SysOptLog;
import com.zeta.system.model.param.SysOptLogQueryParam;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 操作日志 Mapper 接口
 *
 * @author AutoGenerator
 * @date 2023-07-17 14:49:01
 */
@Repository
public interface SysOptLogMapper extends BaseMapper<SysOptLog> {

    /**
     * 分页查询操作日志
     *
     * 说明：
     * 前端数据表格用，不查询请求参数、返回值、异常描述字段
     * @param page
     * @param param
     */
    List<SysOptLogTableDTO> pageTable(
            @Param("page") IPage<SysOptLogTableDTO> page,
            @Param("param") SysOptLogQueryParam param
    );

}
