package com.haogrgr.test.main;

import java.time.LocalDate;

import ma.glasnost.orika.BoundMapperFacade;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.OrikaSystemProperties;
import ma.glasnost.orika.converter.BidirectionalConverter;
import ma.glasnost.orika.converter.builtin.PassThroughConverter;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import ma.glasnost.orika.metadata.Type;

/**
 * 使用orika来方便进行bean拷贝映射等操作 
 * 
 * 原理: 大概看了下, 生成转换的Java代码, 然后使用javassist编译产生实例
 *      所以, 初始化可能比较慢, 但是后期调用的消耗比较小
 *      
 * JDK8支持待改进, 老版本不支持JDK8 , 因为老版本引用的javassist版本不支持
 * 
 * 官方推荐:
 * 1.Use the MapperFactory as a singleton (MapperFacade thread-safe)
 * 2.Cache instances of the Type<?> class
 * 3.Use BoundMapperFacade to avoid repeated lookup of mapping strategy
 * 4.Use a custom BoundMapperFacade when your object graph has no cycles
 *   就是说如果你的对象图里面没有循环依赖, 最好用mapperFactory.getMapperFacade(Person.class, PersonDto.class, false)方法.
 *   减少hash查找次数
 * 
 * @author desheng.tu
 * @date 2015年12月11日 下午7:07:05 
 *
 */
public class BeanMapperTest {

	public static void main(String[] args) {
		// Write out source files to (classpath:)/ma/glasnost/orika/generated/
		System.setProperty(OrikaSystemProperties.WRITE_SOURCE_FILES, "true");

		MapperFactory factory = new DefaultMapperFactory.Builder().build();

		factory.classMap(PersonSource.class, PersonDest.class).mapNulls(true).mapNullsInReverse(true)
				.fieldMap("name", "fullName").mapNulls(true).add().field("age", "currentAge").byDefault().register();

		//JDK8的日期API处理, 因为LocalDate为不变对象, 所以这里直接用PassThroughConverter传引用
		factory.getConverterFactory().registerConverter(new PassThroughConverter(LocalDate.class));

		{
			MapperFacade mapper = factory.getMapperFacade();

			PersonSource source = new PersonSource().setName("hehe").setAge(11).setBirthDate(LocalDate.now());
			PersonDest dest = mapper.map(source, PersonDest.class);
			System.out.println(source);
			System.out.println(dest);
		}

		{
			//用BoundMapperFacade, 文档说性能要好点.
			BoundMapperFacade<PersonSource, PersonDest> mapper = factory.getMapperFacade(PersonSource.class,
					PersonDest.class);

			PersonSource source = new PersonSource().setName("hehe").setAge(11).setBirthDate(LocalDate.now());
			PersonDest dest = mapper.map(source);
			System.out.println(source);
			System.out.println(dest);

		}
	}

	/**
	 * JDK8的日期API转换
	 */
	public static class LocalDateConverter extends BidirectionalConverter<LocalDate, LocalDate> {

		@Override
		public LocalDate convertTo(LocalDate source, Type<LocalDate> destinationType) {
			return LocalDate.from(source);
		}

		@Override
		public LocalDate convertFrom(LocalDate source, Type<LocalDate> destinationType) {
			return LocalDate.from(source);
		}

	}

	public static class PersonSource {
		private String name;
		private int age;
		private LocalDate birthDate;

		public String getName() {
			return name;
		}

		public PersonSource setName(String name) {
			this.name = name;
			return this;
		}

		public int getAge() {
			return age;
		}

		public PersonSource setAge(int age) {
			this.age = age;
			return this;
		}

		public LocalDate getBirthDate() {
			return birthDate;
		}

		public PersonSource setBirthDate(LocalDate birthDate) {
			this.birthDate = birthDate;
			return this;
		}

		@Override
		public String toString() {
			return "PersonSource [name=" + name + ", age=" + age + ", birthDate=" + birthDate + "]";
		}

	}

	public static class PersonDest {
		private String fullName;
		private int currentAge;
		private LocalDate birthDate;

		public String getFullName() {
			return fullName;
		}

		public void setFullName(String fullName) {
			this.fullName = fullName;
		}

		public int getCurrentAge() {
			return currentAge;
		}

		public void setCurrentAge(int currentAge) {
			this.currentAge = currentAge;
		}

		public LocalDate getBirthDate() {
			return birthDate;
		}

		public void setBirthDate(LocalDate birthDate) {
			this.birthDate = birthDate;
		}

		@Override
		public String toString() {
			return "PersonDest [fullName=" + fullName + ", currentAge=" + currentAge + ", birthDate=" + birthDate + "]";
		}

	}
}
