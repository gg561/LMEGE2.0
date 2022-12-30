package font;

import java.util.Arrays;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

import actors.Movable;
import baseComponents.Shape;
import models.TexturedModel;
import renderer.Loader;
import textures.Texture;
import util.Vectors;

public class Caractere extends Movable {
	
	private TexturedModel model;
	private Glyph g;
	private Vector3f color;
	
	public Caractere(Loader loader, char c, GameFont font, Vector3f color) {
		this(loader, Vectors.ZERO_2D, Vectors.ZERO_2D, c, font, color);
	}
	
	public Caractere(Loader loader, Vector2f position, Vector2f size, char c, GameFont font, Vector3f color) {
		this.g =  font.getCharacterInfo().get((int) c);
		model = new TexturedModel(loader.loadToVAO(2, g.getVertices(), g.getTextureCoords()), font.getTexture());
		this.setScale(new Vector3f(size, 0));
		this.setPosition(new Vector3f(position, 0));
		this.color = color;
	}
	
	public Glyph getGlyph() {
		return g;
	}
	
	public TexturedModel getModel() {
		return model;
	}
	
	public Matrix4f getTransformation() {
		Matrix4f transformation = new Matrix4f();
		transformation.translate(this.getPosition());
		transformation.rotateXYZ(this.getRotation());
		transformation.scale(this.getScale());
		return transformation;
	}
	/*
	private float[] getVertices(char c) {
		Glyph g = font.getCharacterInfo().get((int) c);
		float[] nVerts = new float[4 * 2];
		Vector2f[] texCoords = g.getTextureCoords();
		for(int i = 0; i < texCoords.length; i ++) {
			float x = g.getWidth() / 2 * Shape.PLANE_2D_STRIPS[i * 2];
			float y = g.getHeight() / 2 * Shape.PLANE_2D_STRIPS[i * 2 + 1];
			float invertSqrt = (float) (1 / Math.sqrt(x * x + y * y));
			nVerts[i * 2] = x * invertSqrt;
			nVerts[i * 2 + 1] = y * invertSqrt;
		}
		System.err.println(Arrays.toString(nVerts));
		return nVerts;
	}*/

	public Vector3f getColor() {
		return color;
	}
	
	public void setColor(Vector3f color) {
		this.color = color;
	}

}
