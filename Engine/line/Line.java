package line;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import actors.Movable;
import models.Model;
import renderer.Loader;
import scene.Renderable;

public class Line extends Movable implements Renderable {
	
	private Vector3f begin;
	private Vector3f end;
	private Vector3f color; 
	private Model model;
	
	public Line(Loader loader, Vector3f begin, Vector3f end, Vector3f color) {
		this.begin = begin;
		this.end = end;
		this.color = color;
		model = loader.loadToVAO(new float[] {begin.x, begin.y, begin.z, end.x, end.y, end.z}, 3);
	}
	
	public void setBegin(Vector3f begin) {
		this.begin = begin;
	}
	
	public void setEnd(Vector3f end) {
		this.end = end;
	}
	
	public void resetModel(Loader loader) {
		model.delete();
		model = loader.loadToVAO(new float[] {begin.x, begin.y, begin.z, end.x, end.y, end.z}, 3);
	}
	
	public Vector3f getBegin() {
		return begin;
	}
	
	public Vector3f getEnd() {
		return end;
	}
	
	public Matrix4f getTransformation() {
		Matrix4f transformation = new Matrix4f();
		transformation.translate(this.getPosition());
		transformation.rotateXYZ(this.getRotation());
		transformation.scale(this.getScale());
		return transformation;
	}
	
	public Vector3f getColor() {
		return color;
	}
	
	public Model getModel() {
		return model;
	}

}
