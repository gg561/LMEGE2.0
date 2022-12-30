package shaders;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import util.CustomFile;

public class Pipeline {
	
	private Shader[] shaders;
	
	public Shader v;//VERTEX SHADER
	public Shader f;//FRAGMENT SHADER
	public Shader g;//GEOMETRY SHADER
	
	public Pipeline(Shader... shaders) {
		this.shaders = shaders;
		assign();
	}
	
	public Pipeline(int size) {
		this.shaders = new Shader[size];
	}
	
	public Pipeline(CustomFile[][] files, int[] types) {
		List<List<CustomFile>> shaderComps = new ArrayList<List<CustomFile>>();
		for(CustomFile[] composition : files) {
			List<CustomFile> shaderComp = new ArrayList<CustomFile>();
			Collections.addAll(shaderComp, composition);
			System.err.println(shaderComp);
			shaderComps.add(shaderComp);
		}
		shaders = readShaders(types, shaderComps);
		assign();
	}
	
	public void put(int index, Shader shader) {
		shaders[index] = shader;
	}
	
	public void put(int index, CustomFile file, int type) {
		shaders[index] = Shader.loadShader(file, type);
	}
	
	public void put(int index, CustomFile file, CustomFile[] merges, int type) {
		shaders[index] = Shader.loadShaderWithMergeables(file, type, merges);
	}
	
	private void assign() {
		v = shaders[0];
		f = shaders[1];
		if(shaders.length > 2) {
			g = shaders[1];
			f = shaders[2];
		}
	}
	
	public Shader[] shaders() {
		return shaders;
	}
	
	public void attach(int program) {
		for(Shader shader : shaders) {
			shader.attach(program);
		}
	}
	
	public void detach(int program) {
		for(Shader shader : shaders) {
			shader.detach(program);
		}
	}
	
	public void delete(int program) {
		for(Shader shader : shaders) {
			shader.detach(program);
			shader.delete();
		}
	}
	
	public static Pipeline read(int[] types, CustomFile...files) {
		Shader[] shaders = new Shader[files.length];
		for(int i = 0; i < files.length; i++) {
			shaders[i] = Shader.loadShader(files[i], types[i]);
		}
		return new Pipeline(shaders);
	}
	
	public static Shader[] readShaders(int[] types, List<List<CustomFile>> files) {
		Shader[] shaders = new Shader[files.size()];
		for(int i = 0; i < files.size(); i++) {
			List<CustomFile> shaderComp = files.get(i);
			//System.out.println(types[i] + " " + i);
			shaders[i] = Shader.loadShaderWithMergeables(shaderComp.get(0),types[i], shaderComp.subList(1, shaderComp.size()).toArray(CustomFile[]::new));
		}
		return shaders;
	}
	
	public static Pipeline read(int[] types, List<List<CustomFile>> files) {
		return new Pipeline(readShaders(types, files));
	}

}
