package com.fred.api;

import com.fred.common.utils.SimpleErrorCode;

/**
 * 
 * @Description: ErrorCodes of User
 * @Author: Fred Feng
 * @Date: 11/09/2024
 * @Version 1.0.0
 */
public class UserErrorCodes {

    public static final SimpleErrorCode EMAIL_EXISTED =
            new SimpleErrorCode("EMAIL_EXISTED", 1001001, "Email already existed");

    public static final SimpleErrorCode EMAIL_SETTING_FAULT =
            new SimpleErrorCode("EMAIL_SETTING_FAULT", 1001002, "Email setting faults");

    public static final SimpleErrorCode EMAIL_SENDING_FAULT = new SimpleErrorCode(
            "EMAIL_SENDING_FAULT", 1001003, "Failed to send email in the background");

}
