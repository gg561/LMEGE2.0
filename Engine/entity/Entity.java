package entity;

import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import actors.Movable;
import collision.BoxCollider;
import collision.Collider;
import models.Model;
import models.TexturedModel;

public class Entity extends Movable{
	
	private TexturedModel model;
	
	public Entity(TexturedModel model) {
		super();
		this.model = model;
	}
	
	public Entity(Entity model) {
		super();
		this.model = model.getModel();
		this.setLocalPosition(new Vector3f(model.getLocalPosition()));
		this.setPosition(new Vector3f(model.getPosition()));
		this.setRotationWithDirections(new Vector3f(model.getRotation()));
		this.setLocalRotation(new Vector3f(model.getLocalRotation()));
		this.setScale(new Vector3f(model.getScale()));
		Collider collider = new BoxCollider(model.getCollider().getBounds(), model.getCollider().getBounded(), model.getCollider().getScale().x, model.getCollider().isImmobile());
		collider.setBounded(this);
		((BoxCollider) collider).resetAxis();
		this.setCollider(collider);
		this.setHasGravity(model.isHasGravity());
		this.setMass(model.getMass());
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
