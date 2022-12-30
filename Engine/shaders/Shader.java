package shaders;

import java.io.BufferedReader;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import readers.module.ModuleReader;
import readers.variable.VariableReader;
import util.CustomFile;
import util.Logger;

public class Shader {
	
	private static final Logger LOGGER = new Logger("shaders.Shader");

	private static ModuleReader reader = new ModuleReader() {{readToParser(new CustomFile("/readers/module/ShaderRules.src"));}};
	
	private VariableReader soReader = new VariableReader("shaders");
	private int id = -1, type = GL20.GL_VERTEX_SHADER;
	private CustomFile source;
	private String data;
	private ObjectMap objects = new ObjectMap();
	
	public Shader(int id, CustomFile source, String data) {
		this.id = id;
		this.source = source;
		this.data = data;
	}
	
	public Shader(int id, int type, CustomFile source, String data) {
		this(id, source, data);
		this.type = type;
	}
	
	public void attach(int program) {
		GL20.glAttachShader(program, id);
	}

	public void detach(int program) {
		GL20.glDetachShader(program, id);
	}
	
	public void delete() {
		GL20.glDeleteShader(id);
	}
	
	public int id() {
		return id;
	}
	
	public int type() {
		return type;
	}
	
	public String data() {
		return data;
	}
	
	public CustomFile source() {
		return source;
	}
	
	public ObjectMap objects() {
		return objects;
	}
	
	public void printData(PrintStream stream) {
		stream.println(data);
	}
	
	public void readObjects() {
		soReader.readUniforms(objects.getUniforms(), objects.getConstants(), data);
	}
	
	public Uniform find(String name) {
		return objects.get(name);
	}
	
	public <T extends Uniform> T find0(String name, String className) {
		try {
			return (T) find(name);
		}catch(ClassCastException e) {
			throw new RuntimeException("No such element found! " + className + name + " does not exist! Did you mean " + name + " " + e.getClass().getName());
		}
	}
	
	public UniformMatrix matrix(String name) {
		return find0(name, "UniformMatrix");
	}
	
	public UniformVector vector(String name) {
		return find0(name, "UniformVector");
	}
	
	public UniformFloat num(String name) {
		return find0(name, "UniformFloat");
	}
	
	public UniformBoolean bool(String name) {
		return find0(name, "UniformBoolean");
	}
	
	public UniformSampler sampler(String name) {
		return find0(name, "UniformSampler");
	}
	
	public UniformArray array(String name) {
		return find0(name, "UniformArray");
	}
	
	public String toString() {
		return "[type=GLSLShader, source=" + source + "]";
	}
	
	public static Shader loadShader(CustomFile file, int type) {
		/*StringBuilder builder = new StringBuilder();
		try {
			BufferedReader reader = file.getReader();
			String line;
			while((line = reader.readLine()) != null) {
				builder.append(line).append("\n");
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
			System.out.println("Failed to compile shader " + file + " " + shader);
			System.exit(-1);
		}
		return new Shader(shader, type, file, builder.toString());*/
		return loadShaderWithMergeables(file, type, null);
	}
	
	public static Shader loadShaderWithMergeables(CustomFile file, int type, CustomFile... mergeables) {
		LOGGER.info("Compiling " + file.getPath() + "...");
		LOGGER.info("Reading data from source files " + file + " " + (type == GL20.GL_VERTEX_SHADER));
		LOGGER.info("Extension files : " + Arrays.toString(mergeables));
		if(mergeables != null)
			for(CustomFile mergeable : mergeables) {
				LOGGER.info(reader.bind(mergeable));
			}
		String shaderString = reader.make(file);
		reader.getDataCentr().findModule("");
		reader.clear();
		System.out.println(reader.getDataCentr().getModules());
		LOGGER.info("Shader Content : \n" + shaderString);
		return compileShaderFromSource(shaderString, type, file);
	}
	
	private static Shader compileShaderFromSource(String shaderString, int type, CustomFile file) {
		int shader = GL20.glCreateShader(type);
		GL20.glShaderSource(shader, shaderString);
		GL20.glCompileShader(shader);
		checkCompile(shader, file);
		return new Shader(shader, type, file, shaderString);
	}
	
	private static void checkCompile(int shader, CustomFile file) {
		if(GL20.glGetShaderi(shader, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE) {
			LOGGER.info(GL20.glGetShaderInfoLog(shader, 500));
			LOGGER.info("Failed to compile shader " + file + " " + shader);
			System.exit(-1);
		}
	}

}
