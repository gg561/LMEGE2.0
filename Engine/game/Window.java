package game;
import java.awt.Font;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import org.joml.Matrix4f;
import org.lwjgl.*;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.*;
import org.lwjglx.BufferUtils;
import org.lwjglx.opengl.Display;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import org.lwjgl.opengl.GL;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;

public class Window {
	
	public static final int FRAMES_PER_SECOND = 70;
	
	private static List<Window> windows = new ArrayList<Window>();
	
	private long id;
	private int frameBufferWidth;
	private int frameBufferHeight;
	private int windowHeight;
	private int windowWidth;
	private IntBuffer w = BufferUtils.createIntBuffer(1);
	private IntBuffer h = BufferUtils.createIntBuffer(1);
	private Matrix4f orthoMatrix = new Matrix4f();
	
	public Window(long id) {
		this.id = id;
		getFramebufferSize();
		syncOrthoMatrix(frameBufferWidth, frameBufferHeight);
	}
	
	public Window(int width, int height, String title) {
		this.id = createWindow(width, height, title);
		this.windowWidth = width;
		this.windowHeight = height;
		getFramebufferSize();
		syncOrthoMatrix(frameBufferWidth, frameBufferHeight);
	}
	
	private void syncOrthoMatrix(int width, int height) {
		int halfWidth = width/2;
		int halfHeight = height/2;
		this.orthoMatrix.setOrtho2D(halfWidth, -halfWidth, halfHeight, -halfHeight);
	}
	
	public long getId() {
		return id;
	}
	
	public int getFramebufferWidth() {
		return frameBufferWidth;
	}
	
	public int getFramebufferHeight() {
		return frameBufferHeight;
	}
	
	public int getWindowWidth() {
		return windowWidth;
	}
	
	public int getWindowHeight() {
		return windowHeight;
	}
	
	public Window getFramebufferSize() {
		GLFW.glfwGetFramebufferSize(id, w, h);
		frameBufferWidth = w.get(0);
		frameBufferHeight = h.get(0);
		return this;
	}
	
	public Window getWindowSize() {
		GLFW.glfwGetWindowSize(id, w, h);
		windowWidth = w.get(0);
		windowHeight = h.get(0);
		return this;
		
	}
	
	public Window update() {
		GLFW.glfwSwapBuffers(id);
		GL11.glFinish();
		GLFW.glfwPollEvents();
		getFramebufferSize();
		getWindowSize();
		return this;
	}
	
	public Window setCursorLocked(boolean lock) {
		if(lock) { 
			lockCursor(id);
		}
		else {
			unlockCursor(id);
		}
		return this;
	}
	
	public void close() {
		w.clear();
		h.clear();
		closeWindow(this);
	}
	
	public Window setWidth(int width) {
		this.frameBufferWidth = width;
		return this;
	}
	
	public Window setHeight(int height) {
		this.frameBufferHeight = height;
		return this;
	}
	
	public static long createWindow(int width, int height, String title) {
		GLFW.glfwInit();
		//GLFW.glfwSetErrorCallback();
		GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, GLFW.GLFW_TRUE);
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
		windows.add(new Window(window));
		return window;
	}
	
	public static void closeWindow(Window window) {
		windows.remove(window);
		GLFW.glfwDestroyWindow(window.id);
	}
	
	public static void closeAllWindows() {
		for(Window window : windows) {
			window.close();
			windows.remove(window);
		}
	}
	
	public static List<Window> getWindows(){
		return windows;
	}
	
	public static void updateAllWindows() {
		GLFW.glfwPollEvents();
		for(Window window : windows) {
			GLFW.glfwSwapBuffers(window.getId());
		}
	}
	
	public static void lockCursor(long window) {
		GLFW.glfwSetInputMode(window, GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_DISABLED);
	}
	
	public static void unlockCursor(long window) {
		GLFW.glfwSetInputMode(window, GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_NORMAL);
	}

}
