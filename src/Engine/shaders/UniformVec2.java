package Engine.shaders;

import org.joml.Vector2f;
import org.lwjgl.opengl.GL20;

public class UniformVec2 extends Uniform {
	
	private float currentX;
	private float currentY;
	private boolean used = false;

	public UniformVec2(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}
	
	public void loadVector2(Vector2f vector2) {
		loadVector2(vector2.x, vector2.y);
	}
	
	public void loadVector2(float x, float y) {
		if(!used || currentX != x || currentY != y) {
			this.currentX = x;
			this.currentY = y;
			used = true;
			GL20.glUniform2f(super.getLocation(), x, y);
		}
	}

}
