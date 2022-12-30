package util.unsafe;

import java.lang.ref.PhantomReference;
import java.util.HashMap;

import javax.lang.model.type.PrimitiveType;

public class Classified {
	
	private class A extends Throwable {
		
		public A() {
		}
		
		public void printStackTrace() {
			
		}
		
		public void triumph() {
			
		}
		
	}
	
	private class B extends Throwable {
		public void triumph() {
			
		}
	}
	
	public void ab() {
		try {
			t();
		}catch (A | B ex) {//treats all potential parameters as Throwable
			
		}
	}
	private void t() throws A, B {
		
	}
	
	public void ref() {
		PhantomReference<?> phantom;
	}

}
