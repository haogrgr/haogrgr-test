package com.haogrgr.test.func;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.common.collect.Lists;

public class GoogleSearchEngine extends AbstractWebSearchEngine {
	
	public static void main(String[] args) {
		GoogleSearchEngine e = new GoogleSearchEngine();
		System.out.println(e.search("haogrgr"));
	}
	
	public GoogleSearchEngine() {
		super("http://202.86.162.41/search", "q");
	}

	@Override
	protected List<String> proccessResult(String html) {
		ArrayList<String> result = Lists.newArrayList();
		
		Pattern pattern = Pattern.compile("(?<=\\Q<h3 class=\"r\">\\E).*?(?=\\Q</h3>\\E?)");
		System.out.println(html);
		Matcher matcher = pattern.matcher(html.replaceAll("\n", ""));
		while(matcher.find()){
			String group = matcher.group();
			result.add(group);
		}
		
		return result;
	}

}
