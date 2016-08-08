title: java8
speaker: 涂得胜
url: https://github.com/haogrgr
transition: slide3
files: 
theme: moon
highlightStyle: monokai_sublime
usemathjax: no


[slide]
# Java 8

涂得胜

2016-08-08


[slide]
# 主要新特性
----
* Lambda表达式
* Stream API
* 新的时间和日期API
* 并发改进
* 其他改进


[slide]
# Lambda表达式-语法1
----
```java
//不使用Lambda
Function<String, String> func = new Function<String, String>() {
	@Override
	public String apply(String name) {
		return "hello:" + name;
	}
};
String hello = func.apply("haogrgr");
System.out.println(hello);
```

```java
//使用Lambda
Function<String, String> func = (String name) -> {
	return "hello:" + name;
};
String hello = func.apply("haogrgr");
System.out.println(hello);
```


[slide]
# Lambda表达式-语法2
----
```java
//Lambda简化写法
Function<String, String> func = name -> "hello:" + name;
String hello = func.apply("haogrgr");
System.out.println(hello);
```

```java
//Lambda静态方法引用
public static String hello(String name){return "hello:" + name;}
public static void test3() {
	Function<String, String> func = Main::hello;
	String hello = func.apply("haogrgr");
	System.out.println(hello);
}
```

```java
//Lambda实例方法引用,更加简化
Function<String, String> func = "hello:"::concat;
String hello = func.apply("haogrgr");
System.out.println(hello);
```


[slide]
# Lambda表达式-函数接口&默认方法
----
```java
//被该注释注解的接口, 只能有一个接口方法, 否则编译错误
@FunctionalInterface
public interface StrFunc {

	public String apply(String name);

	//默认方法, 所有StrFunc子类都有该方法, 如果子类有同名的方法, 则调用子类方法
	//如果子类实现两个接口, 接口中都有名为hello的默认方法, 则编译报错
	default public String hello(String name) {
		return "hello:" + name;
	}
}
```


[slide]
# Lambda表达式-实现1
----
```java
//Lambda
public static void main(String[] args) throws Throwable {
	String hello = "hello lambda ";
	Function<String, Void> func = (name) -> {
		System.out.println(hello + name);
		return null;
	};
	func.apply("haogrgr");
}
```

```java
//编译后
public static void main(String[] args) throws Throwable {
	String hello = "hello lambda ";
	Function<String, Void> func = (name) -> {
		System.out.println(hello + name);
		return null;
	};
	func.apply("haogrgr");
}

//新产生的方法, synthetic修饰的表示非用户创建的, 如编译器, JVM等生成的.
private static synthetic Void lambda$0(String arg0, String name){
    System.out.println(arg0 + name);
    return null;
}
```


[slide]
# Lambda表达式-实现2
----
```java
//func实例如何产生的, 代码是怎样的?
String hello = "hello lambda ";
Function<String, Void> func = (name) -> {
	System.out.println(hello + name);
	return null;
};
//com.haogrgr.lmd.Main$$Lambda$1/1013423070@2a3046da
System.out.println(func);
```

```java
//设置这个参数, 可以看到动态生成的Class文件
System.setProperty("jdk.internal.lambda.dumpProxyClasses", ".");
```


[slide]
# Lambda表达式-实现3
----
```java
//func实例Class
import java.util.function.Function;
 
final class Main$$Lambda$1 implements Function<String, Void> {
 
    private final String hello;
 
    private Main$$Lambda$1(String hello){ 
        this.hello = hello;
    }
 
    private static Function<String, Void> get$Lambda(String hello){
        return new Main$$Lambda$1(hello);
    }
 
    @Override
    public Void apply(String name) {
        return Main.lambda$0(this.hello, name);
    }
}
```


[slide]
# Lambda表达式-实现3.1
----
```java
final synthetic class com.haogrgr.java8.main.Main$$Lambda$1 implements java.util.function.Function {

  private final java.lang.String arg$1;
  
  private Main$$Lambda$1(java.lang.String arg0);
     0  aload_0 [this]
     1  invokespecial java.lang.Object() [13]
     4  aload_0 [this]
     5  aload_1 [arg0]
     6  putfield com.haogrgr.java8.main.Main$$Lambda$1.arg$1 : java.lang.String [15]
     9  return

  private static java.util.function.Function get$Lambda(java.lang.String arg0);
    0  new com.haogrgr.java8.main.Main$$Lambda$1 [2]
    3  dup
    4  aload_0 [arg0]
    5  invokespecial com.haogrgr.java8.main.Main$$Lambda$1(java.lang.String) [19]
    8  areturn

  public java.lang.Object apply(java.lang.Object arg0);
     0  aload_0 [this]
     1  getfield com.haogrgr.java8.main.Main$$Lambda$1.arg$1 : java.lang.String [15]
     4  aload_1 [arg0]
     5  checkcast java.lang.String [23]
     8  invokestatic com.haogrgr.java8.main.Main.lambda$0(java.lang.String, java.lang.String) : java.lang.Void [29]
    11  areturn

}
```


[slide]
# Lambda表达式-实现4
----
```java
//没有使用外部变量时, 内部会缓存实力, 而不是每次new
for (int i = 0; i < 2; i++) {
	Function<String, String> func = (name) -> "hello:" + name;
	System.out.println(func);
}

//com.haogrgr.lmd.Main$$Lambda$1/1013423070@198e2867
//com.haogrgr.lmd.Main$$Lambda$1/1013423070@198e2867

//引用外部变量时, 每次都产生新实例
for (int i = 0; i < 2; i++) {
	String hstr = "hello:";
	Function<String, String> func = (name) -> hstr + name;
	System.out.println(func);
}

//com.haogrgr.lmd.Main$$Lambda$1/1013423070@2a3046da
//com.haogrgr.lmd.Main$$Lambda$1/1013423070@2a098129
```


[slide]
# Lambda表达式-相关概念
----
* 调用点(CallSite)(JDK7) : 在这个点, 调什么方法
* 方法句柄(MethodHandle)(JDK7) : 用类型类标识方法, 而不是名字, 和Method类似, 更小, 更底层, 更灵活, 也更难用
* IDY指令(JDK7) : JDK7用来支持动态语言的指令


[slide]
# Lambda表达式-总结
----
* 运行期生成字节码, 编译产生class文件体积变小(对比内名内部类方式)
* 生成字节码不包含外部变量引用, 实例内存占用更小(对比内名内部类方式)
* 序列化会有额外的开销
* 尽量不引用外部变量, 避免每次new实例, 尽量不在for循环内使用
* 每次new实例并不可怕



[slide]
# Stream API
----
Stream API + Lambda == '爽'


[slide]
# Stream API-常用操作
----
```java
//初始化Stream对象
Stream<String> list = Arrays.asList("a", "b", "c").stream();
Stream<Entry<String, String>> entry = new HashMap<String, String>().entrySet().stream();
Stream<String> of = Stream.of("a", "b", "c");
Stream<Double> limit = Stream.generate(Math::random).limit(3);
IntStream range = IntStream.range(0, 3);
```

```java
//例子
Stream<String> list = Arrays.asList("a", "b", "c").stream();
Stream<Entry<String, String>> entry = new HashMap<String, String>().entrySet().stream();
Stream<String> of = Stream.of("a", "b", "c");
Stream<Double> limit = Stream.generate(Math::random).limit(3);
IntStream range = IntStream.range(0, 3);
```


