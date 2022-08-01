package actors;

import org.joml.Vector3f;

public class Light {
	
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

}
