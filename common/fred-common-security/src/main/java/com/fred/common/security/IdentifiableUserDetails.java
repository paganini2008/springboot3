package com.fred.common.security;

import java.util.Map;

import org.springframework.security.core.userdetails.UserDetails;

/**
 * 
 * @Description: IdentifiableUserDetails
 * @Author: Fred Feng
 * @Date: 10/09/2024
 * @Version 1.0.0
 */
public interface IdentifiableUserDetails extends UserDetails {

    Long getId();

    String getEmail();

    Map<String, Object> getAdditionalInformation();
}
