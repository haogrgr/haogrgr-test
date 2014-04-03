package com.haogrgr.test.model;

public class Pair implements Comparable<Pair> {

    private String key;
    private Integer value;

    public Pair(String key, Integer value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return this.key + "=" + this.value;
    }

    @Override
    public int compareTo(Pair o) {
        return getValue() - o.getValue();
    }

}
