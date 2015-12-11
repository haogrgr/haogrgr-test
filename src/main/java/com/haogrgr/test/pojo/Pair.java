package com.haogrgr.test.pojo;

import java.util.Objects;

/**
 * Pair
 * 
 * @author desheng.tu
 * @date 2015年12月11日 下午4:28:49 
 *
 */
public class Pair<A, B> {

	public final A fst;
	public final B snd;

	public Pair(A fst, B snd) {
		this.fst = fst;
		this.snd = snd;
	}

	public static <A, B> Pair<A, B> of(A a, B b) {
		return new Pair<>(a, b);
	}

	public A getFst() {
		return fst;
	}

	public B getSnd() {
		return snd;
	}

	public boolean equals(Object other) {
		return other instanceof Pair<?, ?> && Objects.equals(fst, ((Pair<?, ?>) other).fst)
				&& Objects.equals(snd, ((Pair<?, ?>) other).snd);
	}

	public int hashCode() {
		if (fst == null)
			return (snd == null) ? 0 : snd.hashCode() + 1;
		else if (snd == null)
			return fst.hashCode() + 2;
		else
			return fst.hashCode() * 17 + snd.hashCode();
	}

	public String toString() {
		return "Pair[" + fst + "," + snd + "]";
	}

}