package com.haogrgr.test.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;

import com.haogrgr.test.util.DBToolKit;

public class ResourceDao {

    public static void main(String[] args) throws Exception {
        ResourceDao dao = new ResourceDao();
        dao.insert("test", "xx", "hh");
    }

    public void insert(String name, String url, String desc) throws Exception {
        Connection conn = null;
        PreparedStatement stmt = null;
        String sql = "insert into ys_authority(name, description, resource, type, zindex, pid, modify_time, create_time) "
                + "values(?, ?, ?, ?, ?, ?, ?, ?)";
        try {
            conn = DBToolKit.getConnection();
            stmt = conn.prepareStatement(sql);
            
            stmt.setString(1, name);
            stmt.setString(2, desc);
            stmt.setString(3, url);
            stmt.setString(4, "0");
            stmt.setInt(5, 1);
            stmt.setInt(6, 0);
            stmt.setDate(7, new Date(System.currentTimeMillis()));
            stmt.setDate(8, new Date(System.currentTimeMillis()));
            
            stmt.execute();
        } finally {
            DBToolKit.free(conn, stmt, null);
        }
    }

}
