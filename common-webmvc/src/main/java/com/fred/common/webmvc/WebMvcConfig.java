package com.fred.common.webmvc;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.fred.common.utils.WebUtils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * 
 * @Description: WebMvcConfig
 * @Author: Fred Feng
 * @Date: 11/09/2024
 * @Version 1.0.0
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private static final String ATTR_WEB_CONTEXT_PATH = "contextPath";

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(basicHandlerInterceptor()).addPathPatterns("/ui/**").excludePathPatterns("/user/**");

    }

    @Bean
    public BasicHandlerInterceptor basicHandlerInterceptor() {
        return new BasicHandlerInterceptor();
    }

    public static class BasicHandlerInterceptor implements HandlerInterceptor {

        @Override
        public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
                                 Object handler) throws Exception {
            HttpSession session = request.getSession();
            if (session.getAttribute(ATTR_WEB_CONTEXT_PATH) == null) {
                session.setAttribute(ATTR_WEB_CONTEXT_PATH, WebUtils.getContextPath(request));
            }
            return true;
        }

    }

}
