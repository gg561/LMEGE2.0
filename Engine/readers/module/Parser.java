package readers.module;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Stream;

import util.CustomFile;

public class Parser {
	
	/**
	 * A map that contains the Symbols and their respective behaviour stored in  {@link java.util.function.Function Functions}
	 */
	private final HashMap<Symbol, Function<Line, String>> lawbook = new HashMap<Symbol, Function<Line, String>>();
	
	/**
	 * 
	 * Returns a code for comparing and determining the actions that are supposed to be done to the line. 
	 * Returns 0 for no results
	 * 
	 * @param line
	 * @return symbolId if a symbol is present, 0 if no symbols are present
	 */
	public int parseId(String line) {
		int rv = 0;
		Stream<Symbol> symbols = lawbook.keySet().stream().filter(s -> line.contains(s.getSymbol()));
		List<Symbol> list = symbols.toList();
		for(Symbol symbol : list) {
			rv = rv | symbol.getId();
		}
		return rv;
	}
	
	/**
	 * 
	 * Instead of returning a code, this method returns a boolean in the case that a symbol has been detected
	 * inside the given line, and executes the behaviour assigned to that symbol.
	 * 
	 * @param line
	 * @return true if line contains symbols, and false if it does not
	 */
	public boolean parseExec(Line line) {
		Stream<Symbol> symbols = lawbook.keySet().stream().filter(s -> line.line().contains(s.getSymbol()));
		List<Symbol> list = new ArrayList<Symbol>(symbols.toList());
		list.sort(new Symbol(null, null, -1, -1));
		boolean rv = list.size() != 0;
		for(Symbol symbol : list) {
			Function<Line, String> action = lawbook.get(symbol);
			//System.out.println(line.line());
			if(action != null)
				try {
					line.line(action.apply(line));
				}catch (Exception e) {
					
				}
		}
		return rv;
	}
	/**
	 * 
	 * Reads and parses the symbol rules from a specific rules file
	 * 
	 * TODO Change switch-case impl. to a Strategy Pattern impl.
	 * 
	 * @param file
	 */
	public void readFromFile(CustomFile file) {
		file.read(l -> {
			String[] args = l.split(" ");
			Function<Line, String> action = null;
			int id = -1;
			int order = 0;
			switch (args[0]) {
			case "open_module":
				action = BasicActions.MOD_OPEN.getAction();
				id = Rulebook.MOD_OPEN;
				break;
			case "close_module":
				id = Rulebook.MOD_CLOSE;
				break;
			case "name_module":
				action = BasicActions.MOD_NAME.getAction();
				id = Rulebook.MOD_NAME;
				break;
			case "insert_module":
				action = BasicActions.MOD_INSERT.getAction();
				id = Rulebook.MOD_INSERT;
				break;
			case "var_module":
				action = BasicActions.MOD_INSERT.getAction();
				id = Rulebook.MOD_INSERT;
				break;
			case "insert_var_module":
				action = BasicActions.MOD_INSERT_VAR.getAction();
				id = Rulebook.MOD_INSERT_VAR;
				break;
			case "end_symbol":
				id = Rulebook.SYMBOL_END;
				break;
			case "global_module":
				action = BasicActions.MOD_GLOBAL.getAction();
				id = Rulebook.MOD_GLOBAL;
				break;
			case "global_call":
				action = BasicActions.MOD_INSERT_GLOBAL.getAction();
				id = Rulebook.CALL_GLOBAL;
				break;
			}
			if(args.length > 2) {
				order = Integer.parseInt(args[2]);
			}
			Symbol symbol = new Symbol(args[0], args[1], id, order);
			lawbook.put(symbol, action);
		});
	}
	
	/**
	 * 
	 * Basic actions that will be done each when called for.
	 * The below actions are stored in {@link java.util.function.Function Functions} and not {@link java.reflect.Method Methods}
	 * for the simple reason that Functions could be defined and executed
	 * at any time, and stored when unused, whereas Methods
	 * would require Unsafe.Manipulations. to be defined and
	 * used. Functions is thus, in this case, much safer and
	 * more reliable.
	 * 
	 * @author huangyoulin
	 *
	 */
	public static enum BasicActions{
		
		MOD_OPEN(c -> {c.reader.createModule(c.reader.getLastOpened(), c.reader.current(), c.reader.getDataCentr().getData()); return c.line();}),
		MOD_NAME(c -> {c.reader.getLastTemplate().setName(c.reader.parser().args(c.reader.parser().findSymbol("name_module"), c.line())[1]); return c.line();}),
		MOD_INSERT(c ->  {return c.reader.getDataCentr().findModule(c.line().split(" ")[1]).getDataAsLine();}),
		MOD_INSERT_GLOBAL(c -> {return c.reader.getDataCentr().findGlobalModule(c.line().split(" ")[1]).getDataAsLine();}),
		MOD_VAR(c -> {return c.line();}),
		MOD_INSERT_VAR(c -> {return c.reader.getDataCentr().findModule(c.line().split(" ")[1]).getVariableAppropriateData(c.reader.parser().replacements(c.reader.parser().findSymbol("insert_var_module"), c.line()));}),
		MOD_GLOBAL(c -> {c.reader.getLastTemplate().setGlobal(true); return c.line();});
		
		
		private Function<Line, String> action;
		
		private BasicActions(Function<Line, String> action) {
			this.action = action;
		}
		
		public Function<Line, String> getAction(){
			return action;
		}
		
	}
	
	/**
	 * 
	 * Finds the symbol based on the name given.
	 * 
	 * @param name
	 * @return symbol
	 */
	public Symbol findSymbol(String name) {
		return lawbook.keySet().stream().filter(s -> s.getName().equals(name)).findFirst().get();
	}
	
	
	/**
	 * 
	 * Fetches all the arguments that were assigned to the symbol, based on 
	 * how many arguments the given symbol could possess.
	 * 
	 * @param symbol
	 * @param line
	 * @return arguments
	 */
	private String[] args(Symbol symbol, String line) {
		int arguments = numberOfArguments(symbol);
		int index = line.lastIndexOf(symbol.getSymbol());
		String[] args = line.substring(index).split(" ");
		String[] rv = new String[arguments + 1];
		System.arraycopy(args, 0, rv, 0, arguments + 1);
		return rv;
	}
	
	/**
	 * 
	 * For unlimited arguments, use this function.
	 * For explanations 
	 * 
	 * @see #args(Symbol, String)
	 * @param symbol
	 * @param line
	 * @return arguments
	 */
	@SuppressWarnings("unused")
	private String[] infArgs(Symbol symbol, String line) {
		List<String> args = new ArrayList<String>();
		int index = line.lastIndexOf(symbol.getSymbol());
		String[] arguments = line.substring(index).split(" ");
		for(String arg : arguments) {
			if(arg.equals(findSymbol("end_symbol").getSymbol())) break;
			args.add(arg);
		}
		return args.toArray(String[]::new);
	}
	
	/**
	 * 
	 * Fetches the replacements for text-bits inside the Module.
	 * 
	 * @param symbol
	 * @param line
	 * @return replacements
	 */
	private Map<String, String> replacements(Symbol symbol, String line){
		Map<String, String> args = new HashMap<String, String>();
		int index = line.lastIndexOf(symbol.getSymbol());
		String[] arguments = line.substring(index).split(" ");
		for(int i = 2; i < arguments.length; i++) {
			String arg = arguments[i];
			if(arg.equals(findSymbol("end_symbol").getSymbol())) break;
			String[] sides = arg.split("=");
			args.put(sides[0], sides[1]);
		}
		return args;
	}
	
	/**
	 * 
	 * Determines the number of arguments that can be assigned to a symbol based on its ID structure.
	 * 
	 * @param symbol
	 * @return
	 */
	private int numberOfArguments(Symbol symbol) {
		int args = symbol.getId();
		for(int i = 0; i < 4; i ++) {
			if((args & (0x0F << (i * 4))) != 0) {
				args = i;
			}
			if((args & (0x0F << 4)) != 0 && (args & (0x0F << 0)) != 0) {
				System.out.println(args & ((0x0F << 4) | (0x0F << 0)));
				args = -1;
			}
			/*
			if(i > 0) {
				if((id & ((0x0F << i) | (0x0F << (i - 1)))) != 0) {
					
				}
			}*/
		}
		return args;
	}
	
	public void clear() {
		lawbook.clear();
		
	}

}
