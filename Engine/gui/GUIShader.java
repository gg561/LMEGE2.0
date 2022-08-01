package gui;

import shaders.ShaderProgram;
import shaders.UniformMapped;
import shaders.UniformMatrix;
import shaders.UniformSampler;
import textures.Texture;
import util.CustomFile;

import org.joml.Matrix4f;

import actors.Camera;

public class GUIShader extends ShaderProgram {
	
	private static final CustomFile VERTEX_FILE = new CustomFile("gui", "Vertex.glsl");
	private static final CustomFile FRAGMENT_FILE = new CustomFile("gui", "Fragment.glsl");
	
	protected UniformMapped uniformMap = new UniformMapped();

	public GUIShader() {
		super(VERTEX_FILE, FRAGMENT_FILE, "in_positions");
		super.storeAllUniformToMap(uniformMap);
		super.storeAllUniformLocations(uniformMap.getUniforms().values());
		// TODO Auto-generated constructor stub
	}
	
	public void setCamera(Camera camera) {
		((UniformMatrix) uniformMap.get("orthoMatrix")).loadMatrix(camera.getOrthograpic());
	}
	
	public void loadModel(Matrix4f model) {
		((UniformMatrix)uniformMap.get("modelMatrix")).loadMatrix(model);
	}
	
	public void loadTexture() {
		((UniformSampler)uniformMap.get("sampleTexture")).loadSampler(0);
	}

}
