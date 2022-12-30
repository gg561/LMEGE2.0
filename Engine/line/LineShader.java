package line;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import actors.Camera;
import shaders.ShaderProgram;
import shaders.ObjectMap;
import shaders.UniformMatrix;
import shaders.UniformVector;
import util.CustomFile;

public class LineShader extends ShaderProgram {
	
	private static final CustomFile VERTEX_SHADER = new CustomFile("line", "Vertex.glsl");
	private static final CustomFile FRAGMENT_SHADER = new CustomFile("line", "Fragment.glsl");

	public LineShader() {
		super(VERTEX_SHADER, FRAGMENT_SHADER, "in_positions");
		super.storeAllUniformToMap();
		super.storeAllUniformLocations(p().shaders());
		// TODO Auto-generated constructor stub
	}

	public void setProjection(Camera camera) {
		p().v.matrix("projMatrix").loadMatrix(camera.getProjection());
	}
	
	public void setView(Camera camera) {
		p().v.matrix("viewMatrix").loadMatrix(camera.getTransformation());
	}
	
	public void loadModel(Matrix4f model) {
		p().v.matrix("modelMatrix").loadMatrix(model);
	}
	
	public void loadColor(Vector3f color) {
		p().f.vector("color").loadVector3f(color);
	}
	
}
