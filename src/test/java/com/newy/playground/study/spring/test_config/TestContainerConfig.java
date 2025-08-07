package com.newy.playground.study.spring.test_config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.testcontainers.containers.JdbcDatabaseContainer;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

@TestConfiguration(proxyBeanMethods = false)
public class TestContainerConfig {
    private static String getSystemProperty(String key) {
        return System.getProperty(key, System.getenv(key));
    }

    @Bean
    @ServiceConnection // -> @DynamicPropertySource 를 대체한다 (SpringBoot 3.1+)
    public JdbcDatabaseContainer<?> rdbTestContainer() {
        var dbmsName = getSystemProperty("X_DBMS_NAME").toUpperCase();
        return switch (dbmsName) {
            case "MYSQL" -> new MySQLContainer<>(DockerImageName.parse("mysql:8"));
            case "POSTGRESQL" -> new PostgreSQLContainer<>(DockerImageName.parse("postgres:16"));
            default -> throw new IllegalArgumentException("Unknown DBMS: " + dbmsName);
        };
    }
}
