package collision;

import org.joml.Vector3f;

import actors.Movable;

public class CompoundCollider extends Collider{
	
	public CompoundCollider(Vector3f bounds, Movable bounded, float scale, boolean immobile) {
		super(bounds, bounded, scale, immobile);
		// TODO Auto-generated constructor stub
	}

	private Collider[] colliders;
	
	public void move(Vector3f translation) {
		
	}

	@Override
	public Collision detectCollision(Collider other) {
		// TODO Auto-generated method stub
		return null;
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
		// TODO Auto-generated method stub
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
		return false;
	}

	@Override
	public boolean containsExec(Movable movable) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Collider clone() {
		// TODO Auto-generated method stub
		return null;
	}

}
