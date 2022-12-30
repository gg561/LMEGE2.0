package events;

import org.joml.Vector3f;

import actors.Movable;

public class MovableScaleEvent extends AbstractMovableEvent {

	public MovableScaleEvent(Vector3f v0, Vector3f v1, Movable object) {
		super(v0, v1, object);
		// TODO Auto-generated constructor stub
	}
	
	public MovableScaleEvent(Vector3f v0, float percentage, Movable object) {
		super(v0, new Vector3f(percentage), object);
	}

	@Override
	public void execute(Movable t) {
		// TODO Auto-generated method stub
		t.getScale().set(getV1());
	}

}
