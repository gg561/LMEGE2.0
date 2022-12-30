package shaders;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL40;

import readers.module.ModuleReader;
import util.CustomFile;
import util.Logger;

public class ShaderProgram {
	
	private static final Logger LOGGER = new Logger("? ext shaders.ShaderProgram");
	private static final String TAB = "	";
	
	private int id;
	private CustomFile[] vMerges;
	private CustomFile[] fMerges;
	private Pipeline p;
	
	public ShaderProgram(CustomFile vertexShader, CustomFile fragmentShader, String... inVars) {
		p = Pipeline.read(new int[]{GL20.GL_VERTEX_SHADER, GL20.GL_FRAGMENT_SHADER}, vertexShader, fragmentShader);
		init(inVars);
	}
	
	public ShaderProgram(CustomFile vertexShader, CustomFile[] vMerges, CustomFile fragmentShader, CustomFile[] fMerges, String... inVars) {
		p = new Pipeline(
				Shader.loadShaderWithMergeables(vertexShader, GL20.GL_VERTEX_SHADER, vMerges),
				Shader.loadShaderWithMergeables(fragmentShader, GL20.GL_FRAGMENT_SHADER, fMerges));
		this.vMerges = vMerges;
		this.fMerges = fMerges;
		init(inVars);
	}
	
	public ShaderProgram(CustomFile[][] shadersAndExtensions, int[] types, String...inVars) {
		p = new Pipeline(shadersAndExtensions, types);
		init(inVars);
	}
	
	public ShaderProgram(Shader[] shaders, String...inVars) {
		p = new Pipeline(shaders);
		init(inVars);
	}
	
	public void init(String... inVars) {
		id = GL20.glCreateProgram();
		p.attach(id);
		prepare();
		bindAttributes(inVars);
	}
	
	public int getId() {
		return id;
	}
	
	public Pipeline p() {
		return p;
	}
	
	public void storeAllUniformToMap() {
		for(Shader shader : p.shaders()) {
			shader.readObjects();
		}
	}
	
	public void storeAllUniformLocations(Shader...shaders) {
		for(Shader shader : shaders) {
			storeAllUniformLocations(shader.objects().getUniforms().values());
		}
	}
	
	public void storeAllUniformLocations(Collection<? extends Uniform> uniforms) {
		for(Uniform uniform : uniforms) {
			uniform.storeUniformLocation(id);
		}
		System.out.println(id + " " + p.v);
		GL20.glValidateProgram(id);
	}
	
	public void storeAllUniformLocations(Uniform... uniforms) {
		for(Uniform uniform : uniforms) {
			uniform.storeUniformLocation(id);
		}
		GL20.glValidateProgram(id);
	}
	
	public void prepare() {
		LOGGER.info(this.getClass());
		LOGGER.info(id);
		GL20.glLinkProgram(id);
		if(GL20.glGetProgrami(id, GL20.GL_LINK_STATUS) == GL11.GL_FALSE) {
			System.err.println("Program Link: \n" + GL20.glGetProgramInfoLog(id));
			throw new RuntimeException("u suc " + id);
		}
		GL20.glValidateProgram(id);
		if(GL20.glGetProgrami(id, GL20.GL_VALIDATE_STATUS) == -1) {
			System.err.println("Program Validate: \n" + GL20.glGetProgramInfoLog(id));
			throw new RuntimeException("u suc");
		}
	}
	
	public void start() {
		GL20.glUseProgram(id);
	}
	
	public void stop() {
		GL20.glUseProgram(0);
	}
	
	public void cleanUp() {
		p.delete(id);
		stop();
		GL20.glDeleteProgram(id);
	}
	
	
	
	private void bindAttributes(String[] attributes) {
		for(int i = 0; i < attributes.length; i++) {
			GL20.glBindAttribLocation(id, i, attributes[i]);
		}
	}
	
	
	
	private void useTessellation(CustomFile tess) {
		GL20.glCreateShader(GL40.GL_TESS_CONTROL_SHADER);
	}

}
