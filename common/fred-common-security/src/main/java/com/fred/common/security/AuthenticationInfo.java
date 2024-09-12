package com.fred.common.security;

import java.time.LocalDateTime;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * 
 * @Description: AuthenticationInfo
 * @Author: Fred Feng
 * @Date: 10/09/2024
 * @Version 1.0.0
 */
@NoArgsConstructor
@Getter
@Setter
@ToString
public class AuthenticationInfo {

    private Long id;
    private String username;
    private String platform;
    private LocalDateTime loginTime;
    private String ipAddress;
    private GrantedAuthorityInfo[] grantedAuthorities;

    private boolean firstLogin;
    private Map<String, Object> additionalInformation;

    public AuthenticationInfo(Long id, String username, String platform, String ipAddress,
            GrantedAuthorityInfo[] grantedAuthorities) {
        this.id = id;
        this.username = username;
        this.platform = platform;
        this.ipAddress = ipAddress;
        this.grantedAuthorities = grantedAuthorities;
        this.loginTime = LocalDateTime.now();
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    @ToString
    public static class GrantedAuthorityInfo {

        private String role;
        private String[] permissions;
        private String[] opTypes;

    }
}
