package collision;

import org.joml.Vector3f;

import actors.Movable;
import game.Main;

public class BoxCollider extends PointBasedCollider {
	// 		0				1				2			3				4				5				6			7
	//leftBottomBack, rightBottomBack, leftUpBack, rightUpBack, leftBottomFront, rightBottomFront, leftUpFront, rightUpFront
	//private Vector3f[] corners = new Vector3f[8];
	//private Vector3f[] localCorners = new Vector3f[8];

	public BoxCollider(Vector3f bounds, Movable bounded, float scale, boolean immobile) {
		super(bounds, bounded, scale, immobile, 8);
		super.complexity = 2;
		// TODO Auto-generated constructor stub
		recalculateCorners();
		Vector3f left = this.getPosition().sub(this.getRight().mul(bounds.x, new Vector3f()), new Vector3f());
		Vector3f right = this.getPosition().add(this.getRight().mul(bounds.x, new Vector3f()), new Vector3f());
		Vector3f down = this.getPosition().sub(this.getUp().mul(bounds.y, new Vector3f()), new Vector3f());
		Vector3f up = this.getPosition().add(this.getUp().mul(bounds.y, new Vector3f()), new Vector3f());
		Vector3f back = this.getPosition().sub(this.getForward().mul(bounds.z, new Vector3f()), new Vector3f());
		Vector3f front = this.getPosition().add(this.getForward().mul(bounds.z, new Vector3f()), new Vector3f());
		
		localCorners[0] = left.add(down, new Vector3f()).add(back, new Vector3f());
		localCorners[1] = right.add(down, new Vector3f()).add(back, new Vector3f());
		localCorners[2] = left.add(up, new Vector3f()).add(back, new Vector3f());
		localCorners[3] = right.add(up, new Vector3f()).add(back, new Vector3f());
		localCorners[4] = left.add(down, new Vector3f()).add(front, new Vector3f());
		localCorners[5] = right.add(down, new Vector3f()).add(front, new Vector3f());
		localCorners[6] = left.add(up, new Vector3f()).add(front, new Vector3f());
		localCorners[7] = right.add(up, new Vector3f()).add(front, new Vector3f());
	}

	@Override
	public Collision detectCollision(Collider other) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void reactToCollision() {
		// TODO Auto-generated method stub
		if(!this.immobile)
			this.bounded.move(this.getCollision().getCombinedDirection());
	}

	@Override
	public void reactToCollision(Collider other) {
		// TODO Auto-generated method stub
		if(!this.immobile)
			this.bounded.move(this.getCollision().getDirection(other));
		/* BOX SHADOWING DEBUG
		System.out.println("BOUNDED " + this + " " + this.bounded);
		System.out.println(this.bounded.getCollider());
		System.out.println(Main.house.getPosition());*/
	}
/*
	@Override
	public boolean contains(Vector3f location) {
		// TODO Auto-generated method stub
		Vector3f dx = this.getRight(), dy = this.getUp(), dz = this.getForward();
		Vector3f d = location.sub(this.getPosition(), new Vector3f());
		if(Math.abs(d.dot(dx)) <= this.bounds.x && Math.abs(d.dot(dy)) <= this.bounds.y && Math.abs(d.dot(dz)) <= this.bounds.z) {
			return true;
		}
		return false;
	}

	@Override
	public boolean containsExec(Vector3f location) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean contains(Movable movable) {
		// TODO Auto-generated method stub
		if(movable.getCollider() instanceof BoxCollider) {
			BoxCollider other = (BoxCollider) movable.getCollider();
			for(int i = 0; i < corners.length; i++) {
				if(other.contains(corners[i]) || contains(other.corners[i])) {
					return true;
				}
			}
		}
		return false;
	}
	
	@Override
	public boolean containsExec(Movable movable) {
		// TODO Auto-generated method stub
		Vector3f collisionDir = new Vector3f();
		boolean collided = false;
		if(movable.getCollider() instanceof BoxCollider) {
			BoxCollider other = (BoxCollider) movable.getCollider();
			for(int i = 0; i < corners.length; i++) {
				if(other.contains(corners[i]) || contains(other.corners[i])) {
					collisionDir.sub(localCorners[i].mul(0.25f, new Vector3f()));
					collided = true;
				}
			}
		}
		collision.addCollider(movable.getCollider(), collisionDir);
		return collided;
	}*/
	
	public void recalculateCorners() {
		Vector3f left = this.getPosition().sub(this.getRight().mul(bounds.x, new Vector3f()), new Vector3f());
		Vector3f right = this.getPosition().add(this.getRight().mul(bounds.x, new Vector3f()), new Vector3f());
		Vector3f down = this.getPosition().sub(this.getUp().mul(bounds.y, new Vector3f()), new Vector3f());
		Vector3f up = this.getPosition().add(this.getUp().mul(bounds.y, new Vector3f()), new Vector3f());
		Vector3f back = this.getPosition().sub(this.getForward().mul(bounds.z, new Vector3f()), new Vector3f());
		Vector3f front = this.getPosition().add(this.getForward().mul(bounds.z, new Vector3f()), new Vector3f());
		
		corners[0] = left.add(down, new Vector3f()).add(back, new Vector3f());
		corners[1] = right.add(down, new Vector3f()).add(back, new Vector3f());
		corners[2] = left.add(up, new Vector3f()).add(back, new Vector3f());
		corners[3] = right.add(up, new Vector3f()).add(back, new Vector3f());
		corners[4] = left.add(down, new Vector3f()).add(front, new Vector3f());
		corners[5] = right.add(down, new Vector3f()).add(front, new Vector3f());
		corners[6] = left.add(up, new Vector3f()).add(front, new Vector3f());
		corners[7] = right.add(up, new Vector3f()).add(front, new Vector3f());
	}
	
	public void updateCorners() {
		for(int i = 0; i < corners.length; i ++) {
			Vector3f direction = localCorners[i].sub(super.getLocalPosition(), new Vector3f());
			direction.rotateX(this.getRotation().x);
			direction.rotateY(this.getRotation().y);
			direction.rotateZ(this.getRotation().z);
			corners[i] = super.getPosition().add(direction, new Vector3f());
		}
	}
	
	public Vector3f[] getCorners() {
		return corners;
	}
	
	public BoxCollider clone() {
		System.out.println(this.bounds + " " + this.size);
		return new BoxCollider(new Vector3f(this.bounds), null, this.size, this.immobile);
	}
	
	public void addCollision(Collider collider, Vector3f direction) {
		if(!this.immobile) {
			this.collision.addCollider(collider, direction);
		}
	}

}
