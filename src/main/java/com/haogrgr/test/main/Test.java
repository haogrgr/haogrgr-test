package com.haogrgr.test.main;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.haogrgr.test.util.DBToolKit;

public class Test {

    public static void main(String[] args) throws Exception {
        Connection conn = DBToolKit.getConnection();
        PreparedStatement stat = conn.prepareStatement("select 1 from dual");
        ResultSet rs = stat.executeQuery();
        while (rs.next()) {
            System.out.println(rs.getObject(1));
        }
        DBToolKit.free(conn, stat, rs);
    }

}
