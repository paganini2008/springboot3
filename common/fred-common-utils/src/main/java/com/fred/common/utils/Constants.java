package com.fred.common.utils;

import java.time.format.DateTimeFormatter;

/**
 * 
 * @Description: Constants
 * @Author: Fred Feng
 * @Date: 11/09/2024
 * @Version 1.0.0
 */
public interface Constants {

    String VERSION = "1.0.0-SNAPSHOT";

    int SERVER_PORT_START_WITH = 39000;
    int SERVER_PORT_END_WITH = 40000;
    String REQUEST_HEADER_REQUEST_ID = "__request_id__";
    String REQUEST_HEADER_TIMESTAMP = "__timestamp__";
    String REQUEST_HEADER_TRACES = "__traces__";
    String REQUEST_HEADER_TRACE_ID = "__trace_id__";
    String REQUEST_HEADER_SPAN_ID = "__span_id__";
    String REQUEST_HEADER_PARENT_SPAN_ID = "__parent_span_id__";
    String REQUEST_HEADER_API_REALM = "__api__";

    Integer COMMON_VALID = 1;
    Integer COMMON_INVALID = 0;
    Integer COMMON_EXPIRED = -1;

    long DEFAULT_MAXIMUM_RESPONSE_TIME = 3L * 1000;

    String REQUEST_HEADER_ENDPOINT_SECURITY_KEY = "ENDPOINT_SECURITY_KEY";
    String REQUEST_HEADER_REST_CLIENT_SECURITY_KEY = "REST_CLIENT_SECURITY_KEY";

    String ISO8601_DATE_TIME_PATTERN = "yyyy-MM-dd'T'HH:mm:ss'Z'";
    String DEFAULT_DATE_PATTERN = "yyyy-MM-dd";
    String DEFAULT_DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
    String[] SUPPORTED_DATE_TIME_PATTERNS = {"yyyy-MM-dd'T'HH:mm:ss", "yyyy-MM-dd'T'HH:mm:ss.S",
            "yyyy-MM-dd'T'HH:mm:ss.SXXX", "yyyy-MM-dd'T'HH:mm:ss'Z'", "yyyy-MM-dd HH:mm:ss",
            "yyyy-MM-dd.HH:mm:ss", "yyyy-MM-dd", "dd/MM/yyyy HH:mm:ss", "dd/MM/yyyy", "dd/MMM/yyyy",
            "yyyyMMddHHmmss", "yyyyMMdd", "yyyy-MM-dd HH:mm:ss.SSS", "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",
            "yyyy-MM-dd HH:mm:ss Z"};

    String URL_PATTERN_PING = "%s://%s:%d%s/ping";

    String USER_ATTRIBUTE_MY_PROFILE = "MY_PROFILE";
    String PUBSUB_CHANNEL_FANOUT_CHAT_ENABLED = "FANOUT-CHAT-ENABLED";


    DateTimeFormatter DTF_YMD = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    DateTimeFormatter DTF_YMD_HMS = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    DateTimeFormatter DTF_YMDHMS = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");


}
