package com.fred.common.dao;

import static com.fred.common.utils.Constants.REQUEST_HEADER_TIMESTAMP;

import org.apache.commons.lang3.StringUtils;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.fred.common.utils.ApiResult;
import com.fred.common.utils.HttpRequestContextHolder;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @Description: DaoExceptionHandler
 * @Author: Fred Feng
 * @Date: 11/09/2024
 * @Version 1.0.0
 */
@Slf4j
@Order(199)
@RestControllerAdvice
public class DaoExceptionHandler {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ApiResult<?>> handleEntityNotFound(HttpServletRequest request,
            HttpServletResponse response, EntityNotFoundException e) {
        if (log.isErrorEnabled()) {
            log.error(e.getMessage(), e);
        }
        ApiResult<Object> result = ApiResult.failed("User doesn't exist");
        result.setRequestPath(request.getRequestURI());
        String timestamp = HttpRequestContextHolder.getHeader(REQUEST_HEADER_TIMESTAMP);
        if (StringUtils.isNotBlank(timestamp)) {
            result.setElapsed(System.currentTimeMillis() - Long.parseLong(timestamp));
        }
        return new ResponseEntity<ApiResult<?>>(result, HttpStatus.NOT_FOUND);
    }

}
