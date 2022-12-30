package renderer;

import baseComponents.Vao;
import models.Model;

public class Loader {
	
	public Model loadToVAO(float[] positions,float[] textureCoords, float[] normals, int[] indices){
		Vao vao = new Vao(indices.length);
		vao.storeData(indices, positions.length/3, positions, textureCoords, normals);
		return new Model(vao);
	}
	
	public Model loadToVAONormalMap(float[] positions, float[] textureCoords, float[] normals, int[] indices, float[] tangents) {
		Vao vao = new Vao(indices.length);
		vao.storeData(indices, positions.length/3, positions, textureCoords, normals, tangents);
		return new Model(vao);
	}
	
	public Model loadToVAO(float[] positions, int dimensions){
		Vao vao = new Vao(positions.length/dimensions);//subsitute index count for vertex count, since they both have the same uses in this context (GUI)
		vao.bindVao();
		vao.storeData(positions.length/dimensions, positions);
		vao.unbindVao();
		return new Model(vao);
	}
	
	public Model loadToVAO(int dimensions, float[]...datas){
		float[] positions = datas[0];
		Vao vao = new Vao(positions.length/dimensions);//subsitute index count for vertex count, since they both have the same uses in this context (GUI)
		vao.bindVao();
		vao.storeData(positions.length/dimensions, datas);
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
