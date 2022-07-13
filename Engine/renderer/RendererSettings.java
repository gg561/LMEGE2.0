package renderer;

import java.util.function.Consumer;

import org.lwjgl.opengl.*;

public class RendererSettings {
	
	public enum Enabled {
		
		CULL_BACKFACE(GL11.GL_CULL_FACE, new Runnable(){public void run() {GL11.glCullFace(GL11.GL_BACK); GL11.glFrontFace(GL11.GL_CCW);}});
		
		private int enable;
		private Runnable value;
		
		private Enabled(int enable, Runnable value) {
			this.enable = enable;
			this.value = value;
		}
		
		public Runnable getValue() {
			return value;
		}
		
		public int getEnabled() {
			return enable;
		}
	}
	
	public enum Disabled {
		
	}

}
