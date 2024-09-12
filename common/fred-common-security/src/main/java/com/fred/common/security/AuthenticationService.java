package com.fred.common.security;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * 
 * @Description: AuthenticationService
 * @Author: Fred Feng
 * @Date: 10/09/2024
 * @Version 1.0.0
 */
public interface AuthenticationService {

    default String signIn(String username,
                          String password,
                          HttpServletRequest request,
                          HttpServletResponse response) {
        return signIn(new UsernamePasswordAuthenticationToken(username, password), request, response);
    }

    String signIn(AbstractAuthenticationToken authenticationToken,
                  HttpServletRequest request,
                  HttpServletResponse response);

    default IdentifiableUserDetails authenticate(String username, String password) {
        return authenticate(new UsernamePasswordAuthenticationToken(username, password));
    }

    IdentifiableUserDetails authenticate(AbstractAuthenticationToken authenticationToken) throws AuthenticationException;

    void signOut(HttpServletRequest request, HttpServletResponse response);

}
