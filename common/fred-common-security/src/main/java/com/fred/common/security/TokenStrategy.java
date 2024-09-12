package com.fred.common.security;

import org.springframework.security.core.userdetails.UserDetails;

/**
 * 
 * @Description: TokenStrategy
 * @Author: Fred Feng
 * @Date: 10/09/2024
 * @Version 1.0.0
 */
public interface TokenStrategy {

    String encode(UserDetails userDetails, long expiration);

    UserDetails decode(String token);

    default boolean validate(String token) {
        return true;
    }

}
