package com.haogrgr.test.event;

import org.springframework.stereotype.Service;

@Service
public class CGlibInvokerSuperTestService {

	public void justInvorkA() {
		System.err.println("CGlibInvokerSuperTestService justInvorkA");
		this.justInvorkB();
	}

	public void justInvorkB() {
		System.err.println("CGlibInvokerSuperTestService justInvorkB");
	}
}
