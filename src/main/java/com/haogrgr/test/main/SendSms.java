package com.haogrgr.test.main;

import java.io.IOException;
import java.util.HashMap;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.http.HttpEntity;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;

import com.haogrgr.test.util.HttpUtils;

public class SendSms {

    private static String URL = "http://106.ihuyi.com/webservice/sms.php?method=Submit";

    public static void main(String[] args) {
        CloseableHttpClient client = HttpUtils.getClient();

        HttpPost post = new HttpPost(URL);

        HashMap<String, String> param = new HashMap<String, String>();
        param.put("account", "cf_ystz");
        param.put("password", DigestUtils.md5Hex("ystz123"));//32位MD5
        param.put("mobile", "15921785544");
        param.put("content", "您的验证码是：测试中文和长度。请不要把验证码泄露给其他人。");
        post.setEntity(HttpUtils.getFromEntity(param));
        try {
            CloseableHttpResponse response = client.execute(post);
            StatusLine statusLine = response.getStatusLine();
            if (statusLine.getStatusCode() == 200) {
                HttpEntity entity = response.getEntity();
                String text = EntityUtils.toString(entity);
                System.out.println(text);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                client.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
