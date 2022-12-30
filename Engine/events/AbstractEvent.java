package events;

public abstract class AbstractEvent<T> implements Event<T> {
	
	private boolean isActive;
	
	public void start() {
		isActive = true;
	}
	
	public void stop() {
		isActive = false;
	}
	
	public boolean isActive() {
		return isActive;
	}

}
