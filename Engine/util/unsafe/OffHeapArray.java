package util.unsafe;

public class OffHeapArray extends OffHeapStruct {
	
	private long address;
	private int size;
	
	public OffHeapArray(int size, int byteSize) {
		this.size = size;
		address = THE_UNSAFE.allocateMemory(size * byteSize);
	}
	
	public int size() {
		return size;
	}
	
	public void put(int index, byte data) {
		THE_UNSAFE.putByte(address + index, data);
	}
	
	public byte get(int index) {
		return THE_UNSAFE.getByte(address + index);
	}
	
	public void free() {
		THE_UNSAFE.freeMemory(address);
	}

}
