#version 400 core

layout (location = 0) in vec3 in_position;
layout (location = 1) in vec2 in_texCoords;
layout (location = 2) in vec3 in_normal;

out vec3 color;
out vec3 surfaceNormal;
out vec3 lightDirection;
out vec2 textureCoords;
out vec3 toCameraVec;
out float visibility;

uniform mat4 viewMatrix;
uniform mat4 projMatrix;
uniform mat4 modelMatrix;
uniform vec3 lightPosition;

uniform float useFakeLighting;

uniform float fogGradient;
uniform float fogDensity;

void main(void){
	
	//RENDERING
	
	color = vec3(1, 1, 1);
	vec4 worldPosition = modelMatrix * vec4(in_position, 1.0);
	vec4 positionRelativeCamera = viewMatrix * worldPosition;
	gl_Position = projMatrix * positionRelativeCamera;
	textureCoords = in_texCoords;
	
	//FAKE LIGHTING
	
	vec3 normal = in_normal;
	if(useFakeLighting > 0.5){
		normal = vec3(0, 1, 0);
	}
	
	//LIGHTING
	
	surfaceNormal = (modelMatrix * vec4(normal, 0.0)).xyz;
	lightDirection = lightPosition - worldPosition.xyz;
	toCameraVec = (inverse(viewMatrix) * vec4(0, 0, 0, 1)).xyz - worldPosition.xyz;
	
	//FOG
	
	float distance = length(positionRelativeCamera.xyz);
	visibility = exp(-pow((distance * fogDensity), fogGradient));
	visibility = clamp(visibility, 0, 1);
}