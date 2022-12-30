package actors.lights;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

import scene.Renderable;
import util.Vectors;

public class Light implements Renderable {
	
	private Vector3f color;
	private Vector3f position;
	private Vector3f attenuation;
	
	public Light() {
		color = new Vector3f();
		position = new Vector3f();
		attenuation = new Vector3f(1, 0, 0);
	}
	
	public Light(Vector3f color, Vector3f position) {
		this.color = color;
		this.position = position;
		attenuation = new Vector3f(1, 0, 0);
	}
	/*
	 * effectiveness questionned
	 */
	public Light(Vector3f color, Vector3f position, float strength) {
		this(color, position);
		attenuation = new Vector3f(1, strength, strength);
	}
	
	public Light(Vector3f color, Vector3f position, Vector3f attenuation) {
		this(color, position);
		this.attenuation = attenuation;
	}

	public Vector3f getColor() {
		return color;
	}

	public void setColor(Vector3f color) {
		this.color = color;
	}

	public Vector3f getPosition() {
		return position;
	}

	public void setPosition(Vector3f position) {
		this.position = position;
	}

	public Vector3f getAttenuation() {
		return attenuation;
	}

	public void setAttenuation(float strength) {
		this.attenuation = new Vector3f(1, strength, strength);
	}
	
	public void setAttenuation(Vector3f attenuation) {
		this.attenuation = attenuation;
	}
	
	public Vector3f getDirectionToZero() {
		return this.getPosition().sub(Vectors.ZERO_3D, new Vector3f());
	}
	
	public Matrix4f getTransformation() {
		Matrix4f view = new Matrix4f();
		Vector3f lightDirection = this.getPosition().normalize(new Vector3f()).negate();
		//lightDirection.z = -lightDirection.z;
		//view.rotateXYZ(light.getDirectionToZero());
		//view.translate(new Vector3f(0, 30, 0));
		//view.rotateXYZ(cam.getRotation());
		float pitch = (float) Math.acos(new Vector2f(lightDirection.x, lightDirection.z).length());
		view.rotateX(pitch);
		float yaw = (float) Math.toDegrees(Math.atan(lightDirection.x / lightDirection.z));
		yaw = lightDirection.z > 0 ? yaw - 180 : yaw;
		view.rotateY(-yaw);
		return view;
	}

}
