package shaders;

import java.nio.FloatBuffer;

import org.joml.Matrix4f;
import org.lwjgl.opengl.GL20;
import org.lwjglx.BufferUtils;

public class UniformMatrix extends Uniform {
	
	private static FloatBuffer matrixBuffer = BufferUtils.createFloatBuffer(16);

	public UniformMatrix(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}
	
	public void loadMatrix(Matrix4f matrix) {
		float[] matrixData = new float[16];
		matrix.get(matrixData);
		GL20.glUniformMatrix4fv(super.getLocation(), false, matrixData);
	}

}
