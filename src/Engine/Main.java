package Engine;

import static org.lwjgl.glfw.GLFW.glfwSetErrorCallback;

import java.util.ArrayList;
import java.util.List;

import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL11;

import Engine.actors.Camera;
import Engine.actors.Light;
import Engine.entity.Entity;
import Engine.entity.EntityRenderer;
import Engine.models.Model;
import Engine.models.TexturedModel;
import Engine.readers.OBJFileLoader;
import Engine.renderer.BaseRenderer;
import Engine.renderer.Loader;
import Engine.terrain.Terrain;
import Engine.textures.Texture;
import Engine.util.CustomFile;

public class Main {
	
    private static GLFWErrorCallback errorCallback;
	
	public static void main(String[] args) {
		long window = Window.createWindow(1920, 1080, "hi");
		BaseRenderer renderer = new BaseRenderer();
		Loader loader = new Loader();
		float[] vertices = {
			    -0.5f, 0.5f, 0f,
			    -0.5f, -0.5f, 0f,
			    0.5f, -0.5f, 0f,
			    0.5f, -0.5f, 0f,
			    0.5f, 0.5f, 0f,
			    -0.5f, 0.5f, 0f
			  };
		float[] vertices2 = {
				 0.5f, -0.5f, 0f,
				 0.5f, 0.5f, 0f,
				 -0.5f, -0.5f, 0f,
				 -0.5f, 0.5f, 0f,
				 -0.5f, -0.5f, 0f,
				 0.5f, -0.5f, 0f
			  };
		int[] indices = {
				1, 3, 3
		};
		Model model = OBJFileLoader.loadOBJ("parts/cube", loader);
		model.shouldRender(true);
		Model model2 = OBJFileLoader.loadOBJ("parts/part", loader);
		model2.shouldRender(true);
		TexturedModel tmodel = new TexturedModel(model, Texture.loadFromFile(new CustomFile("parts", "colors.png"), 1024));
		tmodel.getTexture().setReflectivity(1);
		tmodel.getTexture().setShineDamper(10);
		TexturedModel tmodel2 = new TexturedModel(model2, Texture.loadFromFile(new CustomFile("parts", "colors.png"), 1024));
		tmodel2.getTexture().setHasTransparency(true);
		tmodel2.getTexture().setReflectivity(1);
		tmodel2.getTexture().setShineDamper(10);
		Entity ent = new Entity(tmodel);
		Entity ent2 = new Entity(tmodel2);
		ent.setPosition(new Vector3f(0, -2, -5));
		ent.setRotation(new Vector3f(0, 0, 0));
		ent.setScale(new Vector3f(1, 1, 1));
		ent2.setPosition(new Vector3f(0, 0, -2f));
		ent2.setRotation(new Vector3f(0, 0, 0));
		ent2.setScale(new Vector3f(1, 1, 1));
		List<Entity> ents = new ArrayList<Entity>();
		ents.add(ent);
		ents.add(ent2);
		Camera camera = new Camera();
		camera.setPosition(new Vector3f(0, 1, 0));
		camera.setRotation(new Vector3f(0, 0, 0));
		camera.setPerspective((float)Math.toRadians(70), 1920/1080, 0.01f, 1000);
		Light light = new Light(new Vector3f(1, 1, 1), new Vector3f(0, 10, 0));
		Texture tt = Texture.loadFromFile(new CustomFile("parts", "colors.jpg"), 1024);
		tt.setReflectivity(1);
		tt.setShineDamper(10);
		Terrain terrain = new Terrain(0, 0, loader, tt);
		Texture t = Texture.loadFromFile(new CustomFile("parts", "colors.jpg"), 1024);
		
		List<Terrain> terrains = new ArrayList<Terrain>();
		terrains.add(terrain);
		
		glfwSetErrorCallback(errorCallback = GLFWErrorCallback.createPrint(System.err));
		renderer.prepareBatch(ents, terrains);
		
		double time = Timer.getTime();
		double frameCap = 1/Window.FRAMES_PER_SECOND;
		int frames = 0;
		double time3 = System.currentTimeMillis();
		while(!GLFW.glfwWindowShouldClose(window)) {
			double time_2 = Timer.getTime();
			double passed = time_2 - time;
			time = time_2;
			frames++;
			if(passed >= frameCap) {
				try {
					Thread.sleep(12);
				}catch(Exception e) {
					e.printStackTrace();
				}
				if (System.currentTimeMillis() > time3 + 1000) {
					GLFW.glfwSetWindowTitle(window, "hi" + " | FPS: " + frames);
					time3 = System.currentTimeMillis();
					frames = 0;
				}
			}
			GLFW.glfwPollEvents();
			GL11.glEnableClientState(GL11.GL_VERTEX_ARRAY);
			ent2.getRotation().add(new Vector3f(0.01f, 0.01f, 0));
			camera.rotate(new Vector3f(0, 0.001f, 0));
			//camera.getPosition().add(new Vector3f(0, 0, -0.001f));
			renderer.render(camera, light);
			GLFW.glfwSwapBuffers(window);
			//Window.updateWindow(window);
		}
		Window.closeWindow(window);
		renderer.cleanUp();
		loader.cleanUp();
	}

}
