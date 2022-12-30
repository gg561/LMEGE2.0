package readers.module;

import java.util.ArrayList;
import java.util.List;

public class DataCenter {
	
	private List<Module> globalProducts = new ArrayList<Module>();
	private List<Module> modules = new ArrayList<Module>();
	private String path;
	private Module defaultModule = new Module(null, "default", -1, -1, true);
	private List<String> data;
	
	public Module getDefaultModule() {
		return defaultModule;
	}
	
	public void setDefaultModule(Module defaultModule) {
		this.defaultModule = defaultModule;
	}
	
	public String getPath() {
		return path;
	}
	
	public void setPath(String path) {
		this.path = path;
	}
	
	public String toString() {
		return path;
	}
	
	public void setData(List<String> data) {
		this.data = data;
		defaultModule.getData().addAll(data);
	}
	
	public List<String> getData(){
		return data;
	}

	public List<Module> getGlobalProducts(){
		return globalProducts;
	}
	
	public Module findModule(String name) {
		if(defaultModule.name.equals(name)) return defaultModule;
		for(Module mod : modules) {
			if(mod.name.equals(name)) {
				return mod;
			}
		}
		return null;
	}
	
	public Module findGlobalModule(String name) {
		for(Module mod : globalProducts) {
			if(mod.name.equals(name)) {
				return mod;
			}
		}
		return null;
	}
	
	public List<Module> getModules(){
		return modules;
	}
	
	public void clear() {
		path = null;
		if(data != null)
			data.clear();
		if(defaultModule != null)
			defaultModule.clear();
		if(globalProducts != null)
			globalProducts.clear();
		if(modules != null)
			modules.clear();
	}

}
