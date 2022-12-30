package readers.variable;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

import readers.module.GLSLTypes;
import readers.module.Module;
import shaders.Uniform;
import shaders.UniformArray;
import util.CustomFile;

/**
 * 
 * TODO Optimize all
 * 
 * Make this an actually omni-operational parser
 * Add support for arithmetic expressions
 * 
 * 
 * @author huangyoulin
 *
 */
public class VariableReader {
	
	/**
	 * 
	 * Assignment Syntax : attr type name = val;
	 * 
	 */
	
	private static final CustomFile RULES = new CustomFile("/readers/variable/VariableReadingRules.src");
	
	private Map<String, Object> valueMap = new HashMap<String, Object>();
	private String path;
	
	public VariableReader(String path) {
		this.path = path;
	}
	
	private boolean isNumeric(String str) {
		return str.matches("-?\\d+(\\.\\d+)?");
	}
	
	private boolean isBoolean(String str) {
		return str.equals("true") || str.equals("false");
	}
	
	private boolean isArray(String str) {
		return str.contains("[");
	}
	
	private void parseExec(String str) {
		
	}
	
	private int getArraySize(String str) {
		String sizeOp = str.split("[")[1].replace(']', '\0');
		if(isNumeric(sizeOp)) return Integer.parseInt(sizeOp);
		return 0;
	}
	
	public void readUniforms(Map uniforms, Map constants, String data) {
		for(String line : data.split("\n")) {
			if(line.startsWith("const")) {
				readConstantFromLine(constants, line);
			}else if(line.startsWith("uniform")) {
				readUniformFromLine(uniforms, line);
			}
		}
	}
	
	/**
	 * 
	 * TODO GLSLTypes is a mess wire. GLSLTypes repeats stored data with Uniform.UniformTypes.
	 * TODO TO fix
	 * 
	 * @param uniforms
	 * @param line
	 */
	private void readUniformFromLine(Map uniforms, String line) {
		String[] args = line.replace(";", " ").split(" ");	
		Class clazz = Uniform.UniformTypes.getByName(args[1]).getClazz();
		Uniform uni;
		String name;
		if(isArray(line)) {
			int size;
			String[] bracketSplit = line.split("\\[");
			String strSize = bracketSplit[1];
			strSize = strSize.substring(0, strSize.lastIndexOf(']'));
			name = args[2].substring(0, args[2].indexOf('['));
			if(isNumeric(strSize))
				size = Integer.parseInt(strSize);
			else {
				size = (int) collapse(strSize.split("\\."));
			}
			uni = new UniformArray(name, size);
			name += "[]";
		}else {
			name = args[2];
			try {
				uni = (Uniform) clazz.getConstructor(String.class).newInstance(args[2]);
			} catch(Exception e) {
				throw new RuntimeException("h");
			}
		}
		uniforms.put(name, uni);
	}
	
	@SuppressWarnings("removal")
	private void readConstantFromLine(Map constants, String line) {
		String[] args = line.replace(";", " ").split(" ");	
		Object val = null;
		Class<?> constClass = GLSLTypes.findClass(args[1]);
		try {
			if(GLSLTypes.wrappers.contains(constClass) && (isNumeric(args[4]) || isBoolean(args[4]))) {
					val = constClass.getConstructor(String.class).newInstance(args[4]);
			}else {
				String[] arguments = line.substring(line.indexOf('(') + 1, line.lastIndexOf(')')).split(", ");
				Object[] pars = new Object[args.length];
				Class[] classes = new Class[args.length];
				for(int i = 0; i < arguments.length; i++) {
					System.out.println(arguments[i]);
					Object argObj = parseAnything(arguments[i]);
					pars[i] = argObj;
					classes[i] = argObj.getClass();
				}
				val = constClass.getConstructor(classes).newInstance(pars);
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		constants.put(args[2], val);
		valueMap.put(args[2], val);
	}
	
	public void clear() {
		valueMap.clear();
	}
	
	private Object parseAnything(String word) {
		if(isNumeric(word)) {
			return Float.parseFloat(word);
		}else if(isBoolean(word)) {
			return Boolean.parseBoolean(word);
		}else if(word.startsWith("\"") && word.endsWith("\"")) {
			return word.substring(1, word.length() - 1);
		}else if(word.contains("(byte)")) {
			return Byte.parseByte(word);
		}else if(!word.matches("![-+*/]"))
			throw new RuntimeException("ARITHMETICS NOT SUPPORTED YET");
		return valueMap.get(word);
	}
	
	private static class BasicActions {
		
		public final static BiFunction<VariableReader, String, Object> DOT = (r, s) -> {return r.collapse(s.split("."));};
		//public final static BiFunction<ShaderObjectReader, String, Object> ARIT = (r, s) -> {};
		
	}
	
	private Object collapse(String[] hierarchy) {
		Object val = null;
		for(String comp : hierarchy) {
			if(isNumeric(comp)) {
				
			}
			if(val == null) {
				val = valueMap.get(comp);
			}else if(val instanceof Map) {
				val = ((Map) val).get(comp);
				if(val == null) {
					break;
				}
			}
		}
		if(val != null) return val;
		throw new NoSuchElementException("The value " + Arrays.toString(hierarchy) + " does not exist in the map.");
	}
	
	//collapse sides -> execute with value -> return value
	/*private Object execute(String command) {
		
		
	}*/
	
	//+ - * / %
	//private Object doMath(String line) {//parentheses first
	//	String collapsables = line.replaceAll("[-+*/](?![^(]*\\))","|");
	//	String[] args = collapsables.split(" ");
	//	Map<String, Object> replacements = new HashMap<String, Object>();
	//	for(String arg : args) {
	//		replacements.put(arg, collapse(arg.split(".")));
	//	}
	//}
	
	private Object add(Module m0, Module m1) {
		Object o = collapse(m0.getDataAsLine().split("."));
		Object p = collapse(m1.getDataAsLine().split("."));
		return add(o, p);
	}
	
	private Object add(Object o, Object p) {
		boolean equ = (p instanceof Map) == (o instanceof Map);
		if(equ) 
			if(o instanceof Map) return addMapMap((Map) o, (Map) p);
			else return (float) o + (float) p; 
		else
			if(o instanceof Map) return addMapObj((Map) o, p);
			else return addMapObj((Map) p, o);
	}
	
	private Object addMapObj(Map o, Object p) {
		for(Object k : o.keySet()) {
			o.put(k, (float) o.get(k) + (float) p);
		}
		return o;
	}
	
	private Map<String, Object> addMapMap(Map<String, Object> o, Map<String, Object> p) {
		if(o.size() != p.size()) throw new IllegalArgumentException("The 2 operatable variables " + o + ", " + p +  " are not compatible with eachother!");
		Map<String, Object> result = new HashMap<String, Object>();
		for(String key : o.keySet()) {
			result.put(key, (float) o.get(key) + (float) p.get(key));
		}
		return result;
	}

}
