package font;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import shaders.ExpandableShader;
import shaders.UniformMatrix;
import shaders.UniformVector;
import util.CustomFile;

public class FontShader extends ExpandableShader {
	
	private static final CustomFile VERTEX_SHADER = new CustomFile("font", "Vertex.glsl");
	private static final CustomFile FRAGMENT_SHADER = new CustomFile("font", "Fragment.glsl");
	
	public FontShader() {
		super(VERTEX_SHADER, FRAGMENT_SHADER, "in_position", "texCoords");
	}
	
	public void setModel(Matrix4f model) {
		p().v.matrix("modelMatrix").loadMatrix(model);
	}
	
	public void setProjection(Matrix4f projection) {
		p().v.matrix("projectionMatrix").loadMatrix(projection);
	}
	
	public void setView(Matrix4f view) {
		p().v.matrix("viewMatrix").loadMatrix(view);
	}
	
	public void setColor(Vector3f color) {
		p().f.vector("color").loadVector3f(color);
	}

}
