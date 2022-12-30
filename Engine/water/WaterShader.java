package water;

import java.util.List;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

import actors.lights.Light;
import game.Main;
import shaders.ShaderProgram;
import shaders.UniformArray;
import shaders.UniformFloat;
import shaders.ObjectMap;
import shaders.UniformMatrix;
import shaders.UniformSampler;
import shaders.UniformVector;
import util.CustomFile;

public class WaterShader extends ShaderProgram {
	
	private static final CustomFile VERTEX_SHADER = new CustomFile("water", "Vertex.glsl");
	private static final CustomFile FRAGMENT_SHADER = new CustomFile("water", "Fragment.glsl");
	
	private static final float DISTORTION_MOVE = 0.2f;
	
	private float distortion_move = 0;

	public WaterShader() {
		super(VERTEX_SHADER, FRAGMENT_SHADER, "in_position");
		// TODO Auto-generated constructor stub
		super.storeAllUniformToMap();
		super.storeAllUniformLocations(p().shaders());
	}
	
	public void setProjection(Matrix4f projection) {
		p().v.matrix("projection").loadMatrix(projection);
	}
	
	public void setView(Matrix4f view) {
		p().v.matrix("view").loadMatrix(view);
	}
	
	public void setModel(Matrix4f model) {
		p().v.matrix("model").loadMatrix(model);
	}
	
	public void loadTextures() {
		p().f.sampler("reflection").loadSampler(0);
		p().f.sampler("refraction").loadSampler(1);
		p().f.sampler("dudvMap").loadSampler(2);
		p().f.sampler("normalMap").loadSampler(3);
		p().f.sampler("depthMap").loadSampler(4);
	}
	
	public void setDistortionMove() {
		p().f.num("distortionMove").loadFloat(distortion_move += (DISTORTION_MOVE * Main.getDeltaCycles() / 1000f));
		distortion_move %= 1;
	}
	
	public void setLight(List<Light> lights) {
		Vector3f[] positions = new Vector3f[lights.size()];
		Vector3f[] colors = new Vector3f[lights.size()];
		for(int i = 0; i < lights.size(); i++) {
			positions[i] = lights.get(i).getPosition();
			colors[i] = lights.get(i).getColor();
		}
		p().v.array("lightPosition[]").loadVectorArray(positions);
		p().f.array("lightColor[]").loadVectorArray(colors);
		p().v.num("numberOfLights").loadFloat(lights.size());
	}
	
	public void setFog(float gradient, float density, Vector3f sky) {
		
	}
	
	public void setTileSize(Vector2f size) {
		p().v.vector("tileSize").loadVector2f(size);
	}

}
