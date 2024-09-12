package com.fred.common.security;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @Description: WebUserAuthenticationService
 * @Author: Fred Feng
 * @Date: 10/09/2024
 * @Version 1.0.0
 */
@Slf4j
@RequiredArgsConstructor
public class WebUserAuthenticationService implements AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final TokenStrategy tokenStrategy;
    private final SecurityClientProperties securityClientProperties;

    @Override
    public String signIn(AbstractAuthenticationToken authenticationToken,
                         HttpServletRequest request,
                         HttpServletResponse response) {
        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        IdentifiableUserDetails user = (IdentifiableUserDetails) authentication.getPrincipal();
        return tokenStrategy.encode(user, securityClientProperties.getExpiration());
    }

    @Override
    public IdentifiableUserDetails authenticate(AbstractAuthenticationToken authenticationToken) throws AuthenticationException {
        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        return (IdentifiableUserDetails) authentication.getPrincipal();
    }

    @Override
    public void signOut(HttpServletRequest request, HttpServletResponse response) {
        WebUser user = null;
        try {
            user = SecurityUtils.getCurrentUser();
        } finally {

        }
    }

}