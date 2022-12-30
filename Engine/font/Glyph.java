package font;

import java.util.Arrays;

import org.joml.Vector2f;

import baseComponents.Shape;

public class Glyph {
	
	private int x;
	private int y;
	private int width;
	private int height;
	
	//private Vector2f[] textureCoords = new Vector2f[4];
	private float[] textureCoords = new float[4 * 2];
	private float[] vertices;

	public Glyph(int x, int y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}
	
	public float[] getTextureCoords() {
		return textureCoords;
	}
	
	public float[] getVertices() {
		return vertices;
	}
	
	public void calculateTextureCoords(int width, int height) {
		float x0 = x / (float) width;
		float x1 = (x + this.width) / (float) width;
		float y0 = y / (float) height;
		float y1 = (y - this.height) / (float) height;
		Vector2f[] texCoords = new Vector2f[] {
				new Vector2f(x0, y1),//10
				new Vector2f(x0, y0),//11
				new Vector2f(x1, y1),//00
				new Vector2f(x1, y0)//10
		};
		for(int i = 0; i < texCoords.length; i++) {
			textureCoords[i * 2] = texCoords[i].x;
			textureCoords[i * 2 + 1] = texCoords[i].y;
		}
	}
	
	public float[] calculateVertices() {
		float[] nVerts = new float[4 * 2];
		for(int i = 0; i < nVerts.length / 2; i ++) {
			float x = getWidth() / 2 * Shape.PLANE_2D_STRIPS[i * 2];
			float y = getHeight() / 2 * Shape.PLANE_2D_STRIPS[i * 2 + 1];
			float invertSqrt = (float) (1 / Math.sqrt(x * x + y * y));
			nVerts[i * 2] = x * invertSqrt;
			nVerts[i * 2 + 1] = y * invertSqrt;
		}
		vertices = nVerts;
		return nVerts;
	}

}
