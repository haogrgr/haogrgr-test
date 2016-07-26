package com.haogrgr.test.server;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class TestMysqlJDBCPrepServiceImpl {

	@Resource
	private JdbcTemplate jdbcTemplate;

	public void test(List<Long> ids) {
		//默认客户端预编译, 批量更新时, 其实只是将每条sql替换参数, 发送到服务端执行, 和一条条执行基本类似
		//com.mysql.cj.jdbc.PreparedStatement.executeInternal(int, Buffer, boolean, boolean, Field[], boolean)
		//断点上面的方法, rs = locallyScopedConnection.execSQL(..., sendPacket, ...);
		//这行, 可以看到, sendPacket里面就是替换参数后的sql, 然后外面是一个循环, 一条条执行

		//服务端预编译的情况, 参考下面的链接
		//http://stackoverflow.com/questions/32286518/whats-the-difference-between-cacheprepstmts-and-useserverprepstmts-in-mysql-jdb/32645365#32645365
		//https://dev.mysql.com/doc/internals/en/prepared-statements.html
		//服务端的情况下, prep的时候, sql就发送给server, 然后server返回一个handle, 参数的发送只会发送参数和这个handle给server
		//prep可以缓存, 特别是服务端预编译的情况下.
		jdbcTemplate.batchUpdate("update test set name = ? where id = ?", new BatchPreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement prep, int index) throws SQLException {
				prep.setString(1, "ddd" + index);
				prep.setLong(2, ids.get(index));
			}

			@Override
			public int getBatchSize() {
				return ids.size();
			}
		});
	}

}
