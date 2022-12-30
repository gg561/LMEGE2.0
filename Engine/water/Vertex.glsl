#version 400 core

layout (location = 0) in vec2 in_position;

const int MAX_NUMBER_OF_LIGHTS = 4;
const int SIZE = 100;

out vec4 clipSpace;
out vec2 textureCoords;
out vec3 toCameraVec;
out vec3 toLightVec[MAX_NUMBER_OF_LIGHTS];
out float numOfLights;

uniform mat4 projection;
uniform mat4 view;
uniform mat4 model;
uniform vec3 lightPosition[MAX_NUMBER_OF_LIGHTS];
uniform float numberOfLights;

uniform vec2 tileSize;

const float tiling = 4.0;

void main(void){

	vec4 worldPosition = model * vec4(in_position.x, 0, in_position.y, 1);
	clipSpace =  projection * view * worldPosition;
	textureCoords = vec2(in_position.x/2.0 + 0.5, in_position.y/2.0 +0.5) * tiling * tileSize / SIZE;
	gl_Position = clipSpace;
	
	toCameraVec = (inverse(view) * vec4(0, 0, 0, 1)).xyz - worldPosition.xyz;
	
	for(int i = 0; i< numberOfLights; i++) {
		toLightVec[i] = lightPosition[i] - worldPosition.xyz;
	}
	
	numOfLights = numberOfLights;

}