package instanced;

import shaders.ExpandableShader;
import shaders.ShaderConsts;
import shaders.ShaderProgram;
import util.CustomFile;

public class InstancedShader extends ExpandableShader {
	
	private static final CustomFile VERTEX_SHADER = new CustomFile("instanced", "Vertex.glsl");
	private static final CustomFile FRAGMENT_SHADER = new CustomFile("instanced", "Fragment.glsl");

	public InstancedShader() {
		super(VERTEX_SHADER, new CustomFile[0], FRAGMENT_SHADER, new CustomFile[] {ShaderConsts.CelShadingFrag}, "in_position", "in_textureCoords", "in_normals", "in_instanceMatrix", "in_instanceMatrix", "in_instanceMatrix", "in_instanceMatrix");
		// TODO Auto-generated constructor stub
	}

}
