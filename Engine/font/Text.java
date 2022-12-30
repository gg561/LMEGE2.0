package font;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.joml.Vector2f;
import org.joml.Vector3f;

import renderer.Loader;
import textures.Texture;

public class Text {
	
	private Word[][] words;
	
	public Text(Loader loader, String text, Vector2f position, Vector2f size, GameFont font, Vector3f color) {
		makeLines(loader, text, position, size, font, color);
	}
	
	public void makeLines(Loader loader, String text, Vector2f position, Vector2f size, GameFont font, Vector3f color) {
		String[] lines = text.split("\n");
		words = new Word[lines.length][];
		for(int i = 0; i < lines.length; i++) {
			words[i] = makeLine(loader, lines[i], position, size, font, color);
		}
	}
	
	public Word[] makeLine(Loader loader, String line, Vector2f position, Vector2f size, GameFont font, Vector3f color) {
		String[] args = line.split(" ");
		Word[] ln = new Word[args.length];
		int space = 0;
		for(int i = 0; i < args.length; i++) {
			ln[i] = new Word(loader, line, position.add(space, 0, new Vector2f()), size, font, color);
			space = 1;
		}
		return ln;
	}
	
	public List<Caractere> getCaracteres(){
		List<Caractere> caracteres = new ArrayList<Caractere>();
		for(Word[] line : words) {
			for(Word word : line) {
				for(Caractere c : word.getCaracteres()) {
					caracteres.add(c);
				}
			}
		}
		return caracteres;
	}

}
