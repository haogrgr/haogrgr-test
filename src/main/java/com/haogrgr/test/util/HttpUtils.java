package com.haogrgr.test.util;

import java.io.File;
import java.io.FileInputStream;
import java.nio.charset.Charset;
import java.security.KeyStore;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.SSLContext;

import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.config.RequestConfig.Builder;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.BasicHttpClientConnectionManager;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;

/**
 * httpclient工具类 
 * <p>Description: httpclient版本4.4</p>
 * <p>Author: desheng.tu</p>
 * <p>Date: 2014年3月18日</p>
 */
public class HttpUtils {

    public static final String USER_AGENT_FIREFOX = "Mozilla/5.0 (Windows; U; Windows NT 6.1; zh-CN; rv:1.9.2.15) Gecko/20110303 Firefox/3.6.15";
    public static final String USER_AGENT_CHROME = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/31.0.1650.57 Safari/537.36";
    public static final String USER_AGENT_IE9 = "Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; Win64; x64; Trident/5.0)";
    public static final String USER_AGENT_APPLE6 = "Mozilla/5.0 (iPhone; CPU iPhone OS 8_0 like Mac OS X) AppleWebKit/600.1.3 (KHTML, like Gecko) Version/8.0 Mobile/12A4345d Safari/600.1.4";
    public static final String USER_AGENT = USER_AGENT_CHROME;

    public static final Integer DEFAULT_CONN_REQ_TIMEOUT = 3000;//向链接池请求链接超时时间(毫秒)
    public static final Integer DEFAULT_CONN_TIMEOUT = 3000;//与主机建立链接的超时时间(毫秒)
    public static final Integer DEFAULT_SO_TIMEOUT = 5000;//建立链接后,传输等待超时时间(毫秒)
    public static final Integer DEFAULT_CONN_POOL_SIZE = 200;//链接池大小

    public static void main(String[] args) throws Exception {
        CloseableHttpClient client = getSSLClient("haogrgr.keystore", "haogrgr");
        HttpGet get = new HttpGet("https://kyfw.12306.cn/otn/");
        CloseableHttpResponse exec = client.execute(get);
        HttpEntity entity = exec.getEntity();
        if (entity != null) {
            String content = EntityUtils.toString(entity);
            System.out.println(content);
        }
        
        client.close();
    }

    public static CloseableHttpClient getClient() {
        return getClient(null);
    }

    /**
     * 获取默认的HttpClient对象
     * @param proxy 代理host
     * @return
     */
    public static CloseableHttpClient getClient(HttpHost proxy) {
        HttpClientBuilder builder = getClientBuilder(proxy);
        return builder.build();
    }

    public static CloseableHttpClient getThreadSafeClient() {
        return getThreadSafeClient(null);
    }
    
    /**
     * 获取线程安全的HttpClient对象
     * @param proxy 代理host
     * @return
     */
    public static CloseableHttpClient getThreadSafeClient(HttpHost proxy) {
        PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
        cm.setMaxTotal(DEFAULT_CONN_POOL_SIZE);
        HttpClientBuilder builder = getClientBuilder(proxy).setConnectionManager(cm);
        return builder.build();
    }
    
    public static CloseableHttpClient getSSLClient(File keyStoreFile, String pwd){
        return getSSLClient(null, false, keyStoreFile, pwd);
    }
    
    public static CloseableHttpClient getThreadSafeSSLClient(File keyStoreFile, String pwd){
        return getSSLClient(null, true, keyStoreFile, pwd);
    }
    
    public static CloseableHttpClient getSSLClient(String keyStoreFile, String pwd){
        File stroeFile = getStroeFile(keyStoreFile);
        return getSSLClient(null, false, stroeFile, pwd);
    }
    
    public static CloseableHttpClient getThreadSafeSSLClient(String keyStoreFile, String pwd){
        File stroeFile = getStroeFile(keyStoreFile);
        return getSSLClient(null, true, stroeFile, pwd);
    }
    
    public static CloseableHttpClient getSSLClient(HttpHost proxy, boolean threadSafe, String keyStoreFile, String pwd) {
        File stroeFile = getStroeFile(keyStoreFile);
        return getSSLClient(proxy, threadSafe, stroeFile, pwd);
    }
    
    /**
     * 获取支持Https的HttpClient对象
     * @param proxy 代理host
     * @param threadSafe 是否使用线程安全的HttpClient
     * @param keyStoreFile 信任证书库文件
     * @param pwd 证书库密码
     * @return
     */
    public static CloseableHttpClient getSSLClient(HttpHost proxy, boolean threadSafe, File keyStoreFile, String pwd) {
        HttpClientBuilder builder = getClientBuilder(proxy);
        try {
            KeyStore trustStore = getTrustKeyStore(keyStoreFile, pwd);
            
            SSLContext sslContext = SSLContexts.custom().loadTrustMaterial(trustStore, new TrustSelfSignedStrategy()).build();
            SSLConnectionSocketFactory sslConnFactory = new SSLConnectionSocketFactory(sslContext);

            RegistryBuilder<ConnectionSocketFactory> registryBuilder = RegistryBuilder.create();
            Registry<ConnectionSocketFactory> registry = registryBuilder.register("https", sslConnFactory).build();
            
            HttpClientConnectionManager cm = null;
            if(threadSafe){
                cm = new PoolingHttpClientConnectionManager(registry);
                ((PoolingHttpClientConnectionManager)cm).setMaxTotal(DEFAULT_CONN_POOL_SIZE);
            }else{
                cm = new BasicHttpClientConnectionManager(registry);
            }

            builder.setConnectionManager(cm);
        } catch (Exception e) {
            throw new RuntimeException("初始化HttpClient实例失败!", e);
        }

        return builder.build();
    }
    
    public static void close(CloseableHttpClient client){
        try{
            client.close();
        }catch(Exception e){
            throw new RuntimeException("关闭实例失败", e);
        }
    }
    
    /**
     * 将查询字符串附加到url后面
     * @param url url
     * @param query 查询字符串
     * @return 拼接后的url
     */
    public static String appendQureyString(String url, String query){
        if(isEmpty(url)){
            throw new IllegalArgumentException("the argument url is empty");
        }
        if(isEmpty(query)){
            return url;
        }
        StringBuilder sb = new StringBuilder(url);
        if(url.endsWith("?")){
            sb.append(query.startsWith("?") ? query.substring(1) : query);
        }else{
            sb.append("?").append(query.startsWith("?") ? query.substring(1) : query);
        }
        return sb.toString();
    }
    
    /**
     * 将参数转换为查询串
     * @param paramMap 参数map
     * @param charset 编码类型
     * @return 例如: key1=value1&key2=value2
     */
    public static String toUrlEncodeQueryString(Map<String, String> paramMap, String charset){
        List <NameValuePair> nvps = fromMap(paramMap);
        String query = URLEncodedUtils.format(nvps, charset);
        return query;
    }
    
    public static String toUrlEncodeQueryString(Map<String, String> paramMap){
        return toUrlEncodeQueryString(paramMap, "UTF-8");
    }
    
    public static List <NameValuePair> fromMap(Map<String, String> paramMap){
        List <NameValuePair> nvps = new ArrayList <NameValuePair>(paramMap.size() + 1);
        
        for (Map.Entry<String, String> entry : paramMap.entrySet()) {
            String key = entry.getKey();
            if(key != null && key.trim().length() > 0){
                nvps.add(new BasicNameValuePair(key, entry.getValue()));
            }
        }
        
        return nvps;
    }
    
    public static Map<String, String> parseQueryString(String queryString, String charset){
        List<NameValuePair> nvps = URLEncodedUtils.parse(queryString, Charset.forName(charset));
        return toMap(nvps);
    }
    
    public static Map<String, String> toMap(List <NameValuePair> nvps){
        HashMap<String, String> map = new HashMap<String, String>();
        
        for (NameValuePair pair : nvps) {
            map.put(pair.getName(), pair.getValue());
        }
        
        return map;
    }
    
    public static String entityToString(HttpEntity entity){
        return entityToString(entity, null);
    }
    
    /**
     * 将响应实体转换为String
     * @param entity 响应实体
     * @param charset 字符集
     */
    public static String entityToString(HttpEntity entity, String charset){
        if(entity == null){
            return null;
        }
        try {
            String content = null;
            if(charset != null){
                content = EntityUtils.toString(entity, charset);
            }else{
                content = EntityUtils.toString(entity);
            }
            return content;
        } catch (Exception e) {
            throw new RuntimeException("获取响应正文失败", e);
        }
    }
    
    public static UrlEncodedFormEntity getFromEntity(Map<String, String> paramMap){
        List<NameValuePair> nvps = fromMap(paramMap);
        UrlEncodedFormEntity entity = new UrlEncodedFormEntity(nvps, Consts.UTF_8);
        return entity;
    }
    
    public static StringEntity getStringEntity(String content, String charset){
        StringEntity entity = new StringEntity(content, charset);
        return entity;
    }
    
    public static StringEntity getStringEntity(String content){
        return getStringEntity(content, Consts.UTF_8.name());
    }
    
    public static HttpClientBuilder getClientBuilder() {
        return getClientBuilder(null);
    }
    
    public static String exec(HttpRequestBase request){
    	try(CloseableHttpClient client = getClient()){
    		CloseableHttpResponse response = client.execute(request);
    		String result = entityToString(response.getEntity());
    		return result;
    	}catch(Exception e){
    		throw new RuntimeException(e);
    	}
    }
    
    /**
     * 获取HttpClientBuilder对象,默认UserAgent为chrome
     * 默认ConnectTimeout DEFAULT_CONN_TIMEOUT 1000
     * 默认SocketTimeout DEFAULT_SO_TIMEOUT 3000
     * @param proxy Http代理
     * @return
     */
    public static HttpClientBuilder getClientBuilder(HttpHost proxy) {
        HttpClientBuilder builder = HttpClients.custom().setUserAgent(USER_AGENT);
        if (proxy != null) {
            builder.setProxy(proxy);
        }

        Builder requestConfigBuilder = RequestConfig.custom();
        requestConfigBuilder.setConnectionRequestTimeout(DEFAULT_CONN_REQ_TIMEOUT);
        requestConfigBuilder.setConnectTimeout(DEFAULT_CONN_TIMEOUT);
        requestConfigBuilder.setSocketTimeout(DEFAULT_SO_TIMEOUT);

        builder.setDefaultRequestConfig(requestConfigBuilder.build());
        
        return builder;
    }
    
    /**
     * 获取证书库文件
     * @param keyStorePath 路径,相当路径(classpath : /keyStorePath),或绝对路径
     * @return
     */
    private static File getStroeFile(String keyStorePath) {
        File keyStoreFile = new File(keyStorePath);
        if(!keyStoreFile.isAbsolute()){
            keyStoreFile = new File(HttpUtils.class.getResource("/").getFile() + keyStorePath);
        }
        return keyStoreFile;
    }
    
    /**
     * 导入证书到证书库中:<br>
     * keytool -import -alias 12306 -keystore haogrgr.keystore -file c:\tmp\12306.cer<br>
     * -keystore haogrgr.keystore 如果不存在haogrgr.keystore则会自动创建改文件
     * @param keyStoreFile eg: haogrgr.keystore
     * @param pwd 证书库密码
     * @return
     */
    private static KeyStore getTrustKeyStore(File keyStoreFile, String pwd) throws Exception {
        KeyStore trustStore = null;
        FileInputStream instream = null;
        try {
            trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            instream = new FileInputStream(keyStoreFile);
            trustStore.load(instream, pwd.toCharArray());
        } catch (Exception e) {
            throw new RuntimeException("加载信任证书库失败!", e);
        } finally {
            instream.close();
        }
        return trustStore;
    }
    
    private static boolean isEmpty(String str){
        return str == null || str.trim().length() == 0;
    }
}
