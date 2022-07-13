package renderer;

import baseComponents.Vao;
import models.Model;

public class Loader {
	
	public Model loadToVAO(float[] positions,float[] textureCoords, float[] normals, int[] indices){
		Vao vao = new Vao(indices.length);
		vao.storeData(indices, positions.length/3, positions, textureCoords, normals);
		return new Model(vao);
	}
	
	public void cleanUp() {
		Vao.cleanUp();
	}

}
