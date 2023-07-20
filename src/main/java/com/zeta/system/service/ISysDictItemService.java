package com.zeta.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zeta.system.model.dto.sysDictItem.SysDictItemDTO;
import com.zeta.system.model.entity.SysDictItem;

import java.util.List;
import java.util.Map;

/**
 * 字典项 服务类
 *
 * @author AutoGenerator
 * @date 2023-07-17 14:47:29
 */
public interface ISysDictItemService extends IService<SysDictItem> {

    /**
     * 根据字典编码查询字典项
     *
     * @param codes 字典编码
     */
    Map<String, List<SysDictItemDTO>> listByCodes(List<String> codes);


    /**
     * 根据字典id查询字典项
     *
     * @param dictIds 字典id
     */
    Map<Long, List<SysDictItem>> listByDictIds(List<Long> dictIds);

}
