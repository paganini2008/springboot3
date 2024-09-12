package com.fred.common.webmvc;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fred.common.utils.DefaultExceptionTransferer;
import com.fred.common.utils.ExceptionTransferer;
import com.fred.common.utils.FormattedMessageLocalization;
import com.fred.common.utils.JacksonUtils;
import com.fred.common.utils.MessageLocalization;
import com.fred.common.utils.ServerProperties;

/**
 * 
 * @Description: WebServerConfig
 * @Author: Fred Feng
 * @Date: 11/09/2024
 * @Version 1.0.0
 */
@EnableConfigurationProperties({ServerProperties.class})
@Configuration(proxyBeanMethods = false)
public class WebServerConfig {

    @Primary
    @Bean
    public ObjectMapper objectMapper() {
        return JacksonUtils.getObjectMapperForWebMvc();
    }

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOriginPattern("*");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }

    @ConditionalOnMissingBean
    @Bean
    public MessageLocalization messageLocalization() {
        return new FormattedMessageLocalization();
    }

    @ConditionalOnMissingBean
    @Bean
    public ExceptionTransferer defaultExceptionTransferer() {
        return new DefaultExceptionTransferer();
    }

}
