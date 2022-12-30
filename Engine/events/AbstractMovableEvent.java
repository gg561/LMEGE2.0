package events;

import org.joml.Vector3f;

import actors.Movable;

public abstract class AbstractMovableEvent extends AbstractEvent<Movable> {
	
	public static final DefaultMovableListener<AbstractMovableEvent> LISTENER = new DefaultMovableListener<AbstractMovableEvent>();
	
	private boolean active;
	private boolean paused;
	private Vector3f v0;
	private Vector3f v1;
	private Movable object;
	
	public AbstractMovableEvent(Vector3f v0, Vector3f v1, Movable object) {
		this.v0 = v0;
		this.v1 = v1;
		this.object = object;
	}

	@Override
	public void start() {
		// TODO Auto-generated method stub
		active = true;
	}

	@Override
	public void stop() {
		// TODO Auto-generated method stub
		active = false;
	}
	
	public Vector3f getV0() {
		return v0;
	}
	
	public Vector3f getV1() {
		return v1;
	}
	
	public void setV1(Vector3f v1) {
		this.v1 = v1;
	}
	
	public Movable getObject() {
		return object;
	}
	
	public void setObject(Movable object) {
		this.object = object;
	}
	
	public boolean isPaused() {
		return paused;
	}
	
	public boolean isActive() {
		return active;
	}
	
}
