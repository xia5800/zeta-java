package org.zetaframework.core.utils;


/**
 * 不接受参数，无返回值的 函数式接口
 *
 * @author gcc
 */
@FunctionalInterface
public interface ZetaFunction {

    /**
     * 执行操作
     */
    void run();

}
