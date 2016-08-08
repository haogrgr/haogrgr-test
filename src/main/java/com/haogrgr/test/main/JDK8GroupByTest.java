package com.haogrgr.test.main;

import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class JDK8GroupByTest {

	public static void main(String[] args) {
		Map<Long, Long> collect = IntStream.range(0, 10).mapToObj(Long::valueOf)
				.collect(Collectors.groupingBy(i -> i, reducing((a, b) -> a)));

		System.out.println(collect);

		Map<Long, Long> collect2 = IntStream.range(0, 10).mapToObj(Long::valueOf)
				.collect(Collectors.toMap(i -> i, i -> i));
		System.out.println(collect2);
	}

	public static <T> Collector<? super T, ?, T> reducing(BinaryOperator<T> op) {
		Supplier<SettableValue<T>> supplier = SettableValue::new;
		BiConsumer<SettableValue<T>, T> accumulator = (box, ele) -> box.set(ele);
		BinaryOperator<SettableValue<T>> combiner = SettableValue::merge;
		Function<SettableValue<T>, T> finisher = SettableValue::get;
		return Collector.of(supplier, accumulator, combiner, finisher);
	}

	public static class SettableValue<T> {

		private T value;

		public T get() {
			return value;
		}

		public void set(T value) {
			this.value = value;
		}

		public boolean isNull() {
			return value == null;
		}

		public static <T> SettableValue<T> merge(SettableValue<T> valuea, SettableValue<T> valueb) {
			return valueb.isNull() ? valuea : valueb;
		}
	}
}
