package com.haogrgr.test.interceptor;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 * 拦截
 */
public class AuthInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpServletRequest wrapper = new HttpServletRequestWrapper(request) {
            @Override
            @SuppressWarnings("unchecked")
            public Map<Object, Object> getParameterMap() {
                return super.getParameterMap();
            }
        };
        return super.preHandle(wrapper, response, handler);
    }

}
