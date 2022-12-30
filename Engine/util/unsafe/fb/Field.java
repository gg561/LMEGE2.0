package util.unsafe.fb;

public class Field implements Comparable<Field>{
	
	public final String name, typeName;
	public final long offset;
	public final boolean isStatic;

	Field(String name, String typeName, long offset, boolean isStatic) {
		super();
		this.name = name;
		this.typeName = typeName;
		this.offset = offset;
		this.isStatic = isStatic;
	}
	
	public int compareTo(Field other) {
		if(isStatic != other.isStatic) {
			return isStatic ? -1 : 1;
		}
		return Long.compare(offset, other.offset);
	}
	
	public String toString() {
		if(isStatic) {
			return "static " + typeName + ' ' + name + " @ 0x" + Long.toHexString(offset);
		}
		return typeName + ' ' + " @ " + offset;
	}

}
