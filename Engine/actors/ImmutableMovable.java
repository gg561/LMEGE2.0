package actors;

import java.util.HashMap;
import java.util.Map;

import org.joml.Vector3f;

import castable.ILocalMovable;
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
 * This class is a useless wrapper around Movable. It removes all actions performed on properties
 * that concerns the location, rotation, and scaling of an object. This is to prevent accidental
 * double fire of the same action on a variable that is referenced from 2 different sources. All
 * public data will be immutable, at the exception of {@link actors.Movable#localPosition localPosition}
 * and {@link actors.Movable#localRotation localRotation} since they are supposedly "local".
 * 
 * @author huangyoulin
 *
 */
public class ImmutableMovable extends Movable {
	
	/**
	 * 
	 * Leechable Constructor/ Cloning Constructor
	 * Use this to share mathematical properties between 2 objects, such as collider and object.
	 * 
	 * @param movable
	 */
	public ImmutableMovable(Movable movable) {
		this.position = movable.position;
		this.rotation = movable.rotation;
		this.scale = movable.scale;
		this.localPosition = movable.localPosition;
		this.localRotation = movable.localRotation;
		this.forward = movable.forward;
		this.right = movable.right;
		this.up = movable.up;
	}
	
	public ImmutableMovable(Vector3f position, Vector3f rotation, Vector3f scale, Vector3f localPosition, Vector3f localRotation, Vector3f forward, Vector3f right, Vector3f up) {
		this.position = position;
		this.rotation = rotation;
		this.scale = scale;
		this.localPosition = localPosition;
		this.localRotation = localRotation;
		this.forward = forward;
		this.right = right;
		this.up = up;
	}
	
	public void rotate(Vector3f rotation) {
		
	}
	
	public void rotate0(Vector3f rotation) {
		
	}
	
	public void setRotationWithDirections(Vector3f rotation) {
		
	}
	
	public void rotateLocal(Vector3f rotation) {
		
	}
	
	public void rotate0(Vector3f rotationVector, Vector3f rotation, boolean rotateDirections) {
		
	}
	
	public void move(Vector3f translation) {
		
	}

	public void move0(Vector3f translation) {
		
	}

	public void move(Vector3f translation, Vector3f forward, Vector3f up, Vector3f right) {
		
	}
	
	public void update() {
		
	}
	
	public Vector3f orbit(Vector3f center, Vector3f velocity) {
		return new Vector3f();
	}
	
	public void scale(float percentage) {
		
	}
	
	public void setDebug(boolean debug) {
		
	}
	
	public void setForward(Vector3f forward) {
		
	}
	
	public void setRight(Vector3f right) {
		
	}
	
	public void setUp(Vector3f up) {
		
	}
	
	public void setPosition(Vector3f position) {
		
	}
	
	public void setPosition0(Vector3f position) {
		
	}
	
	public void setRotation(Vector3f rotation) {
		
	}
	
	public void setRotation0(Vector3f rotation) {
		
	}

	public void setScale(Vector3f scale) {
		
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
	
	public void rotateDirections(Vector3f rotation) {
		
	}
	
	public void setMass(float mass) {
		
	}
	
	public void setHasGravity(boolean hasGravity) {
		
	}
	
	public void setCollider(Collider collider) {
		
	}
}
