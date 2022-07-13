package entity;

import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import actors.Movable;
import models.Model;
import models.TexturedModel;

public class Entity extends Movable{
	
	private TexturedModel model;
	
	public Entity(TexturedModel model) {
		super();
		this.model = model;
	}
	
	public Matrix4f getTransformation() {
		Matrix4f returnValue = new Matrix4f();
		returnValue.translate(super.getPosition());
		returnValue.rotate(super.getRotation().x, new Vector3f(1, 0 ,0).normalize());
		returnValue.rotate(super.getRotation().y, new Vector3f(0, 1 ,0).normalize());
		returnValue.rotate(super.getRotation().z, new Vector3f(0, 0 ,1).normalize());
		returnValue.scale(super.getScale());
		return returnValue;
	}
	
	public TexturedModel getModel() {
		return model;
	}

}
