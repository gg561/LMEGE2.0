package mouse;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL31;
import org.lwjgl.opengl.GL40;

import actors.Camera;
import baseComponents.Fbo;
import baseComponents.Vbo;
import game.Window;
import terrain.Terrain;

public class MouseTracker {

	private static final int RANGE = 100;
	
	public Vector3f getPointedPosition(Window window, Camera camera) {
		Vector4f direction = getWSC(getESC(getHSC(getNDC(window)), camera), camera);
		Vector3f position = new Vector3f(camera.getPosition());
		//SIMPLE RAYCASTING ALGORITHM, to improve for performance and efficiency 
		//This is a O(k) algo of O(60) to be precise, which running at 70fps
		//is a lot for a burnt out toaster for a macbook.
		for(int i = 1; i <= RANGE; i++) {
			position.add(direction.x, direction.y, direction.z);
			if(Terrain.getTerrain(position.x, position.z).getHeightOfTerrain(position.x, position.z) > position.y) {
				break;
			}
		}
		return position;
	}
	
	/**
	 * Here we use "getWindowHeight" and "getWindowWidth" since even though that the resolution of the screen was 
	 * magnified to be twice as big, the mouse coordinates remain the same. The mouse coordinates are based off
	 * of the size of the window opened, and not the size of the framebuffer, thus we have to use the "windowHeight"
	 * and "windowWidth" of the window to avoid getting the magnified coordinates. 
	 * @param window
	 * @return
	 */
	public Vector2f getNDC(Window window) {
		double[] w = new double[1];
		double[] h = new double[1];
		GLFW.glfwGetCursorPos(window.getId(), w, h);
		float x = (float) w[0];
		float y = (float) h[0];
		return new Vector2f(2*x / (window.getWindowWidth()) - 1, 1 - (2*y / window.getWindowHeight()));
	}
	
	public Vector4f getHSC(Vector2f ndc) {
		return new Vector4f(ndc, -1, 1);
	}
	
	public Vector4f getESC(Vector4f hsc, Camera camera) {
		Matrix4f inverseProj = camera.getProjection().invert(new Matrix4f());
		hsc.mul(inverseProj);
		hsc = new Vector4f(hsc.x, hsc.y, -1, 0);
		return hsc;
	}
	
	public Vector4f getWSC(Vector4f esc, Camera camera) {
		Matrix4f inverseView = camera.getTransformation().invert();
		esc.mul(inverseView);
		esc.normalize();
		return esc;
	}
	
	/**
	 * 
	 * VPC -> NDC -> HCS -> EYS -> WOS
	 * Viewport Coordinates into NormalizedDeviceCoordinates, then into HomogenousClipSpace, and then into EyeSpace, then finally into WorldSpace
	 * 
	 * Viewport Coordinates (vec2)
	 * range : [0:width, height:0]
	 * *Y coordinate is inversed
	 * 
	 * TRANSFORMATION BEGIN
	 * 
	 * vec2.x = 2x / width - 1;
	 * vec2.y = 1 - 2x / width;
	 * TRANSFORMATION END
	 * 
	 * NormalizedDeviceCoordinates
	 * 
	 */

}
