package com.zeta.system.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zeta.system.dao.SysDictItemMapper;
import com.zeta.system.model.dto.sysDictItem.SysDictItemDTO;
import com.zeta.system.model.entity.SysDictItem;
import com.zeta.system.service.ISysDictItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 字典项 服务实现类
 *
 * @author AutoGenerator
 * @date 2023-07-17 14:47:29
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class SysDictItemServiceImpl extends ServiceImpl<SysDictItemMapper, SysDictItem> implements ISysDictItemService {

    /**
     * 根据字典编码查询字典项
     *
     * @param codes 字典编码
     */
    @Override
    public Map<String, List<SysDictItemDTO>> listByCodes(List<String> codes) {
        if (CollUtil.isEmpty(codes)) return Collections.emptyMap();

        // 根据字典编码查询字典项
        List<SysDictItemDTO> dictItemList = baseMapper.selectByDictCodes(codes);

        // 按照字典编码分组
        return dictItemList.stream()
                .collect(Collectors.groupingBy(SysDictItemDTO::getDictCode));
    }

    /**
     * 根据字典id查询字典项
     *
     * @param dictIds 字典id
     */
    @Override
    public Map<Long, List<SysDictItem>> listByDictIds(List<Long> dictIds) {
        if (CollUtil.isEmpty(dictIds)) return Collections.emptyMap();

        // 通过字典id查询字典项
        List<SysDictItem> dictItemList = this.list(
                new LambdaQueryWrapper<SysDictItem>()
                        .in(SysDictItem::getDictId, dictIds)
        );
        if (CollUtil.isEmpty(dictItemList)) return Collections.emptyMap();

        // 按照字典id分组
        return dictItemList.stream()
                .collect(Collectors.groupingBy(SysDictItem::getDictId));
    }
}
