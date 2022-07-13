package actors;

import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.joml.Vector4f;

public class Camera extends Movable{
	
	public static float aimSensitivity = 30;
	public static float yawLimit = 180;
	public static float pitchLimit = 60;
	
	private Matrix4f projection;
	
	public Camera() {
		super();
		this.projection = new Matrix4f();
	}
	
	public Matrix4f getTransformation() {
		Matrix4f returnValue = new Matrix4f();
		returnValue.rotateLocal(super.getRotation().x, 1, 0, 0);
		returnValue.rotate(super.getRotation().y, new Vector3f(0, -1 ,0).normalize());
		returnValue.rotate(super.getRotation().z, new Vector3f(0, 0 ,1).normalize());
		returnValue.translate(super.getPosition().mul(-1, new Vector3f()));
		return returnValue;
	}
	
	public void aim(long window) {
		Exoskeleton.aim(this, aimSensitivity, window, new Vector4f((float)Math.toRadians(yawLimit), -(float)Math.toRadians(yawLimit), (float)Math.toRadians(pitchLimit), -(float)Math.toRadians(pitchLimit)));
	}
	
	public void aim(Movable movable, long window) {
		Exoskeleton.aim(this, aimSensitivity, window, new Vector4f((float)Math.toRadians(yawLimit + Math.toDegrees(movable.getRotation().y)), -(float)Math.toRadians(yawLimit - Math.toDegrees(movable.getRotation().y)), (float)Math.toRadians(pitchLimit + Math.toDegrees(movable.getRotation().x)), -(float)Math.toRadians(pitchLimit - Math.toDegrees(movable.getRotation().x))));
		System.out.println(movable.getRotation().y);
	}
	
	public void setOrthographic(float left, float right, float top, float bottom) {
		projection.setOrtho2D(left, right, bottom, top);
	}
	
	public void setPerspective(float fov, float aspectRatio, float zNear, float zFar) {
		projection.perspective(fov, aspectRatio, zNear, zFar);
	}

	public Matrix4f getProjection() {
		return projection;
	}

}
