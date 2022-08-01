package terrain;

import java.util.ArrayList;
import java.util.List;

import org.joml.Vector3f;

import shaders.ExpandableShader;
import shaders.ShaderProgram;
import shaders.UniformFloat;
import shaders.UniformMatrix;
import shaders.UniformSampler;
import util.CustomFile;

public class TerrainShader extends ExpandableShader {
	
	private static final CustomFile VERTEX_MERGEABLE = new CustomFile("terrain", "TerrainVertexParams.glslm");
	private static final CustomFile FRAGMENT_MERGEABLE = new CustomFile("shaders/parameters", "TerrainMultiTextureFrag.glslm");
	/*protected UniformFloat tiles = new UniformFloat("tiles");
	protected UniformSampler backgroundTexture = new UniformSampler("backgroundTexture");
	protected UniformSampler rTexture = new UniformSampler("rTexture");
	protected UniformSampler gTexture = new UniformSampler("gTexture");
	protected UniformSampler bTexture = new UniformSampler("bTexture");
	protected UniformSampler blendMap = new UniformSampler("blendMap");
	protected List<UniformSampler> textures = new ArrayList<UniformSampler>() {{add(backgroundTexture); add(rTexture); add(gTexture); add(bTexture); add(blendMap);}};
	*/
	public TerrainShader() {
		super(new CustomFile[] {}, new CustomFile[] {FRAGMENT_MERGEABLE, new CustomFile("shaders/parameters", "EntityCelShadingFrag.glslm")});
		super.textures.add((UniformSampler) uniformMap.get("backgroundTexture"));
		super.textures.add((UniformSampler) uniformMap.get("rTexture"));
		super.textures.add((UniformSampler) uniformMap.get("gTexture"));
		super.textures.add((UniformSampler) uniformMap.get("bTexture"));
		super.textures.add((UniformSampler) uniformMap.get("blendMap"));
		super.storeAllUniformLocations(textures);
		//super.storeAllUniformLocations(tiles);
		//defaultAllValues();
		super.start();
		connectTextureUnits();
		super.stop();
	}
	
	/*@Override
	public List<UniformSampler> getTextures(){
		return this.textures;
	}*/
	
	public void loadTiles(float tiles) {
		((UniformFloat) uniformMap.get("tiles")).loadFloat(tiles);
	}
	
	public void connectTextureUnits() {
		/*backgroundTexture.loadSampler(0);
		rTexture.loadSampler(1);
		gTexture.loadSampler(2);
		bTexture.loadSampler(3);
		blendMap.loadSampler(4);*/
		((UniformSampler) uniformMap.get("backgroundTexture")).loadSampler(0);
		((UniformSampler) uniformMap.get("rTexture")).loadSampler(1);
		((UniformSampler) uniformMap.get("gTexture")).loadSampler(2);
		((UniformSampler) uniformMap.get("bTexture")).loadSampler(3);
		((UniformSampler) uniformMap.get("blendMap")).loadSampler(4);
	}

}
