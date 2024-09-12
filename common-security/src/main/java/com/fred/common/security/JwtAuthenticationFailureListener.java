package com.fred.common.security;

/**
 * 
 * @Description: JwtAuthenticationFailureListener
 * @Author: Fred Feng
 * @Date: 12/09/2024
 * @Version 1.0.0
 */
public interface JwtAuthenticationFailureListener {

    default void onAuthenticationFailed(JwtAuthenticationException e) {
    }
	
}
