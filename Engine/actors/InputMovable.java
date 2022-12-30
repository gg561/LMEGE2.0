package actors;

import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

import game.Main;
import game.Window;

public class InputMovable extends Movable {
	
	private Movable puppet;
	
	public InputMovable(Movable puppet) {
		this.puppet = puppet;
	}
	
	public void move(Window window) {
		if(InputReceiver.getKeyDown(window, GLFW.GLFW_KEY_W)) {
			puppet.move(new Vector3f(0, 0, -1));
		}else if(InputReceiver.getKeyDown(window, GLFW.GLFW_KEY_S)) {
			puppet.move(new Vector3f(0, 0, 1));
		}if(InputReceiver.getKeyDown(window, GLFW.GLFW_KEY_A)) {
			puppet.rotate(new Vector3f(0, (float) Math.toRadians(1), 0));
		}else if(InputReceiver.getKeyDown(window, GLFW.GLFW_KEY_D)) {
			puppet.rotate(new Vector3f(0, (float) Math.toRadians(-1), 0));
		}else if(InputReceiver.getKeyDown(window, GLFW.GLFW_KEY_SPACE)) {
			puppet.move(new Vector3f(0, 1, 0));
		}else if(InputReceiver.getKeyDown(window, GLFW.GLFW_KEY_Q)) {
			puppet.move(new Vector3f(0, -1, 0));
		}
	}

}
