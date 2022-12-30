package util.unsafe;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

public class FakeClass {
	
	private String name;
	private Method[] methods;
	private Field[] fields;
	private Constructor<?>[] constructors;
	private int accessibleOffset;
	private Object preInstance;
	private Class<?> realClass;
	
	/**
	 * 
	 * Undefined class
	 * 
	 * @param name
	 */
	public FakeClass(String name, Constructor<?>[] constructors, int accessibleOffset) {
		this(name, constructors, new Field[0], accessibleOffset);
	}
	
	/**
	 * 
	 * Executor only class
	 * 
	 * @param name
	 * @param methods
	 */
	public FakeClass(String name, Constructor<?>[] constructors, Method[] methods, int accessibleOffset) {
		this(name, constructors, methods, new Field[0], accessibleOffset);
	}
	
	/**
	 * 
	 * Data host class
	 * 
	 * @param name
	 * @param fields
	 */
	public FakeClass(String name, Constructor<?>[] constructors, Field[] fields, int accessibleOffset) {
		this(name, constructors, new Method[0], fields, accessibleOffset);
	}
	
	/**
	 * 
	 * Fully known class
	 * 
	 * @param name
	 * @param methods
	 * @param fields
	 */
	public FakeClass(String name, Constructor<?>[] constructors, Method[] methods, Field[] fields, int accessibleOffset) {
		this.name = name;
		this.methods = methods;
		this.fields = fields;
		this.constructors = constructors;
		this.accessibleOffset = accessibleOffset;
		passifySecurity();
	}
	
	public void setAccessibleOffset(int offset) {
		this.accessibleOffset = offset;
	}
	
	public void setPreInstance(Object preInstance) {
		this.preInstance = preInstance;
	}
	
	public void setRealClass(Class<?> clazz) {
		realClass = clazz;
	}
	
	public Class<?> getRealClass(){
		return realClass;
	}
	
	public void dumpAllMethods() {
		Arrays.stream(methods).forEach(m -> System.out.println(m));
	}
	
	/**
	 * 
	 * With a cup of tea, not even the S.A.S could se anything wrong with you. This becomes especially true when your holding some nice ol' green tea
	 * 
	 */
	private void passifySecurity() {
		unlockArray(constructors);
		unlockArray(methods);
		unlockArray(fields);
	}
	
	private void unlockArray(AccessibleObject[] accessibles) {
		try {
			for(AccessibleObject acc : accessibles) {
				acc.setAccessible(true);
			}
		}catch(Exception e) {
			for(AccessibleObject acc : accessibles) {
				OffHeapStruct.THE_UNSAFE.putBoolean(acc, accessibleOffset, true);
			}
		}
	}
	
	public Object instantiate(Object...constructs) {
		Class<?>[] params = getTypes(constructs);
		try {
			return Arrays.stream(constructors).filter(c -> Arrays.equals(c.getParameterTypes(), params)).findFirst().get().newInstance(constructs);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public Object invoke(String method, Object instance, Object...objects) {
		if(instance == null) instance = preInstance;
		Class<?>[] params = getTypes(objects);
		try {
			Method toInvoke = findMethod(method, objects);
			return toInvoke.invoke(instance, objects);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	private Method findMethod(String name, Object...objects) {
		try {
			for(Method method : methods) {
				if(method.getName().equals(name) && method.getParameterCount() == objects.length) {
					boolean b = true;
					for(int i = 0; i < objects.length; i++) {
						Class<?> type = method.getParameterTypes()[i];
						Field t = null;
						try {
							t = objects[i].getClass().getDeclaredField("TYPE");
						}catch(Exception e) {
							
						}
						if(!type.isAssignableFrom(objects[i].getClass()) && (t == null || !type.equals(t.get(null)))) {
							b = false;
						}
					}
					if(b) return method;
				}
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private Class<?>[] getTypes(Object...objects){
		Class<?>[] types = new Class<?>[objects.length];
		for(int i = 0; i < objects.length; i++) {
			types[i] = objects[i].getClass();
		}
		return types;
	}

}
