package events;

import org.joml.Vector3f;

import actors.ActorGroup;
import actors.Existable;
import actors.Movable;

public class MovableMoveEvent extends AbstractMovableEvent{
			
	private MovableMoveEvent() {
		super(null, null, null);
	}
	
	public MovableMoveEvent(Movable movable, Vector3f newPos) {
		super(movable.getPosition(), newPos, movable);
	}

	@Override
	public void execute(Movable t) {
		// TODO Auto-generated method stub
		t.getPosition().add(getV1());
		for(Existable exist : t.getAllComponents()) {
			if(exist instanceof Movable) {
				((Movable) exist).move0(getV1());
			}
		}
	}

}
