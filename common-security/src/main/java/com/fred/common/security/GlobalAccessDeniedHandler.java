package com.fred.common.security;

import static com.fred.common.utils.Constants.REQUEST_HEADER_TIMESTAMP;

import java.io.IOException;
import java.util.Locale;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fred.common.utils.ApiResult;
import com.fred.common.utils.ErrorCode;
import com.fred.common.utils.HttpRequestContextHolder;
import com.fred.common.utils.MessageLocalization;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @Description: GlobalAccessDeniedHandler
 * @Author: Fred Feng
 * @Date: 11/09/2024
 * @Version 1.0.0
 */
@Slf4j
@RequiredArgsConstructor
public class GlobalAccessDeniedHandler implements AccessDeniedHandler {

    private final ObjectMapper objectMapper;
    private final MessageLocalization messageLocalization;

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException e) throws IOException, ServletException {
        if (log.isWarnEnabled()) {
            log.warn("[RequestURI: {}]: {}", request.getRequestURI(), e.getMessage(), e);
        }
        response.setCharacterEncoding("UTF-8");
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        String msg = getI18nMessage(request, ErrorCodes.ACCESS_DENIED);
        ApiResult<String> result = ApiResult.failed(msg, ErrorCodes.ACCESS_DENIED.getCode(), null);
        result.setRequestPath(request.getRequestURI());
        String timestamp = HttpRequestContextHolder.getHeader(REQUEST_HEADER_TIMESTAMP);
        if (StringUtils.isNotBlank(timestamp)) {
            result.setElapsed(System.currentTimeMillis() - Long.parseLong(timestamp));
        }
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.getWriter().write(objectMapper.writeValueAsString(result));
    }

    private String getI18nMessage(HttpServletRequest request, ErrorCode errorCode) {
        Locale locale = HttpRequestContextHolder.getLocale();
        return messageLocalization.getMessage(errorCode, locale);
    }
}