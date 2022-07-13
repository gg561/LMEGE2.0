package game;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.*;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.*;

import org.lwjgl.glfw.GLFW;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import org.lwjgl.opengl.GL;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;

public class Window {
	
	public static final int FRAMES_PER_SECOND = 70;
	
	private static List<Long> windows = new ArrayList<Long>();
    
	public static long createWindow(int width, int height, String title) {
		GLFW.glfwInit();
		//GLFW.glfwSetErrorCallback();
		GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, GLFW.GLFW_FALSE);
		GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MAJOR, 3);
		GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MINOR, 2);
		GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_FORWARD_COMPAT, GLFW.GLFW_TRUE);
		GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_PROFILE, GLFW.GLFW_OPENGL_CORE_PROFILE);
		GLFW.glfwSwapInterval(GLFW.GLFW_TRUE);
		long window = GLFW.glfwCreateWindow(width, height, title, NULL, NULL);
		if(window == 0) {
			throw new RuntimeException("Failed to create window");
		}
		GLFW.glfwMakeContextCurrent(window);
		GL.createCapabilities();
		GLFW.glfwShowWindow(window);
		windows.add(window);
		return window;
	}
	
	public static void closeWindow(long window) {
		windows.remove(window);
		GLFW.glfwDestroyWindow(window);
	}
	
	public static void closeAllWindows() {
		for(Long window : windows) {
			GLFW.glfwDestroyWindow(window);
			windows.remove(window);
		}
	}
	
	public static List<Long> getWindows(){
		return windows;
	}
	
	public static void updateWindow(long window) {
		GLFW.glfwPollEvents();
		GLFW.glfwSwapBuffers(window);
	}
	
	public static void updateAllWindows() {
		GLFW.glfwPollEvents();
		for(Long window : windows) {
			GLFW.glfwSwapBuffers(window);
		}
	}
	
	public static void lockCursor(long window) {
		GLFW.glfwSetInputMode(window, GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_DISABLED);
	}
	
	public static void unlockCursor(long window) {
		GLFW.glfwSetInputMode(window, GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_NORMAL);
	}

}
