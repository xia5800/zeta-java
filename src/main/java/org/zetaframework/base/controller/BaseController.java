package org.zetaframework.base.controller;

import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 基础接口
 *
 * @author gcc
 */
public interface BaseController<Entity> extends SuperBaseController {

    /**
     * 获取实体类型
     *
     * @return Class<Entity>
     */
    Class<Entity> getEntityClass();

    /**
     * 获取service
     *
     * @return IService<Entity>
     */
    IService<Entity> getBaseService();

}
