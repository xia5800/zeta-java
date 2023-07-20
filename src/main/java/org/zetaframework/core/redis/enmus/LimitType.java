package org.zetaframework.core.redis.enmus;

/**
 * 限流类型
 *
 * @author gcc
 */
public enum LimitType {
    /** ip限流 */
    IP,
    /** 用户id限流 */
    USERID;
}
