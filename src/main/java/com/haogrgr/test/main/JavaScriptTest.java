package com.haogrgr.test.main;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

public class JavaScriptTest {

	public static void main(String[] args) throws Exception {
		ScriptEngineManager manager = new ScriptEngineManager();
		ScriptEngine engine = manager.getEngineByName("JavaScript");

		User user = new User(1, "haogrgr");
		engine.put("user", user);

		engine.eval("if(user.id == 1) {user.name='xxxxx'; user.id = 2;}");

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
