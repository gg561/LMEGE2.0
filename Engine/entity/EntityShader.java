package entity;

import org.joml.Vector3f;

import shaders.ExpandableShader;
import shaders.ShaderConsts;
import shaders.ShaderProgram;
import shaders.UniformBoolean;
import shaders.UniformFloat;
import shaders.UniformMatrix;
import shaders.UniformSampler;
import util.CustomFile;

public class EntityShader extends ExpandableShader {
	
	public EntityShader() {
		super(new CustomFile[] {ShaderConsts.ShadowVert}, new CustomFile[] {ShaderConsts.CelShadingFrag, new CustomFile("shaders/parameters", "EntityTransparencyFrag.glslm"), ShaderConsts.SoftShadowFrag});
	}

	/*
	 * 
	 * 
	 */
	
	protected void connectSamplers() {
		super.connectSamplers();
		p().f.sampler("depthMap").loadSampler(1);
	}
	
}
