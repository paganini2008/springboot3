package com.fred.common.security;

import java.util.ArrayList;
import java.util.List;
import org.springframework.boot.context.properties.ConfigurationProperties;
import lombok.Getter;
import lombok.Setter;

/**
 * 
 * @Description: SecurityClientProperties
 * @Author: Fred Feng
 * @Date: 10/09/2024
 * @Version 1.0.0
 */
@Getter
@Setter
@ConfigurationProperties("spring.security.client")
public class SecurityClientProperties {

    private int expiration = 1 * 60 * 60;
    private String saPassword;
    private List<String> permittedUrls = new ArrayList<>();
    private boolean basicEnabled = true;
    private boolean showAuthorizationType = false;
}
