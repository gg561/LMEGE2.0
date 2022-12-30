package events;

import java.util.function.Consumer;

public abstract class AbstractActionedEvent<T> extends AbstractEvent<T> {
	
	protected Consumer<T> action;
	
	public AbstractActionedEvent(T t, Consumer<T> action) {
		this.action = action;
	}
	
	public void execute(T t) {
		action.accept(t);
	}

}
