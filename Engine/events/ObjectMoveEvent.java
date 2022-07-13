package events;

import org.joml.Vector3f;

import actors.Movable;

public class ObjectMoveEvent implements Event {
	
	private boolean active;
	private boolean paused;
	private Vector3f originalPosition;
	private Vector3f newPosition;
	private Movable object;
	
	public ObjectMoveEvent(Vector3f originalPosition, Vector3f newPosition, Movable object) {
		this.originalPosition = originalPosition;
		this.newPosition = newPosition;
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

	@Override
	public void finish() {
		// TODO Auto-generated method stub
		active = false;
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		paused = true;
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		paused = false;
	}
	
	public Vector3f getOriginalPosition() {
		return originalPosition;
	}
	
	public Vector3f getNewPosition() {
		return newPosition;
	}
	
	public void setNewPosition(Vector3f newPosition) {
		this.newPosition = newPosition;
	}
	
	public Movable getObject() {
		return object;
	}
	
	public void setObject(Movable object) {
		this.object = object;
	}
	
}
