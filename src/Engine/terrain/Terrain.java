package Engine.terrain;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

import Engine.models.Model;
import Engine.renderer.Loader;
import Engine.textures.Texture;

public class Terrain {
	
	private static final int SIZE = 800;
	private static final int VERTEX_COUNT = 128;
	private static final int TILES = 40;
	
	private float[][]heights = new float[VERTEX_COUNT][VERTEX_COUNT];
	
	private Vector2f position;
	private Model model;
	private Texture texture;
	
	public Terrain(int gridX, int gridZ, Loader loader, Texture texture) {
		this.texture = texture;
		this.position = new Vector2f(gridX * SIZE, gridZ * SIZE);
		this.model = generateTerrain(loader);
	}
	
	public Matrix4f getTransformation() {
		Matrix4f returnValue = new Matrix4f();
		returnValue.translate(new Vector3f(position.x, 0, position.y));
		returnValue.scale(1);
		return returnValue;
	}
	
	public int getTiles() {
		return TILES;
	}
	
	private Model generateTerrain(Loader loader){
		int count = VERTEX_COUNT * VERTEX_COUNT;
		float[] vertices = new float[count * 3];
		float[] normals = new float[count * 3];
		float[] textureCoords = new float[count*2];
		int[] indices = new int[6*(VERTEX_COUNT-1)*(VERTEX_COUNT-1)];
		int vertexPointer = 0;
		for(int i=0;i<VERTEX_COUNT;i++){
			for(int j=0;j<VERTEX_COUNT;j++){
				vertices[vertexPointer*3] = (float)j/((float)VERTEX_COUNT - 1) * SIZE;
				vertices[vertexPointer*3+1] = 0;
				vertices[vertexPointer*3+2] = (float)i/((float)VERTEX_COUNT - 1) * SIZE;
				normals[vertexPointer*3] = 0;
				normals[vertexPointer*3+1] = 1;
				normals[vertexPointer*3+2] = 0;
				textureCoords[vertexPointer*2] = (float)j/((float)VERTEX_COUNT - 1);
				textureCoords[vertexPointer*2+1] = (float)i/((float)VERTEX_COUNT - 1);
				vertexPointer++;
			}
		}
		int pointer = 0;
		for(int gz=0;gz<VERTEX_COUNT-1;gz++){
			for(int gx=0;gx<VERTEX_COUNT-1;gx++){
				int topLeft = (gz*VERTEX_COUNT)+gx;
				int topRight = topLeft + 1;
				int bottomLeft = ((gz+1)*VERTEX_COUNT)+gx;
				int bottomRight = bottomLeft + 1;
				indices[pointer++] = topLeft;
				indices[pointer++] = bottomLeft;
				indices[pointer++] = topRight;
				indices[pointer++] = topRight;
				indices[pointer++] = bottomLeft;
				indices[pointer++] = bottomRight;
			}
		}
		return loader.loadToVAO(vertices, textureCoords, normals, indices);
	}

	public float[][] getHeights() {
		return heights;
	}

	public Vector2f getPosition() {
		return position;
	}

	public Model getModel() {
		return model;
	}

	public Texture getTexture() {
		return texture;
	}

}
