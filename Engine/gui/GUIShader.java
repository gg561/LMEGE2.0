package gui;

import shaders.ShaderProgram;
import shaders.ObjectMap;
import shaders.UniformMatrix;
import shaders.UniformSampler;
import textures.Texture;
import util.CustomFile;

import org.joml.Matrix4f;

import actors.Camera;

public class GUIShader extends ShaderProgram {
	
	private static final CustomFile VERTEX_FILE = new CustomFile("gui", "Vertex.glsl");
	private static final CustomFile FRAGMENT_FILE = new CustomFile("gui", "Fragment.glsl");

	public GUIShader() {
		super(VERTEX_FILE, FRAGMENT_FILE, "in_positions");
		super.storeAllUniformToMap();
		super.storeAllUniformLocations(p().shaders());
		// TODO Auto-generated constructor stub
	}
	
	public void setOrthographic(Matrix4f ortho) {
		p().v.matrix("orthoMatrix").loadMatrix(ortho);
	}
	
	public void loadModel(Matrix4f model) {
		p().v.matrix("modelMatrix").loadMatrix(model);
	}
	
	public void loadTexture() {
		p().f.sampler("sampleTexture").loadSampler(0);
	}

}
