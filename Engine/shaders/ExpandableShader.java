package shaders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

import actors.Light;
import textures.Texture;
import util.CustomFile;

public class ExpandableShader extends ShaderProgram {
	
	private static final CustomFile VERTEX_SHADER = new CustomFile("shaders", "Vertex.glsl");
	private static final CustomFile FRAGMENT_SHADER = new CustomFile("shaders", "Fragment.glsl");
	/*
	protected UniformMatrix viewMatrix = new UniformMatrix("viewMatrix");
	protected UniformMatrix modelMatrix = new UniformMatrix("modelMatrix");
	protected UniformMatrix projMatrix = new UniformMatrix("projMatrix");
	protected UniformVector lightPos = new UniformVector("lightPosition");
	protected UniformVector lightCol = new UniformVector("lightColor");
	protected UniformFloat shineDamper = new UniformFloat("shineDamper");
	protected UniformFloat reflectivity = new UniformFloat("reflectivity");
	protected UniformBoolean useFakeLighting = new UniformBoolean("useFakeLighting");
	protected UniformFloat fogGradient = new UniformFloat("fogGradient");
	protected UniformFloat fogDensity = new UniformFloat("fogDensity");
	protected UniformVector skyColor = new UniformVector("skyColor");
	protected UniformVector textureAtlasOffset = new UniformVector("textureAtlasOffset");
	protected UniformFloat textureAtlasRows = new UniformFloat("textureAtlasRows");
	*/
	protected List<UniformSampler> textures;
	protected UniformMapped uniformMap = new UniformMapped();
	
	public ExpandableShader() {
		super(VERTEX_SHADER, FRAGMENT_SHADER, "in_position", "in_texCoords", "in_normal");
		super.storeAllUniformToMap(uniformMap);
		super.storeAllUniformLocations(uniformMap.getUniforms().values());
		textures = new ArrayList<UniformSampler>() {{add((UniformSampler) uniformMap.get("sampleTexture"));}};
		//super.storeAllUniformLocations(viewMatrix, modelMatrix, projMatrix, lightPos, lightCol, shineDamper, reflectivity, useFakeLighting, fogGradient, fogDensity, skyColor, textureAtlasOffset, textureAtlasRows);
	}
	
	public ExpandableShader(CustomFile vMergeable, CustomFile fMergeable) {
		super(VERTEX_SHADER, vMergeable, FRAGMENT_SHADER, fMergeable, "in_position", "in_texCoords", "in_normal");
		super.storeAllUniformToMap(uniformMap);
		super.storeAllUniformLocations(uniformMap.getUniforms().values());
		textures  = new ArrayList<UniformSampler>() {{add((UniformSampler) uniformMap.get("sampleTexture"));}};
		//super.storeAllUniformLocations(viewMatrix, modelMatrix, projMatrix, lightPos, lightCol, shineDamper, reflectivity, useFakeLighting, fogGradient, fogDensity, skyColor, textureAtlasOffset, textureAtlasRows);
	}
	
	public ExpandableShader(CustomFile[] vMergeable, CustomFile[] fMergeable) {
		super(VERTEX_SHADER, vMergeable, FRAGMENT_SHADER, fMergeable, "in_position", "in_texCoords", "in_normal");
		super.storeAllUniformToMap(uniformMap);
		super.storeAllUniformLocations(uniformMap.getUniforms().values());
		textures  = new ArrayList<UniformSampler>() {{add((UniformSampler) uniformMap.get("sampleTexture"));}};
	}
	/*
	public void defaultAllValues() {
		super.storeAllUniformLocations(getTextures());
	}
	
	public List<UniformSampler> getTextures(){
		return textures;
	}*/
	
	public void setFog(float gradient, float density, Vector3f sky) {
		/*fogGradient.loadFloat(gradient);
		fogDensity.loadFloat(density);
		skyColor.loadVector3f(sky);*/
		((UniformFloat) uniformMap.get("fogGradient")).loadFloat(gradient);
		((UniformFloat) uniformMap.get("fogDensity")).loadFloat(density);
		((UniformVector) uniformMap.get("skyColor")).loadVector3f(sky);
	}
	
	public void setCamera(Matrix4f projMatrix, Matrix4f viewMatrix) {
		/*this.projMatrix.loadMatrix(projMatrix);
		this.viewMatrix.loadMatrix(viewMatrix);*/
		((UniformMatrix) uniformMap.get("projMatrix")).loadMatrix(projMatrix);
		((UniformMatrix) uniformMap.get("viewMatrix")).loadMatrix(viewMatrix);
	}
	
	public void setLight(List<Light> lights) {
		/*lightPos.loadVector3f(light.getPosition());
		lightCol.loadVector3f(light.getColor());*/
		Vector3f[] positions = new Vector3f[lights.size()];
		Vector3f[] colors = new Vector3f[lights.size()];
		Vector3f[] attenuations = new Vector3f[lights.size()];
		for(int i = 0; i < lights.size(); i++) {
			positions[i] = lights.get(i).getPosition();
			colors[i] = lights.get(i).getColor();
			attenuations[i] = lights.get(i).getAttenuation();
		}
		((UniformArray) uniformMap.get("lightPosition[]")).loadVectorArray(positions);
		((UniformArray) uniformMap.get("lightColor[]")).loadVectorArray(colors);
		((UniformArray) uniformMap.get("lightAttenuation[]")).loadVectorArray(attenuations);
		((UniformFloat) uniformMap.get("numberOfLights")).loadFloat(lights.size());
	}
	
	public void loadModel(Matrix4f modelMatrix) {
		//this.modelMatrix.loadMatrix(modelMatrix);
		((UniformMatrix) uniformMap.get("modelMatrix")).loadMatrix(modelMatrix);
	}
	
	public void loadTextures(List<Texture> textures) {
		for(UniformSampler sampler : this.textures) {
			for(Texture tex : textures) {
				sampler.loadSampler(textures.indexOf(tex));
			}
		}
	}
	
	public void loadShineVariables(float shineDamper, float reflectivity) {
		/*this.shineDamper.loadFloat(shineDamper);
		this.reflectivity.loadFloat(reflectivity);*/
		((UniformFloat) uniformMap.get("shineDamper")).loadFloat(shineDamper);
		((UniformFloat) uniformMap.get("reflectivity")).loadFloat(reflectivity);
	}
	
	public void setUseFakeLighting(boolean useFakeLighting) {
		//this.useFakeLighting.loadBoolean(useFakeLighting);
		int i = useFakeLighting ? 1 : 0;
		((UniformFloat) uniformMap.get("useFakeLighting")).loadFloat(i);
	}
	
	public void setTextureAtlasRows(int rows) {
		((UniformFloat) uniformMap.get("textureAtlasRows")).loadFloat(rows);
	}
	
	public void setTextureAtlasOffset(Vector2f offset) {
		((UniformVector) uniformMap.get("textureAtlasOffset")).loadVector2f(offset);
	}

}
