package com.haogrgr.test.func;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.common.collect.Lists;

public class BaiduSearchEngine extends AbstractWebSearchEngine {
	
	public static void main(String[] args) {
		BaiduSearchEngine e = new BaiduSearchEngine();
		System.out.println(e.search("haogrgr"));
	}
	
	public BaiduSearchEngine() {
		super("http://www.baidu.com/s", "wd");
	}

	@Override
	protected List<String> proccessResult(String html) {
		ArrayList<String> result = Lists.newArrayList();
		
		Pattern pattern = Pattern.compile("(?<=\\Q<h3 class=\"t\">\\E).*?(?=\\Q</h3>\\E?)");
		Matcher matcher = pattern.matcher(html.replaceAll("\n", ""));
		while(matcher.find()){
			String group = matcher.group();
			result.add(group);
		}
		
		return result;
	}

}
