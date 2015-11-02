package com.haogrgr.test.main;

import java.math.BigDecimal;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

/**
 * Java调用js
 * 
 * @author desheng.tu
 * @date 2015年11月2日 上午11:12:17
 *
 */
public class JavaScriptTest {

	public static void main(String[] args) throws Exception {
		ScriptEngineManager manager = new ScriptEngineManager();
		ScriptEngine engine = manager.getEngineByName("JavaScript");

		User user = new User(1, "haogrgr");
		user.setMoney(BigDecimal.TEN);
		engine.put("user", user);

		engine.eval("var BigDecimal = Java.type('java.math.BigDecimal');" + "if(user.id == 1) {" + "user.name='xxxxx';"
				+ "user.id = 2;" + "user.money = new BigDecimal(user.id.toString()).multiply(new BigDecimal(4));" + "}");

		System.out.println(user);
	}

	public static class User {
		private Integer id;
		private String name;
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
