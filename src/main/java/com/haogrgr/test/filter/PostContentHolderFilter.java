package com.haogrgr.test.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

public class PostContentHolderFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        PostContentHolderRequestWrapper wrapper = new PostContentHolderRequestWrapper(request);
        //wrapper.getWrapperStream().getAllCacheDate();
        chain.doFilter(wrapper, response);
    }

    @Override
    public void destroy() {
    }

}
