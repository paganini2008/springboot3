package com.fred.common.security;

import static com.fred.common.security.SecurityConstants.LOGIN_KEY;
import static com.fred.common.security.SecurityConstants.TOKEN_KEY;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import com.fred.common.utils.WebUtils;

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
    private final LocalCache localCache;
    private final SecurityClientProperties securityClientProperties;

    @Override
    public String signIn(AbstractAuthenticationToken authenticationToken,
                         HttpServletRequest request,
                         HttpServletResponse response) {
        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        IdentifiableUserDetails user = (IdentifiableUserDetails) authentication.getPrincipal();
        String token = tokenStrategy.encode(user, securityClientProperties.getExpiration());
        String key = String.format(LOGIN_KEY, user.getEmail());
        cleanToken(user, request, response);
        localCache.putObject(key, token);
        AuthenticationInfo authInfo = new AuthenticationInfo(user.getId(), user.getUsername(), user.getEmail(),
                WebUtils.getIpAddr(request),
                SecurityUtils.getGrantedAuthorities(user.getAuthorities()));
        authInfo.setAdditionalInformation(user.getAdditionalInformation());
        key = String.format(TOKEN_KEY, token);
        localCache.putObject(key, authInfo);
        return token;
    }

    @Override
    public IdentifiableUserDetails authenticate(AbstractAuthenticationToken authenticationToken) throws AuthenticationException {
        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        return (IdentifiableUserDetails) authentication.getPrincipal();
    }

    private boolean cleanToken(IdentifiableUserDetails user, HttpServletRequest request, HttpServletResponse response) {
        String key = String.format(LOGIN_KEY, user.getEmail());
        if (localCache.hasKey(key)) {
            String oldToken = (String) localCache.getObject(key);
            localCache.removeObject(key);
            if (StringUtils.isNotBlank(oldToken)) {
                key = String.format(TOKEN_KEY, oldToken);
                localCache.removeObject(key);
            }
            if (log.isInfoEnabled()) {
                log.info("Force logout from system. Token: {}", oldToken);
            }
            return true;
        }
        return false;
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