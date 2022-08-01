package entity;

import org.joml.Vector3f;

import shaders.ExpandableShader;
import shaders.ShaderProgram;
import shaders.UniformBoolean;
import shaders.UniformFloat;
import shaders.UniformMatrix;
import shaders.UniformSampler;
import util.CustomFile;

public class EntityShader extends ExpandableShader {
	
	public EntityShader() {
		super(new CustomFile[] {}, new CustomFile[] {new CustomFile("shaders/parameters", "EntityCelShadingFrag.glslm"), new CustomFile("shaders/parameters", "EntityTransparencyFrag.glslm")});
	}

}
