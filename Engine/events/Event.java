package events;

import castable.Executable;

public interface Event<T> extends Executable<T> {
	
	public void start();
	public void stop();
	public boolean isActive();

}
