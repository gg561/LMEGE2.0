package readers.module;

import java.io.BufferedReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.TreeMap;

import util.CustomFile;

public class ModuleReader {
	
	private DataCenter dataCentr = new DataCenter();
	private Parser parser = new Parser();
	private Module lastOpened;
	private int current = 0;
	private ModuleTemplate lastTemplate = new ModuleTemplate(this);
	
	{
		lastTemplate.setSuprModule(dataCentr.getDefaultModule());
	}
	
	/**
	 * 
	 * Reads data from file to parser to form new rules and behaviours.
	 * 
	 * @see Parser#readFromFile(CustomFile)
	 * @param file
	 */
	public void readToParser(CustomFile file) {
		parser().readFromFile(file);
	}
	
	/**
	 * 
	 * This method initiates some important data inside the {@link #dataCentr file} field.
	 * 
	 * @param path
	 * @param data
	 * @return
	 */
	private DataCenter buildDefaultFile(String path, List<String> data) {
		this.dataCentr.setPath(path);
		this.dataCentr.setData(data);
		return this.dataCentr;
	}
	
	/**
	 * 
	 * Reads data from an outside source and pumps it into the program.
	 * 
	 * @see #buildDefaultFile(String, List)
	 * @see #convertFileToData(CustomFile)
	 * @param file
	 * @return
	 */
	public List<String> readToData(CustomFile file) {
		List<String> data = convertFileToData(file);
		buildDefaultFile(file.getPath(), data);
		return data;
	}
	
	/** @see #readToData(CustomFile) */
	public List<String> readToData(String path, String file){
		List<String> data = convertFileToData(file);
		buildDefaultFile(path, data);
		return data;
	}

	/** @see #readToData(CustomFile) */
	public List<String> readToData(String path, List<String> file){
		buildDefaultFile(path, file);
		return file;
	}
	
	/**
	 * 
	 * Reads through an outside source and compiles it into a {@link java.util.List List<String>}
	 * The method uses a {@link java.io.BufferedReader BufferedReader}
	 * 
	 * @param file
	 * @return data
	 */
	public List<String> convertFileToData(CustomFile file){
		try {
			List<String> data = new ArrayList<String>();
			BufferedReader reader = file.getReader();
			String line;
			while((line = reader.readLine()) != null) {
				data.add(line);
			}
			reader.close();
			return data;
		}catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 
	 * Converts a String of data into a {@link java.util.List List<String>}
	 * 
	 * @param file
	 * @return data
	 */
	public List<String> convertFileToData(String file){
		return new ArrayList<String>(Arrays.asList(file.split("\n")));
	}
	
	/**
	 * 
	 * 
	 * 
	 * @param data
	 * @return
	 */
	public List<String> processData(List<String> data) {
		Line line = new Line("", this);
		int i = 0;
		for(String l : data) {
			line.line(l);
			if(parser().parseExec(line)) {
				data.set(i, line.line());
				//System.err.println(line.line());
			}
			i++;
			current ++;
		}
		return data;
	}
	
	public List<String> processData(List<String> data, PrintStream stream) {
		Line line = new Line("", this);
		int i = 0;
		for(String l : data) {
			line.line(l);
			if(parser().parseExec(line)) {
				data.set(i, line.line());
				stream.println(line.line());
			}
			i++;
			current ++;
		}
		return data;
	}
	
	/**
	 * 
	 * Combines reading and processing into 1 step
	 * 
	 * Currently due to design flaws, this method must iterate over the file twice
	 * 
	 * The method createModule() takes in the entire data of the file in order to begin at the right index,
	 * and THEN iterate to the end, which is not possible for BufferedReaders
	 * 
	 * TODO fix design pattern bug to optimize this from O(2n) to O(n)
	 * TODO TF WAS I THINKING WHEN I MADE THIS
	 * 
	 * @param file
	 * @return
	 */
	public List<String> readAndProcess(CustomFile file){
		return processData(readToData(file));
	}
	
	public List<String> readAndProcess(String path, String file){
		return processData(readToData(path, file));
	}
	
	/**
	 * 
	 * Combines converting and processing into 1 step
	 * 
	 * @param file
	 * @return
	 */
	public List<String> convertAndProcess(CustomFile file){
		try {
			List<String> data = new ArrayList<String>();
			BufferedReader reader = file.getReader();
			String line;
			Line l = new Line("", this);
			while((line = reader.readLine()) != null) {
				l.line(line);
				if(parser().parseExec(l)) {
					//TODO add logic in case parseExec returns true
				}
				data.add(l.line());
				current ++;
			}
			reader.close();
			return data;
		}catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 
	 * TO BE ADDED
	 * 
	 * @param data
	 * @param begin
	 * @param end
	 * @return
	 */
	public List<String> processData(List<String> data, int begin, int end) {
		return data;
	}
	
	/**
	 * 
	 * Binds the file data into the default file
	 * Use for binding source files
	 * 
	 * @param file
	 * @return
	 */
	public String bind(CustomFile file) {
		current(0);
		return String.join("\n", readAndProcess(file));
	}
	
	/**
	 * 
	 * Runs over a file and replaces the symbols with the right replacements
	 * Use for making output files
	 * 
	 * @param file
	 * @return
	 */
	public String make(CustomFile file) {
		current(0);
		return String.join("\n", convertAndProcess(file));
	}
	
	/**
	 * 
	 * Gets the current reading/writing position
	 * 
	 * @return
	 */
	public int current() {
		return current;
	}
	
	/**
	 * Sets the current reading/writing position
	 * @param current
	 */
	public void current(int current) {
		this.current = current;
	}
	
	/**
	 * Clears all cached data
	 */
	public void clear() {
		dataCentr.clear();
	}
	
	public DataCenter getDataCentr() {
		return dataCentr;
	}
	
	public ModuleTemplate getLastTemplate() {
		return lastTemplate;
	}
	
	public Module getLastOpened() {
		return lastOpened;
	}
	
	public static Module createModule(String begin, String close, DataCenter file, Module suprModule, ModuleTemplate template, int current, List<String> data) {
		int start = current, end = current + 1;
		List<String> inputData = new ArrayList<String>(data);
		try {
			int endCounter = 0;
			for(;; end++) {
				if(((data.get(end).contains(begin) ? Rulebook.MOD_OPEN : 0) & Rulebook.MOD_OPEN) == Rulebook.MOD_OPEN) {
					endCounter++;
				}
				if(endCounter > 0){
					inputData.set(end, null);
				}
				if(((data.get(end).contains(close) ? Rulebook.MOD_CLOSE : 0) & Rulebook.MOD_CLOSE) == Rulebook.MOD_CLOSE) {
					endCounter--;
				}
				if(endCounter < 0) break;
			}
		} catch (IndexOutOfBoundsException e) {
			throw new RuntimeException("UNCLOSED MODULE AT : " + start + " IN FILE : " + file);
		}
		template.setStart(start);
		template.setEnd(end);
		template.create();
		Module mod = template.getFinalProduct();
		mod.parseAdd(inputData);
		mod.getData().removeIf(e -> e == null);
		return mod;
	}
	
	/**
	 * 
	 * Creates a new module, based on the current position.
	 * 
	 * TODO add multi-layer module creation support by cancelling out a 
	 * 
	 * @param suprModule
	 * @param current
	 * @param data
	 * @return
	 */
	public static Module createModule(Parser parser, DataCenter dataCentr, Module suprModule, ModuleTemplate template, int current, List<String> data) {
		int start = current, end = current + 1;
		List<String> inputData = new ArrayList<String>(data);
		try {
			int endCounter = 0;
			for(;; end++) {
				if((parser.parseId(data.get(end)) & Rulebook.MOD_OPEN) == Rulebook.MOD_OPEN) {
					endCounter++;
				}
				if(endCounter > 0){
					inputData.set(end, null);
				}
				if((parser.parseId(data.get(end)) & Rulebook.MOD_CLOSE) == Rulebook.MOD_CLOSE) {
					endCounter--;
				}
				if(endCounter < 0) break;
			}
		} catch (IndexOutOfBoundsException e) {
			throw new RuntimeException("UNCLOSED MODULE AT : " + start + " IN FILE : " + dataCentr);
		}
		template.setStart(start);
		template.setEnd(end);
		template.create();
		Module mod = template.getFinalProduct();
		mod.parseAdd(inputData);
		mod.getData().removeIf(e -> e == null);
		dataCentr.getModules().add(mod);
		return mod;
	}
	
	public Module createModule(Module suprModule, int current, List<String> data) {
		lastOpened = createModule(parser, dataCentr, suprModule, lastTemplate, current, data);
		return lastOpened;
	}

	public Parser parser() {
		return parser;
	}

	public void parser(Parser judge) {
		this.parser = judge;
	}

}
