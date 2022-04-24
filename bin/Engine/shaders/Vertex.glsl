#version 400 core

layout (location = 0) in vec3 in_position;
layout (location = 1) in vec2 in_texCoords;
layout (location = 2) in vec3 in_normal;

out vec3 color;
out vec3 surfaceNormal;
out vec3 lightDirection;
out vec2 textureCoords;
out vec3 toCameraVec;

uniform mat4 viewMatrix;
uniform mat4 projMatrix;
uniform mat4 modelMatrix;
uniform vec3 lightPosition;

uniform float tiles;

void main(void){
	
	color = vec3(1, 1, 1);
	vec4 worldPosition = modelMatrix * vec4(in_position, 1.0);
	gl_Position = projMatrix * viewMatrix * worldPosition;
	surfaceNormal = (modelMatrix * vec4(in_normal, 0.0)).xyz;
	lightDirection = lightPosition - worldPosition.xyz;
	textureCoords = in_texCoords * tiles;
	toCameraVec = (inverse(viewMatrix) * vec4(0, 0, 0, 1)).xyz - worldPosition.xyz;
}