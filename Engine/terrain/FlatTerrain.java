package terrain;

import models.Model;
import renderer.Loader;
import textures.Texture;
import textures.TexturePack;

public class FlatTerrain extends Terrain {

	public FlatTerrain(int gridX, int gridZ, Loader loader, TexturePack texturePack) {
		super(gridX, gridZ, loader, texturePack);
	}

	@Override
	protected void getTerrainDataFromHeightMap(Loader loader, HeightMap map) {
		// TODO Auto-generated method stub
		map.setModel(map.generateTerrainFlat(loader, VERTEX_COUNT, SIZE));
	}
}
