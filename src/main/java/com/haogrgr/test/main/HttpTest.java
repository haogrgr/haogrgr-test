package com.haogrgr.test.main;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;

import com.haogrgr.test.util.HttpUtils;

public class HttpTest {

    public static void main(String[] args) throws Exception {
        HttpHost proxy = new HttpHost("127.0.0.1", 8888);
        CloseableHttpClient client = HttpUtils.getClient(proxy);
        
        HttpPost doLogin = new HttpPost("http://www.job1001.com/CheckUserNew.php?uname=qazwsxedcrfv%40163.com&password=123456");
        doLogin.addHeader("Referer", "http://www.job1001.com/index.php");
        HttpEntity pentity = new StringEntity("+++++++", "UTF-8");
        doLogin.setEntity(pentity);
        
        HttpResponse execute = client.execute(doLogin);
        HttpEntity entity = execute.getEntity();
        String content = EntityUtils.toString(entity);
        System.err.println(content);
        
        HttpGet mainPage = new HttpGet("http://www.job1001.com/myNew/admin.php");
        execute = client.execute(mainPage);
        entity = execute.getEntity();
        content = EntityUtils.toString(entity);
        System.out.println(content);
        
        client.close();
    }
    
}
