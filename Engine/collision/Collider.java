package collision;

import java.util.ArrayList;
import java.util.List;

import org.joml.Vector3f;

import actors.Movable;

public abstract class Collider {
	
	protected static List<Collider> colliders = new ArrayList<Collider>();
	
	protected Vector3f bounds;
	protected float scale;
	protected Vector3f position;
	protected boolean isColliding;
	protected boolean immobile;
	protected Collision collision;
	
	public Collider(Vector3f bounds, Vector3f position, float scale, boolean immobile) {
		this.bounds = bounds;
		this.position = position;
		this.scale = scale;
		this.immobile = immobile;
		collision = new Collision();
	}
	
	public abstract Collision detectCollision(Collider other);
	public abstract void reactToCollision();
	public abstract boolean contains(Vector3f location);
	public abstract boolean containsExec(Vector3f location);
	
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
	
	public float getScale() {
		return scale;
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

}
