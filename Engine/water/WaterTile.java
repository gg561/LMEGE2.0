package water;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

import baseComponents.Shape;
import entity.Entity;
import models.Model;
import models.TexturedModel;
import renderer.Loader;
import textures.Texture;

public class WaterTile {
	
	private Model model; public Model model() {return model;}
	
	private float x,y,z; public float x() {return x;} public float y(){return y;} public float z() {return z;}
	private Vector2f size; public Vector2f size() {return size;}

	public WaterTile(Loader loader, float x, float y, float z, Vector2f size) {
		model = loader.loadToVAO(Shape.PLANE_2D_ARRAYS, 2);
		this.x = x;
		this.y = y;
		this.z = z;
		this.size = size;
		// TODO Auto-generated constructor stub
	}
	
	public Matrix4f getTransformation() {
		Matrix4f rv = new Matrix4f();
		rv.translate(new Vector3f(x, y, z));
		rv.scale(size.x, 0, size.y);
		return rv;
	}

}
