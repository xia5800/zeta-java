package com.zeta.system.model.enums;

import com.baomidou.mybatisplus.annotation.IEnum;

/**
 * 菜单类型 枚举
 *
 * @author gcc
 */
public enum MenuTypeEnum implements IEnum<String> {
    /** 菜单 */
    MENU,
    /** 资源 */
    RESOURCE;

    /**
     * 枚举数据库存储值
     * @return name
     */
    @Override
    public String getValue() {
        return this.name();
    }
}
