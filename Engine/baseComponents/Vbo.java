package baseComponents;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL30;
import org.lwjglx.BufferUtils;

public class Vbo {
	
	private final int id;
	private final int type;
	
	public Vbo() {
		id = GL15.glGenBuffers();
		type = GL15.GL_ARRAY_BUFFER;
	}
	
	public Vbo(int type) {
		id = GL15.glGenBuffers();
		this.type = type;
	}
	
	public void bind() {
		GL15.glBindBuffer(type, id);
	}
	
	public void unbind() {
		GL15.glBindBuffer(type, 0);
	}
	
	public void storeData(int[] data) {
		IntBuffer buffer = BufferUtils.createIntBuffer(data.length);
		buffer.put(data);
		buffer.flip();
		storeData(buffer);
	}
	
	public void storeData(float[] data) {
		FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
		buffer.put(data);
		buffer.flip();
		storeData(buffer);
	}
	
	/*
	 * Flip buffer before inserting
	 */
	public void storeData(IntBuffer buffer) {
		GL15.glBufferData(type, buffer, GL15.GL_STATIC_DRAW);
	}
	
	/*
	 * Flip buffer before inserting
	 */
	public void storeData(FloatBuffer buffer) {
		GL15.glBufferData(type, buffer, GL15.GL_STATIC_DRAW);
	}
	
	public void delete() {
		GL30.glDeleteBuffers(id);
	}

}
