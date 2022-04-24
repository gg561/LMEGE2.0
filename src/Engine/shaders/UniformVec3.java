package Engine.shaders;

import org.joml.Vector3f;
import org.lwjgl.opengl.GL20;

public class UniformVec3 extends Uniform {
	
	private float currentX;
	private float currentY;
	private float currentZ;
	private boolean used = false;

	public UniformVec3(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}
	
	public void loadVector3(Vector3f vector3) {
		loadVector3(vector3.x, vector3.y, vector3.z);
	}
	
	public void loadVector3(float x, float y, float z) {
		if(!used || currentX != x || currentY != y || currentZ != z) {
			this.currentX = x;
			this.currentY = y;
			this.currentZ = z;
			used = true;
			GL20.glUniform3f(super.getLocation(), x, y, z);
		}
	}

}
