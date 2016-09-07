package com.haogrgr.test.main;

import rx.Observable;
import rx.functions.Action1;

public class RxJavaMain {

	public static void main(String[] args) {
		hello("xxx", "bbb");
	}

	public static void hello(String... names) {
		Observable.from(names).subscribe(new Action1<String>() {
			@Override
			public void call(String s) {
				System.out.println("Hello " + s + "!");
			}
		});
	}

}
