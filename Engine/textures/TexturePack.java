package textures;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.lwjgl.opengl.GL13;

public class TexturePack {
	
	private List<Texture> textures = new ArrayList<Texture>();
	
	public TexturePack(Texture...textures) {
		this.setTextures(Arrays.asList(textures));
	}

	public List<Texture> getTextures() {
		return textures;
	}

	public void setTextures(List<Texture> textures) {
		this.textures = textures;
	}
	
	public void bind() {
		for(Texture texture : textures) {
			texture.bind(textures.indexOf(texture));
		}
	}
	

}
