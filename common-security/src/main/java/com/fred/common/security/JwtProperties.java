package com.fred.common.security;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;

/**
 * 
 * @Description: JwtProperties
 * @Author: Fred Feng
 * @Date: 12/09/2024
 * @Version 1.0.0
 */
@ConfigurationProperties("spring.security.jwt")
@Getter
@Setter
public class JwtProperties {

    private String prefix = "";
    private String issuer = "consoleconnect";
    private String secretKey = "consoleconnect";
    private String audience = "consoleconnect";
    private int expiration = 24 * 60 * 60;
}