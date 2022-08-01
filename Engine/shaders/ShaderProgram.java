package shaders;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import readers.SimpleLanguageReader;
import readers.SimpleLanguageReader.File;
import readers.SimpleLanguageReader.StringFile;
import util.CustomFile;

public class ShaderProgram {
	
	private static final String TAB = "	";
	
	private int id;
	
	private int vertexShaderID;
	private int fragmentShaderID;
	private CustomFile vertexShader;
	private CustomFile fragmentShader;
	private CustomFile[] vMerges;
	private CustomFile[] fMerges;
	
	public ShaderProgram(CustomFile vertexShader, CustomFile fragmentShader, String... inVars) {
		vertexShaderID = loadShader(vertexShader, GL20.GL_VERTEX_SHADER);
		fragmentShaderID = loadShader(fragmentShader, GL20.GL_FRAGMENT_SHADER);
		this.vertexShader = vertexShader;
		this.fragmentShader = fragmentShader;
		id = GL20.glCreateProgram();
		attachShader(vertexShaderID);
		attachShader(fragmentShaderID);
		prepare();
		bindAttributes(inVars);
	}
	
	public ShaderProgram(CustomFile vertexShader, CustomFile vMerge, CustomFile fragmentShader, CustomFile fMerge, String... inVars) {
		vertexShaderID = loadShader(vertexShader, GL20.GL_VERTEX_SHADER, vMerge);
		fragmentShaderID = loadShader(fragmentShader, GL20.GL_FRAGMENT_SHADER, fMerge);
		id = GL20.glCreateProgram();
		this.vertexShader = vertexShader;
		this.fragmentShader = fragmentShader;
		this.vMerges = new CustomFile[]{vMerge};
		this.fMerges = new CustomFile[]{fMerge};
		attachShader(vertexShaderID);
		attachShader(fragmentShaderID);
		prepare();
		bindAttributes(inVars);
	}
	
	public ShaderProgram(CustomFile vertexShader, CustomFile[] vMerges, CustomFile fragmentShader, CustomFile[] fMerges, String... inVars) {
		vertexShaderID = loadShaderWithMergeables(vertexShader, GL20.GL_VERTEX_SHADER, vMerges);
		fragmentShaderID = loadShaderWithMergeables(fragmentShader, GL20.GL_FRAGMENT_SHADER, fMerges);
		id = GL20.glCreateProgram();
		this.vertexShader = vertexShader;
		this.fragmentShader = fragmentShader;
		this.vMerges = vMerges;
		this.fMerges = fMerges;
		attachShader(vertexShaderID);
		attachShader(fragmentShaderID);
		prepare();
		bindAttributes(inVars);
	}
	
	public void storeAllUniformToMap(UniformMapped uniformMap) {
		if(vertexShader != null) {
			readUniform(vertexShader, uniformMap);
		}
		if(fragmentShader != null) {
			readUniform(fragmentShader, uniformMap);
		}
		if(vMerges != null) {
			for(CustomFile vMerge : vMerges)
				readUniform(vMerge, uniformMap);
		}
		if(fMerges != null) {
			for(CustomFile fMerge : fMerges)
				readUniform(fMerge, uniformMap);
		}
	}
	
	private void readUniform(CustomFile file, UniformMapped uniformMap) {
		BufferedReader reader = file.getReader();
		String line = null;
		HashMap<String, Float> constants = new HashMap<String, Float>();
		try {
			while((line = reader.readLine()) != null) {
				if(line.startsWith("uniform ")) {
					String l = line.split(" ")[2].replace(";", "");
					if(line.contains("[") || line.contains("]")) {
						System.out.println(constants);
						String ln = l.split("\\[")[1].replace("]", "");
						System.out.println(ln);
						int size = 0;
						if(ln.matches("^[+-]?\\d+$"))
							size = Integer.parseInt(ln);
						else
							size = constants.get(ln).intValue();
						uniformMap.getUniforms().put(l.split("\\[")[0] + "[]", new UniformArray(l, size));
					}else
						uniformMap.getUniforms().put(l, (Uniform) Uniform.UniformTypes.getByName(line.split(" ")[1]).getClazz().getConstructor(String.class).newInstance(l));
				}else if(line.startsWith("const ")) {
					String l = line.split(" ")[4].replace(";", "");
					String name = line.split(" ")[2];
					constants.put(name, Float.parseFloat(l));
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void storeAllUniformLocations(Collection<? extends Uniform> uniforms) {
		for(Uniform uniform : uniforms) {
			uniform.storeUniformLocation(id);
		}
		GL20.glValidateProgram(id);
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
			System.out.println("Failed to compile shader " + file);
			System.exit(-1);
		}
		return shader;
	}
	/*
	 * 
	 */
	private int loadShader(CustomFile file, int type, CustomFile mergeable) {
		StringBuilder builder = new StringBuilder();
		try {
			BufferedReader reader = file.getReader();
			String line;
			List<String> lines = new ArrayList<String>();
			HashMap<String, Integer> readOnly = new HashMap<String, Integer>();
			boolean readingOnly = false;
			int lineIndex = 0;
			while((line = reader.readLine()) != null) {
				if(line.startsWith("//READ ONLY")) {
					readingOnly = true;
				}else if(line.startsWith("//END READ ONLY")) {
					readingOnly = false;
				}
				if(readingOnly) {
					readOnly.put(line + "\n", lineIndex);
				}else {
					lines.add(line + "\n");
				}
				lineIndex++;
			}
			if(mergeable != null) {
				BufferedReader reader2 = mergeable.getReader();
				String line2;
				while((line2 = reader2.readLine()) != null) {
					if(line2.startsWith("uniform ") || line2.startsWith("in ") || line2.startsWith("out ") || line2.startsWith("layout ") || line2.startsWith("varying ") || line2.startsWith("const ")) {
						lines.add(1, line2 + "\n");
					}else if(line2.startsWith("-remove ")){
						String toRemove = null;
						for(String l : lines) {
							if(l.replace("\n", "").equals(line2.replace("-remove ", TAB))) {
								toRemove = l;
							}
						}
						if(toRemove != null) {
							lines.remove(toRemove);
						}
					}else {
						if(line2.startsWith("void main()")) continue;
						int ind = 2;
						if(type == GL20.GL_VERTEX_SHADER) {
							ind = 2;
						}else if(type == GL20.GL_FRAGMENT_SHADER) {
							ind = 3;
						}
						lines.add(lines.size() - ind, TAB + line2 + "\n");
					}
				}
				reader2.close();
			}
			for(String l : lines) {
				builder.append(l);
			}
			System.out.println(builder);
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
	
	private int loadShaderWithMergeables(CustomFile file, int type, CustomFile... mergeables) {
		SimpleLanguageReader.StringFile sfile = new SimpleLanguageReader.StringFile(new SimpleLanguageReader.File(file));
		SimpleLanguageReader.StringFile mfile = new SimpleLanguageReader.StringFile(new String[] {});
		for(CustomFile mergeable : mergeables) {
			mfile = new SimpleLanguageReader.StringFile(SimpleLanguageReader.mergeRaw(mfile, new SimpleLanguageReader.File(mergeable)));
		}
		sfile = new SimpleLanguageReader.StringFile(SimpleLanguageReader.merge(sfile, mfile));
		String shaderString = sfile.toString();
		mfile.sortModules();
		System.out.println(mfile.modules.size());
		System.out.println(shaderString);
		int shader = GL20.glCreateShader(type);
		GL20.glShaderSource(shader, shaderString);
		GL20.glCompileShader(shader);
		if(GL20.glGetShaderi(shader, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE) {
			System.out.println(GL20.glGetShaderInfoLog(shader, 500));
			System.out.println("Failed to compile shader " + file);
			System.exit(-1);
		}
		return shader;
	}

}
