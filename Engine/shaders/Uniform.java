package shaders;

import java.util.Arrays;

import org.lwjgl.opengl.GL20;

public abstract class Uniform {
	
	protected static final int NOT_FOUND = -1;
	
	private String name;
	private int location;
	
	protected Uniform(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public boolean storeUniformLocation(int programID) {
		location = GL20.glGetUniformLocation(programID, name);
		if(location == NOT_FOUND) {
			System.err.println("No uniform variable called " + name + " found! " + programID);
			return false;
		}
		return true;
	}
	
	public int getLocation() {
		return location;
	}
	
	public static enum UniformTypes {
		
		FLOAT("float", UniformFloat.class), BOOLEAN("boolean", UniformBoolean.class), VEC3("vec3", UniformVector.class), VEC2("vec2", UniformVector.class), VEC4("vec4", UniformVector.class), SAMPLER("sampler2D", UniformSampler.class), SAMPLER_SHADOW("sampler2DShadow", UniformSampler.class), MATRIX("mat4", UniformMatrix.class), 
		FLOAT_ARRAY("float[]", UniformArray.class), VEC2_ARRAY("vec2[]", UniformArray.class), VEC3_ARRAY("vec3[]", UniformArray.class), VEC4_ARRAY("vec4[]", UniformArray.class);
		
		private String name;
		private Class<?> clazz;
		
		private UniformTypes(String name, Class<? extends Uniform> clazz) {
			this.name = name;
			this.clazz = clazz;
		}
		
		public String getName() {
			return name;
		}
		
		public Class<?> getClazz() {
			return clazz;
		}
		
		public static UniformTypes getByName(String name) {
			return Arrays.asList(UniformTypes.values()).stream().filter(u -> u.getName().equals(name)).findFirst().get();
		}
		
	}
	
	public static class UniformCoverCard extends Uniform {

		protected UniformCoverCard(String name) {
			super(name);
			// TODO Auto-generated constructor stub
		}
		
	}

}
