package actors;

import org.joml.Vector3f;

import game.Main;
import util.Vectors;

public class ActorGroup extends Movable{
	
	private Movable[] actors;
	
	public ActorGroup(Movable...actors) {
		super();
		this.actors = actors;
	}
	
	public void rotate(Vector3f rotation) {
		super.rotate(rotation);
		for(Movable actor : actors) {
			Vector3f orbit = calculateTrigonometryDirection(actor);
			actor.rotate(rotation);
			actor.orbit(super.getPosition(), orbit);
		}
	}
	
	public void move(Vector3f translation, Vector3f forward, Vector3f up, Vector3f right) {
		super.move(translation, forward, up, right);
		for(Movable actor : actors) {
			actor.move(translation, forward, up, right);
		}
	}
	
	
	public void matchPosition(Vector3f position) {
		super.setPosition(position);
		for(Movable actor : actors) {
			if(actor instanceof ActorGroup) ((ActorGroup) actor).matchPosition(actor.getLocalPosition().add(position, new Vector3f()));
			else actor.setPosition(actor.getLocalPosition().add(position, new Vector3f()));
		}
	}
	
	private Vector3f calculateTrigonometryDirection(Movable actor) {
		Vector3f direction = new Vector3f(actor.getLocalPosition());
		direction.rotateX(this.getRotation().x);
		direction.rotateY(this.getRotation().y);
		direction.rotateZ(this.getRotation().z);
		return direction;
	}
	
	public Movable[] getActors() {
		return actors;
	}

}
