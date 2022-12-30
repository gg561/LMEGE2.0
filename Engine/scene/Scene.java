package scene;

import java.util.Collection;
import java.util.Map;

public interface Scene {
	
	public Map<String, Collection<? extends Renderable>> getAllRenderables();
	
	public Collection<? extends Renderable> getRenderables();
	public Collection<? extends Renderable> getRenderables(String id);
	public void setSceneUp();

}
