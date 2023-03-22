package com.boot_board;

import lombok.Cleanup;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.sql.DataSource;
import java.sql.Connection;

@Log4j2
@SpringBootTest
public class DataSourceTests
{

    @Autowired
    private DataSource dataSource;


    @Test
    void testConnection() throws Exception
    {
        @Cleanup Connection conn = dataSource.getConnection();

        log.info(conn);

        Assertions.assertNotNull(conn);
    }
}
