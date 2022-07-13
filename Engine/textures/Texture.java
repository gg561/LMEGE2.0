package textures;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.joml.Vector2f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL30;
import org.lwjgl.stb.STBImage;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;
import org.newdawn.slick.opengl.TextureLoader;

import util.CustomFile;

public class Texture {
	
	private static List<Texture> textures = new ArrayList<Texture>();
	
	private int id;
	private Vector2f scale;
	private float reflectivity = 0;
	private float shineDamper = 1;
	private boolean hasTransparency;
	private CustomFile file;
	
	public Texture() {
		id = GL11.glGenTextures();
		this.scale = new Vector2f(1, 1);
		Texture.settings();
		textures.add(this);
	}
	
	public Texture(CustomFile file, int size) {
		this.id = loadFromFile(file, size).id;
		this.scale = loadFromFile(file, size).scale;
		this.file = file;
		textures.add(this);
	}
	
	public Texture(int id) {
		this.id = id;
		this.scale = new Vector2f(1, 1);
		textures.add(this);
	}
	
	public void bind() {
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, id);
	}
	
	public void bind(int unit) {
		GL13.glActiveTexture(GL13.GL_TEXTURE0 + unit);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, id);
	}
	
	public void unbind() {
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
	}
	
	public void delete() {
		GL11.glDeleteTextures(id);
	}
	
	public int getId() {
		return id;
	}
	
	public static void settings() {
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);
		GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);
		GL11.glTexParameteri(TextureSettings.MAG_FILTER.getLocation(), GL13.GL_TEXTURE_MAG_FILTER, TextureSettings.MAG_FILTER.getValue());
		GL11.glTexParameteri(TextureSettings.MIN_FILTER.getLocation(), GL13.GL_TEXTURE_MIN_FILTER, TextureSettings.MIN_FILTER.getValue());
	}
	
	public static Texture loadFromFile(CustomFile file, int size) {
		int[] x = new int[1];
		int[] y = new int[1];
		int[] component = new int[1];
		//temp fix
		//temp fix
		ByteBuffer image = STBImage.stbi_load_from_memory(file.getByteBuffer(size), x, y, component, 4);
		if(image == null) {
			System.err.println("Could not load image " + file.getPath() + " " + STBImage.stbi_failure_reason() + "\n");
			throw new RuntimeException("Null image");
		}
		Texture texture = new Texture();
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture.getId());
		int width = x[0];
		int height = y[0];
		GL13.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA8, width, height, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, image);
		return texture;
	}
	
	public static Texture loadFromFile2(String format, CustomFile file) {
		try {
			org.newdawn.slick.opengl.Texture texture = TextureLoader.getTexture(format, file.getStream(), GL11.GL_NEAREST);
			Texture tex = new Texture(texture.getTextureID());
			return tex;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public static void cleanUp() {
		for(Texture texture : textures) {
			texture.delete();
		}
	}

	public float getReflectivity() {
		return reflectivity;
	}

	public void setReflectivity(float reflectivity) {
		this.reflectivity = reflectivity;
	}

	public float getShineDamper() {
		return shineDamper;
	}

	public void setShineDamper(float shineDamper) {
		this.shineDamper = shineDamper;
	}

	public boolean isHasTransparency() {
		return hasTransparency;
	}

	public void setHasTransparency(boolean hasTransparency) {
		this.hasTransparency = hasTransparency;
	}

	public CustomFile getFile() {
		return file;
	}

}
