package actors;

import org.joml.Matrix4f;

import baseComponents.Vao;
import models.Model;
import models.TexturedModel;
import scene.Renderable;

public interface Actor extends Renderable {

	public abstract Matrix4f getTransformation();
	public abstract TexturedModel getTexturedModel();
	public abstract Model getModel();
	public abstract Vao getVao();

}
