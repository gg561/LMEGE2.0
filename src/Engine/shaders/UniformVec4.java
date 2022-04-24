package Engine.shaders;

import org.joml.Vector4f;
import org.lwjgl.opengl.GL20;

public class UniformVec4 extends Uniform {

	public UniformVec4(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}
	
	public void loadVec4(Vector4f vector4) {
		loadVec4(vector4.x, vector4.y, vector4.z, vector4.w);
	}
	
	public void loadVec4(float x, float y, float z, float w) {
		GL20.glUniform4f(super.getLocation(), x, y, z, w);
	}

}
