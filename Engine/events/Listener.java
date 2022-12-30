package events;

public interface Listener<T extends Event> {
	
	public void invoke(T t);

}
