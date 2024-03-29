package com.accountingsystem;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.TestPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;


@TestPropertySource(properties = {
        "spring.test.database.replace=none"
})
public abstract class AbstractTestContainerStartUp {

    @Container
    static final MySQLContainer mySQLContainer;

    static {
        mySQLContainer = new MySQLContainer<>("mysql:8.0");
        mySQLContainer.start();
    }

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", mySQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", mySQLContainer::getUsername);
        registry.add("spring.datasource.password", mySQLContainer::getPassword);
    }
}
