package util;

import java.util.HashSet;
import java.util.Set;

import org.joml.Vector3f;

public class VectorPool implements Pool<Vector3f> {
	
	private Set<Vector3f> available = new HashSet<Vector3f>();
	private Set<Vector3f> unavailable = new HashSet<Vector3f>();

	@Override
	public void close() throws Exception {
		// TODO Auto-generated method stub
		pushFront(unavailable.iterator().next());
	}

	@Override
	public void pushFront(Vector3f t) {
		// TODO Auto-generated method stub
		available.add(t);
	}

	@Override
	public void pushBack(Vector3f t) {
		// TODO Auto-generated method stub
		available.add(t);
	}

	@Override
	public Vector3f popFront() {
		// TODO Auto-generated method stub
		Vector3f host = available.iterator().next();
		available.remove(host);
		return host;
	}

	@Override
	public Vector3f popBack() {
		// TODO Auto-generated method stub
		Vector3f host = (Vector3f) available.toArray()[available.size() - 1];
		available.remove(host);
		return host;
	}

	@Override
	public void clear() {
		// TODO Auto-generated method stub
		unavailable.clear();
		available.clear();
	}

}
