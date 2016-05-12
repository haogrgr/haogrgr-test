//package com.haogrgr.test.main;
//
//import java.lang.reflect.Field;
//import java.util.Arrays;
//import java.util.Date;
//import java.util.List;
//import java.util.Random;
//
//import io.github.benas.randombeans.EnhancedRandomBuilder;
//import io.github.benas.randombeans.FieldDefinition;
//import io.github.benas.randombeans.api.EnhancedRandom;
//import io.github.benas.randombeans.api.Randomizer;
//import io.github.benas.randombeans.api.RandomizerRegistry;
//import io.github.benas.randombeans.randomizers.text.StringRandomizer;
//
//public class RandomBeansMain {
//
//	public static void main(String[] args) {
//		EnhancedRandomBuilder builder = EnhancedRandomBuilder.aNewEnhancedRandomBuilder();
//
//		//自定义字段生成规则
//		builder.randomize(new FieldDefinition<>("name", String.class, Person.class), new NameRandomizer());//自定义的
//		builder.randomize(new FieldDefinition<>("name", String.class, Like.class), new StringRandomizer(4, 1l));//内置的
//
//		//自定义类型生成, 默认String 32位, 修改为4位
//		builder.registerRandomizerRegistry(new ShortStringRandomizerRegistry());
//
//		EnhancedRandom enhancedRandom = builder.build();
//
//		//一般的
//		Person person = enhancedRandom.nextObject(Person.class);
//		System.out.println(person);
//
//		//不自动生成age字段
//		person = enhancedRandom.nextObject(Person.class, "age");
//		System.out.println(person);
//	}
//
//	public static final class ShortStringRandomizerRegistry implements RandomizerRegistry {
//		@Override
//		public void setSeed(long seed) {}
//
//		@Override
//		public Randomizer<?> getRandomizer(Class<?> type) {
//			if (type.isAssignableFrom(String.class))
//				return new StringRandomizer(4, 1l);
//			return null;
//		}
//
//		@Override
//		public Randomizer<?> getRandomizer(Field field) {
//			if (field.getType().isAssignableFrom(String.class))
//				return new StringRandomizer(4, 1l);
//			return null;
//		}
//	}
//
//	public static class NameRandomizer implements Randomizer<String> {
//
//		private List<String> names = Arrays.asList("John", "Brad", "Tommy");
//
//		@Override
//		public String getRandomValue() {
//			return names.get(new Random().nextInt(2));
//		}
//
//	}
//
//	public static class Person {
//		private String name;
//		private Integer age;
//		private Date ctime;
//		private Like like;
//
//		public String getName() {
//			return name;
//		}
//
//		public void setName(String name) {
//			this.name = name;
//		}
//
//		public Integer getAge() {
//			return age;
//		}
//
//		public void setAge(Integer age) {
//			this.age = age;
//		}
//
//		public Date getCtime() {
//			return ctime;
//		}
//
//		public void setCtime(Date ctime) {
//			this.ctime = ctime;
//		}
//
//		public Like getLike() {
//			return like;
//		}
//
//		public void setLike(Like like) {
//			this.like = like;
//		}
//
//		@Override
//		public String toString() {
//			return "Person [name=" + name + ", age=" + age + ", ctime=" + ctime + ", like=" + like + "]";
//		}
//
//	}
//
//	public static class Like {
//		private String type;
//		private String name;
//		private String value;
//
//		public String getType() {
//			return type;
//		}
//
//		public void setType(String type) {
//			this.type = type;
//		}
//
//		public String getName() {
//			return name;
//		}
//
//		public void setName(String name) {
//			this.name = name;
//		}
//
//		public String getValue() {
//			return value;
//		}
//
//		public void setValue(String value) {
//			this.value = value;
//		}
//
//		@Override
//		public String toString() {
//			return "Like [type=" + type + ", name=" + name + ", value=" + value + "]";
//		}
//
//	}
//
//}
