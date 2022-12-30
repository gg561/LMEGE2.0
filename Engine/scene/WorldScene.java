package scene;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import actors.Camera;
import util.CustomFile;

public abstract class WorldScene implements Scene {
	
	private HashMap<String, Collection<? extends Renderable>> renderables = new HashMap<String, Collection<? extends Renderable>>();
	private float waterHeight = 0;

	public void setSceneUp() {
		
	}
	
	@Override
	public Map<String, Collection<? extends Renderable>> getAllRenderables() {
		// TODO Auto-generated method stub
		return renderables;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Collection<? extends Renderable> getRenderables() {
		// TODO Auto-generated method stub
		return (Collection<? extends Renderable>) ((Stream<Renderable>) Stream.of(renderables.values().toArray(new Collection[renderables.values().size()])).flatMap(Collection::stream)).collect(Collectors.toList());
	}

	@Override
	public Collection<? extends Renderable> getRenderables(String id) {
		// TODO Auto-generated method stub
		return renderables.get(id);
	}
	
	@SuppressWarnings("unchecked")
	public void addRenderable(String id, Renderable renderable) {
		((Collection<Renderable>) renderables.get(id)).add(renderable);
	}
	
	@SuppressWarnings("unchecked")
	public void removeRenderable(String id, Renderable renderable) {
		((Collection<Renderable>) renderables.get(id)).remove(renderable);
	}
	
	public void readScene(CustomFile worldFile) {
		
	}
	
	public Camera getCamera() {
		return null;
	}
	
	public float getWaterHeight() {
		return waterHeight;
	}

}
