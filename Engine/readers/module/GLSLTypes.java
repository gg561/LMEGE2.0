package readers.module;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NoSuchElementException;

import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import shaders.Uniform;
import shaders.UniformArray;
import shaders.UniformBoolean;
import shaders.UniformFloat;
import shaders.UniformMatrix;
import shaders.UniformSampler;
import shaders.UniformVector;

public class GLSLTypes {
	
	public static final Map<String, Class<?>> classes = new HashMap<String, Class<?>>();
	public static final List<Class<?>> wrappers = new ArrayList<Class<?>>();
	
	static {
		classes.put("float", Float.class);
		wrappers.add(Float.class);
		classes.put("int", Integer.class);
		wrappers.add(Integer.class);
		classes.put("short", Short.class);
		wrappers.add(Short.class);
		classes.put("long", Long.class);
		wrappers.add(Long.class);
		classes.put("double", Double.class);
		wrappers.add(Double.class);
		classes.put("bool", Boolean.class);
		wrappers.add(Boolean.class);
		classes.put("vec2", Vector2f.class);
		classes.put("vec3", Vector3f.class);
		classes.put("vec4", Vector4f.class);
		classes.put("mat3", Matrix3f.class);
		classes.put("mat4", Matrix4f.class);
		classes.put("sampler2D", Void.class);
		classes.put("sampler2DShadow", Void.class);
	}
	

	private static boolean isArray(String str) {
		return str.contains("[");
	}
	
	public static Class<?> findClass(String glslName){
		if(!isArray(glslName)) {
			if(classes.containsKey(glslName)) {
				return classes.get(glslName);
			}
		}else {
			String base = glslName.replace('[', '\0').replace(']', '\0');
			if(classes.containsKey(base))
				return classes.get(base).arrayType();
		}
		throw new NoSuchElementException("Unknown Type with name : " + glslName);
	}

}
