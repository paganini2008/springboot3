package com.fred.common.security;

import org.springframework.security.core.AuthenticationException;

/**
 * 
 * @Description: NoOpLoginFailureListener
 * @Author: Fred Feng
 * @Date: 12/09/2024
 * @Version 1.0.0
 */
public class NoOpLoginFailureListener implements LoginFailureListener {

	@Override
	public void onTryAgain(AuthenticationException e) {
	}

	@Override
	public void onDisabled(AuthenticationException e) {
	}

	@Override
	public void onLocked(AuthenticationException e) {
	}
}