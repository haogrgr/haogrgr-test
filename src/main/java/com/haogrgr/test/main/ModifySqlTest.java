package com.haogrgr.test.main;

import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.expr.SQLBinaryOpExpr;
import com.alibaba.druid.sql.ast.expr.SQLBinaryOperator;
import com.alibaba.druid.sql.ast.statement.SQLSelectStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlSelectQueryBlock;
import com.alibaba.druid.sql.parser.SQLExprParser;
import com.alibaba.druid.sql.parser.SQLParserUtils;
import com.alibaba.druid.sql.parser.SQLStatementParser;
import com.alibaba.druid.util.JdbcUtils;

public class ModifySqlTest {

	public static void main(String[] args) {
		SQLStatementParser statementParser = SQLParserUtils.createSQLStatementParser("select * from dual where 1=1", JdbcUtils.MYSQL);

		SQLSelectStatement select = statementParser.parseSelect();

		MySqlSelectQueryBlock query = (MySqlSelectQueryBlock) select.getSelect().getQuery();

		SQLExpr where = query.getWhere();
		System.out.println(select.toString());

		SQLExprParser exprParser = SQLParserUtils.createExprParser("a = ?", JdbcUtils.MYSQL);
		SQLExpr nwhere = new SQLBinaryOpExpr(where, SQLBinaryOperator.BooleanAnd, exprParser.expr());

		query.setWhere(nwhere);

		System.err.println(select.toString());
	}

}
