package Engine.entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;

import Engine.actors.Camera;
import Engine.actors.Light;
import Engine.baseComponents.Vao;
import Engine.models.TexturedModel;
import Engine.renderer.BaseRenderer;
import Engine.textures.Texture;

public class EntityRenderer {
	
	private EntityShader shader;
	private HashMap<TexturedModel, List<Entity>> batches = new HashMap<TexturedModel, List<Entity>>();
	
	public EntityRenderer() {
		this.shader = new EntityShader();
	}
	
	public void render(Camera cam, Light light) {
		prepare(cam, light);
		for(TexturedModel texture : batches.keySet()) {
			prepareSkin(texture);
			if(texture.getTexture().isHasTransparency()) {
				BaseRenderer.disableCulling();
			}
			for(Entity ent : batches.get(texture)) {
				if(ent.getModel().getModel().shouldRender()) {
					prepareInstance(ent);
					GL11.glDrawElements(GL11.GL_TRIANGLES, ent.getModel().getModel().getVao().getIndexCount(), GL11.GL_UNSIGNED_INT, 0);
				}
			}
			unbindSkin(texture);
			BaseRenderer.enableCulling();
		}
		finish();
	}
	
	private void prepare(Camera camera, Light light) {
		shader.start();
		shader.projMatrix.loadMatrix(camera.getProjection());
		shader.viewMatrix.loadMatrix(camera.getTransformation());
		shader.lightPos.loadVector3(light.getPosition());
		shader.lightCol.loadVector3(light.getColor());
	}
	
	private void prepareInstance(Entity ent) {
		shader.modelMatrix.loadMatrix(ent.getTransformation());
		shader.texture.loadSampler(0);
	}
	
	private void unbindSkin(TexturedModel texture) {
		texture.getModel().getVao().unbindAttributes(0, 1, 2);
	}
	
	private void prepareSkin(TexturedModel texture) {
		shader.reflectivity.loadFloat(texture.getTexture().getReflectivity());
		shader.shineDamper.loadFloat(texture.getTexture().getShineDamper());
		if(texture.isUseFakeLighting()) {
			shader.useFakeLighting.loadBoolean(texture.isUseFakeLighting());
		}
		texture.getTexture().bind();
		Vao model = texture.getModel().getVao();
		model.bindAttributes(0, 1, 2);
	}
	
	private void prepareBatch(Entity entity) {
		if(batches.containsKey(entity.getModel().getTexture())) {
			batches.get(entity.getModel().getTexture()).add(entity);
		}else {
			batches.put(entity.getModel(), new ArrayList<Entity>() {{add(entity);}});
		}
	}
	
	public void prepareBatch(List<Entity> ents) {
		for(Entity ent : ents) {
			prepareBatch(ent);
		}
	}
	
	private void finish() {
		shader.stop();
	}
	
	public void cleanUp() {
		shader.cleanUp();
	}

}
