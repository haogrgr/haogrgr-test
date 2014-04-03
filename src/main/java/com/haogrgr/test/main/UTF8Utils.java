package com.haogrgr.test.main;

import org.apache.commons.io.IOUtils;

public class UTF8Utils {

    public static void main(String[] args) throws Exception {
        String json = IOUtils.toString(UTF8Utils.class.getResourceAsStream("/json.json"));
        System.out.println(json);
        
    }

}
