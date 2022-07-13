package shaders;

import org.joml.Vector2fc;
import org.joml.Vector3fc;
import org.joml.Vector4fc;
import org.lwjgl.opengl.GL30;

public class UniformVector extends Uniform {
	
	private float[] contents;
	private boolean used = false;
	
	public UniformVector(String name) {
		super(name);
	}

	public void loadVector2f(Vector2fc vector) {
		if(contents != null) {
			if(contents.length == 2) {
				if(contents[0] != vector.x() || contents[1] != vector.y()) {
					
				}else {
					return;
				}
			}
		}
		contents = new float[2];
		contents[0] = vector.x();
		contents[1] = vector.y();
		GL30.glUniform2f(super.getLocation(), contents[0], contents[1]);
		used = true;
	}
	
	public void loadVector3f(Vector3fc vector) {
		if(contents != null) {
			if(contents.length == 3) {
				if(contents[0] != vector.x() || contents[1] != vector.y() || contents[2] != vector.z()) {
					
				}else {
					return;
				}
			}
		}
		contents = new float[3];
		contents[0] = vector.x();
		contents[1] = vector.y();
		contents[2] = vector.z();
		GL30.glUniform3f(super.getLocation(), contents[0], contents[1], contents[2]);
		used = true;
	}
	

	public void loadVector4f(Vector4fc vector) {
		if(contents != null) {
			if(contents.length == 4) {
				if(contents[0] != vector.x() || contents[1] != vector.y() || contents[2] != vector.z() || contents[3] != vector.w()) {
					
				}else {
					return;
				}
			}
		}
		contents = new float[4];
		contents[0] = vector.x();
		contents[1] = vector.y();
		contents[2] = vector.z();
		contents[3] = vector.w();
		GL30.glUniform4f(super.getLocation(), contents[0], contents[1], contents[2], contents[3]);
		used = true;
	}

}
