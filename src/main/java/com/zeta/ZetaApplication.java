package com.zeta;

import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.stereotype.Repository;

/**
 * 启动类
 *
 * @author gcc
 */
@ComponentScan(basePackages = {"com.zeta", "org.zetaframework"})
@MapperScan(value= {"com.zeta.**.dao"}, annotationClass = Repository.class)
@SpringBootApplication
public class ZetaApplication {
    private static final Logger logger = LoggerFactory.getLogger(ZetaApplication.class);

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(ZetaApplication.class, args);
        ConfigurableEnvironment env = context.getEnvironment();
        logger.info(
                "\n----------------------------------------------------------\n" +
                "\t项目 '{}' 启动成功! 访问连接:\n" +
                "\tSwagger文档: \t http://127.0.0.1:{}/doc.html\n" +
                "\t数据库监控: \t\t http://127.0.0.1:{}/druid\n" +
                "----------------------------------------------------------",
                env.getProperty("spring.application.name"),
                env.getProperty("server.port", "8080"),
                env.getProperty("server.port", "8080")
        );
    }

}
