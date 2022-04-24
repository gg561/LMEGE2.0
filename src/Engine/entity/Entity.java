package Engine.entity;

import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import Engine.models.Model;
import Engine.models.TexturedModel;

public class Entity {
	
	private Vector3f position;
	private Vector3f rotation;
	private Vector3f scale;
	
	private TexturedModel model;
	
	public Entity(TexturedModel model) {
		this.model = model;
		this.position = new Vector3f();
		this.rotation = new Vector3f();
		this.scale = new Vector3f();
	}
	
	public Matrix4f getTransformation() {
		Matrix4f returnValue = new Matrix4f();
		returnValue.translate(position);
		returnValue.rotate(-rotation.x, new Vector3f(1, 0 ,0).normalize());
		returnValue.rotate(-rotation.y, new Vector3f(0, 1 ,0).normalize());
		returnValue.rotate(-rotation.z, new Vector3f(0, 0 ,1).normalize());
		returnValue.scale(scale);
		return returnValue;
	}
	
	public TexturedModel getModel() {
		return model;
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

	public Vector3f getScale() {
		return scale;
	}

	public void setScale(Vector3f scale) {
		this.scale = scale;
	}

}
