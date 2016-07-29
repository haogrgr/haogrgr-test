//人肉反编译这个里面的字节码
//http://colobu.com/2016/07/14/Java-Fiber-Quasar/

import co.paralleluniverse.fibers.Stack;

static void m1() {
	String m;  Stack localStack;  int i;  Object localObject = null;
	SuspendExecution localSuspendExecution;
	
	localStack = Stack.getStack();
	if(localStack == null){ goto the49; }
	
	i = 1;
	switch(localStack.nextMethodEntry()){
	case 1: { goto the80; }
	case 2: { goto the111; }
	default : {
		if(localStack.isFirstInStackOrPushed() == 0){ localStack = null; }
	}
	}

	try{
the49:
		i = 0;
		m = "m1";
		System.out.println("m1 begin");
		
		if(localStack == null){ goto the89; }
		
		localStack.pushMethod(1, 1);
		Stack.push(m, localStack, 0);
		i = 0;
		
the80:
		m = (String)localStack.getObject(0);
	
the89:
		m = m2();
		
		if(localStack == null){ goto the 120; }
		
		localStack.pushMethod(2, 1);
		Stack.push(m, localStack, 0);
		i = 0;
	
the111:
		m = (String)localStack.getObject(0);
		
the120:
		m = m3();
		
		System.out.println("m1 end");
		System.out.println(m);
		
		if(localStack == null){ return; }
		
		localStack.popMethod();
		
		return;
	}catch(SuspendExecution | RuntimeSuspendExecution e){
		throw e;
	}
	finally{
		if(localStack != null){
			localStack.popMethod();
		}
	}
	
}