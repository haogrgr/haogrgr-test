package com.haogrgr.test.main;

import java.net.URISyntaxException;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.message.BasicHeader;

import com.haogrgr.test.util.HttpUtils;
import com.haogrgr.test.util.MD5Utils;

public class TranslationMain {

	public static void main(String[] args) {
		baidu_py();

	}

	/**
	 * 百度翻译:http://api.fanyi.baidu.com/api/trans/product/apidoc
	 * 
	 * 26中语言
	 * 
	 * 若当月翻译字符数≤2百万，当月免费；若超过2百万字符，按照49元人民币/百万字符，支付当月全部翻译字符数费用
	 * 
	 * 注册成为开发者比较容易
	 * 
	 * 翻译效果可以
	 */
	//{"from":"zh","to":"en","trans_result":[{"src":"你好","dst":"Hello"}]}
	public static void baidu_1() {
		URIBuilder url = builder("http://api.fanyi.baidu.com/api/trans/vip/translate");
		String appid = "20160503000020127";
		String token = "SZ8sAknPqd87qFo7V3VW";
		String salt = "1435660288";
		String q = "你好";
		String sign = MD5Utils.md5Hex(appid + q + salt + token);

		url.addParameter("q", q);
		url.addParameter("from", "zh");
		url.addParameter("to", "en");
		url.addParameter("appid", appid);
		url.addParameter("salt", salt);
		url.addParameter("sign", sign);

		String ret = exec(url);
		System.out.println(ret);
	}

	/**
	 * http://www.thinkphp.cn/code/1152.html
	 * 
	 * 网上的百度免费的翻译API, 应该是百度句库的API
	 * 
	 * 限制不名
	 * 
	 * 翻译效果可以
	 * 
	 * 结果会比较长
	 */
	//{"trans_result":{"from":"zh","to":"en","domain":"all","type":2,"status":0,"data":[{"dst":"Hello","src":"你好","relation":[],"result":[[0,"Hello",["0|6"],[],[
	public static void baidu_2() {
		URIBuilder url = builder("http://fanyi.baidu.com/v2transapi");

		url.addParameter("query", "你好");
		url.addParameter("from", "zh");
		url.addParameter("to", "en");

		String ret = exec(url);
		System.out.println(ret);
	}

	/**
	 * 谷歌翻译 : https://cloud.google.com/translate/v2/quickstart
	 */
	public static void guge() {}

	/**
	 * 有道翻译 : http://fanyi.youdao.com/openapi?path=data-mode
	 * 
	 * 免费, 每小时1000次, 超过会被封, 需要单独联系有道
	 * 
	 * 中英互译
	 * 
	 * 结果较长, 效果还行
	 */
	//{"translation":["How are you"],"basic":{"phonetic":"nǐ hǎo","explains":["hello；hi"]},"query":"你好","errorCode":0,"web":[{"value":["Hello","How do you do","hi"],"key":"你好"},{"value":["How are you","How Do You Do","Harvey, how are you Harvey"],"key":"你好吗"},{"value":["Teacher Kim Bong-du","My Teacher Mr Kim","Seonsaeng Kim Bong-du"],"key":"老师你好"}]}
	public static void youdao_1() {
		URIBuilder url = builder(
				"http://fanyi.youdao.com/openapi.do?keyfrom=MouseTranslate&key=660665783&type=data&doctype=json&version=1.1");
		url.addParameter("q", "你好");

		String ret = exec(url);
		System.out.println(ret);
	}

	public static void jinsan_1() {
		URIBuilder url = builder("http://dict-co.iciba.com/api/dictionary.php");
		url.addParameter("w", "你好");
		url.addParameter("key", "23218F6FEC8C348190F3010FF45BE82D");

		String ret = exec(url);
		System.out.println(ret);
	}

	/**
	 * 百度拼音:http://apistore.baidu.com/apiworks/servicedetail/1124.html
	 */
	//{"status":1,"str":"涂得胜","pinyin":"tu de sheng"}
	public static void baidu_py() {
		URIBuilder url = builder(
				"http://apis.baidu.com/xiaogg/changetopinyin/topinyin?type=json&traditional=0&accent=0&letter=0&only_chinese=0");
		url.addParameter("str", "涂得胜");

		String ret = exec(url, new BasicHeader("apikey", "7cafdb4b22393dd9e66c7e48630c21d1"));
		System.out.println(ret);
	}

	public static URIBuilder builder(String url) {
		URIBuilder builder;
		try {
			builder = new URIBuilder(url);
		} catch (URISyntaxException e) {
			throw new RuntimeException(e);
		}
		return builder;
	}

	public static String exec(URIBuilder url) {
		HttpPost request;
		try {
			request = new HttpPost(url.build());
		} catch (URISyntaxException e) {
			throw new RuntimeException(e);
		}
		String ret = HttpUtils.exec(request);
		return StringEscapeUtils.unescapeJava(ret);
	}

	public static String exec(URIBuilder url, BasicHeader header) {
		HttpPost request;
		try {
			request = new HttpPost(url.build());
			request.addHeader(header);
		} catch (URISyntaxException e) {
			throw new RuntimeException(e);
		}
		String ret = HttpUtils.exec(request);
		return StringEscapeUtils.unescapeJava(ret);
	}

}
