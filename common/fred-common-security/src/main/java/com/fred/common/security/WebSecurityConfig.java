package com.fred.common.security;

import org.slf4j.Marker;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.fred.common.utils.BizException;
import com.fred.common.utils.DefaultExceptionTransferer;
import com.fred.common.utils.ErrorCode;
import com.fred.common.utils.ExceptionTransferer;

import lombok.SneakyThrows;

/**
 * @Description: WebSecurityConfig
 * @Author: Fred Feng
 * @Date: 19/11/2022
 * @Version 1.0.0
 */
@Order(90)
@EnableConfigurationProperties({JwtProperties.class, SecurityClientProperties.class,})
@Import({HttpSecurityConfig.class, LoginFailureHandlerAware.class,
        JwtAuthenticationFailureHandlerAware.class, AuthenticationExceptionAwareHandler.class})
@Configuration(proxyBeanMethods = false)
public class WebSecurityConfig {

    @ConditionalOnMissingBean
    @Bean
    public ExceptionTransferer noneHandlerExceptionTransferer() {
        return new NoneHandlerExceptionTransferer();
    }

    @ConditionalOnMissingBean
    @Bean
    public LoginFailureListener loginFailureListener() {
        return new NoOpLoginFailureListener();
    }

    @ConditionalOnMissingBean
    @Bean
    public JwtAuthenticationFailureListener jwtAuthenticationFailureListener(Marker marker) {
        return new LoggingJwtAuthenticationFailureListener(marker);
    }

    @ConditionalOnMissingBean
    @Bean
    public TokenStrategy tokenStrategy(JwtProperties jwtProperties) {
        return new JwtTokenStrategy(jwtProperties);
    }

    @ConditionalOnMissingBean
    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    @SneakyThrows
    public AuthenticationManager authManager(HttpSecurity http,
            UserDetailsService userDetailsService) {
        AuthenticationManagerBuilder authenticationManagerBuilder =
                http.getSharedObject(AuthenticationManagerBuilder.class);
        return authenticationManagerBuilder.build();
    }

    /**
     * @Description: NoneHandlerExceptionTransferer
     * @Author: Fred Feng
     * @Date: 18/01/2023
     * @Version 1.0.0
     */
    private static class NoneHandlerExceptionTransferer extends DefaultExceptionTransferer {

        @Override
        public Throwable transfer(Throwable e) {
            if (e instanceof AuthenticationException) {
                ErrorCode errorCode = ErrorCodes.matches((AuthenticationException) e);
                return new BizException(errorCode, HttpStatus.UNAUTHORIZED);
            }
            return super.transfer(e);
        }
    }

}
