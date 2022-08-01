package shaders;

import java.util.HashMap;

public class UniformMapped {
	
	private HashMap<String, Uniform> uniforms = new HashMap<String, Uniform>();
	
	public HashMap<String, Uniform> getUniforms(){
		return uniforms;
	}
	
	public Uniform get(String name) {
		return uniforms.get(name);
	}

}
