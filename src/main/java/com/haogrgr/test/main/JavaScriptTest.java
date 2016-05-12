package com.haogrgr.test.main;

import java.math.BigDecimal;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

/**
 * Java调用js
 * 
 * @author desheng.tu
 * @since 2015年11月2日 上午11:12:17
 *
 */
public class JavaScriptTest {

	public static void main(String[] args) throws Exception {
		ScriptEngineManager manager = new ScriptEngineManager();
		ScriptEngine engine = manager.getEngineByName("JavaScript");

		User user = new User(1, "haogrgr");
		user.setMoney(BigDecimal.TEN);
		user.setAge(10);
		engine.put("user", user);

		//注意: java7 下,通过   importClass(java.math.BigDecimal) 来引用Java类
		//		String imports = "importClass(java.math.BigDecimal);";//java7;
		String imports = "var BigDecimal = Java.type('java.math.BigDecimal');";//java8;
		engine.eval(imports + "if(user.id == 1) {" + "user.name='xxxxx';" + "user.id = 2;"
				+ "user.money = new BigDecimal(user.age / 4);" + "}");

		System.out.println(user);
	}

	public static class User {
		private Integer id;
		private String name;
		private Integer age;
		private BigDecimal money;

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

		public Integer getAge() {
			return age;
		}

		public void setAge(Integer age) {
			this.age = age;
		}

		public BigDecimal getMoney() {
			return money;
		}

		public void setMoney(BigDecimal money) {
			this.money = money;
		}

		@Override
		public String toString() {
			return "User [id=" + id + ", name=" + name + ", money=" + money + "]";
		}

	}

}
