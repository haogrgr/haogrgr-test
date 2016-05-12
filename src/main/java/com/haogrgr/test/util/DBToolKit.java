package com.haogrgr.test.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import javax.sql.DataSource;

import com.alibaba.druid.pool.DruidDataSource;

/**
 * 调试原生JDBC源码用
 * 
 * @author tudesheng
 * @since 2016年5月11日 下午2:11:56
 *
 */
public class DBToolKit {

	private static DataSource datasource = null;

	static {
		try {
			Properties propertie = new Properties();
			propertie.load(DBToolKit.class.getClassLoader().getResourceAsStream("config.properties"));

			DruidDataSource druid = new DruidDataSource();
			druid.setUrl(propertie.getProperty("db.url"));
			druid.setUsername(propertie.getProperty("db.username"));
			druid.setPassword(propertie.getProperty("db.password"));
			druid.setDriverClassName("com.mysql.cj.jdbc.Driver");
			druid.setValidationQuery("select 1 from dual");
			druid.setInitialSize(2);
			druid.init();

			datasource = druid;
		} catch (Exception e) {
			throw new ExceptionInInitializerError(e);
		}
	}

	public static DataSource getDatasource() {
		return datasource;
	}

	public static Connection getConnection() throws SQLException {
		return datasource.getConnection();
	}

	/**
	 * 释放数据库链接资源(将connection连接加入连接池中)
	 * 
	 * @param conn
	 * @param statment
	 * @param rs
	 */
	public static void free(Connection conn, PreparedStatement statment, ResultSet rs) {
		try {
			if (rs != null) {
				rs.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (statment != null) {
					statment.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				try {
					if (conn != null) {
						conn.close();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		}
	}

}