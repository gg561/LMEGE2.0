package Engine.baseComponents;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

public class Vao {
	
	public static final int BYTES_PER_FLOAT = 4;
	public static final int BYTES_PER_INT = 4;
	
	public static List<Vao> vaos = new ArrayList<Vao>();
	
	private int id;
	private int indexCount;
	private List<Vbo> dataBuffers = new ArrayList<Vbo>();
	private Vbo dataBuffer;
	private Vbo indexBuffer;
	
	public Vao(int indexCount) {
		this.id = GL30.glGenVertexArrays();
		this.indexCount = indexCount;
	}
	
	public Vao(int id, int indexCount) {
		super();
		this.id = id;
		this.indexCount = indexCount;
	}

	public int getId() {
		return id;
	}

	public int getIndexCount() {
		return indexCount;
	}
	
	public static void cleanUp() {
		for(Vao vao : vaos) {
			vao.delete();
		}
	}
	
	public void bindVao() {
		GL30.glBindVertexArray(id);
	}
	
	public void unbindVao() {
		GL30.glBindVertexArray(0);
	}
	
	public void bindAttributes(int... attributes) {
		bindVao();
		for(int i : attributes) {
			GL20.glEnableVertexAttribArray(i);
		}
	}
	
	public void unbindAttributes(int... attributes) {
		for(int i : attributes) {
			GL20.glDisableVertexAttribArray(i);
		}
	}
	
	public void createIndexBuffer(int[] indices) {
		this.indexBuffer = new Vbo(GL15.GL_ELEMENT_ARRAY_BUFFER);
		indexBuffer.bind();
		indexBuffer.storeData(indices);
		this.indexCount = indices.length;
	}
	
	public void createAttribute(int attribute, float[] data, int attrSize) {
		Vbo dataVbo = new Vbo();
		dataVbo.bind();
		dataVbo.storeData(data);
		GL30.glVertexAttribPointer(attribute, attrSize, GL11.GL_FLOAT, false, attrSize * BYTES_PER_FLOAT, 0);
		dataVbo.unbind();
		dataBuffers.add(dataVbo);
	}
	
	public void createIntAttribute(int attribute, float[] data, int attrSize) {
		Vbo dataVbo = new Vbo();
		dataVbo.bind();
		dataVbo.storeData(data);
		GL30.glVertexAttribIPointer(attribute, attrSize, GL11.GL_INT, attrSize * BYTES_PER_INT, 0);
		dataVbo.unbind();
		dataBuffers.add(dataVbo);
	}
	
	/*
	 * 
	 */
	public void storeData(int[] indices, int vertexCount, float[]... data) {
		bindVao();
		storeData(vertexCount, data);
		createIndexBuffer(indices);
		unbindVao();
	}
	
	public void storeData(int vertexCount, float[]... data) {
		float[] interleavedData = interleaveData(vertexCount, data);
		int[] lengths = getAttributeLengths(data, vertexCount);
		storeInterleavedData(interleavedData, lengths);
	}
	
	public void storeInterleavedData(float[] data, int... lengths) {
		dataBuffer = new Vbo();
		dataBuffer.bind();
		dataBuffer.storeData(data);
		int bytesPerVertex = calculateBytesPerVertex(lengths);
		linkVboDataToAttributes(lengths, bytesPerVertex);
		dataBuffer.unbind();
	}
	
	private void linkVboDataToAttributes(int[] lengths, int bytesPerVertex) {
		int total = 0;
		for(int i = 0; i < lengths.length; i ++) {
			GL20.glVertexAttribPointer(i, lengths[i], GL11.GL_FLOAT, false, bytesPerVertex, BYTES_PER_FLOAT * total);
			total += lengths[i];
		}
	}
	
	private int[] getAttributeLengths(float[][] data, int vertexCount) {
		int[] lengths = new int[data.length];
		for(int i = 0; i < data.length; i++) {
			lengths[i] = data[i].length / vertexCount;//gets the amount of data per vertex in every case of data, and stores it in lengths.
		}
		return lengths;
	}
	
	/*
	 * combines all of the lengths of the different types of data into one
	 * length of the interleaved buffer, then assuming each data slot uses a float,
	 * multiply the data slots by the BYTES_PER_FLOAT constant, receiving the final amount of bytes
	 * that is contained within an INTERLEAVED BUFFER, A.K.A a vertex.
	 */
	private int calculateBytesPerVertex(int[] lengths) {
		int total = 0;
		for(int i = 0; i < lengths.length; i ++) {
			total += lengths[i];
		}
		return BYTES_PER_FLOAT * total;
	}
	
	/*
	 * combines the multiple types of data into one singular vbo
	 */
	private float[] interleaveData(int vertexCount, float[]... data) {
		int totalSize = 0;
		int[] lengths = new int[data.length];
		for(int i = 0; i < data.length; i ++) {
			int elementLength = data[i].length / vertexCount;
			lengths[i] = elementLength;
			totalSize += data[i].length;
		}
		float[] interleavedBuffer = new float[totalSize];
		int pointer = 0;
		for(int i = 0; i < vertexCount; i ++) {
			for(int j = 0; j < data.length; j ++) {
				int elementLength = lengths[j];
				for(int k = 0; k < elementLength; k ++) {
					interleavedBuffer[pointer++] = data[j][i * elementLength + k];
				}
			}
		}
		return interleavedBuffer;
	}
	
	public void delete() {
		GL30.glDeleteVertexArrays(id);
		for(Vbo vbo : dataBuffers) {
			vbo.delete();
		}
		indexBuffer.delete();
	}

}
