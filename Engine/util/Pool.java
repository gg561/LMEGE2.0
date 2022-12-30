package util;

public interface Pool<T> extends AutoCloseable {
	
	public void pushFront(T t);
	public void pushBack(T t);
	public T popFront();
	public T popBack();
	
	public void clear();

}
