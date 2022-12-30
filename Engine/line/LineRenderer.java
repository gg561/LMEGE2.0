package line;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.lwjgl.opengl.GL11;

import actors.Camera;
import renderer.WorldRenderer;

public class LineRenderer {
	
	private LineShader shader = new LineShader();
	
	private List<Line> batch = new ArrayList<Line>();
	
	public LineRenderer() {
		
	}
	
	public void render(Camera camera) {
		if(batch.isEmpty()) return;
		prepare(camera);
		for(Line line : batch) {
			prepareInstance(line);
			GL11.glDrawArrays(GL11.GL_LINES, 0, line.getModel().getVao().getIndexCount());
			unbindInstance(line);
		}
		finish();
	}
	
	public void prepareBatch(Line line) {
		batch.add(line);
	}
	
	public void prepareBatch(Collection<Line> lines) {
		batch.addAll(lines);
	}
	
	public void setProjection(Camera camera) {
		shader.start();
		shader.setProjection(camera);
		shader.stop();
	}
	
	private void prepare(Camera camera) {
		shader.start();
		shader.setView(camera);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		WorldRenderer.disableCulling();
	}
	
	private void prepareInstance(Line line) {
		line.getModel().getVao().bindAttributes(0);
		shader.loadModel(line.getTransformation());
		shader.loadColor(line.getColor());
	}
	
	private void unbindInstance(Line line) {
		line.getModel().getVao().unbindAttributes(0);
	}
	
	private void finish() {
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		WorldRenderer.enableCulling();
		shader.stop();
	}
	
	public void cleanUp() {
		shader.cleanUp();
	}

}
