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
# Lambda表达式-实现5
----
```java
  public static void main(java.lang.String[] args) throws java.lang.Exception;
     0  invokedynamic 0 apply() : java.util.function.Function [22]
     5  astore_1 [func]
     6  aload_1 [func]
     7  bipush 10
     9  invokestatic java.lang.Integer.valueOf(int) : java.lang.Integer [23]
    12  invokeinterface java.util.function.Function.apply(java.lang.Object) : java.lang.Object [29] [nargs: 2]
    17  pop
    18  return
```
```java
Bootstrap methods:
  0 : # 58 invokestatic LambdaMetafactory.metafactory:
			(
			Ljava/lang/invoke/MethodHandles$Lookup;  //JVM填充, 用来获取lambda$0的方法引用
			Ljava/lang/String;                       //JVM填充, Function.apply方法名, 用于动态生成字节码时的方法名
			Ljava/lang/invoke/MethodType;            //JVM填充, 这里为 (String)Function, 用于字节码生成
			Ljava/lang/invoke/MethodType;            //Class文件, 值为(Object)Object, apply方法的类型, 要实现的接口方法类型
			Ljava/lang/invoke/MethodHandle;          //Class文件, 为lambda$0的方法引用, 用于字节码生成
			Ljava/lang/invoke/MethodType;            //Class文件, 为(Integer)Integer, 表示真实的类型信息, 泛型擦除, 所以
			)
			Ljava/lang/invoke/CallSite;
	Method arguments:
		#59 (Ljava/lang/Object;)Ljava/lang/Object;
		#62 invokestatic com/haogrgr/test/main/Temp.lambda$0:(Ljava/lang/Integer;)Ljava/lang/Integer;
		#63 (Ljava/lang/Integer;)Ljava/lang/Integer;
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
# Function
----
函数式编程


[slide]
# Function-Optional
----
```java
Optional<String> op = Optional.ofNullable("value");
String value = op.orElse("defaultValue");
```


[slide]
# Function-Consumer
----
```java
@FunctionalInterface
public interface Consumer<T> {

    void accept(T t);

    default Consumer<T> andThen(Consumer<? super T> after) {
        Objects.requireNonNull(after);
        return (T t) -> { accept(t); after.accept(t); };
    }
}
```

```java
@FunctionalInterface
public interface BiConsumer<T, U> {

    void accept(T t, U u);
    
}
```


[slide]
# Function-Consumer-N
----
```java
//linkedin parseq
Consumer1
Consumer2
Consumer3
Consumer4
Consumer5
Consumer6
Consumer7
Consumer8
Consumer9
```


[slide]
# Function-Function
----
```java
@FunctionalInterface
public interface Function<T, R> {

    R apply(T t);
    
}

//BiFunction, 两个参数, 一个返回值

//BinaryOperator, 两个参数, 一个返回值, 且类型都相同

//UnaryOperator, T和R类型相同
```


[slide]
# Function-Predicate
----
```java
@FunctionalInterface
public interface Predicate<T> {

    boolean test(T t);

    default Predicate<T> and(Predicate<? super T> other) {
        Objects.requireNonNull(other);
        return (t) -> test(t) && other.test(t);
    }

}

//BiPredicate, 两个参数
```


[slide]
# Function-Supplier
----
```java
@FunctionalInterface
public interface Supplier<T> {

    T get();

}
```


[slide]
# Function-Tuple
----
```java
//二元组
public class Tuple2<T1, T2> implements Tuple {

    private final T1 _1;
    private final T2 _2;

    public Tuple2(final T1 t1, final T2 t2) {
    	_1 = t1;
    	_2 = t2;
    }

    public T1 _1() {
    	return _1;
    }
    public T2 _2() {
    	return _2;
    }
  
    public int arity() {
    	return 2;
    }
  
}
```



[slide]
# Stream API
----
Stream API + Lambda == '爽'


[slide]
# Stream API-构造Stream对象
----
```java
//初始化Stream对象方式
Stream<Object> empty = Stream.empty();
Stream<String> list = Arrays.asList("a", "b", "c").stream();
Stream<Entry<String, String>> entry = new HashMap<String, String>().entrySet().stream();
Stream<String> of = Stream.of("a", "b", "c");
Stream<Double> limit = Stream.generate(Math::random).limit(10);
Stream<Double> iterate = Stream.iterate(1d, i -> i + 1d).limit(10);
Stream<Double> concat = Stream.concat(limit, iterate);
Stream<Double> builder = Stream.<Double>builder().add(1d).add(2d).build();

//Double, Int, Long有对应的Stream, 作为优化, 减少Box操作
IntStream range = IntStream.range(0, 10);
```


[slide]
# Stream API-常用方法-forEach
----
```java
Stream<String> list = Arrays.asList("a", "b", "c").stream();
list.forEach(str -> {
	System.out.println(str);
});
```


[slide]
# Stream API-常用方法-filter
----
```java
Stream<String> list = Arrays.asList("a", "b", null, "c").stream();
list.filter(str -> {
	return str != null;
}).forEach(str -> {
	System.out.println(str);
});
```


[slide]
# Stream API-常用方法-map
----
```java
Stream<String> list = Arrays.asList("a", "b", "c").stream();
list.map(str -> str + str).map(str -> str.length()).forEach(len -> {
	System.out.println(len);
});
```


[slide]
# Stream API-常用方法-reduce
----
```java
Stream<String> list = Arrays.asList("aa", "b", "c").stream();
Integer reduce = list.reduce(0, (len, str) -> {
	return len + str.length();
}, (len1, len2) -> {
	return len1 + len2;
});
System.out.println(reduce);
```


[slide]
# Stream API-常用方法-collect
----
```java
Stream<String> list = Arrays.asList("aa", "b", "c").stream();
List<String> collect = list.collect(Collectors.toList());
System.out.println(collect);
```


[slide]
# Stream API-常见collector
----
* joining
* toList
* toSet
* groupingBy


[slide]
# Stream API-常用方法-flatMap
----
```java
Stream<ArrayList<String>> list = Arrays.asList(Lists.array("a", "b"), Lists.array("c", "d")).stream();
List<String> collect = list.flatMap(arr -> arr.stream()).collect(Collectors.toList());
System.out.println(collect);
```


[slide]
# Stream API-常用方法-Stream其他方法
----
* distinct
* sorted
* peek
* limit
* skip
* min, max
* count
* anyMatch, allMatch, noneMatch
* findFirst, findAny
* sum, average, summaryStatistics
* distinct
* distinct



[slide]
# 时间与日期
----
参考joda-time
不可变
线程安全


[slide]
# 时间与日期-概念
----
* Clock (时钟)
* LocalDate (日期)
* LocalTime (时间)
* LocalDateTime (日期&时间)
* Instant (时间点)
* Duration (时间段-基于时间), Period(时间段-基于日期)
* ZoneId (时区)
* ZoneId (时区)







