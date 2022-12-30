package actors;

import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.joml.Vector4f;

import game.Window;

public class Camera extends Movable{
	
	public static final float RENDER_DISTANCE = 450;
	
	public static float aimSensitivity = 30;
	public static float yawLimit = 180;
	public static float pitchLimit = 90;
	
	private Matrix4f projection;
	private Matrix4f orthographic;
	
	public Camera() {
		super();
		this.projection = new Matrix4f();
		this.orthographic = new Matrix4f();
	}
	
	public static Camera getFromWindow(Window window) {
		Camera camera = new Camera();
		camera.setOrthographic(window.getFramebufferWidth(), -window.getFramebufferWidth(), window.getFramebufferHeight(), -window.getFramebufferHeight(), 0.01f, 1000f);
		return camera;
	}
	
	public Matrix4f getTransformation() {
		Matrix4f returnValue = new Matrix4f();
		returnValue.rotateLocal(super.getRotation().x + super.getLocalRotation().x, 1, 0, 0);
		returnValue.rotate(super.getRotation().y + super.getLocalRotation().y, new Vector3f(0, -1 ,0).normalize());
		returnValue.rotate(super.getRotation().z + super.getLocalRotation().z, new Vector3f(0, 0 ,1).normalize());
		returnValue.translate(super.getPosition().negate(new Vector3f()));
		return returnValue;
	}
	
	public void aimNoLim(Window window) {
		Exoskeleton.aim(this, aimSensitivity, window.getId(), null);
	}
	
	public void aim(Window window) {
		Exoskeleton.aim(this, aimSensitivity, window.getId(), new Vector4f((float)Math.toRadians(yawLimit), -(float)Math.toRadians(yawLimit), (float)Math.toRadians(pitchLimit), -(float)Math.toRadians(pitchLimit)));
	}
	
	public void aim(Movable movable, Window window) {
		Exoskeleton.aim(this, aimSensitivity, window.getId(), new Vector4f((float)Math.toRadians(yawLimit + Math.toDegrees(movable.getRotation().y)), -(float)Math.toRadians(yawLimit - Math.toDegrees(movable.getRotation().y)), (float)Math.toRadians(pitchLimit + Math.toDegrees(movable.getRotation().x)), -(float)Math.toRadians(pitchLimit - Math.toDegrees(movable.getRotation().x))));
	}
	
	public void setOrthographic(float left, float right, float top, float bottom, float zNear, float zFar) {
		orthographic.setOrtho(left, right, bottom, top, zNear, zFar);
	}
	
	public void setPerspective(float fov, float aspectRatio, float zNear, float zFar) {
		projection.perspective(fov, aspectRatio, zNear, zFar);
	}

	public Matrix4f getProjection() {
		return projection;
	}
	
	public Matrix4f getOrthographic() {
		return orthographic;
	}

}
