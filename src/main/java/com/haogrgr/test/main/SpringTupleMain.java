package com.haogrgr.test.main;

import org.springframework.tuple.Tuple;
import org.springframework.tuple.TupleBuilder;

public class SpringTupleMain {

	public static void main(String[] args) {
		Tuple tuple = TupleBuilder.tuple().of("foo", "bar");
		System.out.println(tuple.getString(0));

		System.out.println(tuple.toString());
	}

}
