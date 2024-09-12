package com.fred.api.utils;

import org.springframework.stereotype.Component;

/**
 * 
 * @Description: HtmlMessageTemplate
 * @Author: Fred Feng
 * @Date: 11/09/2024
 * @Version 1.0.0
 */
@Component("htmlTemplate")
public class HtmlMessageTemplate extends TextMessageTemplate {

    @Override
    public boolean isHtml() {
        return true;
    }
}
