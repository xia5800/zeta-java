package org.zetaframework.extra.desensitization.utils;

import cn.afterturn.easypoi.util.PoiDataDesensitizationUtil;
import cn.hutool.core.util.StrUtil;
import org.apache.commons.lang3.StringUtils;

/**
 * 脱敏工具类
 *
 * 说明：
 * 参考easypoi的{@link PoiDataDesensitizationUtil}类实现
 *
 * @author gcc
 */
public class DesensitizationUtil {
    /** 规则一 */
    private static final String SPILT_START_END = "_";
    /** 规则二 */
    private static final String SPILT_MAX = ",";
    /** 规则三 */
    private static final String SPILT_MARK = "~";


    /**
     * # 脱敏
     *
     * ## 脱敏规则说明：
     * **规则一**：采用保留头和尾的方式,中间数据按打码方式配置的字符进行打码
     *
     * 如：3_4 表示保留前3位以及后4位
     * ```
     * 3_4    13112311234 --> 131****1234
     * 6_4    370101123456781234 --> 370101********1234
     * ```
     *
     * **规则二**：采用确定隐藏字段的进行隐藏,优先保留头
     *
     * 如：1,3 表示最大隐藏3位,最小一位
     * ```
     *    李 -->  *
     *    李三 --> 李*
     *    张全蛋  --> 张*蛋
     *    李张全蛋 --> 李**蛋
     *    尼古拉斯.李张全蛋 -> 尼古拉***张全蛋
     *```
     *
     * **规则三**：特殊符号后保留
     *
     * 如：1~@ 表示只保留第一位和@之后的字段
     * ```
     * 1~@   alibaba@mail.com -> a********@mail.com
     * 3~#   236121678126381#2236 -> 236***********#2236
     *```
     *
     * @param value 要脱敏的数据
     * @param rule 脱敏规则
     * @param symbol 打码方式
     * @return String 脱敏后的数据
     */
    public static String deserialization(String value, String rule, String symbol) {
        if (StrUtil.isBlank(value)) return value;
        if (StrUtil.isBlank(rule)) return value;

        // 规则一
        if (rule.contains(SPILT_START_END)) {
            String[] arr = rule.split(SPILT_START_END);
            return subStartEndString(Integer.parseInt(arr[0]), Integer.parseInt(arr[1]), value, symbol);
        }
        // 规则二
        if (rule.contains(SPILT_MAX)) {
            String[] arr = rule.split(SPILT_MAX);
            return subMaxString(Integer.parseInt(arr[0]), Integer.parseInt(arr[1]), value, symbol);
        }
        // 规则三
        if (rule.contains(SPILT_MARK)) {
            String[] arr = rule.split(SPILT_MARK);
            return markSpilt(Integer.parseInt(arr[0]), arr[1], value, symbol);
        }

        return value;
    }

    /**
     * 收尾截取数据
     *
     * @param start
     * @param end
     * @param value
     * @param symbol
     */
    private static String subStartEndString(int start, int end, String value, String symbol){
        if (value.length() <= start + end) return value;

        return StringUtils.left(value, start) + StringUtils.leftPad(
                StringUtils.right(value, end),
                StringUtils.length(value) - start,
                symbol
        );
    }

    /**
     * 部分数据截取，优先对称截取
     *
     * @param start
     * @param end
     * @param value
     * @param symbol
     */
    private static String subMaxString(int start, int end, String value, String symbol) {
        if (value == null) return null;
        if (start > end) {
            throw new IllegalArgumentException("start must less end");
        }

        int len = value.length();
        if (len <= start) {
            // 李  ->  *
            return StringUtils.leftPad("", len, symbol);
        } else if (len > start && len <= end) {
            // len > start && len <= end
            if (len == 1) {
                return value;
            }
            if (len == 2) {
                return StringUtils.left(value, 1) + symbol;
            }
            return StringUtils.left(value, 1) + StringUtils.leftPad(
                    StringUtils.right(value, 1),
                    StringUtils.length(value) - 1,
                    symbol
            );
        } else {
            start = (int) Math.ceil((len - end + 0.0D) / 2);
            end = len - start - end;
            end = end == 0 ? 1 : end;
            return StringUtils.left(value, start) + StringUtils.leftPad(StringUtils.right(value, end), len - start, symbol);
        }
    }

    /**
     * 特定字符分隔，添加星号
     *
     * @param start
     * @param mark 特定字符
     * @param value
     * @param symbol
     */
    private static String markSpilt(int start, String mark, String value, String symbol) {
        if (value == null) return null;

        int end = value.lastIndexOf(mark);
        // 如果value中特定字符位置比start小
        if (end <= start) return value;

        return StringUtils.left(value, start) + StringUtils.leftPad(
                StringUtils.right(value, value.length() - end),
                value.length() - start,
                symbol
        );
    }


}
