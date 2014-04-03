package com.haogrgr.test.main;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;

public class GetUrls {

    public static void main(String[] args) throws Exception {
        List<String> readUrls = readUrls();
        for (String str : readUrls) {
            System.out.println(str);
        }
    }

    public static List<String> readUrls() throws URISyntaxException, IOException {
        File file = new File(GetUrls.class.getResource("/url.txt").toURI());
        List<String> lines = FileUtils.readLines(file, "UTF-8");
        
        List<String> urls = new ArrayList<String>();
        Pattern pattern = Pattern.compile("Mapped [\"{].*(?=,methods)");
        for (String line : lines) {
            if (line == null || StringUtils.isBlank(line)) {
                continue;
            }
            Matcher matcher = pattern.matcher(line);
            if (matcher.find()) {
                String group = matcher.group().substring(9);
                String result = group.substring(1, group.length() - 1);
                urls.add(result);
            }
        }
        return urls;
    }

}
