package com.fred.common.security;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.channel.ChannelProcessingFilter;
import org.springframework.security.web.firewall.StrictHttpFirewall;
import org.springframework.web.servlet.HandlerExceptionResolver;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fred.common.utils.MessageLocalization;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


/**
 * 
 * @Description: HttpSecurityConfig
 * @Author: Fred Feng
 * @Date: 11/09/2024
 * @Version 1.0.0
 */
@Slf4j
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true, prePostEnabled = true)
@RequiredArgsConstructor
public class HttpSecurityConfig {

    static final String[] DEFAULT_PERMITTED_URLS = new String[] {"/", "/error", "/ping", "/login",
            "/register", "/social-login", "/favicon.ico","/user/**","/ui/**"};

    static final String[] STATIC_RESOURCES =
            new String[] {"/", "/static/**", "/swagger-ui.html", "/webjars/**", "/v2/**",
                    "/swagger-resources/**", "/doc.html", "/favicon.ico", "/csrf", "/druid/**"};

    private final ObjectMapper objectMapper;
    private final MessageLocalization messageLocalization;
    private final TokenStrategy tokenStrategy;
    private final SecurityClientProperties securityClientProperties;
    private final LocalCache localCache;
    private final HandlerExceptionResolver handlerExceptionResolver;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http,
            AuthenticationManager authenticationManager) throws Exception {
        http.csrf(h -> h.disable()).httpBasic(h -> h.disable()).formLogin(f -> f.disable())
                .logout(l -> l.disable()).cors(c -> c.disable())
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(a -> a.requestMatchers(HttpMethod.OPTIONS).permitAll()
                        .requestMatchers(getPermittedUrls()).permitAll()
                        .requestMatchers("/actuator/**", "/monitor/**").permitAll().anyRequest()
                        .authenticated())
                .exceptionHandling(e -> e
                        .authenticationEntryPoint(new ForbiddenAuthenticationEntryPoint(
                                objectMapper, messageLocalization))
                        .accessDeniedHandler(
                                new GlobalAccessDeniedHandler(objectMapper, messageLocalization)));
        http.addFilterBefore(new WebCorsFilter(), ChannelProcessingFilter.class)
                .addFilter(new MixedAuthenticationFilter(authenticationManager,
                        securityClientProperties, Collections.emptyList(), tokenStrategy,
                        localCache, handlerExceptionResolver));
        return http.build();
    }

    private String[] getPermittedUrls() {
        List<String> urls = new ArrayList<>(Arrays.asList(DEFAULT_PERMITTED_URLS));
        if (CollectionUtils.isNotEmpty(securityClientProperties.getPermittedUrls())) {
            urls.addAll(securityClientProperties.getPermittedUrls());
        }
        if (log.isTraceEnabled()) {
            log.trace("Permitted urls: {}", urls.toString());
        }
        return urls.toArray(new String[0]);
    }

    @Bean
    public StrictHttpFirewall httpFirewall() {
        StrictHttpFirewall httpFirewall = new StrictHttpFirewall();
        httpFirewall.setAllowSemicolon(true);
        httpFirewall.setAllowUrlEncodedPeriod(true);
        httpFirewall.setAllowUrlEncodedSlash(true);
        httpFirewall.setAllowUrlEncodedPercent(true);
        httpFirewall.setAllowUrlEncodedDoubleSlash(true);
        httpFirewall.setAllowedHeaderValues(t -> true);
        return httpFirewall;
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web.httpFirewall(httpFirewall()).ignoring().requestMatchers(STATIC_RESOURCES);
    }

}
