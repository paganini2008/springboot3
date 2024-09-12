package com.fred.api;

import java.util.TimeZone;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * 
 * @Description: Application Entry
 * @Author: Fred Feng
 * @Date: 10/09/2024
 * @Version 1.0.0
 */
@EnableJpaRepositories(basePackages = "com.fred.common.dao.repo")
@EntityScan(basePackages = "com.fred.common.dao.domain")
@EnableAsync
@SpringBootApplication
public class UserManagementApplication {

    static {
        TimeZone.setDefault(TimeZone.getTimeZone("GMT+8"));
    }

    public static void main(String[] args) {
        SpringApplication.run(UserManagementApplication.class, args);
    }

}
