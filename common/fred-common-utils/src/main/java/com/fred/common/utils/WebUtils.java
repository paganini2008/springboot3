package com.fred.common.utils;

import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.ServletContext;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * 
 * @Description: WebUtils
 * @Author: Fred Feng
 * @Date: 11/09/2024
 * @Version 1.0.0
 */
@SuppressWarnings("all")
public abstract class WebUtils {

    private static final String UNKNOWN = "unknown";
    private static final String LOOPBACK_ADDRESS = "127.0.0.1";
    private static final String UNKNOWN_ADDRESS = "0:0:0:0:0:0:0:1";

    public static HttpHeaders copyHeaders(HttpServletRequest request) {
        HttpHeaders headers = new HttpHeaders();
        Collections.list(request.getHeaderNames()).forEach(headerName -> {
            Enumeration<String> en = request.getHeaders(headerName);
            headers.addAll(headerName, en != null ? Collections.list(en) : new ArrayList<>());
        });
        return headers;
    }

    public static HttpHeaders copyHeaders(HttpServletResponse response) {
        HttpHeaders headers = new HttpHeaders();
        response.getHeaderNames().forEach(headerName -> {
            Collection<String> c = response.getHeaders(headerName);
            headers.addAll(headerName, c != null ? new ArrayList<>(c) : new ArrayList<>());
        });
        return headers;
    }

    public static MultiValueMap<String, String> copyParameters(HttpServletRequest request) {
        Map<String, String[]> parameterMap = request.getParameterMap();
        if (MapUtils.isEmpty(parameterMap)) {
            return new LinkedMultiValueMap<>();
        }
        return parameterMap.entrySet().stream().collect(LinkedMultiValueMap::new,
                (m, e) -> m.put(e.getKey(), Arrays.asList(e.getValue())),
                LinkedMultiValueMap::putAll);
    }

    public static String toParameterString(HttpServletRequest request) {
        MultiValueMap<String, String> parameters = copyParameters(request);
        return parameters.entrySet().stream().flatMap(e -> {
            List<String> args = new ArrayList<>();
            e.getValue().forEach(val -> {
                args.add(e.getKey() + "=" + val);
            });
            return args.stream();
        }).collect(Collectors.joining("&"));
    }

    public static String getLang(HttpServletRequest request) {
        return getLang(request, Locale.getDefault().getLanguage());
    }

    public static String getLang(HttpServletRequest request, String defaultLang) {
        String lang = request.getHeader("lang");
        if (StringUtils.isBlank(lang)) {
            lang = request.getHeader("Lang");
        }
        return lang;
    }

    public static String getIpAddr() {
        return getRealIp(getRequiredRequest());
    }

    public static String getIpAddr(HttpServletRequest request) {
        if (request == null) {
            return null;
        }
        String ip = request.getHeader("x-forwarded-for");
        if (StringUtils.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (StringUtils.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (StringUtils.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (StringUtils.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (StringUtils.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }
        if (StringUtils.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    public static String getRealIp(HttpServletRequest request) {
        String ip = getIpAddr(request);
        if (request == null) {
            return ip;
        }
        if (StringUtils.isBlank(ip) || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (StringUtils.isBlank(ip) || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (StringUtils.isBlank(ip) || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
            if (LOOPBACK_ADDRESS.equals(ip) || UNKNOWN_ADDRESS.equals(ip)) {
                try {
                    InetAddress inetAddress = InetAddress.getLocalHost();
                    ip = inetAddress.getHostAddress();
                } catch (UnknownHostException e) {
                }
            }
        }
        int ipLength = 15;
        String separator = ",";
        if (ip != null && ip.length() > ipLength) {
            if (ip.indexOf(separator) > 0) {
                ip = ip.substring(0, ip.indexOf(","));
            }
        }
        return ip;
    }

    public static String getHostUrl(String url) {
        String hostUrl = "";
        try {
            URL u = new URL(url);
            hostUrl = u.getProtocol() + "://" + u.getHost();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return hostUrl;
    }

    public static String getContextPath(HttpServletRequest request) {
        return getHostUrl(request.getRequestURL().toString()) + ":" + request.getServerPort() + request.getContextPath();
    }

    public static boolean hasCurrentRequest() {
        try {
            return null != getRequiredRequest();
        } catch (RuntimeException ignored) {
            return false;
        }
    }

    public static HttpServletRequest getRequiredRequest() {
        try {
            return ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        } catch (RuntimeException e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    public static HttpServletRequest getRequest() {
        try {
            return getRequiredRequest();
        } catch (RuntimeException e) {
            return null;
        }
    }

    public static Cookie getCookie(String name) {
        HttpServletRequest request = getRequest();
        if (request == null) {
            return null;
        }
        Cookie[] cookies = request.getCookies();
        if (cookies != null && cookies.length > 0) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equalsIgnoreCase(name)) {
                    return cookie;
                }
            }
        }
        return null;
    }

    public static String getCookieValue(String name) {
        HttpServletRequest request = getRequest();
        if (request == null) {
            return null;
        }
        Cookie[] cookies = request.getCookies();
        if (cookies != null && cookies.length > 0) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equalsIgnoreCase(name)) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    public static boolean hasCookie(String name) {
        HttpServletRequest request = getRequest();
        if (request == null) {
            return false;
        }
        Cookie[] cookies = request.getCookies();
        if (cookies != null && cookies.length > 0) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equalsIgnoreCase(name)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static HttpSession getSession() {
        return getRequiredRequest().getSession();
    }

    public static <T> T getSessionAttr(String attrName) {
        return (T) getSession().getAttribute(attrName);
    }

    public static ServletContext getServletContext() {
        return getSession().getServletContext();
    }

    public static String getWebRoot(HttpServletRequest request) {
        String path = request.getSession().getServletContext().getRealPath("/");
        path = path.replace("\\", "/");
        return path;
    }
}