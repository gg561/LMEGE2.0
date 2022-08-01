package renderer;

import baseComponents.Vao;
import models.Model;

public class Loader {
	
	public Model loadToVAO(float[] positions,float[] textureCoords, float[] normals, int[] indices){
		Vao vao = new Vao(indices.length);
		vao.storeData(indices, positions.length/3, positions, textureCoords, normals);
		return new Model(vao);
	}
	
	public Model loadToVAO2D(float[] positions){
		Vao vao = new Vao(positions.length/2);//subsitute index count for vertex count, since they both have the same uses in this context (GUI)
		vao.bindVao();
		vao.storeData(positions.length/2, positions);
		vao.unbindVao();
		return new Model(vao);
	}
	
	public Model loadVAO3D(float[] positions) {
		Vao vao = new Vao(positions.length/3);//subsitute index count for vertex count, since they both have the same uses in this context (GUI)
		vao.bindVao();
		vao.storeData(positions.length/3, positions);
		vao.unbindVao();
		return new Model(vao);
	}
	
	public Model loadLine(float[] positions, float[] colors) {
		Vao vao = new Vao(positions.length/3);//subsitute index count for vertex count, since they both have the same uses in this context (GUI)
		vao.bindVao();
		vao.storeData(positions.length/3, positions, colors);
		vao.unbindVao();
		return new Model(vao);
	}
	
	public void cleanUp() {
		Vao.cleanUp();
	}

}
