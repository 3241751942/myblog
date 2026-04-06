/**package com.zzl.myblog.config;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;

@Configuration
public class MyBatisConfig {

    @Autowired
    private DataSource dataSource;

    @Bean
    public SqlSessionFactory sqlSessionFactory() throws Exception {
        SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();

        // 设置数据源
        sessionFactory.setDataSource(dataSource);

        // 设置实体类包路径（用于别名）
        sessionFactory.setTypeAliasesPackage("com.zzl.myblog.entity");

        // 设置 XML 映射文件路径
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        sessionFactory.setMapperLocations(resolver.getResources("classpath:mapper/*.xml"));

        // 配置 MyBatis 设置
        org.apache.ibatis.session.Configuration configuration = new org.apache.ibatis.session.Configuration();
        configuration.setMapUnderscoreToCamelCase(true);  // 驼峰命名转换
        configuration.setLogImpl(org.apache.ibatis.logging.stdout.StdOutImpl.class);  // 打印 SQL
        sessionFactory.setConfiguration(configuration);

        return sessionFactory.getObject();
    }
}**/