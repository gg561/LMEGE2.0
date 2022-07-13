package collision;

import java.util.HashMap;

import org.joml.Vector3f;

public class Collision {
	
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
			System.out.println("COLLIDER " + collisions.get(collider));
			direction.add(collisions.get(collider));
		}
		System.out.println("DIR " + direction);
		return direction;
	}

}
