package com.haogrgr.test.filter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;

public class PostContentHolderInputStream extends ServletInputStream {

    private ServletInputStream real;
    private ByteArrayOutputStream cache;

    public PostContentHolderInputStream(ServletInputStream real) {
        this.real = real;
        this.cache = new ByteArrayOutputStream();
    }

    @Override
    public int read() throws IOException {
        int b = real.read();
        if (b != -1) {
            cache.write(b);
        }
        return b;
    }

    public byte[] getAllCacheDate() throws IOException {
        while (read() != -1) {
            read();
        }
        return cache.toByteArray();
    }

    @Override
    public boolean isFinished() {
        return false;
    }

    @Override
    public boolean isReady() {
        return false;
    }

    @Override
    public void setReadListener(ReadListener arg0) {
        
    }

}
