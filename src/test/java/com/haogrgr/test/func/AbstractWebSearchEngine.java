package com.haogrgr.test.func;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;

import com.haogrgr.test.util.HttpUtils;

/**
 * 搜索引擎抽象类,提供一些公用方法,如请求http并返回html的方法
 */
public abstract class AbstractWebSearchEngine implements SearchEngine {
	
	private String url;//搜索链接如:www.baidu.com/s;
	private String paramName;//搜索条件的参数名 如: http://www.baidu.com/s?wd=haogrgr 其中paramName就是wd;
	
	public AbstractWebSearchEngine(String url, String paramName){
		this.url = url;
		this.paramName = paramName;
	}
	
	/**
	 * 查询指定关键字
	 * @param query 查询关键字
	 * @return 查询html结果
	 */
	protected Optional<String> request(String query){
		if(query == null || query.trim().isEmpty()){
			return Optional.empty();
		}
		
		try(CloseableHttpClient client = HttpUtils.getClient()){
			HttpGet request = new HttpGet(getQureyUrl(query));
			CloseableHttpResponse response = client.execute(request);
			StatusLine statusLine = response.getStatusLine();
			if(statusLine.getStatusCode() == HttpStatus.SC_OK){
				String html = HttpUtils.entityToString(response.getEntity());
				return Optional.ofNullable(html);
			}else{
				throw new Exception("非200响应 : " + statusLine.getStatusCode());
			}
		}catch(Exception e){
			throw new RuntimeException(String.format("请求%s?%s=%s发送错误!", url, paramName, query), e);
		}
	}
	
	/**
	 * 处理request方法返回的html,抽取结果
	 * @param html 查询结果
	 */
	protected abstract List<String> proccessResult(String html);
	
	@Override
	public List<String> search(String query) {
		Optional<String> r = request(query);
		return r.isPresent() ? proccessResult(r.get()) : Collections.emptyList();
	}
	
	private String getQureyUrl(String query){
		return url + "?" +  paramName + "=" + query;
	}
	
}
