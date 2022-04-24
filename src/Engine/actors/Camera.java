package Engine.actors;

import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class Camera {
	
	private Vector3f position;
	private Vector3f rotation;
	private Matrix4f projection;
	
	public Camera() {
		this.position = new Vector3f();
		this.rotation = new Vector3f();
		this.projection = new Matrix4f();
	}
	
	public Matrix4f getTransformation() {
		Matrix4f returnValue = new Matrix4f();
		returnValue.rotate(-rotation.x, new Vector3f(1, 0 ,0).normalize());
		returnValue.rotate(-rotation.y, new Vector3f(0, 1 ,0).normalize());
		returnValue.rotate(-rotation.z, new Vector3f(0, 0 ,1).normalize());
		returnValue.translate(position.mul(-1, new Vector3f()));
		return returnValue;
	}
	
	public void setOrthographic(float left, float right, float top, float bottom) {
		projection.setOrtho2D(left, right, bottom, top);
	}
	
	public void setPerspective(float fov, float aspectRatio, float zNear, float zFar) {
		projection.perspective(fov, aspectRatio, zNear, zFar);
	}
	
	public Matrix4f getProjectionViewMatrix() {
		Matrix4f returnValue = new Matrix4f();
		projection.mul(getTransformation(), returnValue);
		return returnValue;
	}

	public Vector3f getPosition() {
		return position;
	}

	public void setPosition(Vector3f position) {
		this.position = position;
	}

	public Vector3f getRotation() {
		return rotation;
	}

	public void setRotation(Vector3f rotation) {
		this.rotation = rotation;
	}

	public Matrix4f getProjection() {
		return projection;
	}
	
	public void rotate(Vector3f rotation) {
		setRotation(this.rotation.add(rotation, new Vector3f()));
	}

}
