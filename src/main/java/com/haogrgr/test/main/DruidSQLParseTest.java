package com.haogrgr.test.main;

import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.expr.SQLBinaryOpExpr;
import com.alibaba.druid.sql.ast.expr.SQLBinaryOperator;
import com.alibaba.druid.sql.ast.statement.SQLSelectStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlSelectQueryBlock;
import com.alibaba.druid.util.JdbcUtils;

public class DruidSQLParseTest {

	public static void main(String[] args) {
		SQLSelectStatement statement = (SQLSelectStatement) SQLUtils.parseStatements("select * from dual where 1=1", JdbcUtils.MYSQL).get(0);
		MySqlSelectQueryBlock query = (MySqlSelectQueryBlock) statement.getSelect().getQuery();

		SQLExpr where = query.getWhere();
		System.out.println(SQLUtils.toSQLString(statement));

		SQLExpr append = SQLUtils.toSQLExpr("a = ? and b = ?", JdbcUtils.MYSQL);
		where = new SQLBinaryOpExpr(where, SQLBinaryOperator.BooleanAnd, append);

		query.setWhere(where);

		System.err.println(SQLUtils.toSQLString(statement));
	}

}
