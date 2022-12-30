package terrain;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.joml.Vector3f;

import models.Model;
import renderer.Loader;
import textures.Texture;
import textures.TexturePack;

public class MappedTerrain extends Terrain {
	
	public MappedTerrain(int gridX, int gridZ, Loader loader, TexturePack texturePack) {
		super(gridX, gridZ, loader, texturePack);
	}
	
	public MappedTerrain(int gridX, int gridZ, Loader loader, TexturePack texturePack, HeightMap heightMap) {
		super(gridX, gridZ, loader, texturePack, heightMap);
	}

	@Override
	protected void getTerrainDataFromHeightMap(Loader loader, HeightMap map) {
		// TODO Auto-generated method stub
		map.setModel(map.generateHeights(loader, this.getTextures().getTextures().get(5), SIZE));
		
	}

}
