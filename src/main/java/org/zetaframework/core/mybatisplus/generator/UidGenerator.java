package org.zetaframework.core.mybatisplus.generator;

/**
 * Uid生成器 接口类
 *
 * 说明：参考lamp-util项目UidGenerator类实现
 * @author yutianbao
 * @author zuihou
 * @author gcc
 */
public interface UidGenerator {

    /**
     * 获取id
     */
    Long getUid();

    /**
     * 解析uid
     * @param uid
     */
    String parseUid(Long uid);

}
