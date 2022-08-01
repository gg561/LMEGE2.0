package models;

import textures.Texture;

public class TexturedModel {
	
	private Model model;
	private Texture texture;
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

}
