package events;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("rawtypes")
public class EventLobby implements Listener {
	
	private Map<Class<? extends Event>, Listener> listeners = new HashMap<>();
	
	public EventLobby() {
		listeners.put(MovableMoveEvent.class, AbstractMovableEvent.LISTENER);
		listeners.put(MovableRotateEvent.class, AbstractMovableEvent.LISTENER);
		listeners.put(MovablePositionEvent.class, AbstractMovableEvent.LISTENER);
	}
	
	@SuppressWarnings("unchecked")
	public void invoke(Event event) {
		if(listeners.containsKey(event.getClass())) {
			listeners.get(event.getClass()).invoke(event);
		}else {
			
		}
	}

}
