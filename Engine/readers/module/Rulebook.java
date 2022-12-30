package readers.module;

import java.util.function.Consumer;

public class Rulebook {
	
	/**
	 * 
	 * The last numbers that are of the last 4 bits have no arguments
	 * Numbers of the 2nd last 4 bits have 1 argument
	 * Numbers of the 3rd last 4 bits have 2 arguments
	 * Numbers of the first 4 bits have 3 arguments
	 * 
	 * Those that occupies both the last and 2nd last 4 bits have variable argument count.
	 * 
	 */
	
	//Last 4 bits A.K.A has no arguments
	public static final int MOD_OPEN = 0x001;//  	 0000 0000 0000 0001
	public static final int MOD_CLOSE = 0x002;// 	 0000 0000 0000 0010
	public static final int SYMBOL_END = 0x003;// 	 0000 0000 0000 0011
	public static final int MOD_GLOBAL = 0x004;//	 0000 0000 0000 0100
	public static final int CALL_GLOBAL = 0x005;//	 0000 0000 0000 0101
	
	//2nd last 4 bits A.K.A. has 1 argument
	public static final int MOD_NAME = 0x010;//  	 0000 0000 0001 0000
	public static final int MOD_INSERT = 0x020;//	 0000 0000 0010 0000
	
	//Last & 2nd last 4 bits A.K.A has no limits on argument count
	public static final int MOD_VAR = 0x011;//   	 0000 0000 0001 0001
	public static final int MOD_INSERT_VAR = 0x012;//0000 0000 0001 0010

}
