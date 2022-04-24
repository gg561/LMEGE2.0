package Engine.entity;

import Engine.shaders.ShaderProgram;
import Engine.shaders.UniformBoolean;
import Engine.shaders.UniformFloat;
import Engine.shaders.UniformMatrix;
import Engine.shaders.UniformSampler;
import Engine.shaders.UniformVec3;
import Engine.util.CustomFile;

public class EntityShader extends ShaderProgram {
	
	private static final CustomFile VERTEX_SHADER = new CustomFile("Engine", "entity", "EntityVertex.txt");
	private static final CustomFile FRAGMENT_SHADER = new CustomFile("Engine", "entity", "EntityFragment.txt");
	
	protected UniformMatrix viewMatrix = new UniformMatrix("viewMatrix");
	protected UniformMatrix modelMatrix = new UniformMatrix("modelMatrix");
	protected UniformMatrix projMatrix = new UniformMatrix("projMatrix");
	protected UniformVec3 lightPos = new UniformVec3("lightPosition");
	protected UniformVec3 lightCol = new UniformVec3("lightColor");
	protected UniformSampler texture = new UniformSampler("sampleTexture");
	protected UniformFloat shineDamper = new UniformFloat("shineDamper");
	protected UniformFloat reflectivity = new UniformFloat("reflectivity");
	protected UniformBoolean useFakeLighting = new UniformBoolean("useFakeLighting");
	
	public EntityShader() {
		super(VERTEX_SHADER, FRAGMENT_SHADER, "in_position", "in_texCoords", "in_normal");
		super.storeAllUniformLocations(viewMatrix, modelMatrix, projMatrix, lightPos, lightCol, texture, shineDamper, reflectivity, useFakeLighting);
	}
	
	private void connectTextureUnits() {
		
	}

}
