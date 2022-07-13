package shaders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import actors.Light;
import textures.Texture;
import util.CustomFile;

public class ExpandableShader extends ShaderProgram {
	
	private static final CustomFile VERTEX_SHADER = new CustomFile("shaders", "Vertex.glsl");
	private static final CustomFile FRAGMENT_SHADER = new CustomFile("shaders", "Fragment.glsl");
	
	protected UniformMatrix viewMatrix = new UniformMatrix("viewMatrix");
	protected UniformMatrix modelMatrix = new UniformMatrix("modelMatrix");
	protected UniformMatrix projMatrix = new UniformMatrix("projMatrix");
	protected UniformVector lightPos = new UniformVector("lightPosition");
	protected UniformVector lightCol = new UniformVector("lightColor");
	protected List<UniformSampler> textures = new ArrayList<UniformSampler>() {{add(new UniformSampler("sampleTexture"));}};
	protected UniformFloat shineDamper = new UniformFloat("shineDamper");
	protected UniformFloat reflectivity = new UniformFloat("reflectivity");
	protected UniformBoolean useFakeLighting = new UniformBoolean("useFakeLighting");
	protected UniformFloat fogGradient = new UniformFloat("fogGradient");
	protected UniformFloat fogDensity = new UniformFloat("fogDensity");
	protected UniformVector skyColor = new UniformVector("skyColor");
	
	public ExpandableShader() {
		super(VERTEX_SHADER, FRAGMENT_SHADER, "in_position", "in_texCoords", "in_normal");
		super.storeAllUniformLocations(viewMatrix, modelMatrix, projMatrix, lightPos, lightCol, shineDamper, reflectivity, useFakeLighting, fogGradient, fogDensity, skyColor);
	}
	
	public ExpandableShader(CustomFile vMergeable, CustomFile fMergeable) {
		super(VERTEX_SHADER, vMergeable, FRAGMENT_SHADER, fMergeable, "in_position", "in_texCoords", "in_normal");
		super.storeAllUniformLocations(viewMatrix, modelMatrix, projMatrix, lightPos, lightCol, shineDamper, reflectivity, useFakeLighting, fogGradient, fogDensity, skyColor);
	}
	
	public void defaultAllValues() {
		super.storeAllUniformLocations(getTextures());
	}
	
	public List<UniformSampler> getTextures(){
		return textures;
	}
	
	public void setFog(float gradient, float density, Vector3f sky) {
		fogGradient.loadFloat(gradient);
		fogDensity.loadFloat(density);
		skyColor.loadVector3f(sky);
	}
	
	public void setCamera(Matrix4f projMatrix, Matrix4f viewMatrix) {
		this.projMatrix.loadMatrix(projMatrix);
		this.viewMatrix.loadMatrix(viewMatrix);
	}
	
	public void setLight(Light light) {
		lightPos.loadVector3f(light.getPosition());
		lightCol.loadVector3f(light.getColor());
	}
	
	public void loadModel(Matrix4f modelMatrix) {
		this.modelMatrix.loadMatrix(modelMatrix);
	}
	
	public void loadTextures(List<Texture> textures) {
		for(UniformSampler sampler : this.textures) {
			for(Texture tex : textures) {
				sampler.loadSampler(textures.indexOf(tex));
			}
		}
	}
	
	public void loadShineVariables(float shineDamper, float reflectivity) {
		this.shineDamper.loadFloat(shineDamper);
		this.reflectivity.loadFloat(reflectivity);
	}
	
	public void setUseFakeLighting(boolean useFakeLighting) {
		this.useFakeLighting.loadBoolean(useFakeLighting);
	}

}
