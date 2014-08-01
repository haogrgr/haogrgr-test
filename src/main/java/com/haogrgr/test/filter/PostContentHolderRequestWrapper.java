package com.haogrgr.test.filter;

import java.io.IOException;

import javax.servlet.ServletInputStream;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

public class PostContentHolderRequestWrapper extends HttpServletRequestWrapper {

    private PostContentHolderInputStream wrapper;

    public PostContentHolderRequestWrapper(ServletRequest request) {
        super((HttpServletRequest) request);
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        initWrapper();
        return wrapper;
    }
    
    public PostContentHolderInputStream getWrapperStream() throws IOException{
        initWrapper();
        return this.wrapper;
    }
    
    private void initWrapper() throws IOException{
        if (wrapper == null) {
            wrapper = new PostContentHolderInputStream(super.getInputStream());
        }
    }
}
