package game;
import static org.lwjgl.glfw.GLFW.glfwSetErrorCallback;

import java.util.ArrayList;
import java.util.List;

import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

import actors.ActorGroup;
import actors.Camera;
import actors.Exoskeleton;
import actors.InputMovable;
import actors.InputReceiver;
import actors.Light;
import collision.AABBCollider;
import collision.TerrainCollider;
import entity.Entity;
import entity.EntityRenderer;
import gameLogic.Car;
import models.Model;
import models.TexturedModel;
import readers.OBJFileLoader;
import renderer.BaseRenderer;
import renderer.Loader;
import shaders.ShaderProgram;
import terrain.FlatTerrain;
import terrain.MappedTerrain;
import terrain.Terrain;
import textures.Texture;
import textures.TexturePack;
import util.CustomFile;

public class Main {
	
    private static GLFWErrorCallback errorCallback;
    public static long mainWindow = 0;
	
	public static void main(String[] args) throws Exception {
		long window = Window.createWindow(1920, 1080, "hi");
		mainWindow = window;
		BaseRenderer renderer = new BaseRenderer();
		Loader loader = new Loader();
		Model model = OBJFileLoader.loadOBJ("parts/wheel", loader);
		model.shouldRender(true);
		Model model2 = OBJFileLoader.loadOBJ("parts/car", loader);
		model2.shouldRender(true);
		TexturedModel tmodel = new TexturedModel(model, Texture.loadFromFile(new CustomFile("parts", "car.png"), 1024));
		tmodel.getTexture().setReflectivity(5);
		tmodel.getTexture().setShineDamper(5);
		tmodel.getTexture().setHasTransparency(true);
		TexturedModel tmodel2 = new TexturedModel(model2, Texture.loadFromFile(new CustomFile("parts", "car.png"), 1024));
		tmodel2.getTexture().setHasTransparency(true);
		tmodel2.getTexture().setReflectivity(5);
		tmodel2.getTexture().setShineDamper(5);
		Entity ent = new Entity(tmodel);
		Entity ent2 = new Entity(tmodel2);
		Entity ent3 = new Entity(tmodel);
		ent.setLocalPosition(new Vector3f(1, 1.1f, -1.4f));
		ent.setPosition(new Vector3f(0, 1, -0.03f));
		ent.setRotation(new Vector3f(0, 0, 0));
		ent.setScale(new Vector3f(-1, 1, 1));
		ent2.setLocalPosition(new Vector3f(0, 0.1f, 0));
		ent2.setPosition(new Vector3f(0, 0, 0));
		ent2.setLocalRotation(new Vector3f(0, 0, 0));
		ent2.setRotation(new Vector3f(0, 0, 0));
		ent2.setScale(new Vector3f(1, 1, 1));
		ent3.setLocalPosition(new Vector3f(-1, 1.1f, -1.3f));
		ent3.setPosition(new Vector3f(0, 0, -1f));
		ent3.setLocalRotation(new Vector3f(0, 0, 0));
		ent3.setRotation(new Vector3f(0, 0, 0));
		ent3.setScale(new Vector3f(1, 1, 1));
		List<Entity> ents = new ArrayList<Entity>();
		ents.add(ent);
		ents.add(ent2);
		ents.add(ent3);
		Camera camera = new Camera();
		camera.setLocalPosition(new Vector3f(0, 2f, 0));
		camera.setPosition(new Vector3f(0, 2, 0));
		camera.setRotation(new Vector3f(0, 0, 0));
		camera.setPerspective((float)Math.toRadians(130), 1920/1080, 0.01f, 1000);
		Light light = new Light(new Vector3f(1, 1, 0.5f), new Vector3f(0, 100, 0));
		Texture tt = new Texture(new CustomFile("parts", "grass.jpg"), 1024);
		Texture tg = new Texture(new CustomFile("parts", "dirt.jpg"), 1024);
		Texture tb = new Texture(new CustomFile("parts", "bricks.jpg"), 1024);
		Texture tb2 = new Texture(new CustomFile("parts", "bricks.jpg"), 1024);
		Texture tbm = new Texture(new CustomFile("parts", "blendMap.jpg"), 1024);
		Texture hmap = new Texture(new CustomFile("parts", "heightMap.jpg"), 1024);
		TexturePack tp = new TexturePack(tt, tg, tb, tb2, tbm, hmap);
		Terrain terrain = new MappedTerrain(0, 0, loader, tp);
		Terrain terrain2 = new MappedTerrain(0, -1, loader, tp);
		Terrain terrain3 = new MappedTerrain(-1, -1, loader, tp);
		Terrain terrain4 = new MappedTerrain(-1, 0, loader, tp);
		Texture t = Texture.loadFromFile(new CustomFile("parts", "colors.jpg"), 1024);
		TerrainCollider tc = new TerrainCollider(terrain);
		TerrainCollider tc2 = new TerrainCollider(terrain2);
		TerrainCollider tc3 = new TerrainCollider(terrain3);
		TerrainCollider tc4 = new TerrainCollider(terrain4);
		terrain.setTerrainCollider(tc);
		terrain2.setTerrainCollider(tc2);
		terrain3.setTerrainCollider(tc3);
		terrain4.setTerrainCollider(tc4);
		List<Terrain> terrains = new ArrayList<Terrain>();
		terrains.add(terrain);
		terrains.add(terrain2);
		terrains.add(terrain3);
		terrains.add(terrain4);
		
		glfwSetErrorCallback(errorCallback = GLFWErrorCallback.createPrint(System.err));
		camera.setHasGravity(false);
		ent.setHasGravity(false);
		ent2.setHasGravity(false);
		ent3.setHasGravity(false);
		ActorGroup ag = new ActorGroup(new Car(ent2, ent3, ent), camera);
		AABBCollider collider = new AABBCollider(new Vector3f(1, 1f, 1), ag.getPosition(), 1, false);
		ag.setPosition(new Vector3f(0, 0f ,0));
		ag.setCollider(collider);
		ag.setMass(0.01f);
		InputMovable im = new InputMovable(ag);
		ag.setHasGravity(true);
		renderer.prepareBatch(ents, terrains);
		SyncTimer timer = new SyncTimer(SyncTimer.JAVA_NANO);
		while(!GLFW.glfwWindowShouldClose(window)) {
			timer.sync(70);
			GLFW.glfwPollEvents();
			GL11.glEnableClientState(GL11.GL_VERTEX_ARRAY);
			ag.update();
			im.move(window);
			//camera.setRotation((Vector3f) ent2.getRotation().clone());
			camera.aim(ag, window);
			//camera.getPosition().add(new Vector3f(0, 0, -0.001f));
			renderer.render(camera, light);
			GLFW.glfwSwapBuffers(window);
			//Window.updateWindow(window);
		}
		System.out.println(Terrain.getTerrains());
		Window.closeWindow(window);
		renderer.cleanUp();
		loader.cleanUp();
	}

}
