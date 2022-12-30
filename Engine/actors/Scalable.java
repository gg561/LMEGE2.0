package actors;

import org.joml.Vector3f;

public interface Scalable {
	
	public Vector3f getScale();
	public void setScale(Vector3f scale);
	
	public void scale(float percentage);

}
