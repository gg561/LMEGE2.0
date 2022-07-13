package models;

import baseComponents.Vao;
import scene.Renderable;

public class Model implements Renderable {
	
	private Vao vao;
	private boolean shouldRender;
	
	public Model(Vao vao) {
		this.vao = vao;
	}
	
	public Vao getVao() {
		return vao;
	}
	
	public void delete() {
		vao.delete();
	}
	
	public boolean shouldRender() {
		return shouldRender;
	}
	
	public void shouldRender(boolean shouldRender) {
		this.shouldRender = shouldRender;
	}

}
