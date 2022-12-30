package debug;

import org.joml.Matrix4f;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL32;

import shaders.ShaderProgram;
import util.CustomFile;

public class DebugShader extends ShaderProgram {

	public DebugShader() {
		super(new CustomFile[][] {
			{new CustomFile("debug", "Vertex.glsl")},
			{new CustomFile("debug", "Fragment.glsl")},
			{new CustomFile("debug", "Geometry.glsl")}
		}, new int[] {
				GL20.GL_VERTEX_SHADER,
				GL20.GL_FRAGMENT_SHADER,
				GL32.GL_GEOMETRY_SHADER
		}, "in_position", "textureCoords", "in_normal");
		// TODO Auto-generated constructor stub
		super.storeAllUniformToMap();
		super.storeAllUniformLocations(p().shaders());
	}
	
	public void setProjection(Matrix4f projection) {
		p().g.matrix("projection").loadMatrix(projection);
	}
	
	public void setView(Matrix4f view) {
		p().v.matrix("view").loadMatrix(view);
	}
	
	public void setModel(Matrix4f model) {
		p().v.matrix("model").loadMatrix(model);
	}

}
