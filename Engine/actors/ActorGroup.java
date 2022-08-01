package actors;

import org.joml.Vector3f;

public class ActorGroup extends Movable{
	
	private Movable[] actors;
	
	public ActorGroup(Movable...actors) {
		this.actors = actors;
	}
	
	public void move(Vector3f translation) {
		super.move(translation);
		for(Movable actor : actors) {
			if(actor.getLocalPosition().equals(0, 0, 0)) {
				actor.move(translation);
			}else{
				Vector3f direction = actor.getLocalPosition().sub(super.getLocalPosition(), new Vector3f());
				direction.rotateX(this.getRotation().x);
				direction.rotateY(this.getRotation().y);
				direction.rotateZ(this.getRotation().z);
				actor.moveAround(super.getPosition(), direction);
			}
		}
	}
	
	public void rotate(Vector3f rotation) {
		super.rotate(rotation);
		for(Movable actor : actors) {
			Vector3f direction = actor.getLocalPosition().sub(super.getLocalPosition(), new Vector3f());
			direction.rotateX(this.getRotation().x);
			direction.rotateY(this.getRotation().y);
			direction.rotateZ(this.getRotation().z);
			actor.rotate(rotation);
			actor.moveAround(super.getPosition(), direction);
		}
	}
	
	public void update() {
		super.update();
		/*for(Movable actor : actors) {
			actor.setPosition(this.getPosition().add(actor.getLocalPosition(), new Vector3f()));
			/*if(actor instanceof ActorGroup) {
				actor.move(new Vector3f(0, -1, 0));
			}else
		}*/
	}
	
	public Movable[] getActors() {
		return actors;
	}

}
