package water;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;

import actors.Camera;
import actors.lights.Light;
import baseComponents.ClippingPlane;
import game.Window;
import renderer.Renderer;
import renderer.WorldRenderer;
import textures.Texture;
import textures.TextureStaticAssets;
import util.CustomFile;

public class WaterRenderer implements Renderer {
	
	public static final float WATER_HEIGHT = -5;
	
	private Texture dudvMap;
	private Texture normalMap;
	private Texture depthMap;
	
	private WaterShader shader = new WaterShader();
	private WaterBuffers buffers;
	
	private List<WaterTile> batches = new ArrayList<WaterTile>();
	
	public WaterRenderer(WaterBuffers buffers, Texture dudvMap, Texture normalMap) {
		this.buffers = buffers;
		depthMap = buffers.refraction().getDepthTexture();
		this.dudvMap = dudvMap;
		this.normalMap = normalMap;
	}
	
	public void setProjection(Camera camera) {
		shader.start();
		shader.setProjection(camera.getProjection());
		shader.stop();
	}
	
	public void render(Camera camera, List<Light> lights, boolean doFog) {
		shader.start();
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		shader.loadTextures();
		shader.setView(camera.getTransformation());
		shader.setLight(lights);
		shader.setDistortionMove();
		prepareTexture();
		for(WaterTile tile : batches) {
			shader.setModel(tile.getTransformation());
			shader.setTileSize(tile.size());
			tile.model().getVao().bindAttributes(0);
			GL15.glDrawArrays(GL11.GL_TRIANGLES, 0, tile.model().getVao().getIndexCount());
			tile.model().getVao().unbindAttributes(0);
		}
		GL11.glDisable(GL11.GL_BLEND);
		shader.stop();
	}
	
	public void prepareTexture() {
		buffers.reflection().getTexture().bind();
		buffers.refraction().getTexture().bind(1);
		dudvMap.bind(2);
		normalMap.bind(3);
		depthMap.bind(4);
	}
	
	public void prepareBatch(List<WaterTile> tiles) {
		batches.addAll(tiles);
	}
	
	public void cleanUp() {
		
		shader.cleanUp();
		batches.clear();
		buffers.cleanUp();
	}

}
