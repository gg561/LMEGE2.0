package actors;

import org.lwjgl.glfw.GLFW;

import game.Main;
import game.Window;

public class InputReceiver {
	
	public static boolean getKeyDown(Window window, int key) {
		return GLFW.glfwGetKey(window.getId(), key) == GLFW.GLFW_TRUE;
	}

}
