package com.haogrgr.test.main;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import com.haogrgr.test.util.DBToolKit;

public class ResultSetTest {

    public static void main(String[] args) throws Exception {
        Connection conn = DBToolKit.getConnection();
        
        PreparedStatement prep = conn.prepareStatement("select * from test2 where 1=1 order by id asc");
        
        ResultSet result = prep.executeQuery();
        
        result.absolute(2000);
        
        Object content = result.getObject("id");
        
        System.out.println(content);
        
    }
    
    public static void createDate() throws Exception {
        Connection conn = DBToolKit.getConnection();
        Statement s = conn.createStatement();
        
        for (int i = 1; i < 1000000; i++) {
            s.execute("insert into test(id, name) values("+i+", 'name"+i+"')");
        }
        
        conn.close();
    }

}
