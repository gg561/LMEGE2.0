package Engine.shaders;

import Engine.util.CustomFile;

public class ExpandableShader extends ShaderProgram {
	
	private static final CustomFile VERTEX_SHADER = new CustomFile("Engine", "shaders", "Vertex.glsl");
	private static final CustomFile FRAGMENT_SHADER = new CustomFile("Engine", "shaders", "Fragment.glsl");
	
	protected UniformMatrix viewMatrix = new UniformMatrix("viewMatrix");
	protected UniformMatrix modelMatrix = new UniformMatrix("modelMatrix");
	protected UniformMatrix projMatrix = new UniformMatrix("projMatrix");
	protected UniformVector lightPos = new UniformVector("lightPosition");
	protected UniformVector lightCol = new UniformVector("lightColor");
	protected UniformSampler texture = new UniformSampler("sampleTexture");
	protected UniformFloat shineDamper = new UniformFloat("shineDamper");
	protected UniformFloat reflectivity = new UniformFloat("reflectivity");
	protected UniformBoolean useFakeLighting = new UniformBoolean("useFakeLighting");
	protected UniformFloat tiles = new UniformFloat("tiles");
	
	public ExpandableShader() {
		super(VERTEX_SHADER, FRAGMENT_SHADER, "in_position", "in_texCoords", "in_normal");
		super.storeAllUniformLocations(viewMatrix, modelMatrix, projMatrix, lightPos, lightCol, texture, shineDamper, reflectivity, useFakeLighting, tiles);
		defaultAllValues();
	}
	
	/*
	 * default out specific values.
	 * e.g. tiles - used only in terrains, defaulted out, for the entities to work as well.
	 */
	public void defaultAllValues() {
		tiles.loadFloat(1);
	}

}
