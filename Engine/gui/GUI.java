package gui;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

import actors.Movable;
import baseComponents.Shape;
import models.TexturedModel;
import renderer.Loader;
import scene.Renderable;
import textures.Texture;

public class GUI extends Movable implements Renderable {
	
	private TexturedModel model;
	
	public GUI(Loader loader, Vector2f position, Vector2f size, Texture texture) {
		super();
		this.model = new TexturedModel(loader.loadToVAO(Shape.PLANE_2D_STRIPS, 2), texture);
		super.setPosition(new Vector3f(position, 0.1f));
		super.setScale(new Vector3f(size, 0f));
	}
	
	public Matrix4f getTransformation() {
		Matrix4f returnValue = new Matrix4f();
		returnValue.translate(super.getPosition());
		returnValue.rotate(0, new Vector3f(1, 0 ,0).normalize());
		returnValue.rotate(0, new Vector3f(0, 1 ,0).normalize());
		returnValue.rotate(super.getRotation().z, new Vector3f(0, 0 ,1).normalize());
		returnValue.scale(super.getScale());
		return returnValue;
	}
	
	public TexturedModel getModel() {
		return model;
	}
	
	public void shouldRender(boolean shouldRender) {
		this.model.getModel().shouldRender(shouldRender);
	}

}
