package terrain;

import java.util.ArrayList;
import java.util.List;

import org.joml.Vector3f;

import shaders.ExpandableShader;
import shaders.ShaderConsts;
import shaders.ShaderProgram;
import shaders.UniformFloat;
import shaders.UniformMatrix;
import shaders.UniformSampler;
import util.CustomFile;

public class TerrainShader extends ExpandableShader {
	
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
		super(new CustomFile[] {ShaderConsts.ShadowVert}, new CustomFile[] {FRAGMENT_MERGEABLE, ShaderConsts.CelShadingFrag, ShaderConsts.SoftShadowFrag});
		super.textures.add(p().f.sampler("backgroundTexture"));
		super.textures.add(p().f.sampler("rTexture"));
		super.textures.add(p().f.sampler("gTexture"));
		super.textures.add(p().f.sampler("bTexture"));
		super.textures.add(p().f.sampler("blendMap"));
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
		p().f.num("tiles").loadFloat(tiles);
	}
	
	public void connectTextureUnits() {
		/*backgroundTexture.loadSampler(0);
		rTexture.loadSampler(1);
		gTexture.loadSampler(2);
		bTexture.loadSampler(3);
		blendMap.loadSampler(4);*/
		p().f.sampler("backgroundTexture").loadSampler(0);
		p().f.sampler("rTexture").loadSampler(1);
		p().f.sampler("gTexture").loadSampler(2);
		p().f.sampler("bTexture").loadSampler(3);
		p().f.sampler("blendMap").loadSampler(4);
	}
	
	public void loadTexture() {
		p().f.sampler("depthMap").loadSampler(5);
	}

}
