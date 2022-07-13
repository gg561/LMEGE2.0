package scientific;

import org.joml.Vector3f;

import actors.Movable;

public class Graviton {
	
	private float mass;
	private int amount;
	private Movable binder;
	
	public Graviton(int amount, float mass, Movable binder) {
		this.mass = mass;
		this.amount = amount;
		this.binder = binder;
	}
	
	public float getMass() {
		return mass;
	}
	
	public int getAmount() {
		return amount;
	}
	
	public Movable getBinder() {
		return binder;
	}

}
