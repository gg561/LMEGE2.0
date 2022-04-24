package Engine.shaders;

import java.io.BufferedReader;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import Engine.util.CustomFile;

public class ShaderProgram {
	
	private int id;
	
	private int vertexShaderID;
	private int fragmentShaderID;
	
	public ShaderProgram(CustomFile vertexShader, CustomFile fragmentShader, String... inVars) {
		vertexShaderID = loadShader(vertexShader, GL20.GL_VERTEX_SHADER);
		fragmentShaderID = loadShader(fragmentShader, GL20.GL_FRAGMENT_SHADER);
		id = GL20.glCreateProgram();
		attachShader(vertexShaderID);
		attachShader(fragmentShaderID);
		prepare();
		bindAttributes(inVars);
	}
	
	public void storeAllUniformLocations(Uniform... uniforms) {
		for(Uniform uniform : uniforms) {
			uniform.storeUniformLocation(id);
		}
		GL20.glValidateProgram(id);
	}
	
	public void prepare() {
		GL20.glLinkProgram(id);
		if(GL20.glGetProgrami(id, GL20.GL_LINK_STATUS) == -1) {
			System.err.println("Program Link: \n" + GL20.glGetProgramInfoLog(id));
			throw new RuntimeException("u suc");
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
		detachShader(vertexShaderID);
		detachShader(fragmentShaderID);
		deleteShader(vertexShaderID);
		deleteShader(fragmentShaderID);
		stop();
		GL20.glDeleteProgram(id);
	}
	
	private void attachShader(int shader) {
		GL20.glAttachShader(id, shader);
	}
	
	private void detachShader(int shader) {
		GL20.glDetachShader(id, shader);
	}
	
	private void deleteShader(int shader) {
		GL20.glDeleteShader(shader);
	}
	
	private void bindAttributes(String[] attributes) {
		for(int i = 0; i < attributes.length; i++) {
			GL20.glBindAttribLocation(id, i, attributes[i]);
		}
	}
	
	private int loadShader(CustomFile file, int type) {
		StringBuilder builder = new StringBuilder();
		try {
			BufferedReader reader = file.getReader();
			String line;
			while((line = reader.readLine()) != null) {
				builder.append(line).append("//\n");
			}
			reader.close();
		}catch(Exception e) {
			System.out.println("Could not read file.");
			e.printStackTrace();
			System.exit(-1);
		}
		int shader = GL20.glCreateShader(type);
		GL20.glShaderSource(shader, builder);
		GL20.glCompileShader(shader);
		if(GL20.glGetShaderi(shader, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE) {
			System.out.println(GL20.glGetShaderInfoLog(shader, 500));
			System.out.println("Failed to compile shader " + file);
			System.exit(-1);
		}
		return shader;
	}

}
