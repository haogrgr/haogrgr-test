package com.haogrgr.test.model;

public class TestModel extends BaseModel {

	private static final long serialVersionUID = -1L;

	private String name;
	private Integer age;

	public TestModel() {
	}

	public TestModel(Integer id) {
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
}