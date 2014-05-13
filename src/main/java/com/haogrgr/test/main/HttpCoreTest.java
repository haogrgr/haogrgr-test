package com.haogrgr.test.main;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;

import com.haogrgr.test.util.HttpUtils;

public class HttpCoreTest {

    public static void main(String[] args) throws Exception {
        CloseableHttpClient client = HttpUtils.getClient();
        
        HttpGet get = new HttpGet("https://www.yeepay.com/app-merchant-proxy/node");
        
        CloseableHttpResponse execute = client.execute(get);
        
        HttpEntity entity = execute.getEntity();
        
        String content = EntityUtils.toString(entity);
        
        System.out.println(content);
        
        client.close();
    }

}
