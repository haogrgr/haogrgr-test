package com.haogrgr.test.main;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.common.base.Charsets;
import com.google.common.io.Files;

public class MyqlToH2 {

	public static void main(String[] args) throws Exception {
		File file = new File("/Users/tudesheng/projects/haogrgr/haogrgr-test/src/test/resources/sql/test_schema2.sql");
		String content = Files.toString(file, Charsets.UTF_8);

		//设置模式为mysql
		content = "SET MODE MYSQL;\n\n" + content;

		//`不支持, 替换掉
		content = content.replaceAll("`", "");

		//`msg_body` varchar(5000) COLLATE utf8_bin NOT NULL, 中的COLLATE utf8_bin不兼容替换掉
		content = content.replaceAll("COLLATE.*(?=D)", "");

		//注释也替换掉吧, 不替换应该也没啥问题
		content = content.replaceAll("COMMENT.*'(?=,)", "");

		//) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin; 这一行也替换掉, ENGINE语法不支持
		content = content.replaceAll("\\).*ENGINE=InnoDB.*", ");");
		//content = content.replaceAll("\\).*ENGINE.*(?=;)", ")");
		
		//double(22,2)  不支持
		content = content.replaceAll("double\\(.*?\\)", "double");
		
		//KEY idx_cndcp_lg_scheme_cw126126 (channel_key,wh_res_code(255)) 不支持，鬼知道是什么语法, 去掉(255)
		content = content.replaceAll("(KEY .*?\\(.*?)\\(\\d+\\)(.*)", "$1$2");

		//时戳更新不支持, 修改为H2 AS CURRENT_TIMESTAMP语法
		content = content.replaceAll("DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP", " AS CURRENT_TIMESTAMP");

		//H2索引名得全局唯一, 替换为全局唯一
		content = uniqueKey(content);

		System.out.println(content);
	}

	/**
	 * h2的索引名必须全局唯一
	 * 
	 * @param content sql建表脚本
	 * @return 替换索引名为全局唯一
	 */
	private static String uniqueKey(String content) {
		int inc = 0;
		Pattern pattern = Pattern.compile("(?<=KEY )(.*?)(?= \\()");
		Matcher matcher = pattern.matcher(content);
		StringBuffer sb = new StringBuffer();
		while (matcher.find()) {
			matcher.appendReplacement(sb, matcher.group() + inc++);
		}
		matcher.appendTail(sb);
		content = sb.toString();
		return content;
	}

}
