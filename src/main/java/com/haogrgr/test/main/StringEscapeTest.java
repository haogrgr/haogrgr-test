package com.haogrgr.test.main;

import org.apache.commons.lang3.StringEscapeUtils;

public class StringEscapeTest {

	public static void main(String[] args) throws Exception {
		String unescape = StringEscapeUtils.unescapeJava("\u0049");
		System.out.println(unescape);

	}

}
