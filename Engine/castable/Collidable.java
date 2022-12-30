package castable;

import collision.Collider;

public interface Collidable {
	
	public Collider getCollider();
	public void setCollider(Collider collider);

}
