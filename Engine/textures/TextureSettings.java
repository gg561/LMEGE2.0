package textures;

import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL11;

public enum TextureSettings {
	
	MAG_FILTER(GL13.GL_TEXTURE_2D, GL13.GL_LINEAR),
	MIN_FILTER(GL13.GL_TEXTURE_2D, GL11.GL_LINEAR_MIPMAP_LINEAR);
	
	private int location;
	private int value;
	
	private TextureSettings(int location, int value) {
		this.location = location;
		this.value = value;
	}
	
	public int getLocation() {
		return location;
	}
	
	public int getValue() {
		return value;
	}

}
