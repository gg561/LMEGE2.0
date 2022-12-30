package grass;

import org.joml.Matrix4f;

import actors.Movable;

public class Grass extends Movable {
	
	public Matrix4f getTransformation() {
		Matrix4f rv = new Matrix4f();
		rv.translate(this.getPosition());
		return rv;
	}

}
