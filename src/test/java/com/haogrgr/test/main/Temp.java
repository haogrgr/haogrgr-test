package com.haogrgr.test.main;

import java.util.HashMap;

public class Temp {

    public static void main(String[] args) throws Exception {
        HashMap<Long, Integer> mapa = new HashMap<>();
        HashMap<Long, Integer> mapb = new HashMap<>();
        
        mapa.put(new Long(1L), new Integer(1));
        mapb.put(new Long(1L), new Integer(1));
        
        mapa.put(new Long(2L), new Integer(1));
        mapb.put(new Long(2L), new Integer(1));
        
        mapa.put(new Long(3L), new Integer(1));
        mapb.put(new Long(3L), new Integer(1));
        
        //mapa.put(new Long(1L), new Integer(1));
        
        System.out.println(Boolean.FALSE.equals(null));
    }

}
