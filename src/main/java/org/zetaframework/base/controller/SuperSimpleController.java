package org.zetaframework.base.controller;

import com.baomidou.mybatisplus.extension.service.IService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.ParameterizedType;

/**
 * 简单实现的BaseController
 *
 * 无Query、Save、Update、Delete等实现，需手动实现
 * @author gcc
 */
public abstract class SuperSimpleController<S extends IService<Entity>, Entity> implements BaseController<Entity> {
    protected final Logger logger = LoggerFactory.getLogger(this.getEntityClass());

    @Autowired
    protected S service;

    public Class<Entity> clazz = null;

    /**
     * 获取实体类型
     *
     * @return Class<Entity>
     */
    @Override
    public Class<Entity> getEntityClass() {
        if (clazz == null) {
            // 获取当前类的第二个泛型的值(下标从0开始)。 即Entity的值
            clazz = (Class<Entity>) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[1];
        }
        return clazz;
    }

    /**
     * 获取service
     *
     * @return IService<Entity>
     */
    @Override
    public IService<Entity> getBaseService() {
        return service;
    }
}
