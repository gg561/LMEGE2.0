package readers;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Predicate;

import util.CustomFile;
import util.RuntimeFile;

import java.util.Arrays;
import java.util.Comparator;

public class SimpleLanguageReader {
	
	public static String[] merge(CustomFile cfile, CustomFile mergeable) {
		File file = new File(cfile);
		File merge = new File(mergeable);
		return merge(file, merge);
	}
	
	public static String[] merge(File file, File merge) {
		StringFile sfile = new StringFile(file);
		return merge(sfile, merge);
	}
	
	public static String[] merge(StringFile sfile, File merge) {
		int index = 0;
		if(merge == null)
			return sfile.getLines().toArray(new String[sfile.getLines().size()]);
		sfile.getLines().addAll(sfile.getDefaultIndex(), merge.defaultModule.getLines());
		for(int i = 0; i < merge.modules.size(); i++) {
			Module mod = merge.modules.get(i);
			System.out.println(merge.defaultModule.size());
			System.out.println(merge.defaultModule.getLines());
			System.out.println(sfile.getLines().size());
			if(mod.indexToInsertTo != -1) {
				index = mod.indexToInsertTo;
				sfile.getLines().addAll(mod.indexToInsertTo + merge.defaultModule.size(), mod.getLines());
			}else if(mod.name != null && sfile.indexPoints.containsKey(mod.name)){
				index = sfile.indexPoints.get(mod.name);
				sfile.getLines().addAll(sfile.indexPoints.get(mod.name) + merge.defaultModule.size(), mod.getLines());
				System.out.println(sfile.indexPoints.get(mod.name) + merge.defaultModule.size());
			}
			for(int j = i +1; j < merge.modules.size(); j++) {
				Module mod2 = merge.modules.get(j);
				if(sfile.indexPoints.containsKey(mod2.name)) {
					if(sfile.indexPoints.get(mod2.name) > index) {
						sfile.indexPoints.put(mod2.name, sfile.indexPoints.get(mod2.name) + mod.getLines().size() - 1);
					}
				}else if(mod2.indexToInsertTo != -1 && mod2.indexToInsertTo > index) {
					mod2.indexToInsertTo = mod2.indexToInsertTo + mod.getLines().size() - 1;
				}
			}
		}
		System.out.println(sfile.indexPoints);
		return sfile.getLines().toArray(new String[sfile.getLines().size()]);
	}
	
	public static String[] mergeRaw(File file, File merge) {
		StringFile sfile = new StringFile(file);
		return mergeRaw(sfile, merge);
	}
	
	public static String[] mergeRaw(StringFile sfile, File merge) {
		if(merge == null)
			return sfile.getLines().toArray(new String[sfile.getLines().size()]);
		sfile.getLines().addAll(sfile.getDefaultIndex(), merge.defaultModule.getRawLines());
		for(int i = merge.modules.size() - 1; i >= 0; i--) {
			Module mod = merge.modules.get(i);
			sfile.getLines().addAll(sfile.getLines().size(), mod.getRawLines());
		}
		return sfile.getLines().toArray(new String[sfile.getLines().size()]);
	}
	
	public static String arrayToString(String[] arr) {
		StringBuilder builder = new StringBuilder();
		int index = 0;
		for(String str : arr) {
			builder.append(str);
			if(index < arr.length - 1)
				builder.append("\n");
			index++;
		}
		return builder.toString();
	}
	
	public static class File{
		public static boolean writeToDefault = true;
		public static boolean defaultIndexStatic = false;
		public static int currentPointing = 0;
		public static String currentRulesName = "";
		public static HashMap<String, Integer> currentIndexPoints = new HashMap<String, Integer>();
		public HashMap<String, Integer> indexPoints = new HashMap<String, Integer>();
		public int defaultIndex = 0;
		public Module defaultModule;
		public List<Module> modules = new ArrayList<Module>();
		public String path;
		public String name;
		
		public File(String path, String name) {
			this.path = path;
			this.name = name;
			loadFromFile(new CustomFile(path), Rules.loadFromFile(new CustomFile("readers", "ShaderRules.txt")));
			this.modules = sortModules(modules);
			Module.clear();
		}
		
		public File(CustomFile file) {
			this.path = file.getPath();
			this.name = file.getName();
			loadFromFile(file, Rules.loadFromFile(new CustomFile("readers", "ShaderRules.txt")));
			this.modules = sortModules(modules);
			Module.clear();
		}
		
		public File(String[] args) {
			this.path = null;
			this.name = null;
			loadFromString(args, Rules.loadFromFile(new CustomFile("readers", "ShaderRules.txt")));
			this.modules = sortModules(modules);
			Module.clear();
		}
		
		public void sortModules() {
			modules.sort(new Module());
		}
		
		private static List<Module> sortModules(List<Module> modules) {
			modules.sort(new Module());
			return modules;
		}
		
		private static List<Module> merge(List<Module> frst, List<Module> scnd) {
			List<Module> merged = new ArrayList<Module>();
			frst.sort(new Module());
			for(int i = 0; i < frst.size(); i++) {
				if(frst.get(i).indexToInsertTo <= scnd.get(i).indexToInsertTo) {
					merged.add(frst.get(i));
					merged.add(scnd.get(i));
				}else if(frst.get(i).indexToInsertTo > scnd.get(i).indexToInsertTo) {
					merged.add(scnd.get(i));
					merged.add(frst.get(i));
				}
			}
			return merged;
		}
		
		private void loadFromFile(CustomFile file, Rules rules) {
			BufferedReader reader = file.getReader();
			String line = null;
			Module.file = this;
			Module defaultModule = new Module();
			defaultIndexStatic = false;
			currentRulesName = rules.name;
			try {
				while((line = reader.readLine()) != null) {
					rules.test(line);
					if(Module.moduling) {
						Module.opened.get(Module.opened.size() - 1).write(line);
					}else if(Module.linesTilEnd == 0){
						if(writeToDefault)
							defaultModule.write(line);
						else {
							writeToDefault = true;
						}
					}
					if(Module.linesTilEnd > 0) {
						Module.linesTilEnd--;
						this.modules.get(modules.size() - 1).write(line);
					}
					if(defaultIndexStatic) {
						defaultIndex = currentPointing;
						defaultIndexStatic = false;
					}
					currentPointing++;
				}
				reader.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			this.defaultModule = defaultModule;
			indexPoints.putAll(currentIndexPoints);
			currentIndexPoints.clear();
			currentPointing = 0;
		}
		
		protected void loadFromString(String[] strarr, Rules rules) {
			Module.file = this;
			Module defaultModule = new Module();
			defaultIndexStatic = false;
			currentRulesName = rules.name;
			for(String str : strarr) {
				if(str != null) {
					rules.test(str);
					if(Module.moduling) {
						Module.opened.get(Module.opened.size() - 1).write(str);
					}else if(Module.linesTilEnd == 0){
						if(writeToDefault)
							defaultModule.write(str);
						else {
							writeToDefault = true;
						}
					}else {
						if(Module.linesTilEnd > 0) {
							Module.linesTilEnd--;
							this.modules.get(modules.size() - 1).write(str);
						}
						if(defaultIndexStatic) {
							defaultIndex = currentPointing;
							defaultIndexStatic = false;
						}
					}
					currentPointing++;
				}
			}
			this.defaultModule = defaultModule;
			indexPoints.putAll(currentIndexPoints);
			currentIndexPoints.clear();
			currentPointing = 0;
		}
		
		public String toString() {
			StringBuilder builder = new StringBuilder();
			builder.append(defaultModule.getLines());
			builder.append("\n");
			for(Module module : modules) {
				builder.append("index : " + module.indexToInsertTo);
				builder.append(module.getLines());
				builder.append("\n");
			}
			return builder.toString();
		}
		
	}
	
	public static class StringFile extends File {
		private List<String> lines = new ArrayList<String>();
		
		public StringFile(File file) {
			super(file.path, file.name);
			loadFromFile(file);
			this.defaultIndex = file.defaultIndex;
		}
		public StringFile(String[] lines) {
			super(lines);
			this.lines = new ArrayList<String>(Arrays.asList(lines));
		}
		
		public List<String> getLines(){
			return lines;
		}
		
		public List<String> getRawLines(){
			return new ArrayList<String>(Arrays.asList(toStringMods().split("\n")));
		}
		
		public String toString() {
			StringBuilder builder = new StringBuilder();
			for(String str : lines) {
				builder.append(str);
				builder.append("\n");
			}
			return builder.toString();
		}
		
		public String toStringMods() {
			List<String> list = new ArrayList<String>();
			for(Module mod : modules) {
				list.addAll(mod.getRawLines());
			}
			String[] arr = list.toArray(new String[list.size()]);
			return arrayToString(arr);
		}
		
		private void loadFromFile(File file) {
			lines.addAll(file.defaultModule.getLines());
			for(Module mod : file.modules) {
				lines.addAll(mod.getLines());
			}
		}
		
		public int getDefaultIndex() {
			return defaultIndex;
		}
	}
	
	public static class Module implements Comparator<Module>, Comparable<Module> {
		
		public static File file;
		public static int linesTilEnd = 0;
		public static boolean moduling = false;
		public static int nextIndex = 0;
		public static boolean nextEnclosed = false;
		public static String nextName = "";
		public static List<String> nextCmdLines = new ArrayList<String>();
		public static List<Module> opened = new ArrayList<Module>();
		public List<String> lines = new ArrayList<String>();
		public List<String> cmdLines = new ArrayList<String>();
		public int indexToInsertTo = -1;
		public boolean enclosed = false;
		public String name;
		
		public Module() {
			if(nextIndex != 0)
				this.indexToInsertTo = nextIndex;
		}
		
		public List<String> getLines(){
			if(enclosed) {
				return getEnclosedLines();
			}
			return lines;
		}
		
		public void erase(int index) {
			lines.remove(index);
		}
		
		public void write(String line) {
			lines.add(line);
		}
		
		public List<String> getEnclosedLines(){
			return lines.subList(1, lines.size() - 1);
		}
		
		public List<String> getRawLines(){
			List<String> rawLines = new ArrayList<String>();
			rawLines.addAll(cmdLines);
			rawLines.addAll(lines);
			return rawLines;
		}
		
		public int size() {
			return this.lines.size();
		}
		
		public static void clear() {
			linesTilEnd = 0;
			moduling = false;
			nextEnclosed = false;
			nextIndex = 0;
			opened.clear();
		}
		
		public void open() {
			moduling = true;
			if(nextEnclosed) {
				enclosed = true;
			}
			if(!nextCmdLines.isEmpty()) {
				this.cmdLines.addAll(nextCmdLines);
			}
			if(!nextName.isEmpty()) {
				this.name = nextName;
				nextName = "";
			}
			opened.add(this);
			file.modules.add(this);
			nextCmdLines.clear();
		}
		
		public void end() {
			opened.remove(this);
			linesTilEnd = 1;
			if(nextEnclosed) {
				nextEnclosed = false;
			}
			if(opened.isEmpty()) {
				moduling = false;
			}
		}
		
		public static void endLast() {
			opened.remove(opened.size() - 1);
			linesTilEnd = 1;
			if(nextEnclosed) {
				nextEnclosed = false;
			}
			if(opened.isEmpty()) {
				moduling = false;
			}
		}

		@Override
		public int compareTo(Module o) {
			// TODO Auto-generated method stub
			return Integer.compare(this.indexToInsertTo, o.indexToInsertTo);
		}

		@Override
		public int compare(Module o1, Module o2) {
			// TODO Auto-generated method stub
			return o1.indexToInsertTo - o2.indexToInsertTo;
		}
		
	}
	
	public static class Rules {
		public String name;
		public HashMap<Predicate<String>, Consumer<String>> rules = new HashMap<Predicate<String>, Consumer<String>>();
		public static HashMap<String, String> names = new HashMap<String, String>();
		
		public boolean test(String target) {
			boolean retVal = false;
			for(Predicate<String> condition : rules.keySet()) {
				if(condition.test(target)) {
					rules.get(condition).accept(target);
					retVal = true;
				}
			}
			return retVal;
		}
		
		public static Rules loadFromFile(CustomFile file) {
			BufferedReader reader = file.getReader();
			String line = null;
			Rules rules = new Rules();
			try {
				while((line = reader.readLine()) != null) {
					String[] words = line.split(" ");
					Predicate<String> condition = null;
					Consumer<String> action = null;
					switch(words[0]) {
					case "open_module":
						action = BasicRules.OPEN_MODULE.getAction();
						break;
					case "end_module":
						action = BasicRules.END_MODULE.getAction();
						break;
					case "signal_module_index":
						action = BasicRules.SIGNAL_MODULE_INDEX.getAction();
						break;
					case "signal_module_enclosed":
						action = BasicRules.SIGNAL_MODULE_ENCLOSED.getAction();
						break;
					case "signal_default":
						action = BasicRules.SIGNAL_DEFAULT.getAction();
						break;
					case "signal_module_begin":
						action = BasicRules.SIGNAL_MODULE_BEGIN.getAction();
						break;
					case "signal_module_name":
						action = BasicRules.SIGNAL_MODULE_NAME.getAction();
						break;
					case "remove_line":
						action = BasicRules.REMOVE_LINE.getAction();
						break;
					case "name":
						rules.name = words[1];
						break;
					}
					switch(words[1]) {
					case "ends_with":
						condition = s -> s.endsWith(words[words.length - 1]);
						break;
					case "starts_with":
						condition = s -> s.startsWith(words[words.length - 1]);
						break;
					case "contains":
						condition = s -> s.contains(words[words.length - 1]);
						break;
					case "equals":
						condition = s -> s.equals(words[words.length - 1]);
					}
					if(condition != null && action != null)
						rules.rules.put(condition, action);
					names.put(rules.name + "." + words[0], words[words.length - 1]);
				}
				reader.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return rules;
		}
		
	}
	
	public static enum BasicRules {
		END_MODULE(c -> {Module.endLast(); Module.nextIndex = 0; File.writeToDefault = true;}),
		OPEN_MODULE(c -> {Module mod = new Module(); mod.open();}),
		SIGNAL_MODULE_INDEX(c -> {Module.nextIndex = Integer.parseInt(c.substring(1, c.length())); File.writeToDefault = false; Module.nextCmdLines.add(c);}),
		SIGNAL_MODULE_ENCLOSED(c -> {Module.nextEnclosed = true; File.writeToDefault = false; Module.nextCmdLines.add(c);}),
		SIGNAL_DEFAULT(c -> {File.defaultIndexStatic = true;}),
		SIGNAL_MODULE_BEGIN(c -> {File.currentIndexPoints.put(c.replace(Rules.names.get(File.currentRulesName + "." + "signal_module_begin") + " ", ""), File.currentPointing); File.writeToDefault = false; Module.nextCmdLines.add(c);}),
		SIGNAL_MODULE_NAME(c -> {Module.nextName = c.replace(Rules.names.get(File.currentRulesName + "." + "signal_module_name") + " ", ""); File.writeToDefault = false; Module.nextCmdLines.add(c);}),
		REMOVE_LINE(c -> {c = "";});
		
		private Consumer<String> action;
		
		private BasicRules(Consumer<String> action) {
			this.action = action;
		}
		
		public Consumer<String> getAction(){
			return action;
		}
	}

}
