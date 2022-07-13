package actors;

import org.joml.Vector3f;

public class Light {
	
	private Vector3f color;
	private Vector3f position;
	private float strength;
	
	public Light() {
		color = new Vector3f();
		position = new Vector3f();
		strength = 0;
	}
	
	public Light(Vector3f color, Vector3f position) {
		this.color = color;
		this.position = position;
	}
	/*
	 * effectiveness questionned
	 */
	public Light(Vector3f color, Vector3f position, float strength) {
		this(color, position);
		this.strength = strength;
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

	public float getStrength() {
		return strength;
	}

	public void setStrength(float strength) {
		this.strength = strength;
	}

}
