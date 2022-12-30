package events;

public class DefaultMovableListener<T extends AbstractMovableEvent> implements Listener<T> {

	@Override
	public void invoke(T t) {
		// TODO Auto-generated method stub
		t.execute(t.getObject());
	}

}
