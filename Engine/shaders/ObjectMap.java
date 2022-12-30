package shaders;

import java.util.HashMap;
import java.util.Map;

import shaders.Uniform.UniformCoverCard;

public class ObjectMap {
	
	private HashMap<String, Uniform> uniforms = new HashMap<String, Uniform>();
	private HashMap<String, Object> constants = new HashMap<String, Object>();
	
	public HashMap<String, Uniform> getUniforms(){
		return uniforms;
	}
	
	public HashMap<String, Object> getConstants(){
		return constants;
	}
	
	private <T> T find(String name, String debug, Map<String, ?> map) {
		if(map.containsKey(name))
			try {
				return (T) map.get(name);
			}catch(ClassCastException e) {
				throw new RuntimeException("No such element found! " + debug + " " + name + " does not exist in this map, do you mean " + e.getClass().getName());
			}
		else
			throw new RuntimeException("No such element found! Uniform " + name + " not existant in map!");
	}
	
	public Uniform get(String name) {
		return find(name, "Uniform", uniforms);
	}
	
	public Uniform store(String name, Uniform value) {
		uniforms.put(name, value);
		return value;
	}
	
	public Object constant(String name) {
		return find(name, "Constant", constants);
	}
	
	public Object stoConstant(String name, Object value) {
		constants.put(name, value);
		return value;
	}

}
