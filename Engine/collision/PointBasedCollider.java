package collision;

import java.util.Arrays;
import java.util.function.Predicate;

import org.joml.Vector3f;

import actors.Movable;
import castable.AccessRestraint;
import castable.Leech;
import game.Main;
import util.Vectors;

public abstract class PointBasedCollider extends Collider implements Leech {
	
	protected Vector3f[] corners;
	protected Vector3f[] localCorners;
	
	public PointBasedCollider(Vector3f bounds, Movable bounded, float scale, boolean immobile, int points) {
		super(bounds, bounded, scale, immobile);
		// TODO Auto-generated constructor stub
		corners = new Vector3f[points];
		localCorners = new Vector3f[points];
		recalculateCorners();
	}
	
	public PointBasedCollider(Vector3f bounds, Vector3f position, float scale, boolean immobile, int points) {
		super(bounds, position, scale, immobile);
		corners = new Vector3f[points];
		localCorners = new Vector3f[points];
		recalculateCorners();
	}
	
	public Vector3f[] getCorners() {
		return corners;
	}
	
	public abstract void recalculateCorners();
	
	public void updateCorners() {
		for(int i = 0; i < corners.length; i ++) {
			Vector3f direction = localCorners[i].sub(super.getLocalPosition(), new Vector3f());
			direction.rotateX(this.getRotation().x);
			direction.rotateY(this.getRotation().y);
			direction.rotateZ(this.getRotation().z);
			corners[i] = super.getPosition().add(direction, new Vector3f());
		}
	}
	
	//COMMON COLLIDER METHODS
	public boolean contains(Vector3f vector) {
		Vector3f dx = this.getRight(), dy = this.getUp(), dz = this.getForward();
		Vector3f d = vector.sub(this.getPosition(), new Vector3f());
		if(Math.abs(d.dot(dx)) <= this.bounds.x && Math.abs(d.dot(dy)) <= this.bounds.y && Math.abs(d.dot(dz)) <= this.bounds.z) {
			return true;
		}
		return false;
	}
	
	public boolean containsExec(Vector3f vector) {
		if(contains(vector)) {
			this.collision.addCollider(null, vector);
			return true;
		}
		return false;
	}
	
	public boolean contains(Movable movable) {
		Collider other;
		if(movable instanceof Collider) {
			other = (Collider) movable;
		}else {
			other = movable.getCollider();
		}
		Vector3f collisionDir = containsCorners(other);
		return (collisionDir.x != 0 || collisionDir.y != 0 || collisionDir.z != 0);
	}
	
	public boolean containsExec(Movable movable) {
		Collider other;
		if(movable instanceof Collider)
			other = (Collider) movable;
		else
			other = movable.getCollider();
		Vector3f collisionDir = containsCorners(other);
		this.collision.addCollider(other, collisionDir);
		return (collisionDir.x != 0 || collisionDir.y != 0 || collisionDir.z != 0);
	}
	
	public void collide(Collider other) {
		boolean oContain = containsExec(other);
		boolean pContain = other.containsExec(this);
		boolean contains = oContain || pContain;
		if(contains) {
			other.reactToCollision(this);
			other.getCollision().removeCollision(this);
			reactToCollision(other);
			getCollision().removeCollision(other);
		}
	}
	
	public void reactToCollision(Predicate<Collider> pred) {
		if(!this.immobile)
			this.getBounded().move(this.getCollision().getCombinedDirection(pred));
	}
	
	public void reactToCollision(Collider other) {
		if(!this.immobile)
			this.getBounded().move(this.getCollision().getDirection(other));
	}
	
	public void reactToCollision() {
		if(!this.immobile)
			this.getBounded().move(this.getCollision().getCombinedDirection());
	}
	//COMMON COLLIDER METHODS END
	
	//POINT BASED COLLIDER METHODS
	
	public Vector3f containsCorner(Vector3f vector) {
		Vector3f dx = this.getRight(), dy = this.getUp(), dz = this.getForward();
		Vector3f d = vector.sub(this.getPosition(), new Vector3f());
		Vector3f rv = new Vector3f(Math.abs(d.dot(dx)) - this.bounds.x, Math.abs(d.dot(dy)) - this.bounds.y, Math.abs(d.dot(dz)) - this.bounds.z).mul(vector.normalize());
		/*if(Math.abs(d.dot(dx)) <= this.bounds.x && Math.abs(d.dot(dy)) <= this.bounds.y && Math.abs(d.dot(dz)) <= this.bounds.z) {
			return true;
		}*/
		return rv;
	}
	
	public Vector3f containsCorners(Collider other) {
		Vector3f collisionDir = new Vector3f();
		for(int i = 0; i < corners.length; i++) {
			if(other.contains(corners[i])) {
				//collisionDir.add(other.containsCorner(localCorners[i]));
				collisionDir.sub(localCorners[i].mul(0.25f / size, new Vector3f()));
				//collisionDir.sub(localCorners[i].mul);
			}
		}
		if(other instanceof PointBasedCollider) {
			Vector3f[] corners = ((PointBasedCollider) other).corners;
			for(int i = 0; i < corners.length; i++) {
				if(contains(corners[i])) {
					//collisionDir.add(containsCorner(((PointBasedCollider) other).localCorners[i]));
					collisionDir.add(containsCorner(corners[i]).mul(0.25f / size));
					//collisionDir.sub(((PointBasedCollider) other).localCorners[i].mul(0.25f / scale, new Vector3f()));
				}
			}
		}
		if(collisionDir.x != 0 || collisionDir.y != 0 || collisionDir.z != 0)
			collisionDir.add(collisionDir.normalize(new Vector3f()).mul(0.01f));
		return collisionDir;
	}
	//POINT BASED COLLIDER METHODS END
	
	//MOVABLE IMPLEMENTATION METHODS
	@AccessRestraint(modifier = "private")
	public void move0(Vector3f translation) {
		updateCorners();
	}
	
	@AccessRestraint(modifier = "private")
	public void setPosition(Vector3f position) {
		updateCorners();
	}
	
	@AccessRestraint(modifier = "private")
	public void rotate0(Vector3f rotationVector, Vector3f rotation, boolean rotateDirections) {
		updateCorners();
	}
	
	@AccessRestraint(modifier = "private")
	public void setRotationWithDirections(Vector3f rotation) {
		updateCorners();
	}
	
	public void setBounded(Movable bounded) {
		super.setBounded(bounded);
		recalculateCorners();
	}

}
