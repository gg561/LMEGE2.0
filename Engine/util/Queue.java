package util;

public interface Queue<T> extends Container<T> {
	
	public T queue_pop();
	public void queue_push(T t);

}
