package grass;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.lwjgl.opengl.GL11;

import actors.Camera;
import baseComponents.Vao;
import entity.Entity;
import renderer.WorldRenderer;

public class GrassRenderer {
	
	private GrassShader grass = new GrassShader();
	private List<Entity> batch = new ArrayList<Entity>();
	
	public void render(Camera cam) {
		prepare(cam);
		WorldRenderer.disableCulling();
		for(Entity rt : batch) {
			Vao model = rt.getVao();
			model.bindAttributes(0);
			grass.setModel(rt.getTransformation());
			GL11.glDrawElements(GL11.GL_TRIANGLES, rt.getVao().getIndexCount(), GL11.GL_UNSIGNED_INT, 0);
			model.unbindAttributes(0);
		}
		WorldRenderer.enableCulling();
		finish();
	}
	
	public void setProjection(Camera camera) {
		grass.start();
		grass.setProjection(camera.getProjection());
		grass.stop();
	}
	
	public void prepareBatch(Entity bush) {
		batch.add(bush);
	}
	
	public void prepareBatch(Collection<Entity> entities) {
		batch.addAll(entities);
	}
	
	public void prepare(Camera cam) {
		grass.start();
		grass.setView(cam.getTransformation());
	}
	
	public void finish() {
		grass.stop();
	}
	
	public void cleanUp() {
		grass.cleanUp();
	}

}
