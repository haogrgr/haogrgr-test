package com.haogrgr.test.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class CustomObjectMapper extends ObjectMapper {

    private static final long serialVersionUID = 864720021023047299L;

    public static void main(String[] args) throws Exception {
        ObjectMapper mapper = new CustomObjectMapper();
        
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("key", new Date());
        
        mapper.writeValue(System.out, map);
    }

    public CustomObjectMapper() {
        super();
        this.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        this.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    }
    
}
