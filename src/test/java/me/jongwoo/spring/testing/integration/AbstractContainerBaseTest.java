package me.jongwoo.spring.testing.integration;

import org.junit.jupiter.api.Test;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

public abstract class AbstractContainerBaseTest {

    static final MySQLContainer MY_SQL_CONTAINER;

    static {
        MY_SQL_CONTAINER = new MySQLContainer("mysql:latest")
                .withUsername("username")
                .withPassword("password")
                .withDatabaseName("ems")
        ;

        MY_SQL_CONTAINER.start();
    }

    @DynamicPropertySource
    public static void dynamicPropertySource(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", MY_SQL_CONTAINER::getJdbcUrl);
        registry.add("spring.datasource.username", MY_SQL_CONTAINER::getUsername);
        registry.add("spring.datasource.password", MY_SQL_CONTAINER::getPassword);
    }

    @Test
    void printDbInfo() {
        System.out.println("MY_SQL_CONTAINER.getJdbcUrl(): " + MY_SQL_CONTAINER.getJdbcUrl());
        System.out.println("MY_SQL_CONTAINER.getDatabaseName(): " + MY_SQL_CONTAINER.getDatabaseName());
        System.out.println("MY_SQL_CONTAINER.getUsername(): " + MY_SQL_CONTAINER.getUsername());
        System.out.println("MY_SQL_CONTAINER.getPassword(): " + MY_SQL_CONTAINER.getPassword());
    }
}
