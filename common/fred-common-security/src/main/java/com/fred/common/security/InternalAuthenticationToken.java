package com.fred.common.security;

import java.util.Collection;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.SpringSecurityCoreVersion;
import org.springframework.security.core.authority.AuthorityUtils;

/**
 * 
 * @Description: InternalAuthenticationToken
 * @Author: Fred Feng
 * @Date: 11/09/2024
 * @Version 1.0.0
 */
public class InternalAuthenticationToken extends AbstractAuthenticationToken implements WebToken {

    private static final long serialVersionUID = SpringSecurityCoreVersion.SERIAL_VERSION_UID;

    private final Object principal;
    private final Object credentials;

    InternalAuthenticationToken(Object principal, Object credentials) {
        this(principal, credentials, AuthorityUtils.NO_AUTHORITIES);
    }

    InternalAuthenticationToken(Object principal, Object credentials,
            Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.principal = principal;
        this.credentials = credentials;
        super.setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return this.credentials;
    }

    @Override
    public Object getPrincipal() {
        return this.principal;
    }
}
