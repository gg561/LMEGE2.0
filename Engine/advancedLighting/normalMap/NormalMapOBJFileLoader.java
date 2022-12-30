package advancedLighting.normalMap;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiConsumer;

import org.joml.Vector2f;
import org.joml.Vector3f;

import game.Main;
import models.Model;
import readers.Vertex;
import renderer.Loader;
import util.CustomFile;

public class NormalMapOBJFileLoader {
	
	public static Model loadOBJ(CustomFile file, Loader loader) {
		InputStreamReader isr = new InputStreamReader(file.getStream());
		BufferedReader reader = new BufferedReader(isr);
		List<VertexNM> vertices = new ArrayList<VertexNM>();
		List<Vector2f> textures = new ArrayList<Vector2f>();
		List<Vector3f> normals = new ArrayList<Vector3f>();
		List<Integer> indices = new ArrayList<Integer>();
		readToList(reader, vertices, textures, normals, indices);
		removeUnusedVertices(vertices);
		float[] verticesArray = new float[vertices.size() * 3];
		float[] texturesArray = new float[vertices.size() * 2];
		float[] normalsArray = new float[vertices.size() * 3];
		float[] tangentsArray = new float[vertices.size() * 3];
		float furthest = convertDataToArrays(vertices, textures, normals, verticesArray,
				texturesArray, normalsArray, tangentsArray);
		int[] indicesArray = convertIndicesListToArray(indices);
		return loader.loadToVAONormalMap(verticesArray, texturesArray, normalsArray, indicesArray, tangentsArray);
	}
	
	private static void readToList(BufferedReader reader, List<VertexNM> vertices, List<Vector2f> textures, List<Vector3f> normals, List<Integer> indices) {
		String line;
		try {
			while (true) {
				line = reader.readLine();
				if (line.startsWith("v ")) {
					String[] currentLine = line.split(" ");
					Vector3f vertex = new Vector3f((float) Float.valueOf(currentLine[1]),
							(float) Float.valueOf(currentLine[2]),
							(float) Float.valueOf(currentLine[3]));
					VertexNM newVertex = new VertexNM(vertices.size(), vertex);
					vertices.add(newVertex);

				} else if (line.startsWith("vt ")) {
					String[] currentLine = line.split(" ");
					Vector2f texture = new Vector2f((float) Float.valueOf(currentLine[1]),
							(float) Float.valueOf(currentLine[2]));
					textures.add(texture);
				} else if (line.startsWith("vn ")) {
					String[] currentLine = line.split(" ");
					Vector3f normal = new Vector3f((float) Float.valueOf(currentLine[1]),
							(float) Float.valueOf(currentLine[2]),
							(float) Float.valueOf(currentLine[3]));
					normals.add(normal);
				} else if (line.startsWith("f ")) {
					break;
				}
			}
			while (line != null && line.startsWith("f ")) {
				String[] currentLine = line.split(" ");
				String[] vertex1 = currentLine[1].split("/");
				String[] vertex2 = currentLine[2].split("/");
				String[] vertex3 = currentLine[3].split("/");
				VertexNM v0 = processVertex(vertex1, vertices, indices);
				VertexNM v1 = processVertex(vertex2, vertices, indices);
				VertexNM v2 = processVertex(vertex3, vertices, indices);
				calculateTangent(v0, v1, v2, textures);
				line = reader.readLine();
			}
			reader.close();
		} catch (IOException e) {
			System.err.println("Error reading the file");
		}
	}

	private static VertexNM processVertex(String[] vertex, List<VertexNM> vertices, List<Integer> indices) {
		int index = Integer.parseInt(vertex[0]) - 1;
		VertexNM currentVertex = vertices.get(index);
		int textureIndex = Integer.parseInt(vertex[1]) - 1;
		int normalIndex = Integer.parseInt(vertex[2]) - 1;
		if (!currentVertex.isSet()) {
			currentVertex.setTextureIndex(textureIndex);
			currentVertex.setNormalIndex(normalIndex);
			indices.add(index);
			return currentVertex;
		} else {
			return dealWithAlreadyProcessedVertex(currentVertex, textureIndex, normalIndex, indices,
					vertices);
		}
	}

	private static int[] convertIndicesListToArray(List<Integer> indices) {
		int[] indicesArray = new int[indices.size()];
		for (int i = 0; i < indicesArray.length; i++) {
			indicesArray[i] = indices.get(i);
		}
		return indicesArray;
	}

	private static float convertDataToArrays(List<VertexNM> vertices, List<Vector2f> textures,
			List<Vector3f> normals, float[] verticesArray, float[] texturesArray,
			float[] normalsArray, float[] tangentsArray) {
		float furthestPoint = 0;
		for (int i = 0; i < vertices.size(); i++) {
			VertexNM currentVertex = vertices.get(i);
			if (currentVertex.getLength() > furthestPoint) {
				furthestPoint = currentVertex.getLength();
			}
			Vector3f position = currentVertex.getPosition();
			Vector2f textureCoord = textures.get(currentVertex.getTextureIndex());
			Vector3f normalVector = normals.get(currentVertex.getNormalIndex());
			Vector3f tangent = currentVertex.getAveragedTangent();
			verticesArray[i * 3] = position.x;
			verticesArray[i * 3 + 1] = position.y;
			verticesArray[i * 3 + 2] = position.z;
			texturesArray[i * 2] = textureCoord.x;
			texturesArray[i * 2 + 1] = 1 - textureCoord.y;
			normalsArray[i * 3] = normalVector.x;
			normalsArray[i * 3 + 1] = normalVector.y;
			normalsArray[i * 3 + 2] = normalVector.z;
			tangentsArray[i * 3] = tangent.x;
			tangentsArray[i * 3 + 1] = tangent.y;
			tangentsArray[i * 3 + 2] = tangent.z;
		}
		return furthestPoint;
	}

	private static VertexNM dealWithAlreadyProcessedVertex(VertexNM previousVertex, int newTextureIndex,
			int newNormalIndex, List<Integer> indices, List<VertexNM> vertices) {
		if (previousVertex.hasSameTextureAndNormal(newTextureIndex, newNormalIndex)) {
			indices.add(previousVertex.getIndex());
			return previousVertex;
		} else {
			VertexNM anotherVertex = previousVertex.getDuplicateVertex();
			if (anotherVertex != null) {
				return dealWithAlreadyProcessedVertex(anotherVertex, newTextureIndex, newNormalIndex,
						indices, vertices);
			} else {
				VertexNM duplicateVertex = previousVertex.duplicate(vertices.size());
				duplicateVertex.setTextureIndex(newTextureIndex);
				duplicateVertex.setNormalIndex(newNormalIndex);
				previousVertex.setDuplicateVertex(duplicateVertex);
				vertices.add(duplicateVertex);
				indices.add(duplicateVertex.getIndex());
				return duplicateVertex;
			}

		}
	}
	
	private static void removeUnusedVertices(List<VertexNM> vertices){
		for(VertexNM vertex:vertices){
			vertex.averageTangents();
			if(!vertex.isSet()){
				vertex.setTextureIndex(0);
				vertex.setNormalIndex(0);
			}
		}
	}
	
	private static void calculateTangent(VertexNM vertex0, VertexNM vertex1, VertexNM vertex2, List<Vector2f> textures) {
		Vector3f e1 = vertex1.getPosition().sub(vertex0.getPosition(), new Vector3f());
		Vector3f e2 = vertex2.getPosition().sub(vertex1.getPosition(), new Vector3f());
		Vector2f uv0 = textures.get(vertex0.getTextureIndex());
		Vector2f uv1 = textures.get(vertex1.getTextureIndex());
		Vector2f uv2 = textures.get(vertex2.getTextureIndex());
		Vector2f deltaUV0 = uv1.sub(uv0, new Vector2f());
		Vector2f deltaUV1 = uv2.sub(uv1, new Vector2f());
		
		float determinant = 1f / (deltaUV0.x * deltaUV1.y - deltaUV1.x * deltaUV0.y);
		Vector3f tangent = 
		
				/*THE CALCULATIONS BEHIND E1->mul(DELTAUV1.y)->sub(E2->mul(DELTAUV0.y);*/
				
				new Vector3f(
				determinant * (deltaUV1.y * e1.x - deltaUV0.y * e2.x),
				determinant * (deltaUV1.y * e1.y - deltaUV0.y * e2.y),
				determinant * (deltaUV1.y * e1.z - deltaUV0.y * e2.z));
				
				//e1.mul(deltaUV1.y).sub(e2.mul(deltaUV0.y)).mul(determinant);
		vertex0.addTangent(tangent);
		vertex1.addTangent(tangent);
		vertex2.addTangent(tangent);
	}
	
	

}
