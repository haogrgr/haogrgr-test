package com.haogrgr.test.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 非常非常简单的模板实现
 * 格式: #{变量名} 或者 ${变量名, 默认值}
 * 
 * @author desheng.tu
 * @date 2015年6月23日 下午4:38:05
 *
 */
public class SimpleTmpl {

	public static void main(String[] args) {
		String t1 = "你好 $$name$$, 您的验证码是:$$code$$";
		String t2 = "你好 #{name}, 您的验证码是:${code}, ${null, null}";
		
		Map<String, Object> param = MapBuilder.makeO("name", "haogrgr").build("code", "1314");
//		for (int i = 0; i < 100; i++) {
//			SimpleTempletUtil.render(t1, param);
//			Templ.of(t2).render(param);
//		}
		
		String result = "";
		long start = 0;
		
		start = System.currentTimeMillis();
		for (int i = 0; i < 100000; i++) {
			param.put("code", i);
			result = SimpleTempletUtil.render(t1, param);
		}
		System.out.println(System.currentTimeMillis() - start);
		System.out.println(result);
		
		start = System.currentTimeMillis();
		Templ templ = Templ.of(t2);
		for (int i = 0; i < 100000; i++) {
			param.put("code", i);
			result = templ.render(param);
		}
		System.out.println(System.currentTimeMillis() - start);
		System.out.println(result);
		
	}

}

abstract class Exp {

	abstract String resolve(Map<String, Object> context);

	static Exp of(String exp) {
		Objects.requireNonNull(exp);
		if (exp.startsWith("#{") || exp.startsWith("${")) {
			return new VarExp(exp);
		}
		return new StrExp(exp);
	}
}

class StrExp extends Exp {

	String value;

	StrExp(String exp) {
		this.value = exp;
	}

	@Override
	public String resolve(Map<String, Object> context) {
		return this.value;
	}

	@Override
	public String toString() {
		return "StrExp [value=" + value + "]";
	}

}

class VarExp extends Exp {

	String varName;
	String defaultValue;
	Boolean nullable = false;

	VarExp(String varName, String defaultValue, Boolean nullable) {
		this.varName = varName;
		this.defaultValue = defaultValue;
		this.nullable = nullable;
	}

	VarExp(String exp) {
		Objects.requireNonNull(exp);
		if (!(exp.startsWith("#{") || exp.startsWith("${")) || !exp.endsWith("}")) {
			throw new IllegalArgumentException("表达式[" + exp + "]必须类似于#{}或${}");
		}

		String[] nodes = exp.substring(2, exp.length() - 1).split(",");
		if (nodes.length > 2) {
			throw new IllegalArgumentException("表达式[" + exp + "]只能出现一个','");
		}

		this.varName = nodes[0].trim();
		this.defaultValue = nodes.length == 2 ? nodes[1].trim() : "";
		this.nullable = exp.startsWith("$");
	}

	@Override
	public String resolve(Map<String, Object> context) {
		Object value = context.get(varName);
		if (value == null && nullable) {
			value = defaultValue == null ? "" : defaultValue;
		}
		if (value == null) {
			throw new NullPointerException("上下文中没有指定的变量:var=" + varName + " map=" + context);
		}
		return value.toString();
	}

	@Override
	public String toString() {
		return "VarExp [varName=" + varName + ", defaultValue=" + defaultValue + ", nullable="
				+ nullable + "]";
	}

}

class Templ {

	List<Exp> exps = new ArrayList<>();

	static Templ of(String templStr) {
		Objects.requireNonNull(templStr, "模板为空");

		Templ templ = new Templ();
		StringBuilder sb = new StringBuilder();

		char[] chars = templStr.toCharArray();
		for (int i = 0; i < chars.length; i++) {
			switch (chars[i]) {
			case '#':
			case '$':
				if(i < chars.length - 1 && chars[i+1] == '{'){
					templ.addExp(Exp.of(sb.toString()));
					sb.setLength(0);
				}
				sb.append(chars[i]);
				break;
			case '}':
				sb.append('}');
				if(sb.charAt(1) == '{'){
					templ.addExp(Exp.of(sb.toString()));
					sb.setLength(0);
				}
				break;
			default:
				sb.append(chars[i]);
				break;
			}
		}
		
		if(sb.length() > 0){
			templ.addExp(Exp.of(sb.toString()));
		}

		return templ;
	}

	Templ addExp(Exp exp) {
		Objects.requireNonNull(exp, "表达式为空");
		exps.add(exp);
		return this;
	}

	String render(Map<String, Object> context) {
		StringBuilder sb = new StringBuilder(128);
		for (Exp exp : exps) {
			sb.append(exp.resolve(context));
		}
		return sb.toString();
	}

}
