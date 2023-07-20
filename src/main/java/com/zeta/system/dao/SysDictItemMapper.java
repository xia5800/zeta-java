package com.zeta.system.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zeta.system.model.dto.sysDictItem.SysDictItemDTO;
import com.zeta.system.model.entity.SysDictItem;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 字典项 Mapper 接口
 *
 * @author AutoGenerator
 * @date 2023-07-17 14:47:29
 */
@Repository
public interface SysDictItemMapper extends BaseMapper<SysDictItem> {

    /**
     * 根据字典编码查询字典项
     *
     * @param codes
     */
    List<SysDictItemDTO> selectByDictCodes(@Param("codes") List<String> codes);

}
