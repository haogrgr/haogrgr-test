package com.haogrgr.test.util;

import java.util.ArrayList;
import java.util.List;

public class ListBuilder<T> {
    
    private List<T> list;
    
    public static void main(String[] args) {
        
    }
    
    public ListBuilder(){
        this.list = new ArrayList<T>();
    }
    
    public ListBuilder(int initialCapacity){
        this.list = new ArrayList<T>(initialCapacity);
    }
    
    public static <T> ListBuilder<T> make(T element){
        ListBuilder<T> builder = new ListBuilder<T>();
        builder.add(element);
        return builder;
    }
    
    public static <T> ListBuilder<T> make(T element, int initialCapacity){
        ListBuilder<T> builder = new ListBuilder<T>(initialCapacity);
        builder.add(element);
        return builder;
    }
    
    public void add(T element){
        this.list.add(element);
    }
    
    @SafeVarargs
    public final void adds(T ... elements){
        for (T element : elements) {
            this.list.add(element);
        }
    }
    
    public List<T> build(T element){
        this.list.add(element);
        return this.list;
    }
    
    public List<T> build(){
        return this.list;
    }
}
