package readers.module;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class Module {
	
	public final Module suprModule;
	public final List<Module> subModules = new ArrayList<Module>();
	public final int start;
	public final int end;
	public final boolean isGlobal;
	public final String name;
	private List<String> data = new ArrayList<String>();
	
	public Module(Module suprModule, String name, int start, int end, boolean isGlobal) {
		if(suprModule != null)
			suprModule.subModules.add(this);
		this.suprModule = suprModule;
		this.name = name;
		this.start = start;
		this.end = end;
		this.isGlobal = isGlobal;
	}
	
	public void parse(List<String> grandData) {
		if(start != -1)
			data = grandData.subList(start + 1, end);
		else
			data = grandData;
	}
	
	public void parseAdd(List<String> grandData) {
		if(data.isEmpty()) parse(grandData);
		else data.addAll(grandData.subList(start + 1,  end));
	}
	
	public List<String> getData(){
		return data;
	}
	
	public String getDataAsLine() {
		return String.join("\n", data.subList(0, data.size()));
	}
	
	public String getVariableAppropriateData(Map<String, String> replacementMap) {
		String data = getDataAsLine();
		for(String key : replacementMap.keySet()) {
			data = data.replace(key, replacementMap.get(key));
		}
		return data;
	}
	
	public Module findSubModules(String name) {
		if(this.name != null) {
			if(this.name.equals(name)) {
				return this;
			}
		}
		for(Module sub : subModules) {
			Module r = sub.findSubModules(name);
			if(r != null) return r;
		}
		return null;
	}
	
	public void clear() {
		subModules.clear();
		data.clear();
	}

}
