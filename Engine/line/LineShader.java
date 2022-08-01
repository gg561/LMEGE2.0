package line;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import actors.Camera;
import shaders.ShaderProgram;
import shaders.UniformMapped;
import shaders.UniformMatrix;
import shaders.UniformVector;
import util.CustomFile;

public class LineShader extends ShaderProgram {
	
	private static final CustomFile VERTEX_SHADER = new CustomFile("line", "Vertex.glsl");
	private static final CustomFile FRAGMENT_SHADER = new CustomFile("line", "Fragment.glsl");
	
	private UniformMapped uniformMap = new UniformMapped();

	public LineShader() {
		super(VERTEX_SHADER, FRAGMENT_SHADER, "in_positions");
		super.storeAllUniformToMap(uniformMap);
		super.storeAllUniformLocations(uniformMap.getUniforms().values());
		// TODO Auto-generated constructor stub
	}

	public void setProjMatrix(Camera camera) {
		((UniformMatrix) uniformMap.get("projMatrix")).loadMatrix(camera.getProjection());
	}
	
	public void setViewMatrix(Camera camera) {
		((UniformMatrix) uniformMap.get("viewMatrix")).loadMatrix(camera.getTransformation());
	}
	
	public void loadModel(Matrix4f model) {
		((UniformMatrix) uniformMap.get("modelMatrix")).loadMatrix(model);
	}
	
	public void loadColor(Vector3f color) {
		((UniformVector) uniformMap.get("color")).loadVector3f(color);
	}
	
}
