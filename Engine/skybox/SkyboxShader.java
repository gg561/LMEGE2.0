package skybox;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import game.Main;
import shaders.ShaderProgram;
import shaders.UniformFloat;
import shaders.UniformMatrix;
import shaders.UniformSampler;
import shaders.UniformVector;
import util.CustomFile;

public class SkyboxShader extends ShaderProgram {
	
	private static final CustomFile VERTEX_SHADER = new CustomFile("skybox", "Vertex.glsl");
	private static final CustomFile FRAGMENT_SHADER = new CustomFile("skybox", "Fragment.glsl");
	
	private static final float ROTATION_PER_SECOND = 0.2f;
	
	private UniformMatrix projection = new UniformMatrix("projection");
	private UniformMatrix view = new UniformMatrix("view");
	private UniformVector fogColor = new UniformVector("fogColor");
	private UniformSampler samplerTexture = new UniformSampler("samplerTexture");
	private UniformSampler mixable = new UniformSampler("mixable");
	private UniformFloat blendFactor = new UniformFloat("blendFactor");
	
	private float rotation = 0;

	public SkyboxShader() {
		super(VERTEX_SHADER, FRAGMENT_SHADER, "in_position");
		super.storeAllUniformLocations(projection, view, fogColor, samplerTexture, mixable, blendFactor);
		// TODO Auto-generated constructor stub
	}
	
	public void setProjection(Matrix4f projection) {
		this.projection.loadMatrix(projection);
	}
	
	public void setView(Matrix4f view) {
		view.m30(0);
		view.m31(0);
		view.m32(0);
		rotation += ROTATION_PER_SECOND * Main.getDeltaCycles()  / 1000f;
		view.rotate(rotation, 0, 1, 0);
		this.view.loadMatrix(view);
	}
	
	public void setFog(Vector3f color) {
		this.fogColor.loadVector3f(color);
	}
	
	public void setBlendFactor(float factor) {
		this.blendFactor.loadFloat(factor);
	}
	
	public void loadTextures() {
		samplerTexture.loadSampler(0);
		mixable.loadSampler(1);
	}

}
