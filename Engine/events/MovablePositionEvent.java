package events;

import org.joml.Vector3f;

import actors.Existable;
import actors.Movable;

public class MovablePositionEvent extends AbstractMovableEvent {

	public MovablePositionEvent(Vector3f v1, Movable object) {
		super(object.getPosition(), v1, object);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void execute(Movable t) {
		// TODO Auto-generated method stub
		t.setPosition0(getV1());
		for(Existable exist : t.getAllComponents()) {
			if(exist instanceof Movable) {
				((Movable) exist).setPosition0(getV1());
			}
		}
	}

}
