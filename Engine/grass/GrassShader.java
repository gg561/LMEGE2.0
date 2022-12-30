package grass;

import org.joml.Matrix4f;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL32;

import shaders.ShaderProgram;
import util.CustomFile;

public class GrassShader extends ShaderProgram {

	public GrassShader() {
		super(new CustomFile[][] {
			{new CustomFile("grass", "Vertex.glsl")},
			{new CustomFile("grass", "Geometry.glsl")},
			{new CustomFile("grass", "Fragment.glsl")}
		}, 
				new int[] {
						GL20.GL_VERTEX_SHADER,
						GL32.GL_GEOMETRY_SHADER,
						GL20.GL_FRAGMENT_SHADER
				}, "in_positions");
		super.storeAllUniformToMap();
		super.storeAllUniformLocations(p().shaders());
		// TODO Auto-generated constructor stub
	}
	
	public void setProjection(Matrix4f projection) {
		System.out.println(p().g.source());
		p().g.matrix("projection").loadMatrix(projection);
	}
	
	public void setView(Matrix4f view) {
		p().g.matrix("view").loadMatrix(view);
	}
	
	public void setModel(Matrix4f model) {
		p().v.matrix("model").loadMatrix(model);
	}

}
