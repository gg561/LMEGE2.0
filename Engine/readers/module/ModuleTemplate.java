package readers.module;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ModuleTemplate extends Template<Module> {
	
	private String name;
	private Module suprModule;
	private boolean isGlobal;
	private int start;
	private int end;
	private List<Module> subModules = new ArrayList<Module>();
	private ModuleReader reader;
	
	public ModuleTemplate(ModuleReader reader) {
		this.reader = reader;
	}
	
	public void create() {
		Module mod;
		mod = new Module(suprModule, name, start, end, isGlobal);
		if(isGlobal) {
			suprModule.subModules.remove(mod);
			if(reader.getDataCentr().getGlobalProducts().stream().anyMatch(p -> p.name.equals(name))) {
				Module other = reader.getDataCentr().getGlobalProducts().stream().filter(p -> p.name.equals(name)).findFirst().get();
				mod.getData().add(other.getDataAsLine());
				reader.getDataCentr().getGlobalProducts().remove(other);
			}
			reader.getDataCentr().getGlobalProducts().add(mod);
		}
		mod.subModules.clear();
		mod.subModules.addAll(subModules);
		setFinalProduct(mod);
		isGlobal = false;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Module getSuprModule() {
		return suprModule;
	}

	public void setSuprModule(Module suprModule) {
		this.suprModule = suprModule;
	}

	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public int getEnd() {
		return end;
	}

	public void setEnd(int end) {
		this.end = end;
	}
	
	public boolean isGlobal() {
		return isGlobal;
	}
	
	public void setGlobal(boolean isGlobal) {
		this.isGlobal = isGlobal;
	}

	public List<Module> getSubModules() {
		return subModules;
	}

	public void setSubModules(List<Module> subModules) {
		this.subModules = subModules;
	}
	

}
