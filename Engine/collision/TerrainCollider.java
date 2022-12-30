package collision;

import java.util.Arrays;
import java.util.function.Predicate;

import org.joml.Vector3f;

import actors.Movable;
import terrain.Terrain;
import util.Vectors;

public class TerrainCollider extends Collider {
	
	private Terrain binder;

	public TerrainCollider(Terrain binder) {
		super(null, new Vector3f(binder.getPosition().x, 0, binder.getPosition().y), 1, true);
		super.complexity = 1;
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
		float height = binder.getHeightOfTerrain(location.x, location.z);
		if(location.y < height) {
			return true;
		}/*else if(location.y - height == 1) {
			return true;
		}*/
		return false;
	}

	@Override
	public boolean containsExec(Vector3f location) {
		// TODO Auto-generated method stub
		float height = binder.getHeightOfTerrain(location.x, location.z);
		if(location.y <= height) {
			collision.addCollider(null, new Vector3f(0, height - location.y, 0));
			return true;
		}else if(location.y - height == 1) {
			collision.addCollider(null, new Vector3f(0));
		}
		return false;
	}
	
	@Override
	public boolean contains(Movable movable) {
		Collider other = movable.getCollider();
		if(other.contains(this)) {
			return true;
		}
		return false;
	}
	
	@Override
	public boolean containsExec(Movable movable) {
		// TODO Auto-generated method stub this.collider.getPosition().y - this.collider.getBounds().y
		float x = movable.getPosition().x, z = movable.getPosition().z;
		//System.out.println(x + " " + y + " " + z);
		float height = binder.getHeightOfTerrain(x, z);
		Collider other = movable.getCollider();
		/*
		if(other.containsExec(this)) {
			return true;
		}*/
		if(other.containsExec(this)) {/*
			if(other.collision.getDirection(this).distance(0, 0, 0) < 1.01f) {
				other.collision.removeCollision(this);
				other.collision.addCollider(this, new Vector3f(0, 0, 0));
			}
			if(other.getPosition().y + other.getBounds().y < height) {
				other.getCollision().addCollider(this, new Vector3f(0, height - y, 0));
				return true;
			}*/
			//ROTATE OTHER
			/*
			if(other instanceof PointBasedCollider) {
				Vector3f zPointFront = new Vector3f(((PointBasedCollider) other).getCorners()[5].sub(((PointBasedCollider) other).getCorners()[4], new Vector3f())).div(2).add(((PointBasedCollider) other).getCorners()[4]);
				Vector3f zPointBack = new Vector3f(((PointBasedCollider) other).getCorners()[1].sub(((PointBasedCollider) other).getCorners()[0], new Vector3f())).div(2).add(((PointBasedCollider) other).getCorners()[0]);
				float frontHeight = Terrain.getTerrain(zPointFront.x, zPointFront.z).getHeightOfTerrain(zPointFront.x, zPointFront.z);
				float backHeight = Terrain.getTerrain(zPointBack.x, zPointBack.z).getHeightOfTerrain(zPointBack.x, zPointBack.z);
				//System.out.println((frontHeight - backHeight) / zPointFront.mul(other.getForward()).distance(zPointBack.mul(other.getForward())));
				//System.out.println(zPointFront.mul(other.getForward()).distance(zPointBack.mul(other.getForward())));
				movable.rotate(movable.getRight().mul((float) Math.toRadians(Math.asin((frontHeight - backHeight) / zPointFront.distance(zPointBack))), new Vector3f()));
				System.out.println(movable.getRotation());
				System.out.println(movable.getRight());
			}*/
			return true;
		}/*
		if(other.getPosition().y + other.getBounds().y < height) {
			other.getCollision().addCollider(this, new Vector3f(0, height - y, 0));
			System.out.println("OUTERMOST " + other.getCollision());
			return true;
		}*/
		/*
		if(y <= height) {
			other.collision.addCollider(this, new Vector3f(0, 1, 0));
			return true;
		}else if(y - height <= 1) {
			other.collision.addCollider(this, new Vector3f(0));
			return true;
		}else 
		*/
		return false;
	}
	
	public void clearCollision() {
		collision.clearCollisions();
	}
	
	public TerrainCollider clone() {
		return new TerrainCollider(this.binder);
	}
	
	public void addCollision(Collider collider, Vector3f direction) {
		if(!this.immobile) {
			this.collision.addCollider(collider, direction);
		}
	}
	
	public void collide(Collider other) {
		
	}
	
	public void reactToCollision(Predicate<Collider> pred) {
		
	}

	@Override
	public Vector3f containsCorner(Vector3f vector) {
		// TODO Auto-generated method stub
		float height = binder.getHeightOfTerrain(vector.x, vector.z);
		if(vector.y < height) {
			return new Vector3f(0, height - vector.y, 0);
		}else if(Float.isNaN(height)) {
			return new Vector3f(0, 0, 0);
		}
		System.out.println(height);
		return Vectors.ZERO_3D;
	}

}
