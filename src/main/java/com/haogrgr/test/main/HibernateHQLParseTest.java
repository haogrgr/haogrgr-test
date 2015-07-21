//package com.haogrgr.test.main;
//
//import org.hibernate.hql.internal.antlr.HqlTokenTypes;
//import org.hibernate.hql.internal.ast.HqlParser;
//import org.hibernate.hql.internal.ast.QueryTranslatorImpl.JavaConstantConverter;
//import org.hibernate.hql.internal.ast.util.ASTPrinter;
//import org.hibernate.hql.internal.ast.util.NodeTraverser;
//
//import antlr.collections.AST;
//
//public class HibernateHQLParseTest {
//
//	public static void main(String[] args) throws Exception {
//		HqlParser parser = HqlParser.getInstance("from com.haogrgr.test.model.TestModel t where t.name is not null");
//		parser.statement();
//		
//		AST hqlAst = parser.getAST();
//		NodeTraverser walker = new NodeTraverser( new JavaConstantConverter() );
//		walker.traverseDepthFirst(hqlAst);
//		
//		ASTPrinter HQL_TOKEN_PRINTER = new ASTPrinter( HqlTokenTypes.class );
//		System.out.println(HQL_TOKEN_PRINTER.showAsString( hqlAst, "--- HQL AST ---" ));
//		
//	}
//
//}
