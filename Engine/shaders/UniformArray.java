package shaders;

import java.lang.reflect.Array;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL20;

public class UniformArray extends Uniform {
	
	private int size = 0;
	private int[] locations;

	public UniformArray(String name, int size) {
		super(name);
		this.size = size;
		locations = new int[size];
		// TODO Auto-generated constructor stub
	}
	
	public boolean storeUniformLocation(int programID) {
		for(int i = 0; i < size; i ++) {
			int location = GL20.glGetUniformLocation(programID, getName().split("\\[")[0] + "[" + i + "]");
			if(location == NOT_FOUND) {
				System.err.println("No uniform variable called " + getName().split("\\[")[0] + "[" + i + "]" + " found!");
				return false;
			}
			locations[i] = location;
		}
		return true;
	}
	
	public int getLocation() {
		return locations[0];
	}
	
	public int[] getLocations() {
		return locations;
	}
	
	public int getSize() {
		return size;
	}
	
	public void loadFloatArray(float[] floats) {
		for(int i = 0; i< floats.length; i ++) {
			GL20.glUniform1f(locations[i], floats[i]);
		}
	}
	
	public void loadVectorArray(Vector2f[] vec2) {
		for(int i = 0; i < vec2.length; i++) {
			int location = locations[i];
			GL20.glUniform2f(location, vec2[i].x, vec2[i].y);
		}
	}
	
	public void loadVectorArray(Vector3f[] vec3) {
		for(int i = 0; i < vec3.length; i++) {
			int location = locations[i];
			GL20.glUniform3f(location, vec3[i].x, vec3[i].y, vec3[i].z);
		}
	}
	
	public void loadVectorArray(Vector4f[] vec4) {
		for(int i = 0; i < vec4.length; i++) {
			int location = locations[i];
			GL20.glUniform4f(location, vec4[i].x, vec4[i].y, vec4[i].z, vec4[i].w);
		}
	}

}
