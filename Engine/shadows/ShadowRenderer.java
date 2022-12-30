package shadows;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.joml.AxisAngle4d;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;

import actors.Actor;
import actors.Camera;
import actors.Movable;
import actors.lights.Light;
import entity.Entity;
import game.Window;
import renderer.WorldRenderer;
import textures.Texture;
import util.Matrices;
import util.Vectors;

public class ShadowRenderer {
	
	private static final int SHADOW_MAP_SIZE = 4096;
	
	private ShadowShader shader = new ShadowShader();
	private HashMap<Light, List<Actor>> shadows = new HashMap<Light, List<Actor>>();
	private ShadowBuffers buffers;
	
	public ShadowRenderer(ShadowBuffers buffers) {
		this.buffers = buffers;
		ShadowBuffers.shadowTexture(getShadowMap());
	}
	
	public void render(Camera cam, List<Light> lights) {
		if(shadows.isEmpty()) return;
		buffers.shadowBuffer().bind();
		prepare();
		shader.setDepthMap();
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glCullFace(GL11.GL_FRONT_FACE);
		for(Light light : shadows.keySet()) {
			for(Actor actor : shadows.get(light)) {
				shader.setMvp(getMvpMatrix(cam, light, actor));
				actor.getVao().bindAttributes(0, 1);
				GL11.glDrawElements(GL11.GL_TRIANGLES, actor.getVao().getIndexCount(), GL11.GL_UNSIGNED_INT, 0);
				actor.getVao().unbindAttributes(0, 1);
			}
		}
		GL11.glCullFace(GL11.GL_BACK);
		shader.stop();
	}
	
	public void prepare() {
		GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);
		shader.start();
	}
	
	public static Matrix4f getMvpMatrix(Camera cam, Light light, Actor actor) {
		Matrix4f proj = new Matrix4f();
		proj = cam.getOrthographic();
		Matrix4f view = light.getTransformation().translate(cam.getPosition().negate(new Vector3f()));
		Matrix4f model = actor != null ? actor.getTransformation() : new Matrix4f().identity();
		return proj.mul(view, new Matrix4f()).mul(model);
	}
	
	public void prepareBatch(Light light, Actor actor) {
		if(shadows.containsKey(light)) {
			shadows.get(light).add(actor);
		}else {
			shadows.put(light, new ArrayList<Actor>() {{add(actor);}});
		}
	}
	
	public void prepareBatch(Light light, List<Actor> actors) {
		for(Actor actor : actors) {
			prepareBatch(light, actor);
		}
	}
	
	public Texture getShadowMap() {
		return buffers.shadowBuffer().getDepthTexture();
	}
	
	public void cleanUp() {
		shader.cleanUp();
		buffers.cleanUp();
	}

}
