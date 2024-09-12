package com.fred.common.webmvc;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;

/**
 * 
 * @Description: OpenApiConfig
 * @Author: Fred Feng
 * @Date: 11/09/2024
 * @Version 1.0.0
 */
@Configuration(proxyBeanMethods = false)
public class OpenApiConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI().info(new Info().title("User Management API Documentation")
                .description("User Management API Documentation").version("v1.0.0")
                .contact(new Contact().name("Fred Feng").email("paganini.fy@gmail.com"))
                .license(new License().name("Apache 2.0").url("http://springdoc.org")));
    }

}
