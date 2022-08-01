package collision;

import java.util.ArrayList;
import java.util.List;

import org.joml.Vector3f;

import actors.Movable;

public abstract class Collider extends Movable {
	
	protected static List<Collider> colliders = new ArrayList<Collider>();
	
	protected Vector3f bounds;
	protected Vector3f position;
	protected boolean isColliding;
	protected boolean immobile;
	protected Collision collision;
	protected Movable bounded;
	protected float scale;
	
	public Collider(Vector3f bounds, Movable bounded, float scale, boolean immobile) {
		super();
		this.scale = scale;
		super.setScale(new Vector3f(scale));
		this.bounds = bounds.mul(scale, new Vector3f());
		this.position = bounded.getPosition();
		this.bounded = bounded;
		this.immobile = immobile;
		collision = new Collision();
	}
	
	public Collider(Vector3f bounds, Vector3f position, float scale, boolean immobile) {
		super();
		super.setScale(new Vector3f(scale));
		this.bounds = bounds;
		this.position = position;
		this.immobile = immobile;
		collision = new Collision();
	}
	
	public abstract Collision detectCollision(Collider other);
	public abstract void reactToCollision();
	public abstract void reactToCollision(Collider other);
	public abstract boolean contains(Vector3f location);
	public abstract boolean containsExec(Vector3f location);
	public abstract boolean contains(Movable movable);
	public abstract boolean containsExec(Movable movable);
	
	public boolean isColliding() {
		return isColliding;
	}
	
	public Collision getCollision() {
		return collision;
	}
	
	public Vector3f getPosition() {
		return position;
	}
	
	public void setPosition(Vector3f position) {
		this.position = position;
	}
	
	public Vector3f getBounds() {
		return bounds;
	}
	
	public Movable getBounded() {
		return bounded;
	}
	
	public void setBounded(Movable bounded) {
		this.bounded = bounded;
	}
	
	public boolean isImmobile() {
		return immobile;
	}
	
	public void setImmobile(boolean immobile) {
		this.immobile = immobile;
	}
	
	public Collision detectForAllCollisions() {
		for(Collider collider : colliders) {
			this.detectCollision(collider);
		}
		return collision;
	}
	
	public abstract Collider clone();
	
	public void move(Vector3f translation) {
		super.move(translation);
	}
}
