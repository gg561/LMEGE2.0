package textures;

import util.CustomFile;

public class TextureStaticAssets {
	
	public static Texture WATER_DUDV;
	public static Texture WATER_NORMAL_MAP;
	
	public static void setWaterNormalMap(Texture texture) {
		WATER_NORMAL_MAP = texture;
	}
	
	public static void setWaterDuDv(Texture texture) {
		WATER_DUDV = texture;
	}

}
