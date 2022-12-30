package util.unsafe.fb;

import java.util.NoSuchElementException;
import java.util.Set;

public class Type {
	
	private static final Field[] NO_FIELDS = new Field[0];
	
	public final String name;
	public final String superName;
	public final int size;
	public final boolean isOop;
	public final boolean isInt;
	public final boolean isUnsigned;
	public final Field[] fields;
	
	Type(String name, String superName, int size, boolean isOop, boolean isInt, boolean isUnsigned,
			Set<Field> fields) {
		super();
		this.name = name;
		this.superName = superName;
		this.size = size;
		this.isOop = isOop;
		this.isInt = isInt;
		this.isUnsigned = isUnsigned;
		this.fields = fields == null ? NO_FIELDS : fields.toArray(new Field[0]);
	}
	
	public Field field(String name) {
		for(Field f : fields) {
			if(f.name.equals(name)) {
				return f;
			}
		}
		throw new NoSuchElementException("No such field : " + name + " in the type " + this.name);
	}
	
	public long global(String name) {
		Field f = field(name);
		if(f.isStatic) {
			return f.offset;
		}
		throw new IllegalArgumentException(name + " is not a static field");
	}
	
	public long offset(String name) {
		Field f = field(name);
		if(!f.isStatic) {
			return f.offset;
		}
		throw new IllegalArgumentException(name + " is not an instance field");
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder(name);
		if(superName != null) sb.append(" extends ").append(superName);
		sb.append(" @ ").append(size).append('\n');
		for(Field f : fields) {
			sb.append(" ").append(f).append('\n');
		}
		return sb.toString();
	}
	
}
