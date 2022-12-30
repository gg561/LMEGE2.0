package util.unsafe.fb;

public class NatTest {
	
	static {
		System.load("/Users/huangyoulin/Documents/Eclipse/libnatest.dylib");
	}
	
	public static native String hello_world();

}
