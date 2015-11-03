package com.haogrgr.test.model;

public class TestModel extends BaseModel<Integer> {

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

	public TestModel setName(String name) {
		this.name = name;
		return this;
	}

	public Integer getAge() {
		return age;
	}

	public TestModel setAge(Integer age) {
		this.age = age;
		return this;
	}
}