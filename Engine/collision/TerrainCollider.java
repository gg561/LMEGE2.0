package collision;

import org.joml.Vector3f;

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

}
