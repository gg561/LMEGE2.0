package collision;

import org.joml.Vector3f;

import actors.Movable;
import terrain.Terrain;

public class TerrainCollider extends Collider {
	
	private Terrain binder;

	public TerrainCollider(Terrain binder) {
		super(null, new Vector3f(binder.getPosition().x, 0, binder.getPosition().y), 1, true);
		this.binder = binder;
		// TODO Auto-generated constructor stub
	}

	@Override
	public Collision detectCollision(Collider other) {
		// TODO Auto-generated method stub
		Collision collision = new Collision();
		if(other.getPosition().y + other.getBounds().y < binder.getHeightOfTerrain(other.getPosition().x, other.getPosition().z)) {
			collision.addCollider(other, new Vector3f(0, -1, 0));
		}
		return collision;
	}

	@Override
	public void reactToCollision() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void reactToCollision(Collider other) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean contains(Vector3f location) {
		if(location.y < binder.getHeightOfTerrain(location.x, location.z)) {
			return true;
		}
		return false;
	}

	@Override
	public boolean containsExec(Vector3f location) {
		// TODO Auto-generated method stub
		float height = binder.getHeightOfTerrain(location.x, location.z);
		System.out.println("HEIGHT " + height);
		System.out.println("LOC " + location.y);
		if(location.y <= height) {
			System.out.println("LOCALE " + (location.y - height));
			collision.addCollider(null, new Vector3f(0, height - location.y, 0));
			return true;
		}
		return false;
	}
	
	@Override
	public boolean contains(Movable movable) {
		if(movable.getPosition().y < binder.getHeightOfTerrain(movable.getPosition().x, movable.getPosition().z)) {
			return true;
		}
		return false;
	}
	
	@Override
	public boolean containsExec(Movable movable) {
		// TODO Auto-generated method stub this.collider.getPosition().y - this.collider.getBounds().y
		float x = movable.getPosition().x, z = movable.getPosition().z, y = movable.getPosition().y - movable.getCollider().getBounds().y;
		//System.out.println(x + " " + y + " " + z);
		float height = binder.getHeightOfTerrain(x, z);
		if(y <= height) {
			movable.getCollider().collision.addCollider(this, new Vector3f(0, height - y, 0));
			return true;
		}else if(y < height + 1 && y > height) {
			movable.getCollider().collision.addCollider(this, new Vector3f(0));
			return true;
		}
		return false;
	}
	
	public void clearCollision() {
		collision.clearCollisions();
	}
	
	public TerrainCollider clone() {
		return new TerrainCollider(this.binder);
	}

}
