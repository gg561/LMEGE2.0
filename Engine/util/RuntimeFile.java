package util;

public class RuntimeFile {
	
	private String[] lines;
	
	public RuntimeFile(String...lines) {
		this.lines = lines;
	}
	
	public void writeLines(int index, String... lines) {
		String[] newLines = new String[this.lines.length + lines.length];
		System.arraycopy(this.lines, 0, newLines, 0, this.lines.length);
		System.arraycopy(lines, 0, newLines, index, lines.length);
		this.lines = newLines;
	}
	
	public String[] getLines() {
		return lines;
	}

}
