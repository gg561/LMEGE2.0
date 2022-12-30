package readers.module;

public class Line {
	
	private StringBuilder line;
	public final ModuleReader reader;
	
	public Line(String line, ModuleReader reader) {
		this.line = new StringBuilder(line);
		this.reader = reader;
	}
	
	public String line() {
		return line.toString();
	}
	
	public void line(String line) {
		this.line.setLength(0);
		this.line.insert(0, line);
	}

}
