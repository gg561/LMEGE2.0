package collision;

import org.joml.Quaternionf;
import org.joml.Vector3f;

import actors.Movable;

public class AABBCollider extends Collider{

	public AABBCollider(Vector3f bounds, Movable bounded, float scale, boolean immobile) {
		super(bounds, bounded, scale, immobile);
	}

	@Override
	public Collision detectCollision(Collider other) {
		if(this.position.x + this.bounds.x > other.position.x - other.bounds.x) {
			collision.addCollider(other, new Vector3f(1, 0, 0));
		}
		if(this.position.x - this.bounds.x < other.position.x + other.bounds.x) {
			collision.addCollider(other, new Vector3f(-1, 0, 0));
		}
		if(this.position.z + this.bounds.z > other.position.z - other.bounds.z) {
			collision.addCollider(other, new Vector3f(0, 0, 1));
		}
		if(this.position.z - this.bounds.z < other.position.z + other.bounds.z) {
			collision.addCollider(other, new Vector3f(0, 0, -1));
		}
		if(this.position.y + this.bounds.y > other.position.y - other.bounds.y) {
			collision.addCollider(other, new Vector3f(0, 1, 0));
		}
		if(this.position.y - this.bounds.y < other.position.y + other.bounds.y) {
			collision.addCollider(other, new Vector3f(0, -1, 0));
		}
		return collision;
	}
	
	@Override
	public boolean contains(Vector3f location) {
		if(location.x > this.position.x - this.bounds.x && location.x < this.position.x + this.bounds.x) {
			if(location.z > this.position.z - this.bounds.z && location.z < this.position.z + this.bounds.z) {
				if(location.y > this.position.y - this.bounds.y && location.y < this.position.y + this.bounds.y) {
					return true;
				}
			}
		}
		return false;
	}
	
	@Override
	public boolean containsExec(Vector3f location) {
		if(location.x > this.position.x - this.bounds.x && location.x < this.position.x + this.bounds.x) {
			collision.addCollider(null, new Vector3f(location.x - this.position.x, 0, 0));
			if(location.z > this.position.z - this.bounds.z && location.z < this.position.z + this.bounds.z) {
				collision.addCollider(null, new Vector3f(0, 0, location.z - this.position.z));
				if(location.y > this.position.y - this.bounds.y && location.y < this.position.y + this.bounds.y) {
					collision.addCollider(null, new Vector3f(0, location.y - this.position.y, 0));
					return true;
				}
			}
		}
		return false;
	}
	
	@Override
	public boolean contains(Movable movable) {
		float x = movable.getPosition().x, y = movable.getPosition().y, z = movable.getPosition().z;
		if(x > this.position.x - this.bounds.x && x < this.position.x + this.bounds.x) {
			if(z > this.position.z - this.bounds.z && z < this.position.z + this.bounds.z) {
				if(y > this.position.y - this.bounds.y && y < this.position.y + this.bounds.y) {
					return true;
				}
			}
		}
		return false;
	}
	
	@Override
	public boolean containsExec(Movable movable) {
		float x = movable.getPosition().x, y = movable.getPosition().y, z = movable.getPosition().z;
		Collider collider = movable.getCollider();
		if(x > this.position.x - this.bounds.x && x < this.position.x + this.bounds.x) {
			collision.addCollider(collider, new Vector3f(x - this.position.x, 0, 0));
			if(z > this.position.z - this.bounds.z && z < this.position.z + this.bounds.z) {
				collision.addCollider(collider, new Vector3f(0, 0, z - this.position.z));
				if(y > this.position.y - this.bounds.y && y < this.position.y + this.bounds.y) {
					collision.addCollider(collider, new Vector3f(0, y - this.position.y, 0));
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public void reactToCollision() {
		if(!immobile)
			this.bounded.move(this.collision.getCombinedDirection());
	}
	

	@Override
	public void reactToCollision(Collider other) {
		if(!immobile)
			this.bounded.move(this.collision.getDirection(other));
	}

	@Override
	public AABBCollider clone() {
		// TODO Auto-generated method stub
		return new AABBCollider(this.bounds, this.bounded, this.scale, this.immobile);
	}

}
