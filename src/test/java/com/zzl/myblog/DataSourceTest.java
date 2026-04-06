package com.zzl.myblog;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.sql.DataSource;
import java.sql.Connection;

@SpringBootTest
public class DataSourceTest {

    @Autowired
    private DataSource dataSource;

    @Test
    void testConnection() throws Exception {
        System.out.println("DataSource: " + dataSource);
        try (Connection conn = dataSource.getConnection()) {
            System.out.println("数据库连接成功！");
            System.out.println("数据库URL: " + conn.getMetaData().getURL());
            System.out.println("数据库产品: " + conn.getMetaData().getDatabaseProductName());
        }
    }
}