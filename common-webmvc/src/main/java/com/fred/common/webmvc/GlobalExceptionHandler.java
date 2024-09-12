package com.fred.common.webmvc;

import static com.fred.common.utils.Constants.REQUEST_HEADER_TIMESTAMP;

import java.util.Locale;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import com.fred.common.utils.ApiResult;
import com.fred.common.utils.BizException;
import com.fred.common.utils.ErrorCode;
import com.fred.common.utils.ExceptionTransferer;
import com.fred.common.utils.HttpRequestContextHolder;
import com.fred.common.utils.LangUtils;
import com.fred.common.utils.MessageLocalization;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @Description: GlobalExceptionHandler
 * @Author: Fred Feng
 * @Date: 11/09/2024
 * @Version 1.0.0
 */
@Slf4j
@Order(200)
@RestControllerAdvice
public class GlobalExceptionHandler {

    @Autowired
    private MessageLocalization messageLocalization;

    @Autowired
    private ExceptionTransferer exceptionTransferer;

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(BizException.class)
    public ResponseEntity<ApiResult<?>> handleBizException(HttpServletRequest request,
            HttpServletResponse response, BizException e) {
        ErrorCode errorCode = e.getErrorCode();
        if (log.isErrorEnabled() && errorCode.isFatal()) {
            log.error(e.getMessage(), e);
        }

        ApiResult<Object> result =
                ApiResult.failed(getErrorMessage(errorCode, LangUtils.toObjectArray(e.getArg())),
                        errorCode.getCode(), e.getArg());
        result.setRequestPath(request.getRequestURI());

        String timestamp = HttpRequestContextHolder.getHeader(REQUEST_HEADER_TIMESTAMP);
        if (StringUtils.isNotBlank(timestamp)) {
            result.setElapsed(System.currentTimeMillis() - Long.parseLong(timestamp));
        }
        return new ResponseEntity<>(result, e.getHttpStatus());
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ApiResult<?>> handleNoHandlerFoundException(HttpServletRequest request,
            HttpServletResponse response, NoHandlerFoundException e) {
        if (log.isErrorEnabled()) {
            log.error(e.getMessage(), e);
        }
        ApiResult<Object> result = ApiResult.failed("NO_HANDLER_FOUND");
        result.setRequestPath(request.getRequestURI());
        String timestamp = HttpRequestContextHolder.getHeader(REQUEST_HEADER_TIMESTAMP);
        if (StringUtils.isNotBlank(timestamp)) {
            result.setElapsed(System.currentTimeMillis() - Long.parseLong(timestamp));
        }
        return new ResponseEntity<ApiResult<?>>(result, HttpStatus.NOT_FOUND);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResult<?>> handleException(HttpServletRequest request,
            HttpServletResponse response, Exception e) {
        if (log.isErrorEnabled()) {
            log.error(e.getMessage(), e);
        }
        Throwable te = exceptionTransferer.transfer(e);
        ApiResult<Object> result;
        if (te instanceof BizException) {
            BizException bte = (BizException) te;
            result = ApiResult.failed(
                    getErrorMessage(bte.getErrorCode(), LangUtils.toObjectArray(bte.getArg())),
                    bte.getErrorCode().getCode(), bte.getArg());
        } else {
            result = ApiResult.failed("INTERNAL_SERVER_ERROR: " + te.getMessage());
        }
        result.setRequestPath(request.getRequestURI());
        String timestamp = HttpRequestContextHolder.getHeader(REQUEST_HEADER_TIMESTAMP);
        if (StringUtils.isNotBlank(timestamp)) {
            result.setElapsed(System.currentTimeMillis() - Long.parseLong(timestamp));
        }
        return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private String getErrorMessage(ErrorCode errorCode, Object[] args) {
        Locale locale = HttpRequestContextHolder.getLocale();
        return messageLocalization.getMessage(errorCode, locale, args);
    }


}
