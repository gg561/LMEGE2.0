package textures;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.commons.io.IOUtils;
import org.joml.Vector2f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL14;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.stb.STBImage;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;
import org.lwjglx.BufferUtils;
import org.newdawn.slick.opengl.TextureLoader;

import de.matthiasmann.twl.utils.PNGDecoder;
import de.matthiasmann.twl.utils.PNGDecoder.Format;
import util.CustomFile;

public class Texture {
	
	private static List<Texture> textures = new ArrayList<Texture>();
	
	private int id;
	private Vector2f scale;
	private float reflectivity = 0;
	private float shineDamper = 1;
	private boolean hasTransparency;
	private CustomFile file;
	private int rows = 1;
	private int type = GL11.GL_TEXTURE_2D;
	
	public Texture() {
		this(true);
	}	
	
	public Texture(boolean setFlags) {
		id = GL11.glGenTextures();
		this.scale = new Vector2f(1, 1);
		if(setFlags)
			Texture.settings(this.type, true);
		textures.add(this);
	}
	
	public Texture(CustomFile file, int size) {
		loadFromFile(file, size);
	}
	
	public Texture(int id) {
		this.id = id;
		this.scale = new Vector2f(1, 1);
		textures.add(this);
	}
	
	public Texture(Vector2f scale, int type) {
		id = GL11.glGenTextures();
		this.type = type;
		this.scale = scale;
		Texture.settings(this.type, true);
		textures.add(this);
	}
	
	public void bind() {
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(type, id);
	}
	
	public void bind(int unit) {
		GL13.glActiveTexture(GL13.GL_TEXTURE0 + unit);
		GL11.glBindTexture(type, id);
	}
	
	public void unbind() {
		GL11.glBindTexture(type, 0);
	}
	
	public void delete() {
		GL11.glDeleteTextures(id);
	}
	
	public int getId() {
		return id;
	}
	
	public int getType() {
		return type;
	}
	
	public void setType(int type) {
		this.type = type;
	}
	
	public static void settings(int type, boolean mipmap) {
		GL11.glTexParameteri(type, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
		GL11.glTexParameteri(type, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);
		if(mipmap) {
			GL30.glGenerateMipmap(type);
			GL11.glTexParameteri(type, GL13.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR_MIPMAP_LINEAR);
			GL11.glTexParameterf(type, GL14.GL_TEXTURE_LOD_BIAS, -0.4f);
		}else {
			GL11.glTexParameteri(type, GL13.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
		}
		GL11.glTexParameteri(type, GL13.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
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
		Texture texture = new Texture(false);
		texture.file = file;
		GL11.glBindTexture(texture.type, texture.getId());
		int width = x[0];
		int height = y[0];
		GL13.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA8, width, height, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, image);
		settings(texture.type, true);
		return texture;
	}
	
	public static Texture loadFromImage(BufferedImage image) {
		int width = image.getWidth();
		int height = image.getHeight();
		ByteBuffer buffer = ByteBuffer.allocateDirect(width * height * 4);
		int[] pixels = new int[width * height];
		image.getRGB(0, 0, width, height, pixels, 0, width);
		for(int h = 0; h < height; h++) {//h on top of w is very important. The order must be kept.
			for(int w = 0; w < width; w++) {
				int pixel = pixels[(h * width + w)];
				
				buffer.put((byte) ((pixel >> 16) & 0xFF));//r
				buffer.put((byte) ((pixel >> 8) & 0xFF));//g
				buffer.put((byte) ((pixel) & 0xFF));//b
				buffer.put((byte) ((pixel >> 24) & 0xFF));//alpha
			}
		}
		if(buffer == null) {
			System.err.println("Could not load BufferedImage");
			throw new RuntimeException("Null image");
		}
		buffer.flip();
		Texture texture = new Texture(false);
		texture.file = null;
		GL11.glBindTexture(texture.type, texture.getId());
		GL13.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA8, width, height, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buffer);
		settings(texture.type, true);
		return texture;
	}
	
	public static byte[] getDataFromImage(BufferedImage image) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] bytes = null;
		try {
			ImageIO.write(image, "PNG", baos);
			bytes = baos.toByteArray();
			baos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return bytes;
	}
	
	public static BufferedImage unloadToFile(CustomFile file) {
		int width = GL11.glGetTexLevelParameteri(GL11.GL_TEXTURE_2D, 0, GL11.GL_TEXTURE_WIDTH), height = GL11.glGetTexLevelParameteri(GL11.GL_TEXTURE_2D, 0, GL11.GL_TEXTURE_HEIGHT), format = GL11.glGetTexLevelParameteri(GL11.GL_TEXTURE_2D, 0, GL11.GL_TEXTURE_INTERNAL_FORMAT);
		int channels = 4;
		if(format == GL11.GL_RGB) {
			channels = 3;
		}
		ByteBuffer buffer = BufferUtils.createByteBuffer(width * height * channels);
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		GL11.glGetTexImage(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buffer);
		for(int x = 0; x < width; ++x) {
			for(int y = 0; y < height; ++y) {
				int i = (x + y * width) * channels;
				int r = buffer.get(i) & 0xFF; // Maxes the Values at 0xFF, which is hexadecimal for 255
				int g = buffer.get(i + 1) & 0xFF;
				int b = buffer.get(i + 2) & 0xFF;
				int a = 255;
				if(channels == 4) {
					a = buffer.get(i + 3) & 0xFF;
				}
				// each Byte has 8 bits, thus the distance between each value : 24 -> 16 -> 8 -> 0
				// say a = 1, r = 255, g = 255, and b = 255
				// 01000001 01111111 01111111 01111111
				//     A       R         G       B
				image.setRGB(x, y, (a << 24) | (r << 16) | (g << 8) | b);
			}
		}
		try {
			if(file != null) {
				File f = new File(Class.class.getResource(file.getPath()).getPath());
				ImageIO.write(image, "PNG", f);
				System.out.println(f.getAbsolutePath() + " " + width + " " + height);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return image;
	}
	
	public static Texture loadFromPNGDecoder(CustomFile file) {
		Texture tex = new Texture();
		try {
			InputStream in = file.getStream();
			PNGDecoder decoder = new PNGDecoder(in);
			tex.scale = new Vector2f(decoder.getWidth(), decoder.getHeight());
			ByteBuffer image = ByteBuffer.allocateDirect((int) (4 * tex.scale.x * tex.scale.y));
			decoder.decode(image, (int) (4 * tex.scale.x), Format.RGBA);
			image.flip();
			in.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.err.println("Could not load image from PNGDecoder");
		}
		return tex;
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
	
	public void setFile(CustomFile file) {
		this.file = file;
	}
	
	public int getRows() {
		return rows;
	}
	
	public void setRows(int rows) {
		this.rows = rows;
	}
	
	public Vector2f calculateOffset(int index) {
		int column = index % rows;
		float xOffset = ((float) column) / rows;
		int row = index / rows;
		float yOffset = ((float) row) / rows;
		return new Vector2f(xOffset, yOffset);
	}

}
