package Engine.models;

import Engine.textures.Texture;

public class TexturedModel {
	
	private Model model;
	private Texture texture;
	private boolean useFakeLighting;
	
	public TexturedModel(Model model, Texture texture) {
		this.model = model;
		this.texture = texture;
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
