package advancedLighting.normalMap;

import org.joml.Matrix4f;

import actors.Camera;
import shaders.ExpandableShader;
import shaders.ShaderConsts;
import shaders.UniformSampler;
import textures.Texture;
import util.CustomFile;

public class NormalMapShader extends ExpandableShader {
	
	public NormalMapShader() {
		super(new CustomFile[] {new CustomFile("shaders/parameters", "NormalMapVert.glslm"), ShaderConsts.ShadowVert}, new CustomFile[] {ShaderConsts.NormalMapFrag, ShaderConsts.SoftShadowFrag});
		super.textures.add(p().f.sampler("normalMap"));
		super.textures.add(p().f.sampler("depthMap"));
	}
	
	public void setView(Matrix4f view) {
		super.setView(view);
	}
	
	protected void connectSamplers() {
		super.connectSamplers();
		p().f.sampler("normalMap").loadSampler(1);
		p().f.sampler("depthMap").loadSampler(2);
	}

}
