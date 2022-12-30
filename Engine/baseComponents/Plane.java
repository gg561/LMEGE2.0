package baseComponents;

import org.joml.Vector2f;
import org.joml.Vector3f;

public class Plane {
	
	private static final int DIMENSIONS = 3;
	
	private float[] corners = new float[4 * DIMENSIONS];
	
	public Plane(Vector2f position, Vector2f size) {
		this(new Vector3f(position, 0), size);
	}
	
	public Plane(Vector3f position, Vector2f size) {
		calculateCorners(position, size);
	}
	
	private void calculateCorners(Vector3f position, Vector2f size) {
		Vector3f[] verts = new Vector3f[4];
		verts[0] = position.add(size.x, size.y, 0);
		verts[1] = position.add(size.x, -size.y, 0);
		verts[2] = position.add(-size.x, size.y, 0);
		verts[3] = position.add(-size.x, -size.y, 0);
		for(int i = 0; i < verts.length; i ++) {
			corners[i * 3] = verts[i].x;
			corners[i * 3 + 1] = verts[i].y;
			corners[i * 3 + 2] = verts[i].z;
		}
	}
	
	public float[] getCorners() {
		return corners;
	}

}
