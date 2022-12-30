package scientific;

import org.joml.Vector3f;

public class GravitationalDimension {
	
	public void pull(Graviton attraction, Graviton[] attracted) {
		
	}
	
	public void pull(Vector3f fakeForce, Graviton[] attracted) {
		pull(fakeForce, 1, attracted);
	}
	
	private void pull(Vector3f direction, float force, Graviton[] attracted) {
		Vector3f forceVector = direction.mul(force, new Vector3f());
		for(Graviton graviton : attracted) {
			graviton.getBinder().move(forceVector);
		}
	}

}
