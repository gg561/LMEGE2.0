package shadows;

import java.io.BufferedReader;
import java.io.IOException;

import org.joml.Matrix4f;
import org.lwjgl.opengl.GL30;

import shaders.ShaderProgram;
import shaders.UniformMatrix;
import shaders.UniformSampler;
import util.CustomFile;

public class ShadowShader extends ShaderProgram {
	
	private static final CustomFile VERTEX_SHADER = new CustomFile("shadows", "Vertex.glsl");
	private static final CustomFile FRAGMENT_SHADER = new CustomFile("shadows", "Fragment.glsl");
	
	protected UniformMatrix mvp = new UniformMatrix("mvp");
	protected UniformSampler depthMap = new UniformSampler("depthMap");

	public ShadowShader() {
		// TODO Auto-generated constructor stub
		super(VERTEX_SHADER, FRAGMENT_SHADER, "in_position");
		super.storeAllUniformLocations(mvp);
	}
	
	public void setMvp(Matrix4f mvp) {
		this.mvp.loadMatrix(mvp);
	}
	
	public void setDepthMap() {
		depthMap.loadSampler(0);
	}

}
