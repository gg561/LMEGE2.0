package util.unsafe.fb;

public class GHotSpot {
	
	static {
		System.load("/Users/huangyoulin/Documents/Eclipse/libnative.dylib");
	}
	
	{

		System.load("/Users/huangyoulin/Documents/Eclipse/libnative.dylib");
	}
	
	public static native long gHotSpotVMStructs();

}
