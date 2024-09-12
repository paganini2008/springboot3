package com.fred.common.utils;

import java.util.Locale;

/**
 * 
 * @Description: MessageLocalization
 * @Author: Fred Feng
 * @Date: 11/09/2024
 * @Version 1.0.0
 */
public interface MessageLocalization {

    String DEFAULT_LANGUAGE = "en";
    String DEFAULT_GROUP_NAME = "default";

    default String getMessage(ErrorCode errorCode, Locale locale, Object... args) {
        return getMessage(errorCode.getMessageKey(), locale, errorCode.getDefaultMessage(), args);
    }

    String getMessage(String messageKey, Locale locale, String defaultMessage, Object... args);
}