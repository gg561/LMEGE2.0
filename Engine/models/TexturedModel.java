package models;

import textures.Texture;
import textures.TexturePack;

public class TexturedModel {
	
	private Model model;
	private Texture texture;
	private TexturePack additionalTextures = new TexturePack();
	private boolean useFakeLighting;
	private int textureIndex = 0;//for texture atlas
	
	public TexturedModel(Model model, Texture texture) {
		this.model = model;
		this.texture = texture;
	}
	
	public TexturedModel(Model model, Texture texture, int textureIndex) {
		this.model = model;
		this.texture = texture;
		this.textureIndex = textureIndex;
	}
	
	public int getTextureIndex() {
		return textureIndex;
	}
	
	public void setTextureIndex(int textureIndex) {
		this.textureIndex = textureIndex;
	}
	
	public Model getModel() {
		return model;
	}
	
	public Texture getTexture() {
		return texture;
	}

	public boolean isUseFakeLighting() {
		return useFakeLighting;
	}

	public void setUseFakeLighting(boolean useFakeLighting) {
		this.useFakeLighting = useFakeLighting;
	}

	public TexturePack getAdditionalTextures() {
		return additionalTextures;
	}

	public void setAdditionalTextures(TexturePack additionalTextures) {
		this.additionalTextures = additionalTextures;
	}

}
