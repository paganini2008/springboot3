package com.fred.common.security;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.security.core.SpringSecurityCoreVersion;
import org.springframework.security.core.userdetails.User;

/**
 * 
 * @Description: WebUser
 * @Author: Fred Feng
 * @Date: 10/09/2024
 * @Version 1.0.0
 */
public class WebUser extends User implements IdentifiableUserDetails {

    private static final long serialVersionUID = SpringSecurityCoreVersion.SERIAL_VERSION_UID;

    public WebUser(Long id, String username, String password, String email) {
        this(id, username, password, email, true);
    }

    public WebUser(Long id, String username, String password, String email, boolean enabled) {
        this(id, username, password, email, enabled, SecurityUtils.NO_AUTHORITIES);
    }

    public WebUser(Long id, String username, String password, String email, boolean enabled,
            Collection<PermissionGrantedAuthority> authorities) {
        super(username, password, enabled, true, true, true, authorities);
        this.id = id;
        this.email = email;
        this.additionalInformation = new HashMap<>();
    }

    private final Long id;
    private final String email;
    private final Map<String, Object> additionalInformation;

    @Override
    public Map<String, Object> getAdditionalInformation() {
        return additionalInformation;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public String getEmail() {
        return email;
    }

    public String[] getRoles() {
        return getAuthorities().stream().map(au -> au.getAuthority()).toArray(l -> new String[l]);
    }

    public String[] getPermissions() {
        return getAuthorities().stream()
                .flatMap(au -> Arrays.stream(((PermissionGrantedAuthority) au).getPermissions()))
                .distinct().toArray(l -> new String[l]);
    }

    public List<PermissionGrantedAuthority> getPermissionGrantedAuthorities() {
        return getAuthorities().stream().map(au -> (PermissionGrantedAuthority) au)
                .collect(Collectors.toList());
    }

    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append(String.format("Id: %s, Email: %s\n", id, email));
        str.append(super.toString());
        return str.toString();
    }



}
