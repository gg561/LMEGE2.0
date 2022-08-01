package game;
import static org.lwjgl.glfw.GLFW.glfwSetErrorCallback;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.joml.Vector2f;
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
import collision.BoxCollider;
import collision.TerrainCollider;
import entity.Entity;
import entity.EntityRenderer;
import gameLogic.Car;
import generator.Populator;
import gui.GUI;
import gui.GUIRenderer;
import line.Line;
import line.LineRenderer;
import models.Model;
import models.TexturedModel;
import readers.OBJFileLoader;
import readers.SimpleLanguageReader;
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
    public static long mainWindow;
	
	public static void main(String[] args) throws Exception {
		long window = Window.createWindow(1920, 1080, "hi");
		mainWindow = window;
		Loader loader = new Loader();
		BaseRenderer renderer = new BaseRenderer();
		Texture fern = Texture.loadFromFile(new CustomFile("parts", "atlasFern.png"), 128);
		Model model = OBJFileLoader.loadOBJ("parts/wheel", loader);
		model.shouldRender(true);
		Model model2 = OBJFileLoader.loadOBJ("parts/car", loader);
		model2.shouldRender(true);
		Model model3 = OBJFileLoader.loadOBJ("parts/house", loader);
		model3.shouldRender(true);
		Texture ta = Texture.loadFromFile(new CustomFile("parts", "car.png"), 1024);
		TexturedModel tmodel = new TexturedModel(model, ta);
		tmodel.getTexture().setReflectivity(0);
		tmodel.getTexture().setShineDamper(1);
		tmodel.getTexture().setHasTransparency(true);
		TexturedModel tmodel2 = new TexturedModel(model2, ta);
		tmodel2.setTextureIndex(0);
		tmodel2.getTexture().setHasTransparency(true);
		tmodel2.getTexture().setReflectivity(1);
		tmodel2.getTexture().setShineDamper(1);
		TexturedModel thouse = new TexturedModel(model3, Texture.loadFromFile(new CustomFile("parts", "house.png"), 1024));
		//TexturedModel thouse = new TexturedModel(model3, socuwan);
		thouse.getTexture().setReflectivity(0);
		thouse.getTexture().setShineDamper(0);
		thouse.getTexture().setHasTransparency(true);
		//test only
		//TextureAtlas ta = new TextureAtlas(new CustomFile("test"), 1024, 1);
		//TexturedModel tmodelt = new TexturedModel(model2, ta, 0);
		Entity ent = new Entity(tmodel);
		Entity ent2 = new Entity(tmodel2);
		Entity ent3 = new Entity(tmodel);
		Entity house = new Entity(thouse);
		Entity d1 = new Entity(tmodel);
		Entity d2 = new Entity(tmodel);
		Entity d3 = new Entity(tmodel);
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
		house.setPosition(new Vector3f(50, 30, -50));
		house.setRotation(new Vector3f(0, 0, 0));
		house.setScale(new Vector3f(5, 5, 5));
		BoxCollider houseCollider = new BoxCollider(new Vector3f(5, 1f, 5), house, 1, false);
		house.setCollider(houseCollider);
		house.setMass(0.5f);
		house.getCollider().setImmobile(true);
		house.setDebug(true);
		List<Entity> ents = new ArrayList<Entity>();
		ents.add(ent);
		ents.add(ent2);
		ents.add(ent3);
		ents.add(house);
		ents.add(d1);
		ents.add(d2);
		ents.add(d3);
		Camera camera = new Camera();
		camera.setLocalPosition(new Vector3f(0, 2f, 0));
		camera.setPosition(new Vector3f(0, 2, 0));
		camera.setRotation(new Vector3f(0, 0, 0));
		camera.setPerspective((float)Math.toRadians(130), 1920/1080, 0.001f, 1000);
		Light light = new Light(new Vector3f(2, 2, 2f), new Vector3f(-10, 10, 0), new Vector3f(1f, 0f, 0f));
		Light light2 = new Light(new Vector3f(0, 0, 0f), new Vector3f(100, 10, 0));
		List<Light> lights = new ArrayList<Light>();
		lights.add(light);
		//lights.add(light2);
		Texture tt = Texture.loadFromFile(new CustomFile("parts", "grass.jpg"), 128);
		Texture tg = Texture.loadFromFile(new CustomFile("parts", "dirt.jpg"), 128);
		Texture tb = Texture.loadFromFile(new CustomFile("parts", "bricks.jpg"), 128);
		Texture tb2 = Texture.loadFromFile(new CustomFile("parts", "bricks.jpg"), 128);
		Texture tbm = Texture.loadFromFile(new CustomFile("parts", "blendMap.jpg"), 128);
		Texture hmap = Texture.loadFromFile(new CustomFile("parts", "heightMap.jpg"), 128);
		TexturePack tp = new TexturePack(tt, tg, tb, tb2, tbm, hmap);
		Terrain terrain = new MappedTerrain(0, 0, loader, tp);
		Terrain terrain2 = new MappedTerrain(0, -1, loader, tp);
		Terrain terrain3 = new MappedTerrain(-1, -1, loader, tp);
		Terrain terrain4 = new MappedTerrain(-1, 0, loader, tp);
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
		house.setHasGravity(true);
		ActorGroup ag = new ActorGroup(new Car(ent2, ent3, ent), camera);
		BoxCollider collider = new BoxCollider(new Vector3f(2, 1f, 2.5f), ag, 1, false);
		ag.setPosition(new Vector3f(0, 0f ,0));
		ag.setCollider(collider);
		ag.setMass(1f);
		InputMovable im = new InputMovable(ag);
		ag.setHasGravity(true);
		List<Entity> copies = Arrays.asList(Populator.randomPopulate(house, 50));
		ents.addAll(copies);
		//Atlas test
		fern.setRows(2);
		fern.setHasTransparency(true);
		Model fernModel = OBJFileLoader.loadOBJ("parts/plane", loader);
		fernModel.shouldRender(true);
		TexturedModel texturedFern = new TexturedModel(fernModel, fern);
		texturedFern.setTextureIndex(3);
		texturedFern.setUseFakeLighting(true);
		Entity entFern = new Entity(texturedFern);
		entFern.setPosition(new Vector3f(0, 10, 0));
		//
		ents.add(entFern);
		//GUIS TEST
		Texture socuwan = Texture.loadFromFile(new CustomFile("parts", "newArtIdea.jpg"), 128);
		GUI gui1 = new GUI(loader, new Vector2f(0.5f, 0.5f), new Vector2f(0.2f, 0.25f), socuwan);
		Texture placeholder = Texture.loadFromFile(new CustomFile("parts", "socuwan.png"), 128);
		gui1.shouldRender(true);
		List<GUI> guis = new ArrayList<GUI>();
		guis.add(gui1);
		//LINES
		Line line = new Line(loader, new Vector3f(0f, -10, 0), new Vector3f(0f, 10, 0), new Vector3f(0, 0, 100));
		line.setPosition(new Vector3f(0, 0, 0));
		line.setRotation(new Vector3f(0, 0, 0));
		line.setScale(new Vector3f(1, 1, 1));
		List<Line> lines = new ArrayList<Line>();
		lines.add(line);
		renderer.prepareBatch(ents, terrains, guis, lines);
		//SLR TEST
		//LINE TEST
		SyncTimer timer = new SyncTimer(SyncTimer.JAVA_NANO);
		/*BOX SHADOWING DEBUG
		house.setMass(0);
		Vector3f oldpos = new Vector3f(house.getPosition());
		System.out.println(oldpos);*/
		while(!GLFW.glfwWindowShouldClose(window)) {
			timer.sync(70);
			GLFW.glfwPollEvents();
			GL11.glEnableClientState(GL11.GL_VERTEX_ARRAY);
			ag.update();
			house.update();
			if(ag.getCollider().containsExec(house)) {
				ag.getCollider().reactToCollision(house.getCollider());
				ag.getCollider().getCollision().removeCollision(house.getCollider());
			}
			/*BOX SHADOWING DEBUG
			System.out.println(house.getCollider());
			System.out.println("FIRST CALL " + house.getPosition());
			*/
			/*BOX SHADOWING PROBLEM
			int cycles = 0;
			for(Entity enti : copies) {
				if(enti.getCollider() != null) {
					System.out.println(enti.getCollider());
					enti.update();
					if(house.getPosition().y != oldpos.y) {
						throw new RuntimeException(cycles + " " +  enti + " " + house.getPosition() + " " + oldpos + " " + enti.getCollider().getBounded() + " " + house.getCollider().getBounded() + " " + enti.getPosition() + " " + house.getPosition());
					}
					if(enti.getCollider().getBounded() == house.getCollider().getBounded()) {
						//System.out.println("SCND TERR " + ents);
						//System.out.println("SCND CALL " + house.getPosition());
					}
					//System.out.println(enti.getCollider().getBounds());
					//enti.getCollider().setPosition(enti.getPosition());
					if(ag.getCollider().containsExec(enti)) {
						ag.getCollider().reactToCollision(enti.getCollider());
						ag.getCollider().getCollision().removeCollision(enti.getCollider());
					}
				}
				cycles++;
			}*/
			//System.out.println("IND HOUS " + ind);
			line.setBegin(ents.get(0).getPosition());
			line.setEnd(light.getPosition());
			line.resetModel(loader);
			im.move(window);
			d1.setPosition(((BoxCollider) ag.getCollider()).getCorners()[0]);
			d2.setPosition(((BoxCollider) ag.getCollider()).getCorners()[3]);
			d3.setPosition(((BoxCollider) ag.getCollider()).getCorners()[7]);
			//camera.setRotation((Vector3f) ent2.getRotation().clone());
			camera.aim(ag, window);
			//camera.getPosition().add(new Vector3f(0, 0, -0.001f));
			renderer.render(camera, lights);
			GLFW.glfwSwapBuffers(window);
			//Window.updateWindow(window);
		}
		System.out.println(Arrays.toString(((BoxCollider) ag.getCollider()).getCorners()));
		System.out.println(Terrain.getTerrains());
		Window.closeWindow(window);
		renderer.cleanUp();
		loader.cleanUp();
	}

}
