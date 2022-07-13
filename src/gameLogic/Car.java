package gameLogic;

import org.joml.Vector3f;

import actors.*;

public class Car extends ActorGroup {
	
	public Movable[] wheels = new Movable[2];
	private Movable chassis;
	
	public Car(Movable chassis, Movable left, Movable right) {
		super(chassis, left, right);
		this.chassis = chassis;
		this.wheels[0] = left;
		this.wheels[1] = right;
	}
	
	public void rotate(Vector3f rotation) {
		super.rotate(rotation);
		for(Movable wheel : wheels) {
			if(wheel.getLocalRotation().y + rotation.y > Math.toRadians(-30) && wheel.getLocalRotation().y + rotation.y < Math.toRadians(30)) {
				wheel.rotateLocal(rotation);
			}
		}
	}
}
