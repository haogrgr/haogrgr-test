package com.haogrgr.test.main;

import org.hibernate.hql.internal.ast.HqlParser;

public class HibernateHQLParseTest {

	public static void main(String[] args) {
		HqlParser parser = HqlParser.getInstance("from com.haogrgr.test.model.TestModel t where t.name is not null");
	}

}
