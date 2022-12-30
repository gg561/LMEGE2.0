package textures;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;

import org.joml.Vector2f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL13;
import org.lwjgl.stb.STBImage;

import de.matthiasmann.twl.utils.PNGDecoder;
import util.CustomFile;

public class CubeMapTexture extends Texture {
	
	private CustomFile[] files = new CustomFile[6];
	
	public CubeMapTexture() {
		super(GL11.glGenTextures());
		this.setType(GL13.GL_TEXTURE_CUBE_MAP);
	}
	
	public static CubeMapTexture loadFromFile(CustomFile[] files, int size) throws IOException {
		CubeMapTexture cube = new CubeMapTexture();
		cube.bind();
		int i = 0;
		int[] widths = new int[6];
		int[] heights = new int[6];
		ByteBuffer[] images = new ByteBuffer[6];
		for(CustomFile file : files) {
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
			files[i] = file;
			int width = x[0];
			int height = y[0];
			widths[i] = width;
			heights[i] = height;
			images[i] = image;
			GL13.glTexImage2D(GL13.GL_TEXTURE_CUBE_MAP_POSITIVE_X + i, 0, GL11.GL_RGBA8, width, height, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, image);
			//STBImage.stbi_image_free(image);
			i++;
		}
		GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
		GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);
		GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL12.GL_TEXTURE_WRAP_R, GL12.GL_CLAMP_TO_EDGE);
		GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
		GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
		return cube;
	}
	
	public CustomFile[] getFiles() {
		return files;
	}
	
	public CustomFile getFile() {
		return null;
	}

}
