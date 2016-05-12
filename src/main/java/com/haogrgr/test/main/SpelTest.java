package com.haogrgr.test.main;

import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

/**
 * spring el 使用
 * 
 * @author desheng.tu
 * @since 2015年10月20日 下午6:44:39
 *
 */
public class SpelTest {

	public static void main(String[] args) throws Exception {
		User user = new User(1, "haogrgr");
		Expression exp = new SpelExpressionParser().parseExpression("(id = 2)+(name = 'test')");
		EvaluationContext context = new StandardEvaluationContext(user);
		exp.getValue(context);
		System.out.println(user);
	}

	public static class User {
		private Integer id;
		private String name;

		public User(Integer id, String name) {
			super();
			this.id = id;
			this.name = name;
		}

		public Integer getId() {
			return id;
		}

		public void setId(Integer id) {
			this.id = id;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		@Override
		public String toString() {
			return "User [id=" + id + ", name=" + name + "]";
		}

	}

}
