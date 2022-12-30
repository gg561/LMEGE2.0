package util.unsafe;

public class OffHeap4Byte extends OffHeapStruct {
	
	private long address;
	
	public OffHeap4Byte () {
		this.address = THE_UNSAFE.allocateMemory(4);
	}
	
	public OffHeap4Byte(byte info) {
		this();
		set(info);
	}
	
	public void set(byte info) {
		THE_UNSAFE.putByte(address, info);
	}
	
	public byte get() {
		return THE_UNSAFE.getByte(address);
	}
	
	public void free() {
		THE_UNSAFE.freeMemory(address);
	}

}
