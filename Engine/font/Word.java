package font;

import org.joml.Vector2f;
import org.joml.Vector3f;

import renderer.Loader;
import textures.Texture;

public class Word {
	
	private Caractere[] caracteres;
	
	public Word(Loader loader, String str, Vector2f position, Vector2f size, GameFont font, Vector3f color) {
		fillCaracteres(loader, str, font, color);
		organizeCaracteres(position, size);
	}
	
	public void fillCaracteres(Loader loader, String str, GameFont font, Vector3f color) {
		fillCaracteres(loader, str.toCharArray(), font, color);
	}
	
	public void fillCaracteres(Loader loader, char[] chars, GameFont font, Vector3f color) {
		caracteres = new Caractere[chars.length];
		for(int i = 0; i < chars.length; i++) {
			caracteres[i] = new Caractere(loader, chars[i], font, color);
		}
	}
	
	public void organizeCaracteres(Vector2f position, Vector2f size) {
		organizeCaracteres(caracteres, position, size);
	}
	
	public void organizeCaracteres(Caractere[] caracteres, Vector2f position, Vector2f size) {
		Vector3f proxyPosition = new Vector3f(position, 0);
		float x = 0;
		for(int i = 0; i < caracteres.length; i++) {
			Caractere c = caracteres[i];
			float absVal = Math.abs(c.getGlyph().getVertices()[0]);//Vertices[0] is 1/2 of the width of the character.
			c.setScale(new Vector3f(size, 0));
			x = absVal * c.getScale().x; //Add an offset of half a width of the character (A.K.A absVal)
			c.setPosition(new Vector3f(proxyPosition.add(x, 0, 0)));
			proxyPosition.add(x, 0, 0);// Add a second offset of half of a width of the character to complete a character, thus the next character will start at the right position. (1/2 + 1/2 = 1)
		}
		
	}

	public Caractere[] getCaracteres() {
		return caracteres;
	}

}
