package util.unsafe;

import sun.misc.Unsafe;

public class Geneticist {
	
	private static final Unsafe unsafe = OffHeapStruct.THE_UNSAFE;
	private static final FakeClass internal = OffHeapStruct.INTERNAL;
	private static final int ADDRESS_SIZE;
	private static final int BITNESS;
	private static final long KLS_DEF_OFS;
	
	static {
		ADDRESS_SIZE = unsafe.addressSize();
		String b = System.getProperty("sun.arch.data.model");
		BITNESS = Integer.parseInt(!b.equals("unknown") ? b : "-1");
		KLS_DEF_OFS = BITNESS == 64 ? 8 : 4;
		
	}
	
	public long toAddress(Object object) {
		Object[] arr = new Object[] {object};
		int offset = unsafe.arrayBaseOffset(Object[].class);
		return normalize(unsafe.getInt(arr, offset));
	}
	
	public Object fromAddress(long address) {
		Object[] arr = new Object[] {null};
		long baseOffset = unsafe.arrayBaseOffset(Object[].class);
		unsafe.putLong(arr, baseOffset, address);
		return arr[0];
	}
	
	public long klassPtr(Object object) {
		return getSys(object, KLS_DEF_OFS);
	}
	
	private long getSys(long address) {
		if(ADDRESS_SIZE == 4) {
			return normalize(unsafe.getInt(address));
		}
		return normalize(unsafe.getLong(address));
	}
	
	private long getSys(Object o, long offset) {
		if(ADDRESS_SIZE == 4) {
			return normalize(unsafe.getInt(o, offset));
		}
		return normalize(unsafe.getLong(o, offset));
	}
	
	private static long normalize(long value) {
	    if(value >= 0) return value;
	    return (~0L >>> 32) & value;
	}
	
	/**
	 * 
	 * Manipulates the type of the object to the classDefAddress provided by address
	 * This effectively turns the object into a bizarro of classes. 
	 * It can be both A and B at once, but certain values need to be abusively and lazily injected.
	 * 
	 * @param o
	 * @param address
	 * @return
	 */
	public Object manTypeRaw(Object o, long address) {
		internal.invoke("putAddress", null, o, KLS_DEF_OFS, address);
		return o;
	}
	
	public Object manType(Object o, Object p) {
		long address = klassPtr(p);
		return manTypeRaw(o, address);
	}
	
	public void dump(Object dumped, int size) {
		for(int i = 0; i < size; i++) {
			if (i % 16 == 0) {
                System.out.print(String.format("[0x%04x]: ", i));
            }
            System.out.print(String.format("%02x ",
                    OffHeapStruct.THE_UNSAFE.getByte(dumped, new Long(i))));
            if((i + 1) % 8 == 0) {
            		System.out.print(" ");
            }
            if ((i + 1) % 16 == 0) {
                System.out.println();
            }
		}
	}
	
	public void setFieldValue4(Object o, String field, int value) {
		long offset = (long) OffHeapStruct.INTERNAL.invoke("objectFieldOffset", null, o.getClass(), field);
		OffHeapStruct.INTERNAL.invoke("putInt", null, o, offset, value);
	}
	
	public void setFieldValue8(Object o, String field, long value) {
		long offset = (long) OffHeapStruct.INTERNAL.invoke("objectFieldOffset", null, o.getClass(), field);
		OffHeapStruct.INTERNAL.invoke("putAddress", null, o, offset, value);
	}
	
	public Object getFieldValue4(Object o, String field) {
		long offset = (long) OffHeapStruct.INTERNAL.invoke("objectFieldOffset", null, o.getClass(), field);
		return OffHeapStruct.THE_UNSAFE.getInt(o, offset);
	}
	
	public void dumpAll() {
		for(int i = 1; i != -1; i++) {
			if (i % 16 == 0) {
                System.out.print(String.format("[0x%04x]: ", i));
            }
            System.out.print(String.format("%02x ",
                    OffHeapStruct.THE_UNSAFE.getByte(new Long(i))));
            if((i + 1) % 8 == 0) {
            		System.out.print(" ");
            }
            if ((i + 1) % 16 == 0) {
                System.out.println();
            }
		}
	}

}
