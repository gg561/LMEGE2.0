package actors;

import org.lwjgl.glfw.GLFW;

import game.Main;

public class InputReceiver {
	
	public static boolean getKeyDown(long window, int key) {
		return GLFW.glfwGetKey(window, key) == GLFW.GLFW_TRUE;
	}

}
