package castable;

import actors.Existable;
import events.Event;

public interface Compound {
	
	public void fireEvent(Event event);
	public void syncAll();
	public Existable[] getAllComponents();

}
