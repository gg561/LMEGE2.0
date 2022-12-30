package collision;

import java.util.function.Predicate;

import org.joml.Vector3f;

import actors.Movable;

public class SphereCollider extends Collider {
	
	float radius;
	
	public SphereCollider(Vector3f bounds, Movable bounded, float scale, boolean immobile) {
		super(bounds, bounded, scale, immobile);
	}

	@Override
	public Collision detectCollision(Collider other) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean contains(Vector3f location) {
		// TODO Auto-generated method stub
		return this.position.distance(location) < radius;
	}

	@Override
	public boolean containsExec(Vector3f location) {
		// TODO Auto-generated method stub
		Vector3f dir = location.sub(position, new Vector3f());
		if(dir.distance(0, 0, 0) < radius) {
			this.collision.addCollider(null, dir.negate());
			return true;
		}
		return false;
	}

	@Override
	public boolean contains(Movable movable) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean containsExec(Movable movable) {
		// TODO Auto-generated method stub
		Vector3f direction = movable.getPosition().sub(this.getPosition(), new Vector3f()).normalize();
		if(movable.getCollider().contains(direction.mul(radius))) {
			addCollision(movable.getCollider(), direction.normalize());
			return true;
		}
		return false;
	}

	@Override
	public Vector3f containsCorner(Vector3f vector) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void collide(Collider other) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Collider clone() {
		// TODO Auto-generated method stub
		return null;
	}

}
