package baseComponents;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL31;
import org.lwjgl.opengl.GL33;
import org.lwjgl.opengl.GL40;

public class Vao {
	
	public static final int BYTES_PER_FLOAT = 4;
	public static final int BYTES_PER_INT = 4;
	
	public static List<Vao> vaos = new ArrayList<Vao>();
	
	private int id;
	private int indexCount;
	private List<Vbo> dataBuffers = new ArrayList<Vbo>();
	private Vbo dataBuffer;
	private Vbo indexBuffer;
	private int lastAttributeBound;
	
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
	
	public void bindAttributesStream(int begin, int end) {
		for(int beg = begin; beg <= end; beg++) {
			bindAttributes(beg);
		}
	}
	
	public void unbindAttributes(int... attributes) {
		for(int i : attributes) {
			GL20.glDisableVertexAttribArray(i);
		}
	}
	
	public void unbindAttributesStream(int begin, int end) {
		for(int beg = begin; beg <= end; beg++) {
			unbindAttributes(beg);
		}
	}
	
	public void createIndexBuffer(int[] indices) {
		this.indexBuffer = new Vbo(GL15.GL_ELEMENT_ARRAY_BUFFER);
		indexBuffer.bind();
		indexBuffer.storeData(indices);
		this.indexCount = indices.length;
	}
	
	/**
	 * 
	 * @param attribute : The attribute ID
	 * @param data : The data array to be stored
	 * @param attrSize : Amount of data per vertex
	 * 
	 * Creates an extra vbo inside the vao. The new vbo will contain GL_FLOAT values.
	 */
	public void createAttribute(int attribute, float[] data, int attrSize) {
		Vbo dataVbo = new Vbo();
		dataVbo.bind();
		dataVbo.storeData(data);
		//GL20.glVertexAttribPointer(attribute, attrSize, GL11.GL_FLOAT, false, attrSize * BYTES_PER_FLOAT, 0);
		bindAttribute(attribute, attrSize, GL11.GL_FLOAT, false, attrSize * BYTES_PER_FLOAT, 0);
		dataVbo.unbind();
		dataBuffers.add(dataVbo);
	}

	
	/**
	 * 
	 * @param attribute : The attribute ID
	 * @param data : The data array to be stored
	 * @param attrSize : Amount of data per vertex
	 * @param divisor : How many instances to advance after one query
	 * 
	 * Creates an extra vbo inside the vao. The vbo will be instance divided and will contain GL_FLOAT values.
	 */
	public void createInstancedAttribute(int attribute, float[] data, int attrSize, int divisor) {
		Vbo dataVbo = new Vbo();
		dataVbo.bind();
		dataVbo.storeData(data);
		//GL20.glVertexAttribPointer(attribute, attrSize, GL11.GL_FLOAT, false, attrSize * BYTES_PER_FLOAT, 0);
		bindAttribute(attribute, attrSize, GL11.GL_FLOAT, false, attrSize * BYTES_PER_FLOAT, 0);
		GL33.glVertexAttribDivisor(attribute, divisor);
		dataVbo.unbind();
		dataBuffers.add(dataVbo);
	}
	
	/**
	 * 
	 * BIND VAO BEFORE USE!
	 * 
	 * @param attribute
	 * @param attrSize
	 */
	public void createAttribute(Vbo vbo, int attribute, int attrSize, int stride, int offset) {
		vbo.bind();
		//GL20.glVertexAttribPointer(attribute, attrSize, GL11.GL_FLOAT, false, stride * BYTES_PER_FLOAT, offset * BYTES_PER_FLOAT);
		bindAttribute(attribute, attrSize, GL11.GL_FLOAT, false, stride * BYTES_PER_FLOAT, offset * BYTES_PER_FLOAT);
		vbo.unbind();
	}
	
	public void createInstanceAttribute(Vbo vbo, int attribute, int divisor, int attrSize, int stride, int offset) {
		vbo.bind();
		//GL20.glVertexAttribPointer(attribute, attrSize, GL11.GL_FLOAT, false, stride * BYTES_PER_FLOAT, offset * BYTES_PER_FLOAT);
		bindAttribute(attribute, attrSize, GL11.GL_FLOAT, false, stride * BYTES_PER_FLOAT, offset * BYTES_PER_FLOAT);
		GL33.glVertexAttribDivisor(attribute, divisor);
		vbo.unbind();
	}
	
	/**
	 * 
	 * @param attribute : The attribute ID
	 * @param data : The data array to be stored
	 * @param attrSize : Amount of data per vertex
	 * 
	 * Creates an extra vbo inside the vao. The new vbo will contain GL_INT values.
	 */
	public void createIntAttribute(int attribute, float[] data, int attrSize) {
		Vbo dataVbo = new Vbo();
		dataVbo.bind();
		dataVbo.storeData(data);
		//GL30.glVertexAttribIPointer(attribute, attrSize, GL11.GL_INT, attrSize * BYTES_PER_INT, 0);
		bindAttribute(attribute, attrSize, GL11.GL_INT, attrSize * BYTES_PER_INT, 0);
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
	
	public Vbo getVbo() {
		return dataBuffer;
	}
	
	private void linkVboDataToAttributes(int[] lengths, int bytesPerVertex) {
		linkVboDataToAttributes(lengths, 0, bytesPerVertex);
	}
	
	private void linkVboDataToAttributes(int[] lengths, int basis, int bytesPerVertex) {
		int total = 0;
		for(int i = 0; i < lengths.length; i ++) {
			//GL20.glVertexAttribPointer(i + basis, lengths[i], GL11.GL_FLOAT, false, bytesPerVertex, BYTES_PER_FLOAT * total);
			bindAttribute(i + basis, lengths[i], GL11.GL_FLOAT, false, bytesPerVertex, BYTES_PER_FLOAT * total);
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
		if(indexBuffer != null)
			indexBuffer.delete();
	}
	
	public List<Vbo> getDataBuffers(){
		return dataBuffers;
	}
	
	/**
	 * 
	 * FOR INSTANCING ONLY
	 * 
	 * W. I. P.
	 */
	public void minimizeCurrentVBOs(int lengthsCount, int attributesBefore) {
		Vbo minimized = new Vbo();
		minimized.bind();
		minimized.initiateDataBuffer(0);
		int[] lengths = new int[dataBuffers.size()];//this lengths is the inverse of the others
		/*
		 * e.g.
		 * 
		 * other lengths :
		 * [attributes][attrSize]
		 * 
		 * this lengths :
		 * [attributes][instances]
		 */
		List<Vbo> toRemove = new ArrayList<Vbo>();
		float[][] datas = new float[dataBuffers.size()][];
		for(int i = 0; i < dataBuffers.size(); i++){
			FloatBuffer data = dataBuffers.get(i).getData();
			float[] fdata = new float[data.capacity()];
			data.get(fdata);
			datas[i] = fdata;
			lengths[i] = data.capacity() / lengthsCount;
			dataBuffers.get(i).delete();
			toRemove.add(dataBuffers.get(i));
		}
		float[] interleavedData = interleaveData(lengthsCount, datas);
		//minimized.initiateDataBuffer(interleavedData.length);
		minimized.add(interleavedData);
		dataBuffers.removeAll(toRemove);
		int bytesPerVertex = calculateBytesPerVertex(lengths);
		linkVboDataToAttributes(lengths, attributesBefore, bytesPerVertex);
		System.out.println("BPV " + bytesPerVertex);
		System.out.println("LES " + Arrays.toString(lengths));
		minimized.unbind();
		dataBuffers.add(minimized);
	}
	
	public void addDataBuffer(Vbo dataBuffer) {
		this.dataBuffers.add(dataBuffer);
	}
	
	private void bindAttribute(int attribute, int attrSize, int type, int strideBytes, int offsetBytes) {
		bindAttribute(attribute, attrSize, type, false, strideBytes, offsetBytes);
	}
	
	private void bindAttribute(int attribute, int attrSize, int type, boolean normalize, int strideBytes, int offsetBytes) {
		if(type == GL11.GL_INT) {
			GL30.glVertexAttribIPointer(attribute, attrSize, type, strideBytes, offsetBytes);
		}
		GL20.glVertexAttribPointer(attribute, attrSize, type, normalize, strideBytes, offsetBytes);
		lastAttributeBound = attribute;
	}
	
	public int getLastAttributeBound() {
		return lastAttributeBound;
	}

}
