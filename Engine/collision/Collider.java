package collision;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import org.joml.Vector3f;

import actors.Camera;
import actors.ImmutableMovable;
import actors.Movable;
import castable.Leech;


/**
 * 
 * Collider class is Property-Shared by default.
 * Other subclasses may change this by using cloned Properties. (e.g. new Vector3f(position))
 * 
 * @author huangyoulin
 *
 */
public abstract class Collider extends Movable {
	
	protected static List<Collider> colliders = new ArrayList<Collider>();
	
	protected Vector3f bounds;
	protected boolean isColliding;
	protected boolean immobile;
	protected Collision collision;
	protected Movable bounded;
	protected float size;
	protected float complexity;
	
	/**
	 * 
	 * Property Sharing constructor
	 * 
	 * @param bounds
	 * @param bounded
	 * @param scale
	 * @param immobile
	 */
	public Collider(Vector3f bounds, Movable bounded, float scale, boolean immobile) {
		super(bounded);
		this.size = scale;
		this.bounds = bounds.mul(scale, new Vector3f());
		this.bounded = bounded;
		this.immobile = immobile;
		collision = new Collision();
	}
	
	public Collider(Vector3f bounds, Vector3f position, float scale, boolean immobile) {
		super();
		this.scale = new Vector3f(scale);
		this.bounds = bounds;
		this.position = position;
		this.immobile = immobile;
		collision = new Collision();
	}
	
	public abstract Collision detectCollision(Collider other);
	
	public void reactToCollision() {
		if(!immobile) {
			this.getBounded().move(this.getCollision().getCombinedDirection());
		}
	}
	
	public void reactToCollision(Collider other) {
		if(!immobile) {
			this.getBounded().move(this.getCollision().getDirection(other));
		}
	}
	
	public void reactToCollision(Predicate<Collider> pred) {
		if(!immobile) {
			this.getBounded().move(this.getCollision().getCombinedDirection(pred));
		}
	}
	
	public abstract boolean contains(Vector3f location);
	public abstract boolean containsExec(Vector3f location);
	public abstract boolean contains(Movable movable);
	public abstract boolean containsExec(Movable movable);
	public abstract Vector3f containsCorner(Vector3f vector);
	
	public void addCollision(Collider collider, Vector3f direction) {
		this.getCollision().addCollider(collider, direction);
	}
	
	public abstract void collide(Collider other);
	
	public boolean isColliding() {
		return isColliding;
	}
	
	public Collision getCollision() {
		return collision;
	}

	/**
	 * This method returns a clone of the Host's position
	 * The actual reference is inaccessible using this method,
	 * please refer to the Host object instead.
	 */
	public Vector3f getPosition() {
		return new Vector3f(position);
	}
	
	public void setPosition(Vector3f position) {
		Leech.leechWarning();
	}

	/**
	 * This method returns a clone of the Host's rotation
	 * The actual reference is inaccessible using this method,
	 * please refer to the Host object instead.
	 */
	public Vector3f getRotation() {
		return new Vector3f(rotation);
	}
	
	public void setRotation(Vector3f rotation) {
		Leech.leechWarning();
	}
	
	/**
	 * This method returns a clone of the Host's scale
	 * The actual reference is inaccessible using this method,
	 * please refer to the Host object instead.
	 */
	public Vector3f getScale() {
		return new Vector3f(scale);
	}
	
	public void setScale(Vector3f scale) {
		Leech.leechWarning();
	}
	
	/**
	 * This method returns a clone of the Host's forward direction
	 * The actual reference is inaccessible using this method,
	 * please refer to the Host object instead.
	 */
	public Vector3f getForward() {
		return new Vector3f(forward);
	}
	
	public void setForward(Vector3f forward) {
		Leech.leechWarning();
	}
	
	/**
	 * This method returns a clone of the Host's right direction
	 * The actual reference is inaccessible using this method,
	 * please refer to the Host object instead.
	 */
	public Vector3f getRight() {
		return new Vector3f(right);
	}
	
	public void setRight(Vector3f right) {
		Leech.leechWarning();
	}
	
	/**
	 * This method returns a clone of the Host's up direction
	 * The actual reference is inaccessible using this method,
	 * please refer to the Host object instead.
	 */
	public Vector3f getUp() {
		return new Vector3f(up);
	}
	
	public void setUp(Vector3f up) {
		Leech.leechWarning();
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
}
