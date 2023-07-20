package org.zetaframework.core.utils;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import com.alibaba.ttl.TransmittableThreadLocal;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 获取当前线程变量中的 用户id、令牌等信息
 *
 * 说明：参考lamp-util项目ContextUtil类实现
 * @author zuihou
 * @author gcc
 */
public final class ContextUtil {

    private ContextUtil() {
    }

    /**
     * 支持多线程传递参数
     */
    private static final ThreadLocal<Map<String, String>> THREAD_LOCAL = new TransmittableThreadLocal<>();

    public static void putAll(Map<String, String> map) {
        map.forEach(ContextUtil::set);
    }

    public static void set(String key, Object value) {
        Map<String, String> map = getLocalMap();
        map.put(key, value == null ? StrUtil.EMPTY : value.toString());
    }

    public static <T> T get(String key, Class<T> type) {
        Map<String, String> map = getLocalMap();
        return Convert.convert(type, map.get(key));
    }

    public static <T> T get(String key, Class<T> type, Object def) {
        Map<String, String> map = getLocalMap();
        return Convert.convert(type, map.getOrDefault(key, String.valueOf(def == null ? StrUtil.EMPTY : def)));
    }

    public static String get(String key) {
        Map<String, String> map = getLocalMap();
        return map.getOrDefault(key, StrUtil.EMPTY);
    }

    /**
     * 获取ThreadLocal中的值
     * @return Map<String, String>
     */
    public static Map<String, String> getLocalMap() {
        Map<String, String> map = THREAD_LOCAL.get();
        if (map == null) {
            map = new ConcurrentHashMap<>(10);
            THREAD_LOCAL.set(map);
        }
        return map;
    }

    /**
     * 设置ThreadLocal中的值
     * @param localMap
     */
    public static void setLocalMap(Map<String, String> localMap) {
        THREAD_LOCAL.set(localMap);
    }

    /**
     * 防止内存溢出
     */
    public static void remove() {
        THREAD_LOCAL.remove();
    }


    /*********************** 用户id ***********************/

    /**
     * 设置用户ID (Int)
     *
     * @param userId Int
     */
    public static void setSubjectId(Integer userId) {
         set("subjectId", userId);
    }

    /**
     * 设置用户ID (Int)
     *
     * @param userId Int
     */
    public static void setUserId(Integer userId) {
        setSubjectId(userId);
    }

    /**
     * 设置用户ID (Long)
     *
     * @param userId Long
     */
    public static void setUserId(Long userId) {
        set("userId", userId);
    }

    /**
     * 设置用户ID (String)
     *
     * @param userId String
     */
    public static void setUserId(String userId) {
        set("userId", userId);
    }

    /**
     * 获取用户ID (Long)
     *
     * @return Long
     */
    public static Long getUserId() {
        return get("userId", Long.class, 0L);
    }

    /**
     * 获取用户ID (String)
     *
     * 说明：如果userId获取不到，就获取subjectId
     * @return String
     */
    public static String getUserIdStr() {
        String userId = get("userId", String.class, "");
        // 如果userId获取不到，就获取subjectId
        return StrUtil.isNotBlank(userId) ? userId : Convert.toStr(getSubjectId());
    }

    /**
     * 获取用户ID (Int)
     */
    public static Integer getUserIdInt() {
        return getSubjectId();
    }

    /**
     * 获取用户ID (Int)
     *
     * @return Int
     */
    public static Integer getSubjectId() {
        return get("subjectId", Integer.class, 0);
    }
    /*********************** 用户id end ***********************/


    /*********************** 令牌 ***********************/
    /**
     * 设置token
     *
     * @param token String
     */
    public static void setToken(String token) {
        set("token", token);
    }

    /**
     * 获取token
     *
     * @return String
     */
    public static String getToken() {
        return get("token");
    }
    /*********************** 令牌 end ***********************/
}
