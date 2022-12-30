package baseComponents;

import org.lwjgl.opengl.GL30;

public class Rbo {
	
	private int id;
	
	public Rbo() {
		this.id = GL30.glGenRenderbuffers();
	}
	
	public int id() {
		return id;
	}
	
	public void bind() {
		GL30.glBindRenderbuffer(GL30.GL_RENDERBUFFER, id);
	}
	
	public void storeData(int internalformat, int width, int height) {
		GL30.glRenderbufferStorage(GL30.GL_RENDERBUFFER, internalformat, width, height);
	}
	
	public void delete() {
		GL30.glDeleteRenderbuffers(id);
	}

}
