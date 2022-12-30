package shaders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL32;
import org.lwjgl.opengl.GL40;

import actors.lights.Light;
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
	
	public ExpandableShader() {
		this(VERTEX_SHADER, FRAGMENT_SHADER, "in_position", "in_texCoords", "in_normal");
	}
	
	public ExpandableShader(CustomFile vMergeable, CustomFile fMergeable) {
		this(VERTEX_SHADER, new CustomFile[] {vMergeable}, FRAGMENT_SHADER, new CustomFile[] {fMergeable}, "in_position", "in_texCoords", "in_normal");
		//super.storeAllUniformLocations(viewMatrix, modelMatrix, projMatrix, lightPos, lightCol, shineDamper, reflectivity, useFakeLighting, fogGradient, fogDensity, skyColor, textureAtlasOffset, textureAtlasRows);
	}
	
	public ExpandableShader(CustomFile[] vMergeable, CustomFile[] fMergeable) {
		this(vMergeable, fMergeable, "in_position", " in_texCoords", "in_normal");
	}
	
	public ExpandableShader(CustomFile[] vMergeables, CustomFile[] fMergeables, String...inputs) {
		this(VERTEX_SHADER, vMergeables, FRAGMENT_SHADER, fMergeables, inputs);
	}
	
	public ExpandableShader(CustomFile vertex, CustomFile fragment, String...inputs) {
		super(vertex, fragment, inputs);
		initiation();
	}
	
	public ExpandableShader(CustomFile vertex, CustomFile[] vMergeables, CustomFile fragment, CustomFile[] fMergeables, String...inputs) {
		super(vertex, vMergeables, fragment, fMergeables, inputs);
		initiation();
	}
	
	/*
	public void defaultAllValues() {
		super.storeAllUniformLocations(getTextures());
	}
	
	public List<UniformSampler> getTextures(){
		return textures;
	}*/
	
	protected void initiation() {
		super.storeAllUniformToMap();
		super.storeAllUniformLocations(p().shaders());
		textures  = new ArrayList<UniformSampler>() {{add(p().f.sampler("sampleTexture"));}};
		super.start();
		connectSamplers();
		super.stop();
	}
	
	public void setFog(float gradient, float density, Vector3f sky) {
		/*fogGradient.loadFloat(gradient);
		fogDensity.loadFloat(density);
		skyColor.loadVector3f(sky);*/
		p().v.num("fogGradient").loadFloat(gradient);
		p().v.num("fogDensity").loadFloat(density);
		p().f.vector("skyColor").loadVector3f(sky);
	}
	
	public void setProjection(Matrix4f projMatrix) {
		//((UniformMatrix) uniformMap.get("projMatrix")).loadMatrix(projMatrix);
		p().v.matrix("projMatrix").loadMatrix(projMatrix);
	}
	
	public void setView(Matrix4f viewMatrix) {
		/*this.projMatrix.loadMatrix(projMatrix);
		this.viewMatrix.loadMatrix(viewMatrix);*/
		p().v.matrix("viewMatrix").loadMatrix(viewMatrix);
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
		p().v.array("lightPosition[]").loadVectorArray(positions);
		p().f.array("lightColor[]").loadVectorArray(colors);
		p().f.array("lightAttenuation[]").loadVectorArray(attenuations);
		p().v.num("numberOfLights").loadFloat(lights.size());
	}
	
	public void setModel(Matrix4f modelMatrix) {
		//this.modelMatrix.loadMatrix(modelMatrix);
		p().v.matrix("modelMatrix").loadMatrix(modelMatrix);
	}
	
	public void loadTextures(List<Texture> textures) {
		int i = 0;
		for(UniformSampler sampler : this.textures) {
			sampler.loadSampler(i);
			if(i < textures.size())
				textures.get(i).bind(i);
			i++;
		}
	}
	
	public void loadTexturesDebug(List<Texture> textures) {
		int i = 0;
		for(UniformSampler sampler : this.textures) {
			sampler.loadSampler(i);
			if(i < textures.size()) {
				textures.get(i).bind(i);
			}
			i++;
		}
	}
	
	protected void connectSamplers() {
		p().f.sampler("sampleTexture").loadSampler(0);
	}
	
	public void setReflectivity(float shineDamper, float reflectivity) {
		/*this.shineDamper.loadFloat(shineDamper);
		this.reflectivity.loadFloat(reflectivity);*/
		p().f.num("shineDamper").loadFloat(shineDamper);
		p().f.num("reflectivity").loadFloat(reflectivity);
	}
	
	public void setUseFakeLighting(boolean useFakeLighting) {
		//this.useFakeLighting.loadBoolean(useFakeLighting);
		int i = useFakeLighting ? 1 : 0;
		p().v.num("useFakeLighting").loadFloat(i);
	}
	
	public void setTextureAtlasRows(int rows) {
		p().v.num("textureAtlasRows").loadFloat(rows);
	}
	
	public void setTextureAtlasOffset(Vector2f offset) {
		p().v.vector("textureAtlasOffset").loadVector2f(offset);
	}
	
	public void setClippingPlane(Vector4f plane) {
		p().v.vector("clippingDistance").loadVector4f(plane);
	}
	
	public void setShadow(Matrix4f lightBiasedMatrix) {
		p().v.matrix("lightBiasedMatrix").loadMatrix(lightBiasedMatrix);
		if(!this.textures.contains(p().f.sampler("depthMap")))
			this.textures.add(p().f.sampler("depthMap"));
	}
	
	/*
	 * Work.In.Progress
	 */
	protected void setUniform(Class<? extends Uniform> clazz, String name, Object value) {
		
	}
	
	public final Uniform getUniform(String name) {
		//Uniform res = uniformMap.get(name);
		return null;
	}

}
