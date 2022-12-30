package gui;

import font.GameFont;
import font.Text;

public class Label {
	
	private String text;
	private Text guiElement;
	private GameFont font;
	
	public Label(String text, GameFont font) {
		this.text = text;
		guiElement = null;
	}

}
