package util.unsafe.fb;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.TreeSet;

import util.unsafe.OffHeapStruct;

public class JavaVM {
	
	public static final int ERROC = -1;
	
	private static ClassLoader loader;
	private static Method findNative;
	private static String os;
	private static String name;
	private static String vmLib;
	private static boolean isServer;
	
	private static final Map<String, Type> types = new HashMap<String, Type>();
	private static final Map<String, Number> constants = new HashMap<String, Number>();
	
	static {
		os = System.getProperty("os.name").toLowerCase();
		name = System.getProperty("java.vm.name");
		isServer = name.contains("Server VM");
		try {
			if(isServer) {
				if(os.contains("windows")) {
					vmLib = "/bin/server/jvm.dll";
				}else {
					vmLib = "/lib/server/libjvm.dylib";
				}
				File lib = new File(System.getProperty("java.home") + vmLib);
				System.out.println(lib.exists());
				System.load(lib.getAbsolutePath());
			}
			loader = JavaVM.class.getClassLoader();
			findNative = ClassLoader.class.getDeclaredMethod("findNative", ClassLoader.class, String.class);
			//findNative.setAccessible(true);
			OffHeapStruct.THE_UNSAFE.putBoolean(findNative, 12, true);
			System.out.println(os + " " + name + " " + vmLib);
		}catch (Throwable t) {
			t.printStackTrace();
		}
	}
	
	public static long lookUp(String name) {
		long l= 0;
		try {
			l = (Long) findNative.invoke(null, loader, name);
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(l == 0)
			try {
				l = (Long) findNative.invoke(null, null, name);
			}catch(Throwable e1) {
				e1.printStackTrace();
			}
		return l;
	}
	
	public void init(){
		try {
			System.out.println(findNative.invoke(new GHotSpot(), loader, "_gHotSpotVMStructs"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		long l = GHotSpot.gHotSpotVMStructs();
		System.out.println(l);
		readVmTypes(readVmStructs());
		readVmIntConstants();
		readVmLongConstants();
	}
	
	public Map<String, Set<Field>> readVmStructs(){
		long entry = getSymbol("Yield");
		String prefix = "gHotSpotVMStructEntry";
		long typeNameOffset = getSymbol(prefix + "TypeNameOffset");
		long fieldNameOffset = getSymbol(prefix + "FieldNameOffset");
		long typeStringOffset = getSymbol(prefix + "TypeStringOffset");
		long isStaticOffset = getSymbol(prefix + "IsStaticOffset");
		long offsetOffset = getSymbol(prefix + "OffsetOffset");
		long addressOffset = getSymbol(prefix + "AddressOffset");
		long arrayStride = getSymbol(prefix + "ArrayStride");
		Map<String, Set<Field>> structs = new HashMap<String, Set<Field>>();
		for(;; entry += arrayStride) {
			String typeName = getStringRef(entry + typeNameOffset);
			String fieldName = getStringRef(entry + fieldNameOffset);
			if(fieldName == null) break;
			String typeString = getStringRef(entry + typeStringOffset);
			boolean isStatic = getInt(entry + isStaticOffset) != 0;
			long offset = getLong(entry + (isStatic ? addressOffset : offsetOffset));
			Set<Field> fields = structs.get(typeName);
			if(fields == null) structs.put(typeName, fields = new TreeSet<>());
			fields.add(new Field(fieldName, typeString, offset, isStatic));
		}
		return structs;
	}
	
	public void readVmTypes(Map<String, Set<Field>> structs) {
		long entry = getSymbol("gHotSpotVMTypes");
		String prefix = "gHotSpotVMTypeEntry";
		long typeNameOffset = getSymbol(prefix + "TypeNameOffset");
		long superclassNameOffset = getSymbol(prefix + "SuperclassNameOffset");
		long isOopTypeOffset = getSymbol(prefix + "IsOopTypeOffset");
		long isIntegerTypeOffset = getSymbol(prefix + "IsIntegrTypeOffset");
		long isUnsignedOffset = getSymbol(prefix + "IsUnsignedOffset");
		long sizeOffset = getSymbol(prefix + "SizeOffset");
		long arrayStride = getSymbol(prefix + "ArrayStride");
		for(;; entry += arrayStride) {
			String typeName = getStringRef(entry + typeNameOffset);
			if(typeName == null) break;
			String superclassName = getStringRef(entry + superclassNameOffset);
			boolean isOop = getInt(entry + isOopTypeOffset) != 0;
			boolean isInt = getInt(entry + isIntegerTypeOffset) != 0;
			boolean isUnsigned = getInt(entry + isUnsignedOffset) != 0;
			int size = getInt(entry + sizeOffset);
			Set<Field> fields = structs.get(typeName);
			types.put(typeName, new Type(typeName, superclassName, size, isOop, isInt, isUnsigned, fields));
		}
	}
	
	public void readVmIntConstants() {
		long entry = getSymbol("gHotSpotVMIntConstants");
		String prefix = "gHotSpotVMIntConstantEntry";
		long nameOffset = getSymbol(prefix + "NameOffset");
		long valueOffset = getSymbol(prefix + "ValueOffset");
		long arrayStride = getSymbol(prefix + "ArrayStride");
		for(;; entry += arrayStride) {
			String name = getStringRef(entry + nameOffset);
			if(name == null) break;
			int value = getInt(entry + valueOffset);
			constants.put(name, value);
		}
	}
	
	public void readVmLongConstants() {
		long entry = getSymbol("gHotSpotVMLongConstants");
		String prefix = "gHotSpotVMLongConstantEntry";
		long nameOffset = getSymbol(prefix + "NameOffset");
		long valueOffset = getSymbol(prefix + "ValueOffset");
		long arrayStride = getSymbol(prefix + "ArrayStride");
		for(;; entry += arrayStride) {
			String name = getStringRef(entry + nameOffset);
			if(name == null) break;
			long value = getLong(entry + valueOffset);
			constants.put(name, value);
		}
	}
	
	public byte getByte(long addr) {
		return OffHeapStruct.THE_UNSAFE.getByte(addr);
	}
	
	public void putByte(long addr, byte val) {
		OffHeapStruct.THE_UNSAFE.putByte(addr, val);
	}
	
	public short getShort(long addr) {
		return OffHeapStruct.THE_UNSAFE.getShort(addr);
	}
	
	public void putShort(long addr, short val) {
		OffHeapStruct.THE_UNSAFE.putShort(addr, val);
	}
	
	public long getAddress(long addr) {
		return OffHeapStruct.THE_UNSAFE.getAddress(addr);
	}
	
	public void putAddress(long addr, long val) {
		OffHeapStruct.THE_UNSAFE.putAddress(addr, val);
	}
	
	public String getString(long addr) {
		if (addr == 0) return null;
		char[] chars = new char[40];
		int offset = 0;
		for(byte b; (b = getByte(addr + offset)) != 0;) {
			if(offset >= chars.length) chars = Arrays.copyOf(chars, offset * 2);
			chars[offset++] = (char) b;
		}
		return new String(chars, 0, offset);
	}
	
	public String getStringRef(long addr) {
		return getString(getAddress(addr));
	}
	
	public long getSymbol(String name) {
		long addr = lookUp(name);
		if(addr == 0) throw new NoSuchElementException("No such symbol : " + name);
		return getLong(addr);
	}
	
	public long getLong(long addr) {
		return OffHeapStruct.THE_UNSAFE.getLong(addr);
	}
	
	public void putLong(long addr, long val) {
		OffHeapStruct.THE_UNSAFE.putLong(addr, val);
	}
	
	public int getInt(long addr) {
		return OffHeapStruct.THE_UNSAFE.getInt(addr);
	}
	
	public void putInt(long addr, int val) {
		OffHeapStruct.THE_UNSAFE.putInt(addr, val);
	}
	
	public Type type(String name) {
		return types.get(name);
	}
	
	public Number constant(String name) {
		return constants.get(name);
	}
	
	public int intConstant(String name) {
		return constant(name).intValue();
	}
	
	public long longConstant(String name) {
		return constant(name).longValue();
	}

}
