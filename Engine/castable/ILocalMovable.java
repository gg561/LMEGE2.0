package castable;

import org.joml.Vector3f;

public interface ILocalMovable extends IMovable {
	
	public Vector3f getLocalPosition();
	public Vector3f getLocalRotation();
	public void setLocalPosition(Vector3f position);
	public void setLocalRotation(Vector3f rotation);

}
