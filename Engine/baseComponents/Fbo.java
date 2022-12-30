package baseComponents;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL14;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL32;
import org.lwjglx.BufferUtils;

import game.Main;
import game.Window;
import textures.Texture;

public class Fbo implements BufferObject {
	
	private final int id;
	private Texture texture;
	private Texture depth; 
	private Rbo bufferAttachment; 
	private int width;
	private int height;
	
	public Fbo(int width, int height) {
		this.id = GL30.glGenFramebuffers();
		this.width = width;
		this.height = height;
		initialize();
	}
	
	public void initialize() {
		GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, id);
		GL11.glDrawBuffer(GL30.GL_COLOR_ATTACHMENT0);
	}
	
	public void bind() {
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
		GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, id);
		GL30.glViewport(0, 0, width, height);
	}
	
	public void bind(int type) {
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
		GL30.glBindFramebuffer(type, id);
		GL30.glViewport(0, 0, width, height);
	}
	
	public static void unbind(Window frame) {
		GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, 0);
		GL30.glViewport(0, 0, frame.getFramebufferWidth(), frame.getFramebufferHeight());
	}
	
	public Texture createTextureAttachment() {
		return createTextureAttachment(width, height);
	}
	
	public Texture createTextureAttachment(int width, int height) {
		texture = new Texture(false);
		texture.bind();
		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGB, width, height, 0, GL11.GL_RGB, GL11.GL_UNSIGNED_BYTE, (ByteBuffer) null);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
		GL32.glFramebufferTexture(GL30.GL_FRAMEBUFFER, GL30.GL_COLOR_ATTACHMENT0, texture.getId(), 0);
		return texture;
	}
	
	public Texture createDepthTextureAttachment() {
		return createDepthTextureAttachment(width, height);
	}
	
	public Texture createDepthTextureAttachment(int width, int height) {
		depth = new Texture(false);
		depth.bind();
		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL14.GL_DEPTH_COMPONENT32, width, height, 0, GL11.GL_DEPTH_COMPONENT, GL11.GL_FLOAT, (ByteBuffer) null);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
		GL32.glFramebufferTexture(GL30.GL_FRAMEBUFFER, GL30.GL_DEPTH_ATTACHMENT, depth.getId(), 0);
		return depth;
	}
	
	public Texture createShadowDepthTextureAttachment() {
		return createShadowDepthTextureAttachment(width, height);
	}
	
	public Texture createShadowDepthTextureAttachment(int width, int height) {
		depth = new Texture(false);
		depth.bind();
		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL14.GL_DEPTH_COMPONENT32, width, height, 0, GL11.GL_DEPTH_COMPONENT, GL11.GL_FLOAT, (ByteBuffer) null);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL13.GL_CLAMP_TO_BORDER);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL13.GL_CLAMP_TO_BORDER);
		GL11.glTexParameterfv(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_BORDER_COLOR, new float[] {1, 1, 1, 1});
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL30.GL_TEXTURE_COMPARE_MODE, GL30.GL_COMPARE_REF_TO_TEXTURE);
		GL32.glFramebufferTexture(GL30.GL_FRAMEBUFFER, GL30.GL_DEPTH_ATTACHMENT, depth.getId(), 0);
		return depth;
	}
	
	public Rbo createDepthBufferAttachment() {
		return createDepthBufferAttachment(width, height);
	}
	
	public Rbo createDepthBufferAttachment(int width, int height) {
		bufferAttachment = new Rbo();
		bufferAttachment.bind();
		bufferAttachment.storeData(GL11.GL_DEPTH_COMPONENT, width, height);
		GL30.glFramebufferRenderbuffer(GL30.GL_FRAMEBUFFER, GL30.GL_DEPTH_ATTACHMENT, GL30.GL_RENDERBUFFER, bufferAttachment.id());
		return bufferAttachment;
	}
	
	public void delete() {
		GL30.glDeleteFramebuffers(id);
		if(bufferAttachment != null)
			bufferAttachment.delete();
		if(texture != null)
			texture.delete();
		if(depth != null)
			depth.delete();
	}
	
	 public int getId() {
		 return id;
	 }
	 
	 public Texture getTexture() {
		 return texture;
	 }
	 
	 public Texture getDepthTexture() {
		 return depth;
	 }
	 
	 public Rbo getDepthBuffer() {
		 return bufferAttachment;
	 }

}
