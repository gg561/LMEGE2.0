package baseComponents;

import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.List;

import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL30;
import org.lwjglx.BufferUtils;

public class Vbo {
	
	static final int JNI_COPY_FROM_ARRAY_THRESHOLD = 6;
	
	private final int id;
	private final int type;
	private FloatBuffer data;
	
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
		_storeData(buffer);
	}
	
	public void storeData(float[] data) {
		FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
		buffer.put(data);
		this.data = buffer;
		buffer.flip();
		_storeData(buffer);
	}
	
	public void storeData(FloatBuffer buffer) {
		this.data = buffer;
		buffer.flip();
		_storeData(buffer);
	}
	
	/*
	 * Flip buffer before inserting
	 */
	private void _storeData(IntBuffer buffer) {
		GL15.glBufferData(type, buffer, GL15.GL_STATIC_DRAW);
	}
	
	/*
	 * Flip buffer before inserting
	 */
	private void _storeData(FloatBuffer buffer) {
		GL15.glBufferData(type, buffer, GL15.GL_STATIC_DRAW);
	}
	
	public void delete() {
		GL30.glDeleteBuffers(id);
	}
	
	public FloatBuffer getData() {
		return data;
	}
	
	public void initiateDataBuffer(int capacity) {
		this.data = BufferUtils.createFloatBuffer(capacity);
	}
	
	public void add(float[] data) {
		FloatBuffer newHost = BufferUtils.createFloatBuffer(data.length + this.data.capacity());
		newHost.put(this.data);
		newHost.put(data);
		this.data = newHost;
		newHost.flip();
		_storeData(newHost);
	}
	
	public void add(FloatBuffer buffer) {
		FloatBuffer newHost = BufferUtils.createFloatBuffer(buffer.capacity() + data.capacity());
		newHost.put(data);
		newHost.put(buffer);
		this.data = newHost;
		newHost.flip();
		buffer.clear();
		_storeData(newHost);
	}
	
	public void update(float[] data) {
		this.data.clear();
		this.data = BufferUtils.createFloatBuffer(data.length);
		this.data.put(data);
		this.data.flip();
		bind();
		GL15.glBufferData(type, this.data.capacity() * 4, GL15.GL_STREAM_DRAW);
		GL15.glBufferSubData(type, 0, this.data);
		unbind();
	}
	
	public void update(List<Float> data) {
		this.data.clear();
		this.data = BufferUtils.createFloatBuffer(data.size());
		for(float f : data) {
			this.data.put(f);
		}
		this.data.flip();
		bind();
		GL15.glBufferData(type, this.data.capacity() * 4, GL15.GL_STREAM_DRAW);
		GL15.glBufferSubData(type, 0, this.data);
		unbind();
	}

}
