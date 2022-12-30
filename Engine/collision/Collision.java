package collision;

import java.util.HashMap;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.joml.Vector3f;

import util.Vectors;

public class Collision {
	
	private static final float SLOPE_TOLERANCE = 1f;
	
	private HashMap<Collider, Vector3f> collisions = new HashMap<Collider, Vector3f>();
	
	public Collision(Collider[] colliders, Vector3f[] directions) {
		int index = 0;
		for(Collider collider : colliders) {
			collisions.put(collider, directions[index]);
			index++;
		}
	}
	
	public Collision() {
		
	}
	/*
	 * if collision does not contain this collider, add it to the list
	 * else overlap the collision directions
	 */
	public void addCollider(Collider collider, Vector3f direction) {
		if(!collisions.containsKey(collider)) collisions.put(collider, direction);
		else collisions.get(collider).add(direction);
	}
	
	public Vector3f getDirection(Collider collider) {
		return this.collisions.get(collider);
	}
	
	public Vector3f getCombinedDirection() {
		Vector3f direction = new Vector3f();
		for(Collider collider : collisions.keySet()) {
			direction.add(collisions.get(collider));
		}
		return direction;
	}
	
	public Vector3f getCombinedDirection(Predicate<Collider> pred) {
		Vector3f direction = new Vector3f();
		for(Collider collider : collisions.keySet().stream().filter(pred).collect(Collectors.toList())) {
			direction.add(collisions.get(collider));
		}
		return direction;
	}
	
	public void mergeCollisions(Collider first, Collider scnd) {
		if(first == null || scnd == null) return;
		addCollider(first, getDirection(scnd));
		removeCollision(scnd);
	}
	
	public void mergeCollisions(Predicate<Collider> pred) {
		Collider cached = null;
		for(Collider collider : collisions.keySet().stream().filter(pred).collect(Collectors.toList())) {
			mergeCollisions(collider, cached);
			cached = collider;
		}
	}
	
	public void clearCollisions() {
		collisions.clear();
	}
	
	public void removeCollision(Collider collider) {
		this.collisions.remove(collider);
	}
	
	public void removeCollision(Predicate<Collider> pred) {
		for(Collider col : collisions.keySet().stream().filter(pred).collect(Collectors.toList())) {
			this.collisions.remove(col);
		}
	}
	
	public String toString() {
		return collisions.toString();
	}

}
