package com.haogrgr.test.main;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.Statement;
import java.util.Arrays;

import com.haogrgr.test.util.DBToolKit;

public class JDBCTest {

    public static void main(String[] args) throws Exception {
        Connection conn = DBToolKit.getConnection();

        DatabaseMetaData dbmd = conn.getMetaData();
        boolean a = dbmd.supportsBatchUpdates();
        System.out.println(a);

        conn.setAutoCommit(false);

        Statement statement = conn.createStatement();
        for (int i = 1; i < 10; i++) {
            if (i % 13 == 0) {
                continue;
            }
            String sql = "update ys_account_history set account_id = " + i + " where id = " + i % 13;
            statement.addBatch(sql);
        }
        int[] execute = statement.executeBatch();
        System.out.println(Arrays.toString(execute));

        conn.commit();

        DBToolKit.free(conn, null, null);
    }

}
