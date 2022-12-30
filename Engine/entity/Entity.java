package entity;

import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import actors.Actor;
import actors.Movable;
import baseComponents.Vao;
import collision.BoxCollider;
import collision.Collider;
import game.Main;
import models.Model;
import models.TexturedModel;
import scene.Renderable;

public class Entity extends Movable implements Actor {
	
	private TexturedModel model;
	
	public Entity(TexturedModel model) {
		super();
		this.model = model;
	}
	
	public Matrix4f getTransformation() {
		Matrix4f returnValue = new Matrix4f();
		returnValue.translate(super.getPosition());
		//super.getRotation().mul(Main.getDelta());
		returnValue.rotate(super.getRotation().x, new Vector3f(1, 0 ,0).normalize());
		returnValue.rotate(super.getRotation().y, new Vector3f(0, 1 ,0).normalize());
		returnValue.rotate(super.getRotation().z, new Vector3f(0, 0 ,1).normalize());
		returnValue.scale(super.getScale());
		return returnValue;
	}
	
	public TexturedModel getTexturedModel() {
		return model;
	}

	@Override
	public Model getModel() {
		// TODO Auto-generated method stub
		return model.getModel();
	}

	@Override
	public Vao getVao() {
		// TODO Auto-generated method stub
		return getModel().getVao();
	}

}
