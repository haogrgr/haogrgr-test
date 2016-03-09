package com.haogrgr.test.model;

import java.io.Serializable;
import java.util.Date;

import javax.validation.constraints.NotNull;
import javax.validation.groups.Default;

import org.hibernate.validator.constraints.NotEmpty;

public class TestModel implements Serializable {

	private static final long serialVersionUID = -1L;

	public interface Select {
	}

	public interface Edit {
	}

	/** id : 主键**/
	@NotNull
	private Integer id;
	/** name : 名字**/
	@NotEmpty(groups = { Default.class, Select.class, Edit.class })
	private String name;
	/** age : 年龄**/
	@NotNull(groups = { Edit.class })
	private Integer age;
	/** modify_time : 修改时间**/
	private Date modifyTime;
	/** create_time : 创建时间**/
	private Date createTime;

	public TestModel() {

	}

	public TestModel(Integer id) {
		this.id = id;
	}

	public Integer getId() {
		return id;
	}

	public TestModel setId(Integer id) {
		this.id = id;
		return this;
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

	public Date getModifyTime() {
		return modifyTime;
	}

	public TestModel setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
		return this;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public TestModel setCreateTime(Date createTime) {
		this.createTime = createTime;
		return this;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(getClass().getSimpleName());
		sb.append(" [");
		sb.append("Hash = ").append(hashCode());
		sb.append(", id=").append(id);
		sb.append(", name=").append(name);
		sb.append(", age=").append(age);
		sb.append(", modifyTime=").append(modifyTime);
		sb.append(", createTime=").append(createTime);
		sb.append("]");
		return sb.toString();
	}
}