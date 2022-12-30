package util.unsafe;

public class OffHeapMultiDimensionalArray extends OffHeapStruct {
	
	private long address;
	private int pointer;
	//16, 16, 16
	public OffHeapMultiDimensionalArray(int byteSize, int... sizes) {
		int dimSize = 1;
		for(int size : sizes) {
			dimSize *= size;
		}
		address = THE_UNSAFE.allocateMemory(byteSize * dimSize);
	}
	
	public void put(byte data, int...indexes) {
		
	}
	
	public void put(int position, byte data) {
		THE_UNSAFE.putByte(address + position, data);
	}
	//4, 3
	public void get(int...indexes) {
		int pointer = 0;
		int multiplier = 1;
		for(int index : indexes) {
			
		}
	}
	
	public byte get(int position) {
		return THE_UNSAFE.getByte(address + position);
	}
	
	public void free() {
		THE_UNSAFE.freeMemory(address);
	}

}
