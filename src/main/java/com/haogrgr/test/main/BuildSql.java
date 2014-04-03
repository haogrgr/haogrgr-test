package com.haogrgr.test.main;

import java.io.File;
import java.util.List;

import org.apache.commons.io.FileUtils;

public class BuildSql {

    public static void main(String[] args) throws Exception  {
        File file = new File(BuildSql.class.getResource("/data.txt").toURI());
        List<String> lines = FileUtils.readLines(file, "UTF-8");
        
        for (String line : lines) {
            System.out.println(line);
        }
        
    }

}
