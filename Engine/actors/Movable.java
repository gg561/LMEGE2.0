package actors;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joml.Vector2f;
import org.joml.Vector3f;

import castable.Compound;
import castable.Directable;
import castable.ILocalMovable;
import castable.IMovable;
import castable.Syncable;
import collision.Collider;
import collision.TerrainCollider;
import events.Event;
import events.MovableMoveEvent;
import events.MovablePositionEvent;
import events.MovableRotateEvent;
import game.Main;
import terrain.Terrain;

/**
 * 
 * NOTE to apply Location Sharing
 * 
 * prhaps turn Movable into an objct instead of a superclass
 * 
 * @author huangyoulin
 *
 */
public class Movable implements ILocalMovable, Directable, Compound {
	
	protected Vector3f position;
	protected Vector3f localPosition;
	protected Vector3f rotation;
	protected Vector3f localRotation;
	protected Vector3f scale;
	protected Vector3f forward;
	protected Vector3f right;
	protected Vector3f up;
	@Attached
	private Collider collider;
	private boolean hasGravity;
	private float mass;
	protected boolean onGround;
	protected boolean debug;
	private Map<String, ILocalMovable> components = new HashMap<String, ILocalMovable>();
	
	public Movable() {
		this.position = new Vector3f(0);
		this.rotation = new Vector3f(0);
		this.scale = new Vector3f(1);
		this.localPosition = new Vector3f(0);
		this.localRotation = new Vector3f(0);
		this.forward = new Vector3f(0, 0, 1);
		this.right = new Vector3f(1, 0, 0);
		this.up = new Vector3f(0, 1, 0);
	}
	
	/**
	 * 
	 * Leechable Constructor/ Cloning Constructor
	 * 
	 * @param movable
	 */
	public Movable(Movable movable) {
		this.position = movable.position;
		this.rotation = movable.rotation;
		this.scale = movable.scale;
		this.localPosition = movable.localPosition;
		this.localRotation = movable.localRotation;
		this.forward = movable.forward;
		this.right = movable.right;
		this.up = movable.up;
	}
	
	public void rotate(Vector3f rotation) {
		rotate0(rotation);
	}
	
	public void rotate0(Vector3f rotation) {
		rotate0(this.getRotation(), rotation, true);
	}
	
	public void setRotationWithDirections(Vector3f rotation) {
		this.rotation.set(rotation);
		syncDirections();
		if(this.collider != null)
			this.collider.setRotationWithDirections(rotation);
	}
	
	/**
	 * TODO : test out effectiveness
	 * might require modifications to the MovableRotateEvent
	 * 
	 * The MovableRotateEvent calls for a rotation and to rotate the directions, but rotateLocal should only affect localRotation.
	 * 
	 * @param rotation
	 */
	public void rotateLocal(Vector3f rotation) {
		rotate0(this.getLocalRotation(), rotation, false);
	}
	
	public void rotate0(Vector3f rotationVector, Vector3f rotation, boolean rotateDirections) {
		MovableRotateEvent movableRotateEvent = new MovableRotateEvent(rotationVector, this.getRotation(), rotation, this, rotateDirections);
		Main.EVENT_LOBBY.invoke(movableRotateEvent);
	}
	
	/**
	 * 
	 * User-friendly move method
	 * 
	 */
	public void move(Vector3f translation) {
		move(translation, forward, up, right);
	}
	
	/**
	 * 
	 * MOVE RAW
	 * 
	 * This translation is unaffected by the directions
	 * 
	 */
	public void move0(Vector3f translation) {
		MovableMoveEvent moveEvent = new MovableMoveEvent(this, translation);
		Main.EVENT_LOBBY.invoke(moveEvent);
	}
	
	/**
	 * 
	 * This translation has settable directions
	 * 
	 * @param translation
	 * @param forward - forward direction
	 * @param up - up direction
	 * @param right - right direction
	 */
	public void move(Vector3f translation, Vector3f forward, Vector3f up, Vector3f right) {
		Vector3f posDelta = new Vector3f().add(forward.mul(translation.z, new Vector3f())).add(up.mul(translation.y, new Vector3f())).add(right.mul(translation.x, new Vector3f()));
		move0(posDelta);
	}
	
	public void update() {
		if(hasGravity) {
			boolean rebound = false;
			Terrain terrain = Terrain.getTerrain(this.getPosition().x, this.getPosition().z);
			float height = terrain.getHeightOfTerrain(this.getPosition().x, this.getPosition().z);
			for(Terrain te : Terrain.getTerrains().values()) {
				if(te.getCollider().containsExec(this)) {
					rebound = true;
				}
			}
			if(rebound) {
				Vector3f dir = this.getCollider().getCollision().getCombinedDirection(c -> c instanceof TerrainCollider);
				this.move(dir.mul(new Vector3f(0.15f, 0, 0.15f)));
				if(!Float.isNaN(height)) {
					this.move(new Vector3f(0, (height - this.getCollider().getPosition().y + this.getCollider().getBounds().y - 0.01f) / 2, 0));
				}
				this.getCollider().getCollision().removeCollision(c -> c instanceof TerrainCollider);
				onGround = true;
			}else {
				this.move(new Vector3f(0, -mass, 0));
			}
		}
	}
	
	public Vector3f orbit(Vector3f center, Vector3f velocity) {
		Vector3f orbitPosition = center.add(velocity, new Vector3f());
		this.setPosition(orbitPosition);
		return orbitPosition;
	}
	
	public void scale(float percentage) {
		scale = scale.mul(percentage);
		if(this.collider != null)
			this.collider.scale(percentage);
	}
	
	public void setDebug(boolean debug) {
		this.debug = debug;
	}
	
	public boolean isDebug() {
		return debug;
	}
	
	public Vector3f getForward() {
		return forward;
	}
	
	public void setForward(Vector3f forward) {
		this.forward = forward;
	}
	
	public Vector3f getRight() {
		return right;
	}
	
	public void setRight(Vector3f right) {
		this.right = right;
	}
	
	public Vector3f getUp() {
		return up;
	}
	
	public void setUp(Vector3f up) {
		this.up = up;
	}
	
	public Vector3f getPosition() {
		return position;
	}
	
	public void setPosition(Vector3f position) {
		MovablePositionEvent movablePositionEvent = new MovablePositionEvent(position, this);
		Main.EVENT_LOBBY.invoke(movablePositionEvent);
	}
	
	public void setPosition0(Vector3f position) {
		this.position.set(position);
	}
	
	public Vector3f getRotation() {
		return rotation;
	}
	
	public void setRotation(Vector3f rotation) {
		this.rotation = rotation;
	}
	
	public void setRotation0(Vector3f rotation) {
		this.rotation.set(rotation);
	}

	public Vector3f getScale() {
		return scale;
	}

	public void setScale(Vector3f scale) {
		this.scale.set(scale);
	}
	
	public Vector3f getLocalPosition() {
		return localPosition;
	}
	
	public void setLocalPosition(Vector3f localPosition) {
		this.localPosition.set(localPosition);
	}
	
	public Vector3f getLocalRotation() {
		return localRotation;
	}
	
	public void setLocalRotation(Vector3f localRotation) {
		this.localRotation.set(localRotation);
	}
	
	/**
	 * Do not employ without rotate(Vector3f rotation);
	 * @param rotation
	 */
	public void rotateDirections(Vector3f rotation) {
		this.forward.rotateY(rotation.y);
		this.forward.rotateX(rotation.x);
		this.right.rotateY(rotation.y);
		this.right.rotateZ(rotation.z);
		this.up.rotateX(rotation.x);
		this.up.rotateZ(rotation.z);
	}
	
	public void setMass(float mass) {
		this.mass = mass;
	}
	
	public float getMass() {
		return mass;
	}
	
	public void setHasGravity(boolean hasGravity) {
		this.hasGravity = hasGravity;
	}
	
	public boolean isHasGravity() {
		return hasGravity;
	}
	
	public Collider getCollider() {
		return (Collider) components.get("collider");
	}
	
	public void setCollider(Collider collider) {
		components.put("collider", collider);
	}
	
	public boolean isOnGround() {
		return onGround;
	}

	@Override
	public void fireEvent(Event event) {
		// TODO Auto-generated method stub
		event.start();
	}

	@Override
	public void syncAll() {
		// TODO Auto-generated method stub
		for(Syncable syncable : components.values()) {
			syncable.syncWith(this);
		}
	}

	@Override
	public Existable[] getAllComponents() {
		// TODO Auto-generated method stub
		return components.values().toArray(new Existable[components.size()]);
	}

	@Override
	public void syncWith(Movable movable) {
		// TODO Auto-generated method stub
		this.position = movable.getPosition();
		this.rotation = movable.getRotation();
		this.scale = movable.getScale();
		this.forward = movable.getForward();
		this.right = movable.getRight();
		this.up = movable.getUp();
		this.localPosition = movable.getLocalPosition();
		this.localRotation = movable.getLocalRotation();
		this.mass = movable.getMass();
		this.hasGravity = movable.isHasGravity();
	}

	@Override
	public void resetDirections() {
		// TODO Auto-generated method stub
		this.forward.set(0, 0, 1);
		this.right.set(1, 0, 0);
		this.up.set(0, 1, 0);
	}

	@Override
	public void syncDirections() {
		// TODO Auto-generated method stub
		resetDirections();
		rotateDirections(this.getRotation());
	}

}
