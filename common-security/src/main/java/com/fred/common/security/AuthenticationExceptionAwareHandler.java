package com.fred.common.security;

import static com.fred.common.utils.Constants.REQUEST_HEADER_TIMESTAMP;

import java.util.Locale;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.fred.common.utils.ApiResult;
import com.fred.common.utils.ErrorCode;
import com.fred.common.utils.ExceptionDescriptor;
import com.fred.common.utils.HttpRequestContextHolder;
import com.fred.common.utils.MessageLocalization;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @Description: AuthenticationExceptionAwareHandler
 * @Author: Fred Feng
 * @Date: 12/09/2024
 * @Version 1.0.0
 */
@Slf4j
@Order(90)
@RestControllerAdvice
public class AuthenticationExceptionAwareHandler {

    @Autowired
    private MessageLocalization messageLocalization;

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ApiResult<?>> handleAuthenticationException(HttpServletRequest request,
            AuthenticationException e) {
        ErrorCode errorCode;
        Object[] args;
        if (e instanceof ExceptionDescriptor) {
            errorCode = ((ExceptionDescriptor) e).getErrorCode();
            args = new Object[] {((ExceptionDescriptor) e).getArg()};
        } else {
            errorCode = ErrorCodes.matches(e);
            args = null;
        }
        if (log.isErrorEnabled() && errorCode.isFatal()) {
            log.error(e.getMessage(), e);
        }
        ApiResult<Object> result =
                ApiResult.failed(getErrorMessage(errorCode, args), errorCode.getCode());
        result.setRequestPath(request.getRequestURI());
        String timestamp = HttpRequestContextHolder.getHeader(REQUEST_HEADER_TIMESTAMP);
        if (StringUtils.isNotBlank(timestamp)) {
            result.setElapsed(System.currentTimeMillis() - Long.parseLong(timestamp));
        }
        return new ResponseEntity<>(result, HttpStatus.UNAUTHORIZED);
    }

    private String getErrorMessage(ErrorCode errorCode, Object[] args) {
        Locale locale = HttpRequestContextHolder.getLocale();
        return messageLocalization.getMessage(errorCode, locale, args);
    }
}
