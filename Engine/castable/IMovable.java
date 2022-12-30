package castable;

import org.joml.Vector3f;

import actors.Scalable;

public interface IMovable extends Scalable, Syncable {
	
	public void move(Vector3f position);
	public void move0(Vector3f position);
	public void rotate(Vector3f rotation);
	public void rotate0(Vector3f rotation);
	public void setPosition(Vector3f position);
	public void setPosition0(Vector3f position);
	public void setRotation(Vector3f rotation);
	public void setRotation0(Vector3f rotation);

}
