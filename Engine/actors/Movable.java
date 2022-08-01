package actors;

import org.joml.Vector3f;

import collision.Collider;
import game.Main;
import terrain.Terrain;

public class Movable implements Scalable {
	
	private Vector3f position;
	private Vector3f localPosition;
	private Vector3f rotation;
	private Vector3f localRotation;
	private Vector3f scale;
	private Vector3f forward;
	private Vector3f right;
	private Vector3f up;
	@Attached
	private Collider collider;
	private boolean hasGravity;
	private float mass;
	protected boolean onGround;
	protected boolean debug;
	
	public Movable() {
		this.position = new Vector3f();
		this.rotation = new Vector3f();
		this.scale = new Vector3f(1, 1, 1);
		this.localPosition = new Vector3f();
		this.localRotation = new Vector3f(0, 0 ,0);
		this.forward = new Vector3f(0, 0, 1);
		this.right = new Vector3f(1, 0, 0);
		this.up = new Vector3f(0, 1, 0);
	}
	
	public void rotate(Vector3f rotation) {
		this.rotation.add(rotation);
		rotateDirections(rotation);
		if(this.collider != null)
			this.collider.rotate(rotation);
	}
	
	public void setRotationWithDirections(Vector3f rotation) {
		this.rotation.set(rotation);
		rotateDirections(rotation);
		if(this.collider != null)
			this.collider.setRotationWithDirections(rotation);
	}
	
	public void rotateLocal(Vector3f rotation) {
		this.localRotation.add(rotation);
		rotate(rotation);
		if(this.collider != null)
			this.collider.rotateLocal(rotation);
	}
	
	public void move(Vector3f translation) {
		//BOX SHADOWING DEBUG
		//System.out.println(this + " trans1 " + this.position);
		this.getPosition().add(forward.mul(translation.z, new Vector3f()));
		this.getPosition().add(right.mul(translation.x, new Vector3f()));
		this.getPosition().add(up.mul(translation.y, new Vector3f()));
		/*BOX SHADOWING DEBUG
		System.out.println(this + " trans " + this.position);
		System.out.println(Main.house + " house " + Main.house.getPosition());
		*/
		if(collider != null) {
			/*Vector3f factor = collider.getCollision().getCombinedDirection();
			translation.add(translation.mul(factor.x, factor.y, factor.z, new Vector3f()));*/
			this.collider.move(translation);
		}
	}
	
	public void update() {
		if(hasGravity) {
			Terrain terrain = Terrain.getTerrain(this.getPosition().x, this.getPosition().z);
			//System.out.println("TERR " + this.getPosition());
			//float height = terrain.getHeightOfTerrain(this.getPosition().x, this.getPosition().z);
			//this.collider.setPosition(this.getPosition());
			/*if(this.getPosition().y > height) {
				System.out.println("Falling " + this.getPosition().y);
				onGround = false;
				this.move(new Vector3f(0, -mass, 0));
			}else if(this.getPosition().y <= height) {
				this.move(new Vector3f(0, height - this.getPosition().y, 0));
				onGround = true;
			}*/
			if(terrain.getCollider().containsExec(this)) {
				/*BOX SHADOWING DEBUG
				if(debug) {
					System.out.println("SCND TERR " + getCollider().getCollision());
				}*/
				this.collider.reactToCollision(terrain.getCollider());
				/*BOX SHADOWING DEBUG
				if(debug) {
					System.out.println("THRD TERR " + getCollider().getBounds());
				}*/
				this.collider.getCollision().removeCollision(terrain.getCollider());
				onGround = true;
			}else {
				this.move(new Vector3f(0, -mass, 0));
			}
		}
	}
	
	public void move(Vector3f translation, Vector3f direction) {
		this.getPosition().add(direction.mul(translation.z, new Vector3f()));
		if(this.collider != null)
			this.collider.move(translation, direction);
	}
	
	public void moveAround(Vector3f center, Vector3f dirToDistance) {
		Vector3f orbitPosition = center.add(dirToDistance, new Vector3f());
		this.setPosition(orbitPosition);
		if(this.collider != null)
			this.collider.setPosition(orbitPosition);
	}
	
	public void setDebug(boolean debug) {
		this.debug = debug;
	}
	
	public boolean isDebug() {
		return debug;
	}
	
	public Vector3f getForward() {
		return forward;
	}
	
	public void setForward(Vector3f forward) {
		this.forward = forward;
	}
	
	public Vector3f getRight() {
		return right;
	}
	
	public void setRight(Vector3f right) {
		this.right = right;
	}
	
	public Vector3f getUp() {
		return up;
	}
	
	public void setUp(Vector3f up) {
		this.up = up;
	}
	
	public Vector3f getPosition() {
		return position;
	}
	
	public void setPosition(Vector3f position) {
		this.position = position;
	}
	
	public Vector3f getRotation() {
		return rotation;
	}
	
	public void setRotation(Vector3f rotation) {
		this.rotation = rotation;
	}

	public Vector3f getScale() {
		return scale;
	}

	public void setScale(Vector3f scale) {
		this.scale = scale;
	}
	
	public Vector3f getLocalPosition() {
		return localPosition;
	}
	
	public void setLocalPosition(Vector3f localPosition) {
		this.localPosition = localPosition;
	}
	
	public Vector3f getLocalRotation() {
		return localRotation;
	}
	
	public void setLocalRotation(Vector3f localRotation) {
		this.localRotation = localRotation;
	}
	
	private void rotateDirections(Vector3f rotation) {
		this.forward.rotateY(rotation.y);
		this.forward.rotateX(rotation.x);
		this.right.rotateY(rotation.y);
		this.right.rotateZ(rotation.z);
		this.up.rotateX(rotation.x);
		this.up.rotateZ(rotation.z);
	}
	
	public void setMass(float mass) {
		this.mass = mass;
	}
	
	public float getMass() {
		return mass;
	}
	
	public void setHasGravity(boolean hasGravity) {
		this.hasGravity = hasGravity;
	}
	
	public boolean isHasGravity() {
		return hasGravity;
	}
	
	public Collider getCollider() {
		return collider;
	}
	
	public void setCollider(Collider collider) {
		this.collider = collider;
	}

}
