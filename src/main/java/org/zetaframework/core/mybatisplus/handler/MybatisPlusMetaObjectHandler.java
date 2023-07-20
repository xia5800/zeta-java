package org.zetaframework.core.mybatisplus.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.zetaframework.base.entity.Entity;
import org.zetaframework.base.entity.SuperEntity;
import org.zetaframework.core.mybatisplus.generator.UidGenerator;
import org.zetaframework.core.utils.ContextUtil;

import java.time.LocalDateTime;

/**
 * 自动填充处理
 *
 * @author gcc
 */
@Component
public class MybatisPlusMetaObjectHandler implements MetaObjectHandler {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final UidGenerator uidGenerator;
    public MybatisPlusMetaObjectHandler(UidGenerator uidGenerator) {
        this.uidGenerator = uidGenerator;
    }

    /**
     * 插入元对象字段填充（用于插入时对公共字段的填充）
     *
     * @param metaObject 元对象
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        fillCreated(metaObject);
        fillUpdated(metaObject);
        fillId(metaObject);
    }

    /**
     * 更新元对象字段填充（用于更新时对公共字段的填充）
     *
     * @param metaObject 元对象
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        fillUpdated(metaObject);
    }

    /**
     * 填充创建时间、创建人
     *
     * 说明：
     * 字段上未设置填充方式 fill = [FieldFill].INSERT也能进行填充
     * @param metaObject MetaObject
     */
    private void fillCreated(MetaObject metaObject) {
        // 有createTime字段，且字段值为null
        if (metaObject.hasGetter(SuperEntity.CREATE_TIME)) {
            Object value = metaObject.getValue(SuperEntity.CREATE_TIME);
            // 设置创建时间
            if (value == null) {
                setFieldValByName(SuperEntity.CREATE_TIME, LocalDateTime.now(), metaObject);
            }
        }

        // 有createdBy字段，且字段值为null
        if (metaObject.hasGetter(SuperEntity.CREATED_BY)) {
            Object value = metaObject.getValue(SuperEntity.CREATED_BY);
            if (value == null) {
                // 判断创建人字段的类型
                switch (metaObject.getGetterType(SuperEntity.CREATED_BY).getName()) {
                    case "java.lang.String":
                        setFieldValByName(SuperEntity.CREATED_BY, ContextUtil.getUserIdStr(), metaObject);
                        break;
                    case "java.lang.Long":
                        setFieldValByName(SuperEntity.CREATED_BY, ContextUtil.getUserId(), metaObject);
                        break;
                    case "java.lang.Integer":
                        setFieldValByName(SuperEntity.CREATED_BY, ContextUtil.getSubjectId(), metaObject);
                        break;
                    default:
                        logger.warn("【{}】字段仅支持String、Long、Integer类型填充，不是这三种类型的请插入时手动设置值", SuperEntity.CREATED_BY_COLUMN);
                        break;
                }
            }
        }
    }

    /**
     * 填充修改时间修改人
     *
     * 说明：
     * 字段上未设置填充方式 fill = [FieldFill].UPDATE也能进行填充
     * @param metaObject MetaObject
     */
    private void fillUpdated(MetaObject metaObject) {
        // 有updateTime字段，且字段值为null
        if (metaObject.hasGetter(Entity.UPDATE_TIME)) {
            Object value = metaObject.getValue(Entity.UPDATE_TIME);
            if (value == null) {
                setFieldValByName(Entity.UPDATE_TIME, LocalDateTime.now(), metaObject);
            }
        }

        // 有updatedBy字段，且字段值为null
        if (metaObject.hasGetter(Entity.UPDATED_BY)) {
            Object value = metaObject.getValue(Entity.UPDATED_BY);
            if (value == null) {
                switch (metaObject.getGetterType(Entity.UPDATED_BY).getName()) {
                    case "java.lang.String":
                        setFieldValByName(Entity.UPDATED_BY, ContextUtil.getUserIdStr(), metaObject);
                        break;
                    case "java.lang.Long":
                        setFieldValByName(Entity.UPDATED_BY, ContextUtil.getUserId(), metaObject);
                        break;
                    case "java.lang.Integer":
                        setFieldValByName(Entity.UPDATED_BY, ContextUtil.getSubjectId(), metaObject);
                        break;
                    default:
                        logger.warn("【{}】字段仅支持String、Long、Integer类型填充，不是这三种类型的请插入时手动设置值", Entity.UPDATED_BY_COLUMN);
                        break;
                }
            }
        }
    }

    /**
     * 填充主键
     * @param metaObject MetaObject
     */
    private void fillId(MetaObject metaObject) {
        // 有Id字段，且字段值为null
        if (metaObject.hasGetter(SuperEntity.FIELD_ID)) {
            Object value = metaObject.getValue(SuperEntity.FIELD_ID);
            if (value == null) {
                switch (metaObject.getGetterType(SuperEntity.FIELD_ID).getName()) {
                    case "java.lang.String":
                        setFieldValByName(SuperEntity.FIELD_ID, uidGenerator.getUid().toString(), metaObject);
                        break;
                    case "java.lang.Long":
                        setFieldValByName(SuperEntity.FIELD_ID, uidGenerator.getUid(), metaObject);
                        break;
                    case "java.lang.Integer":
                        // 不处理，数据库id设置为自增即可
                        break;
                    default:
                        logger.warn("【{}】字段仅支持String、Long、Integer类型填充，不是这三种类型的请插入时手动设置值", SuperEntity.FIELD_ID);
                        break;
                }
            }
        }
    }
}
