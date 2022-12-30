package game;
import java.awt.Font;
import java.io.File;
import java.lang.ref.SoftReference;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.glfw.GLFW;

import actors.Actor;
import actors.ActorGroup;
import actors.Camera;
import actors.ImmutableMovable;
import actors.InputMovable;
import actors.lights.Light;
import advancedLighting.normalMap.NormalMapOBJFileLoader;
import baseComponents.ClippingPlane;
import baseComponents.Fbo;
import collision.BoxCollider;
import collision.PointBasedCollider;
import collision.TerrainCollider;
import entity.Entity;
import entity.ImmutableEntity;
import events.EventLobby;
import font.FontRenderer;
import font.GameFont;
import font.Text;
import gameLogic.Car;
import grass.GrassRenderer;
import gui.GUI;
import gui.GUIRenderer;
import sun.misc.Unsafe;
import line.Line;
import line.LineRenderer;
import models.Model;
import models.TexturedModel;
import mouse.MouseTracker;
import readers.OBJFileLoader;
import renderer.Loader;
import renderer.WorldRenderer;
import scene.SceneRenderer;
import shadows.ShadowBuffers;
import shadows.ShadowRenderer;
import skybox.Skybox;
import terrain.HeightMap;
import terrain.MappedTerrain;
import terrain.Terrain;
import textures.CubeMapTexture;
import textures.Texture;
import textures.TexturePack;
import util.CustomFile;
import water.WaterBuffers;
import water.WaterRenderer;
import water.WaterTile;
import world.time.WorldClock;

public class Main {

	private static float delta = 0;
	
    public static final Settings SETTINGS = new Settings();
	public static final SyncTimer TIMER = new SyncTimer(SyncTimer.JAVA_NANO);
	public static final EventLobby EVENT_LOBBY = new EventLobby();
	public static final WorldClock CLOCK = new WorldClock();
	
	public static void main(String[] args) throws Exception {
		Window window = new Window(1792, 1011, "hi");
		Loader loader = new Loader();
		CLOCK.reset();
		final Skybox DAY_DEFAULT = new Skybox(loader, CubeMapTexture.loadFromFile(new CustomFile[] {
				new CustomFile("Skybox Images", "right.png"),
				new CustomFile("Skybox Images", "left.png"),
				new CustomFile("Skybox Images", "top.png"),
				new CustomFile("Skybox Images", "bottom.png"),
				new CustomFile("Skybox Images", "back.png"),
				new CustomFile("Skybox Images", "front.png"),
				}, 1024));
		final Skybox NIGHT_DEFAULT = new Skybox(loader, CubeMapTexture.loadFromFile(new CustomFile[] {
				new CustomFile("Skybox Images", "nightRight.png"),
				new CustomFile("Skybox Images", "nightLeft.png"),
				new CustomFile("Skybox Images", "nightTop.png"),
				new CustomFile("Skybox Images", "nightBottom.png"),
				new CustomFile("Skybox Images", "nightBack.png"),
				new CustomFile("Skybox Images", "nightFront.png"),
				}, 1024));
		Model model = OBJFileLoader.loadOBJ(new CustomFile("parts", "wheel.obj"), loader);
		model.shouldRender(true);
		Model model2 = OBJFileLoader.loadOBJ(new CustomFile("parts", "car.obj"), loader);
		model2.shouldRender(true);
		Model model3 = OBJFileLoader.loadOBJ(new CustomFile("parts", "house.obj"), loader);
		model3.shouldRender(true);
		Model model4 = OBJFileLoader.loadOBJ(new CustomFile("parts", "wallsOfTime.obj"), loader);
		model4.shouldRender(true);
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
		TexturedModel twall = new TexturedModel(model4, Texture.loadFromFile(new CustomFile("parts", "Rock020_2K_Color.jpg"), 1024));
		thouse.getTexture().setReflectivity(0);
		thouse.getTexture().setShineDamper(0);
		thouse.getTexture().setHasTransparency(true);
		twall.getTexture().setReflectivity(0);
		twall.getTexture().setShineDamper(0);
		Entity ent = new Entity(tmodel);
		Entity ent2 = new Entity(tmodel2);
		Entity ent3 = new Entity(tmodel);
		Entity house = new Entity(thouse);
		Entity wall = new Entity(twall);
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
		house.setPosition(new Vector3f(0, 0, 0));
		house.setRotation(new Vector3f(0, 0, 0));
		house.setScale(new Vector3f(5));
		wall.setPosition(new Vector3f(0, 10, 0));
		wall.setScale(new Vector3f(1));
		BoxCollider houseCollider = new BoxCollider(new Vector3f(5, 2f, 5), house, 1, true);
		house.setCollider(houseCollider);
		house.setMass(1f);
		house.move(new Vector3f(0, 10, 0));
		house.getCollider().setImmobile(true);
		house.setDebug(true);
		List<Entity> ents = new ArrayList<Entity>();
		ents.add(ent);
		ents.add(ent2);
		ents.add(ent3);
		ents.add(house);
		ents.add(wall);
		Camera camera = new Camera();
		camera.setLocalPosition(new Vector3f(0, 2f, 0));
		camera.setPosition(new Vector3f(0, 2, 0));
		camera.setRotation(new Vector3f(0, 0, 0));
		camera.setPerspective((float)Math.toRadians(110), ((float) window.getFramebufferWidth())/ ((float) window.getFramebufferHeight()), 0.001f, 1000f);
		camera.setOrthographic(-100, 100, -100, 100, -100, 200);
		Light light = new Light(new Vector3f(1, 1, 0.9f), new Vector3f(-1000, 1000, 1000), new Vector3f(1f, 0f, 0f));
		//Light light2 = new Light(new Vector3f(0, 0, 0f), new Vector3f(100, 10, 0));
		List<Light> lights = new ArrayList<Light>();
		lights.add(light);
		//lights.add(light2);
		Texture tt = Texture.loadFromFile(new CustomFile("parts", "grass.jpg"), 128);
		Texture tg = Texture.loadFromFile(new CustomFile("parts", "dirt.jpg"), 128);
		Texture tb = Texture.loadFromFile(new CustomFile("parts", "bricks.jpg"), 128);
		Texture tb2 = Texture.loadFromFile(new CustomFile("parts", "bricks.jpg"), 128);
		Texture tbm = Texture.loadFromFile(new CustomFile("parts", "blendMap.jpg"), 128);
		Texture hmap = Texture.loadFromFile(new CustomFile("parts", "heightmap.png"), 128);
		TexturePack tp = new TexturePack(tt, tg, tb, tb2, tbm, hmap);
		HeightMap hm = new HeightMap();
		hm.setModel(hm.generateHeights(loader, hmap, Terrain.SIZE));
		Terrain terrain = new MappedTerrain(0, 0, loader, tp, hm);
		Terrain terrain2 = new MappedTerrain(0, -1, loader, tp, hm);
		Terrain terrain3 = new MappedTerrain(-1, -1, loader, tp, hm);
		Terrain terrain4 = new MappedTerrain(-1, 0, loader, tp, hm);
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
		camera.setHasGravity(false);
		ent.setHasGravity(false);
		ent2.setHasGravity(false);
		ent3.setHasGravity(false);
		house.setHasGravity(true);
		ActorGroup ag = new ActorGroup(new Car(ent2, ent3, ent), camera);
		BoxCollider collider = new BoxCollider(new Vector3f(2, 1f, 2.5f), ag, 1, false);
		ag.matchPosition(new Vector3f(0));
		ag.setCollider(collider);
		ag.move(new Vector3f(0, 0, 0));
		ag.setMass(1f);
		ag.setHasGravity(true);
		//GUIS TEST
		InputMovable im = new InputMovable(ag);
		ImmutableEntity ho = new ImmutableEntity(thouse, ag);
		Texture socuwan = Texture.loadFromFile(new CustomFile("parts", "newArtIdea.jpg"), 1024);
		GUI gui1 = new GUI(loader, new Vector2f(0.5f, 0.5f), new Vector2f(0.2f, 0.25f), socuwan);
		gui1.shouldRender(true);
		List<GUI> guis = new ArrayList<GUI>();
		guis.add(gui1);
		//LINES
		
		Line line = new Line(loader, new Vector3f(0f, -10, 0), new Vector3f(0f, 10, 0), new Vector3f(0, 50, 100));
		line.setPosition(new Vector3f(0, 0, 0));
		line.setRotation(new Vector3f(0, 0, 0));
		line.setScale(new Vector3f(1, 1, 1));
		List<Line> lines = new ArrayList<Line>();
		lines.add(line);
		//SLR TEST
		//LINE TEST
		MouseTracker mp = new MouseTracker();
		Texture dudvMap = Texture.loadFromFile(new CustomFile("WaterTex", "vornoi.png"), 1024);
		Texture waterNormal = Texture.loadFromFile(new CustomFile("WaterTex", "vornoiNoise.png"), 1024);
		Texture normMap = Texture.loadFromFile(new CustomFile("NormalMapped", "barrelNormal.png"), 1024);
		

		Model normaled = NormalMapOBJFileLoader.loadOBJ(new CustomFile("NormalMapped", "barrel.obj"), loader);
		normaled.shouldRender(true);
		TexturedModel tm = new TexturedModel(normaled, Texture.loadFromFile(new CustomFile("NormalMapped", "barrel.png"), 1024));
		tm.getAdditionalTextures().addTexture(normMap);
		tm.getTexture().setReflectivity(1);
		tm.getTexture().setShineDamper(100);
		
		Entity nm = new Entity(tm);
		nm.setPosition(new Vector3f(0, 10, 0));
		nm.setScale(new Vector3f(10));
		
		List<Entity> normalEntities = new ArrayList<Entity>();
		normalEntities.add(nm);
		
		Model quad = OBJFileLoader.loadOBJ(new CustomFile("parts", "tree_obj.obj"), loader);
		TexturedModel tquad = new TexturedModel(quad, Texture.loadFromFile(new CustomFile("parts", "tree_obj_uv.png"), 1024));
		Entity e1 = new Entity(tquad);
		e1.move(new Vector3f(5, 5, 0));
		Entity e2 = new Entity(tquad);
		e2.move(new Vector3f(5, 5, 5));
		Entity e3 = new Entity(tquad);
		e3.move(new Vector3f(5, 5, 10));
		Entity e4 = new Entity(tquad);
		e4.move(new Vector3f(5, 5, 15));
		Entity e5 = new Entity(tquad);
		e5.move(new Vector3f(5, 5, 20));
		List<Entity> tents = new ArrayList<Entity>();
		tents.add(e1);
		tents.add(e2);
		tents.add(e3);
		tents.add(e4);
		tents.add(e5);
		quad.shouldRender(true);
		tquad.getTexture().setHasTransparency(true);
		tquad.getTexture().setReflectivity(1);
		tquad.getTexture().setShineDamper(10);
		quad.getVao().unbindVao();
		//GRASS TEST
		Model gmodel = OBJFileLoader.loadOBJ(new CustomFile("parts", "bush.obj"), loader);
		TexturedModel tgmodel = new TexturedModel(gmodel, null);
		List<Entity> bushes = new ArrayList<Entity>();
		//FBO TEST
		ShadowBuffers sb = new ShadowBuffers(window, 1024, 1024);
		WaterBuffers wb = new WaterBuffers(window);
		ClippingPlane cp = new ClippingPlane(new Vector4f(0, 1, 0, -5));
		WaterTile tile = new WaterTile(loader, 50, -5, 50, new Vector2f(100, 100));
		List<WaterTile> tiles = new ArrayList<WaterTile>();
		tiles.add(tile);
		//RENDERERS
		FontRenderer fr = new FontRenderer();
		ShadowRenderer sr = new ShadowRenderer(sb); // RENDERS TO SHADOW MAP ONLY
		WaterRenderer wr = new WaterRenderer(wb, dudvMap, waterNormal); // RENDERS TO WATER FL AND FR TEXTURES ONLY
		WorldRenderer renderer = createFinalRenderer(window, null, null, null, wr, null); // RENDERS ALL
		GameFont gf = new GameFont("Arial", "png", Font.PLAIN, 48);
		gf.createAsResource();
		//Texture tf = gf.getTexture();//Texture.loadFromImage(gf.createFontAtlas(new File("/Users/huangyoulin/Desktop/DataOutput.png")));
		/*GUI gui = new GUI(loader, new Vector2f(0.5f, 0.5f), new Vector2f(0.25f, 0.25f), 
				sr.getShadowMap()); // DEPTH MAP VISUALIZER
		gui.shouldRender(true);
		guis.add(gui);*/
		renderer.setProjection(camera); // SEND PROJECTION MATRIX TO ALL RENDERERS
		List<Actor> actors = new ArrayList<Actor>();
		actors.addAll(ents);
		actors.addAll(normalEntities);
		actors.addAll(terrains);
		sr.prepareBatch(lights.get(0), actors); // PREPARE SHADOWS FOR LIGHT-0
		ShadowBuffers.shadowTexture(sr.getShadowMap()); // LINKS SHADOW MAP AS SHADOW TEXTURE
		tents.clear();
		renderer.prepareBatch(ents, normalEntities, tents, terrains, tiles, guis, lines, bushes); // PREPARES ALL OBJECTS TO RENDER
		fr.prepareBatch(new ArrayList<Text>() {{add(new Text(loader, "Hello Friend", new Vector2f(0f, 0.5f), new Vector2f(0.1f ,0.125f), gf, new Vector3f(1, 1, 0)));}});
		tents.clear();
		System.gc();
		Vector3f treeSize = new Vector3f(3);
		wall.setPosition(((PointBasedCollider) house.getCollider()).getCorners()[0]);
		while(!GLFW.glfwWindowShouldClose(window.getId())) {
			int sync = TIMER.sync(70);
			delta = sync;
			for(int i = 0; i < delta; i++) {
				house.update();
				ag.update();
				if(ag.getCollider().containsExec(house)) {
					ag.getCollider().reactToCollision(house.getCollider());
					ag.getCollider().getCollision().removeCollision(house.getCollider());
				}
				im.move(window);
				camera.aim(window);
				line.setBegin(light.getPosition());
				if(GLFW.glfwGetMouseButton(window.getId(), GLFW.GLFW_MOUSE_BUTTON_LEFT) == GLFW.GLFW_PRESS) {
					Vector3f mousePos = mp.getPointedPosition(window, camera);
					/*Entity tree = new Entity(tquad);
					tree.setScale(treeSize);
					tree.setPosition(mousePos);
					renderer.getSceneRenderer().getInstancedRenderer().prepareBatch(tree);*/
					Entity bush = new Entity(tgmodel);
					bush.setScale(new Vector3f(5));
					bush.setPosition(mousePos);
					renderer.getGrassRenderer().prepareBatch(bush);
				}
				line.setEnd(((PointBasedCollider) house.getCollider()).getCorners()[0]);
				line.resetModel(loader);
				CLOCK.advance();
			}
			//CLIPPING PLANE BEGINS
			cp.apply();
			sr.render(camera, lights); //RENDER TO SHADOW MAP
			renderWaterTexture(wb, cp, -5, renderer.getSceneRenderer(), camera, lights, DAY_DEFAULT, NIGHT_DEFAULT); //RENDER BOTH WATER TEXTURES FL AND FR
			cp.cease();
			//CLIPPING PLANE STOPS - RESETS TO 0
			Fbo.unbind(window); 
			renderer.render(camera, lights, DAY_DEFAULT, NIGHT_DEFAULT); //RENDER WORLD
			fr.render(camera);
			window.update();
			GLFW.glfwSwapInterval(0);
		}
		System.out.println(Terrain.getTerrains());
		renderer.cleanUp();
		wr.cleanUp();
		loader.cleanUp();
		Texture.cleanUp();
		sr.cleanUp();
		fr.cleanUp();
		window.close();
	}
	
	public static float getDeltaCycles() {
		return delta;
	}
	
	public static SyncTimer getTimer() {
		return TIMER;
	}
	
	public static WorldRenderer createFinalRenderer(Window window, SceneRenderer scr, GUIRenderer gr, LineRenderer lr, WaterRenderer wr, GrassRenderer gar) {
		scr = scr != null ? scr : new SceneRenderer();
		gr = gr != null ? gr : new GUIRenderer();
		lr = lr != null ? lr : new LineRenderer();
		gar = gar != null ? gar : new GrassRenderer();
		WorldRenderer renderer = new WorldRenderer(window, scr, gr, lr, wr, gar);
		return renderer;
	}
	
	public static void renderWaterTexture(WaterBuffers buffer, ClippingPlane cp, float waterHeight, SceneRenderer renderer, Camera camera, List<Light> lights, Skybox present, Skybox future) {
		renderWaterReflection(buffer, cp, waterHeight, renderer, camera, lights, present, future);
		renderWaterRefraction(buffer, cp, waterHeight, renderer, camera, lights, present, future);
	}
	
	public static void renderWaterReflection(WaterBuffers buffer, ClippingPlane cp, float waterHeight, SceneRenderer renderer, Camera camera, List<Light> lights, Skybox present, Skybox future) {
		buffer.reflection().bind();
		cp.plane().set(0, 1, 0, -waterHeight + 1);
		float distance = camera.getPosition().y - waterHeight;
		camera.getPosition().sub(0, distance * 2, 0);
		camera.getLocalRotation().x = -camera.getLocalRotation().x;
		renderer.render(camera, lights, present, future, cp.plane());
		camera.getPosition().add(0, distance * 2, 0);
		camera.getLocalRotation().x = -camera.getLocalRotation().x;
	}
	
	public static void renderWaterRefraction(WaterBuffers buffer, ClippingPlane cp, float waterHeight, SceneRenderer renderer, Camera camera, List<Light> lights, Skybox present, Skybox future) {
		buffer.refraction().bind();
		cp.plane().set(0, -1, 0, waterHeight + 1);
		renderer.render(camera, lights, present, future, cp.plane());
	}
	
	private static List<Entity> copy(TexturedModel tm){
		List<Entity> tents = new ArrayList<Entity>();
		for(int i = 0; i < 10000; i++) {
			Entity et = new Entity(tm);
			et.rotate(new Vector3f(0, i/180, i));
			et.move(new Vector3f(i, 0 , i/2));
			tents.add(et);
		}
		return tents;
	}
	

}
