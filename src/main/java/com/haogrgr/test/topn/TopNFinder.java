package com.haogrgr.test.topn;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

public class TopNFinder {

    static long start, end;

    static final int HASH_TABLE_SIZE = 512;

    static final String FILE_PATH = "C:/tmp/src.txt";
    static final String FILE_TEMP_PERFIX = "C:/tmp/temp"+HASH_TABLE_SIZE+"/temp_";

    static final BufferedWriter[] writers = new BufferedWriter[HASH_TABLE_SIZE];
    static final BufferedReader[] readers = new BufferedReader[HASH_TABLE_SIZE];
    static final Object[] results = new Object[HASH_TABLE_SIZE];

    static final ExecutorService executor = Executors.newFixedThreadPool(4);

    public static void main(String[] args) throws Exception {
        start = System.currentTimeMillis();
        
//        hashToFile();

        end = System.currentTimeMillis();
        System.out.println("hash cost :" + (end - start));

        topN(10);

        end = System.currentTimeMillis();
        System.out.println("total cost : " + (end - start));
        
    }
    
    @SuppressWarnings("unused")
    private static void hashToFile() throws FileNotFoundException, IOException {
        initWriter(HASH_TABLE_SIZE);
        FileReader r = new FileReader(FILE_PATH);
        BufferedReader br = new BufferedReader(r);

        String line = br.readLine();
        while (line != null) {
            BufferedWriter w = writers[index(line, HASH_TABLE_SIZE)];
            w.write(line + "\n");
            line = br.readLine();
        }

        br.close();
        r.close();

        for (int i = 0; i < HASH_TABLE_SIZE; i++) {
            writers[i].close();
        }
    }
    
    @SuppressWarnings({"rawtypes", "unchecked"})
    public static HashMap<String, Integer> topN(final int topSize) throws Exception {
        initReader(HASH_TABLE_SIZE);
        HashMap<String, Integer> result = new HashMap<String, Integer>();
        
        final AtomicInteger inc1 = new AtomicInteger();
        final AtomicInteger inc2 = new AtomicInteger();
        Future[] submits = new Future[HASH_TABLE_SIZE];
        for (final BufferedReader reader : readers) {
            Future<?> submit = executor.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        int index = inc1.getAndIncrement();
                        
                        HashMap<String, Integer> map = new HashMap<String, Integer>(200000000 / HASH_TABLE_SIZE);
                        
                        String line = reader.readLine();
                        while (line != null) {
                            inc(map, line);
                            line = reader.readLine();
                        }
                        reader.close();
                        TopNContainer<Entry<String, Integer>> tops = TopNContainer.getTopNContainer(topSize);
                        for (Entry<String, Integer> entry : map.entrySet()) {
                            tops.add(entry);
                        }
                        results[index] = tops;
                        
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            submits[inc2.getAndIncrement()] = submit;
        }
        
        for (Future future : submits) {
            future.get();
        }
        
        TopNContainer<Entry<String, Integer>> tops = TopNContainer.getTopNContainer(topSize);
        for (int i = 0; i < results.length; i++) {
            TopNContainer<Entry<String, Integer>> object = (TopNContainer<Entry<String, Integer>>) results[i];
            for (Entry<String, Integer> entry : object) {
                tops.add(entry);
            }
        }
        
        System.out.println(tops);
        
        return result;
    }
    
    public static void initWriter(int size) {
        for (int i = 0; i < size; i++) {
            try {
                String fileName = FILE_TEMP_PERFIX + i;
                writers[i] = new BufferedWriter(new FileWriter(fileName));
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    public static void initReader(int size) {
        for (int i = 0; i < size; i++) {
            try {
                String fileName = FILE_TEMP_PERFIX + i;
                readers[i] = new BufferedReader(new FileReader(fileName));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void inc(HashMap<String, Integer> map, String key) {
        Integer count = map.get(key);
        if (count == null) {
            map.put(key, 1);
        } else {
            map.put(key, count + 1);
        }
    }
    
    public static int index(String str, int tableSize) {
        int hash = hash(str);
        int index = Math.abs(hash % tableSize);
        return index;
    }

    public static int hash(String str) {
        return sun.misc.Hashing.stringHash32(str);
    }
}
