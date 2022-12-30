package actors;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.glfw.GLFW;

import game.Window;

public class Exoskeleton {
	
	private static final int ESCAPE = GLFW.GLFW_KEY_ESCAPE;
	
	private static double[] cursorX = new double[1];
	private static double[] cursorY = new double[1];
	
	private static boolean cursorLocked = false;
	
	public static void move(MovePreset preset, Movable movable, float distance, long window) {
		Vector3f direction = detectInput(preset, window, movable);
		movable.getPosition().add(direction.mul(distance));
	}
	
	public static void copyMotion(Movable origin, Vector3f offset, Movable...targets) {
		for(Movable target : targets) {
			target.setPosition(origin.getPosition().add(offset, new Vector3f()));
			target.setRotation(origin.getRotation());
		}
	}
	
	public static void copyMotion(Camera origin, Vector3f offset, Vector3f rotOffset, Movable...targets) {
		for(Movable target : targets) {
			target.setPosition(origin.getPosition().add(offset, new Vector3f()));
			target.setRotation(origin.getRotation().add(rotOffset, new Vector3f()).negate(new Vector3f()));
		}
	}
	
	public enum MovePreset{
		ARROWS(GLFW.GLFW_KEY_UP, GLFW.GLFW_KEY_LEFT, GLFW.GLFW_KEY_DOWN, GLFW.GLFW_KEY_RIGHT, GLFW.GLFW_KEY_SPACE, GLFW.GLFW_KEY_LEFT_SHIFT), 
		WASD(GLFW.GLFW_KEY_W, GLFW.GLFW_KEY_A, GLFW.GLFW_KEY_S, GLFW.GLFW_KEY_D, GLFW.GLFW_KEY_SPACE, GLFW.GLFW_KEY_LEFT_SHIFT), 
		WASDEQ(GLFW.GLFW_KEY_W, GLFW.GLFW_KEY_A, GLFW.GLFW_KEY_S, GLFW.GLFW_KEY_D, GLFW.GLFW_KEY_E, GLFW.GLFW_KEY_Q), 
		WASD_NO_SLIDE(GLFW.GLFW_KEY_W, 0, GLFW.GLFW_KEY_S, 0, GLFW.GLFW_KEY_SPACE, GLFW.GLFW_KEY_LEFT_SHIFT, GLFW.GLFW_KEY_A, GLFW.GLFW_KEY_D),
		CUSTOM; 
		
		private int[] keys;
		
		private MovePreset(int...keys) {
			this.keys = keys;
		}
		
		public int[] getKeys() {
			return keys;
		}
		
		public void setKeys(int...keys) {
			this.keys = keys;
		}
	}
	
	public static void aim(Movable movable, float sensitivity, long window, Vector4f limits) {
		if((GLFW.glfwGetKey(window, ESCAPE) == GLFW.GLFW_TRUE)) {
			if(cursorLocked) {
				Window.unlockCursor(window);
				cursorLocked = false;
			}
		}else {
			if(!cursorLocked) {
				Window.lockCursor(window);
				cursorLocked = true;
			}
			double[] x = new double[1];
			double[] y = new double[1];
			GLFW.glfwGetCursorPos(window, x, y);
			float dy = (float)Math.toRadians(x[0] - cursorX[0]) * -sensitivity / 100;
			float dx = (float)Math.toRadians(y[0] - cursorY[0]) * sensitivity / 100;
			if(limits != null) {
				if(movable.getLocalRotation().y + dy < limits.x && movable.getLocalRotation().y + dy > limits.y)
					movable.getLocalRotation().add(new Vector3f(0, dy, 0));
				if(movable.getLocalRotation().x + dx < limits.z && movable.getLocalRotation().x + dx > limits.w)
					movable.getLocalRotation().add(new Vector3f(dx, 0, 0));
			}else {
				movable.rotate(new Vector3f(dx, dy, 0));
			}
			cursorX = x;
			cursorY = y;
		}
	}
	
	private static Vector3f detectInput(MovePreset preset, long window, Movable movable) {
		Vector3f direction = new Vector3f();
		int index = 0;
		Vector3f rot = movable.getRotation();
		for(int key : preset.getKeys()) {
			if(key != 0 && GLFW.glfwGetKey(window, key) == GLFW.GLFW_TRUE) {
				System.out.println(index);
				if(index == 0) {
					direction = new Vector3f(0, 0, -1);
					direction.rotateY(rot.y);
				}else if(index == 1) {
					direction = new Vector3f(-1, 0, 0);
					direction.rotateY(rot.y);
				}else if(index == 2) {
					direction = new Vector3f(0, 0, 1);
					direction.rotateY(rot.y);
				}else if(index == 3) {
					direction = new Vector3f(1, 0, 0);
					direction.rotateY(rot.y);
				}else if(index == 4) {
					direction = new Vector3f(0, 1, 0);
				}else if(index == 5) {
					direction = new Vector3f(0, -1, 0);
				}else if(index == 6) {
					movable.getRotation().add(new Vector3f(0, (float)Math.toRadians(1), 0));
				}else if(index == 7) {
					movable.getRotation().add(new Vector3f(0, (float)Math.toRadians(-1), 0));
				}
			}
			index++;
		}
		index = 0;
		return direction;
	}

}
