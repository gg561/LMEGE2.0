package events;

import org.joml.Vector3f;

import actors.Existable;
import actors.Movable;

public class MovableRotateEvent extends AbstractMovableEvent {
	
	private Vector3f rotationVector;
	private boolean rotateDirections;

	public MovableRotateEvent(Vector3f rotationVector, Vector3f v0, Vector3f v1, Movable object, boolean rotateDirections) {
		super(v0, v1, object);
		this.rotateDirections = rotateDirections;
		this.rotationVector = rotationVector;
		// TODO Auto-generated constructor stub
	}

	@Override
	public void execute(Movable t) {
		// TODO Auto-generated method stub
		rotationVector.add(getV1());
		if(rotateDirections)
			t.rotateDirections(getV1());
		for(Existable exist : t.getAllComponents()) {
			if(exist instanceof Movable) {
				((Movable) exist).rotate(getV1());
			}
		}
	}

}
