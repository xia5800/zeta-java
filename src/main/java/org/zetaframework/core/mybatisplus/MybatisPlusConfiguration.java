package org.zetaframework.core.mybatisplus;

import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.BlockAttackInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.IllegalSQLInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.OptimisticLockerInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.zetaframework.core.mybatisplus.generator.HuToolUidGenerator;
import org.zetaframework.core.mybatisplus.generator.UidGenerator;
import org.zetaframework.core.mybatisplus.properties.DatabaseProperties;

/**
 * mybatis plus 配置类
 *
 * @author gcc
 */
@Configuration
@EnableTransactionManagement
@EnableConfigurationProperties(DatabaseProperties.class)
public class MybatisPlusConfiguration {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final DatabaseProperties databaseProperties;
    public MybatisPlusConfiguration(DatabaseProperties databaseProperties) {
        this.databaseProperties = databaseProperties;
    }

    /**
     * mybatis插件配置
     * @return MybatisPlusInterceptor
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();

        // 分页插件配置
        PaginationInnerInterceptor paginationInnerInterceptor = new PaginationInnerInterceptor();
        paginationInnerInterceptor.setOverflow(databaseProperties.getOverflow());
        paginationInnerInterceptor.setMaxLimit(databaseProperties.getMaxLimit());
        paginationInnerInterceptor.setDbType(databaseProperties.getDbType());
        paginationInnerInterceptor.setOptimizeJoin(databaseProperties.getOptimizeJoin());
        interceptor.addInnerInterceptor(paginationInnerInterceptor);

        // 乐观锁插件
        if (databaseProperties.getOptimisticLocker()) {
            logger.info("mybatis-plus乐观锁插件：启用");
            interceptor.addInnerInterceptor(new OptimisticLockerInnerInterceptor());
        }

        // 防止全表更新与删除
        if (databaseProperties.getBlockAttack()) {
            logger.info("mybatis-plus防止全表更新与删除插件：启用");
            interceptor.addInnerInterceptor(new BlockAttackInnerInterceptor());
        }

        // sql性能规范
        if (databaseProperties.getIllegalSql()) {
            logger.info("mybatis-plus sql性能规范插件：启用");
            interceptor.addInnerInterceptor(new IllegalSQLInnerInterceptor());
        }
        return interceptor;
    }

    /**
     * id生成策略
     * @return UidGenerator
     */
    @Bean
    public UidGenerator getUidGenerator() {
        return new HuToolUidGenerator(
                databaseProperties.getHutoolId().getWorkderId(),
                databaseProperties.getHutoolId().getDataCenterId()
        );
    }

}
