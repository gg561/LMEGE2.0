package util.unsafe;

import java.lang.reflect.Field;

import sun.misc.Unsafe;

public abstract class OffHeapStruct {
	
	public static final Unsafe THE_UNSAFE = getUnsafe();
	public static final FakeClass INTERNAL = getInternalUnsafe();
	
	private static Unsafe getUnsafe() {
		try {
			Field unsafe = Unsafe.class.getDeclaredField("theUnsafe");
			unsafe.setAccessible(true);
			return (Unsafe) unsafe.get(null);
		} catch (NoSuchFieldException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	private static FakeClass getInternalUnsafe(){
		try {
			Field f = Unsafe.class.getDeclaredField("theUnsafe"), f1 = Unsafe.class.getDeclaredField("theUnsafe");
			f.setAccessible(true);
			f1.setAccessible(false);
			Unsafe unsafe = (Unsafe) f.get(null);
			int i;
			for(i = 0; unsafe.getBoolean(f, i) == unsafe.getBoolean(f1, i); i++);
			Field internal = Unsafe.class.getDeclaredField("theInternalUnsafe");
			unsafe.putBoolean(internal, i, true);
			Object internalUnsafe = internal.get(null);
			FakeClass rv = new FakeClass("jdk.internal.misc.Unsafe", internalUnsafe.getClass().getDeclaredConstructors(), internalUnsafe.getClass().getDeclaredMethods(), internalUnsafe.getClass().getDeclaredFields(), i);
			rv.setPreInstance(internalUnsafe);
			rv.setRealClass(internalUnsafe.getClass());
			return rv;
		}catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
