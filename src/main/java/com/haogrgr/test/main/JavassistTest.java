package com.haogrgr.test.main;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.CtNewMethod;

public class JavassistTest {

	public void say() {
		System.out.println("Hello");
	}

	public static void main(String[] args) throws Exception {
		CtClass clazz = ClassPool.getDefault().get(JavassistTest.class.getName());
		CtMethod m = CtNewMethod.make("public int xmove(int dx) { System.out.println(dx); return dx;}", clazz);
		m.addLocalVariable("dx", CtClass.intType);
		clazz.addMethod(m);
		clazz.stopPruning(true);
		clazz.writeFile("c:\\temp");
	}

}
