package com.haogrgr.test.main;

import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import com.google.common.base.Stopwatch;

/**
 * spring el 使用
 * 
 * @author desheng.tu
 * @date 2015年10月20日 下午6:44:39
 *
 */
public class SpelTest {

	public static void main(String[] args) throws Exception {
		Expression exp = new SpelExpressionParser().parseExpression("#a > (10 + 5)");

		EvaluationContext context = new StandardEvaluationContext();
		context.setVariable("a", 1000);

		Boolean value = exp.getValue(context, Boolean.class);

		Stopwatch st = Stopwatch.createStarted();
		for (int i = 0; i < 100_0000; i++) {
			value = exp.getValue(context, Boolean.class);
		}
		System.out.println(st.stop());

		System.out.println(value);
	}

}
