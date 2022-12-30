package advancedLighting.normalMap;

import java.util.ArrayList;
import java.util.List;

import org.joml.Vector3f;

import readers.Vertex;

public class VertexNM extends Vertex {
	
	private List<Vector3f> tangents = new ArrayList<Vector3f>();
	private Vector3f averagedTangent = new Vector3f(0);
	private VertexNM duplicateVertex = null;

	public VertexNM(int index, Vector3f position) {
		super(index, position);
		// TODO Auto-generated constructor stub
	}
	
	public VertexNM getDuplicateVertex() {
		return duplicateVertex;
	}
	
	public void setDuplicateVertex(VertexNM duplicate) {
		this.duplicateVertex = duplicate;
	}
	
	protected VertexNM duplicate(int newIndex) {
		VertexNM vertex = new VertexNM(newIndex, getPosition());
		vertex.tangents = tangents;
		return vertex;
	}
	
	public void addTangent(Vector3f tangent) {
		tangents.add(tangent);
	}
	
	public void averageTangents() {
		if(tangents.isEmpty()) return;
		tangents.forEach(t -> averagedTangent.add(t));
		averagedTangent.normalize();
	}
	
	public Vector3f getAveragedTangent() {
		return averagedTangent;
	}

}
