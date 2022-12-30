package util;

import org.joml.Matrix4f;

public class Matrices {
	
	public static final Matrix4f LIGHT_BIASED_DEFAULT = new Matrix4f() {{translate(0.5f, 0.5f, 0.5f); scale(0.5f, 0.5f, 0.5f);}};
	public static final Matrix4f ZERO_4X4 = new Matrix4f();

}
