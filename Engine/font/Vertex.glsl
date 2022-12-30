#version 330 core

layout (location = 0) in vec2 in_position;
layout (location = 1) in vec2 texCoords;

out vec2 textureCoords;

uniform mat4 projectionMatrix;
uniform mat4 viwMatrix;
uniform mat4 modelMatrix;

void main(void) { 
	vec4 worldPosition = modelMatrix * vec4(vec3(in_position, 0), 1);
	gl_Position = worldPosition;
	
	textureCoords = texCoords;

}