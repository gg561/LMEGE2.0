package readers.module;

import java.util.Comparator;

public class Symbol implements Comparable<Symbol>, Comparator<Symbol> {
	
	private String name;
	private String symbol;
	private int id;
	private int order;
	
	public Symbol(String name, String symbol, int id, int order) {
		this.name = name;
		this.symbol = symbol;
		this.id = id;
		this.order = order;
	}
	
	public boolean equals(Symbol symbol) {
		if(symbol.symbol.equals(this.symbol)) {
			return true;
		}
		return false;
	}
	
	public boolean equals(String symbol) {
		if(symbol.equals(this.symbol)) {
			return true;
		}
		return false;
	}
	
	public String getSymbol() {
		return symbol;
	}
	
	public String toString() {
		return symbol;
	}

	public int getId() {
		return id;
	}
	
	public int getOrder() {
		return order;
	}

	public String getName() {
		return name;
	}

	@Override
	public int compareTo(Symbol o) {
		// TODO Auto-generated method stub
		return Integer.compare(order, o.order);
	}

	@Override
	public int compare(Symbol o1, Symbol o2) {
		// TODO Auto-generated method stub
		return o1.compareTo(o2);
	}

}
