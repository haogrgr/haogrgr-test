package com.haogrgr.test.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 自已用的Map工具类, 大部分API参考Guava, 主要是为了没有guava时使用. 主要目的:精简代码
 * 
 * @author desheng.tu
 * @since 2015年7月23日 下午1:32:28
 *
 */
public class Maps {

	public static void main(String[] args) {
		System.out.println(Maps.of(1, "a"));
		System.out.println(Maps.of(1, "a", 2, "b"));
		System.out.println(Maps.of(1, "a", 2, "b", 3, "c"));
		System.out.println(Maps.of(1, "a", 2, "b", 3, "c", 4, "d"));

		System.out.println(Maps.builder().plus(1, "a").build());
		System.out.println(Maps.builder().plus(1, "a", 2, "b").build());
		System.out.println(Maps.builder().plus(1, "a", 2, "b", 3, "c").build());
		System.out.println(Maps.builder().plus(1, "a", 2, "b", 3, "c", 4, "d").build());
		
		System.out.println(Maps.builder().plusNotNull(1, null, 2, null, 3, null, 4, null).build());
	}

	public static <K, V> Map<K, V> of(K k, V v) {
		Map<K, V> map = new HashMap<>(4);
		map.put(k, v);
		return map;
	}

	public static <K, V> Map<K, V> of(K k1, V v1, K k2, V v2) {
		Map<K, V> map = new HashMap<>(4);
		map.put(k1, v1);
		map.put(k2, v2);
		return map;
	}

	public static <K, V> Map<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3) {
		Map<K, V> map = new HashMap<>(8);
		map.put(k1, v1);
		map.put(k2, v2);
		map.put(k3, v3);
		return map;
	}

	public static <K, V> Map<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4) {
		Map<K, V> map = new HashMap<>(8);
		map.put(k1, v1);
		map.put(k2, v2);
		map.put(k3, v3);
		map.put(k4, v4);
		return map;
	}

	public static <K, V> Map<K, V> plus(Map<K, V> map, K k1, V v1) {
		map.put(k1, v1);
		return map;
	}

	public static <K, V> Map<K, V> plus(Map<K, V> map, K k1, V v1, K k2, V v2) {
		map.put(k1, v1);
		map.put(k2, v2);
		return map;
	}

	public static <K, V> Map<K, V> plus(Map<K, V> map, K k1, V v1, K k2, V v2, K k3, V v3) {
		map.put(k1, v1);
		map.put(k2, v2);
		map.put(k3, v3);
		return map;
	}

	public static <K, V> Map<K, V> plus(Map<K, V> map, K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4) {
		map.put(k1, v1);
		map.put(k2, v2);
		map.put(k3, v3);
		map.put(k4, v4);
		return map;
	}

	public static <K, V> Builder<K, V> builder() {
		return new Builder<K, V>();
	}

	public static <K, V> Builder<K, V> builder(Map<K, V> map) {
		return new Builder<K, V>(map);
	}

	public static <K, V> Builder<K, V> builder(int initialCapacity) {
		return new Builder<K, V>(initialCapacity);
	}

	public static <K> Builder<K, Object> builderO() {
		return new Builder<K, Object>();
	}

	public static class Builder<K, V> {

		private final Map<K, V> map;

		public Builder() {
			this.map = new HashMap<>();
		}

		public Builder(Map<K, V> map) {
			Objects.requireNonNull(map);
			this.map = new HashMap<>(map);
		}

		public Builder(int initialCapacity) {
			this.map = new HashMap<>(initialCapacity);
		}

		public Builder<K, V> plus(K k1, V v1) {
			map.put(k1, v1);
			return this;
		}

		public Builder<K, V> plus(K k1, V v1, K k2, V v2) {
			map.put(k1, v1);
			map.put(k2, v2);
			return this;
		}

		public Builder<K, V> plus(K k1, V v1, K k2, V v2, K k3, V v3) {
			map.put(k1, v1);
			map.put(k2, v2);
			map.put(k3, v3);
			return this;
		}

		public Builder<K, V> plus(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4) {
			map.put(k1, v1);
			map.put(k2, v2);
			map.put(k3, v3);
			map.put(k4, v4);
			return this;
		}

		public Builder<K, V> plusNotNull(K k1, V v1) {
			if (v1 != null) {
				map.put(k1, v1);
			}
			return this;
		}

		public Builder<K, V> plusNotNull(K k1, V v1, K k2, V v2) {
			plusNotNull(k1, v1);
			plusNotNull(k2, v2);
			return this;
		}

		public Builder<K, V> plusNotNull(K k1, V v1, K k2, V v2, K k3, V v3) {
			plusNotNull(k1, v1);
			plusNotNull(k2, v2);
			plusNotNull(k3, v3);
			return this;
		}

		public Builder<K, V> plusNotNull(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4) {
			plusNotNull(k1, v1);
			plusNotNull(k2, v2);
			plusNotNull(k3, v3);
			plusNotNull(k4, v4);
			return this;
		}

		public Map<K, V> build() {
			return map;
		}

	}
}
