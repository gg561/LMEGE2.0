package castable;

import org.joml.Vector3f;

public interface Directable {
	
	public Vector3f getForward();
	public void setForward(Vector3f forward);
	public Vector3f getRight();
	public void setRight(Vector3f right);
	public Vector3f getUp();
	public void setUp(Vector3f up);
	
	public void resetDirections();
	public void syncDirections();

}
