package Engine.terrain;

import Engine.shaders.ShaderProgram;
import Engine.shaders.UniformFloat;
import Engine.shaders.UniformMatrix;
import Engine.shaders.UniformSampler;
import Engine.shaders.UniformVec3;
import Engine.util.CustomFile;

public class TerrainShader extends ShaderProgram {
	
	private static final CustomFile VERTEX_SHADER = new CustomFile("Engine", "terrain", "TerrainVertex.glsl");
	private static final CustomFile FRAGMENT_SHADER = new CustomFile("Engine", "terrain", "TerrainFragment.glsl");

	protected UniformMatrix viewMatrix = new UniformMatrix("viewMatrix");
	protected UniformMatrix modelMatrix = new UniformMatrix("modelMatrix");
	protected UniformMatrix projMatrix = new UniformMatrix("projMatrix");
	protected UniformVec3 lightPos = new UniformVec3("lightPosition");
	protected UniformVec3 lightCol = new UniformVec3("lightColor");
	protected UniformSampler texture = new UniformSampler("sampleTexture");
	protected UniformFloat shineDamper = new UniformFloat("shineDamper");
	protected UniformFloat reflectivity = new UniformFloat("reflectivity");
	protected UniformFloat tiles = new UniformFloat("tiles");
	
	public TerrainShader() {
		super(VERTEX_SHADER, FRAGMENT_SHADER, "in_position", "in_texCoords", "in_normal");
		super.storeAllUniformLocations(viewMatrix, modelMatrix, projMatrix, lightPos, lightCol, texture, shineDamper, reflectivity, tiles);
	}
	
	private void connectTextureUnits() {
		
	}

}
