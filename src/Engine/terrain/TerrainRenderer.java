package Engine.terrain;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;

import Engine.actors.Camera;
import Engine.actors.Light;
import Engine.baseComponents.Vao;
import Engine.entity.Entity;
import Engine.models.TexturedModel;

public class TerrainRenderer {
	
	private TerrainShader shader = new TerrainShader();
	
	private List<Terrain> terrains = new ArrayList<Terrain>();
	
	public void render(Camera cam, Light light) {
		prepare(cam, light);
		for(Terrain terrain : terrains) {
			prepareTerrain(terrain);
			GL11.glDrawElements(GL11.GL_TRIANGLES, terrain.getModel().getVao().getIndexCount(), GL11.GL_UNSIGNED_INT, 0);
			unbindTerrain(terrain);
		}
		finish();
	}
	
	public void prepare(Camera cam, Light light) {
		shader.start();
		shader.projMatrix.loadMatrix(cam.getProjection());
		shader.viewMatrix.loadMatrix(cam.getTransformation());
		shader.lightPos.loadVector3(light.getPosition());
		shader.lightCol.loadVector3(light.getColor());
	}
	
	private void finish() {
		shader.stop();
	}
	
	private void unbindTerrain(Terrain terrain) {
		terrain.getModel().getVao().unbindAttributes(0, 1, 2);
	}
	
	private void prepareTerrain(Terrain terrain) {
		shader.reflectivity.loadFloat(terrain.getTexture().getReflectivity());
		shader.shineDamper.loadFloat(terrain.getTexture().getShineDamper());
		shader.tiles.loadFloat(terrain.getTiles());
		terrain.getTexture().bind();
		Vao model = terrain.getModel().getVao();
		model.bindAttributes(0, 1, 2);
		shader.modelMatrix.loadMatrix(terrain.getTransformation());
		shader.texture.loadSampler(0);
	}
	
	public void prepareBatch(List<Terrain> terrains) {
		this.terrains.addAll(terrains);
	}

}
