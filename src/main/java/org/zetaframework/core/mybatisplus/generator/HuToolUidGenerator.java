package org.zetaframework.core.mybatisplus.generator;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;

/**
 * 基于Hutool工具类实现的雪花id生成器
 *
 * 说明：参考lamp-util项目HuToolUidGenerator类实现
 * @author zuihou
 * @author gcc
 */
public class HuToolUidGenerator implements UidGenerator {
    private final Snowflake snowflake;

    public HuToolUidGenerator(Long workerId, Long datacenterId) {
        this.snowflake = IdUtil.getSnowflake(workerId, datacenterId);
    }

    /**
     * 获取id
     */
    @Override
    public Long getUid() {
        return snowflake.nextId();
    }

    /**
     * 解析uid
     *
     * @param uid
     */
    @Override
    public String parseUid(Long uid) {
        long workerId = snowflake.getWorkerId(uid);
        long dataCenterId = snowflake.getDataCenterId(uid);
        long timestamp = snowflake.getGenerateDateTime(uid);

        return String.format("{\"UID\":\"%d\",\"timestamp\":\"%d\",\"workerId\":\"%d\",\"dataCenterId\":\"%d\"}",
                uid, timestamp, workerId, dataCenterId);
    }

}
