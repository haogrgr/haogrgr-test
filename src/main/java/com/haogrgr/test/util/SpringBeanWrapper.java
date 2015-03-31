package com.haogrgr.test.util;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.DirectFieldAccessor;
import org.springframework.beans.PropertyAccessorFactory;

/**
 * Spring的BeanWrapper可以方便的设置属性,对象图,PropertyEditor支持
 *
 * @date 2015年3月31日 下午4:31:52
 * @author https://github.com/stillotherguy/javaopensource/blob/master/src/main/java/com/rabbit/spring/FieldAccessorTest.java
 */
public class SpringBeanWrapper {

	public static void main(String[] args) {
		Department department = new Department();
		department.setName("test");
		Department parent = new Department();
		parent.setName("parent");
		department.setParent(parent);
		
		BeanWrapper wrapper = PropertyAccessorFactory.forBeanPropertyAccess(department);
		wrapper.setPropertyValue("name", "test1");
		wrapper.setPropertyValue("parent.name", "parent1");
		
		System.out.println(department.getName());
		System.out.println(department.getParent().getName());
		
		//========================================
		
		Department dept = new Department();
		
		DirectFieldAccessor accessor = new DirectFieldAccessor(dept);
		accessor.setPropertyValue("level", 5);
		
		System.out.println(dept.getLevel());
	}

	public static class Department {

		private Department parent;
		private String name;
		private int level;

		public Department getParent() {
			return parent;
		}

		public void setParent(Department parent) {
			this.parent = parent;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public int getLevel() {
			return level;
		}

	}

}
