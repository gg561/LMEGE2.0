#version 400 core

const int MAX_NUMBER_OF_LIGHTS = 4;

layout (location = 0) in vec3 in_position;
layout (location = 1) in vec2 in_texCoords;
layout (location = 2) in vec3 in_normal;
layout (location = 3) in mat4 in_instanceMatrix;

out vec3 color;
out vec3 surfaceNormal;
out vec3 lightDirection[MAX_NUMBER_OF_LIGHTS];
out vec2 textureCoords;
out vec3 toCameraVec;
out float visibility;
out float numOfLights;

uniform mat4 viewMatrix;
uniform mat4 projMatrix;
uniform vec3 lightPosition[MAX_NUMBER_OF_LIGHTS];

uniform float useFakeLighting;

uniform float fogGradient;
uniform float fogDensity;

uniform vec2 textureAtlasOffset;
uniform float textureAtlasRows;

uniform float numberOfLights;

uniform vec4 clippingDistance;

//g init

vec3 calculateSurfaceNormal(mat4 matrix, vec3 normal){
	return (matrix * vec4(normal, 0.0)).xyz;
}

void main(void){
	
	//RENDERING
	
	color = vec3(1, 1, 1);
	vec4 worldPosition = in_instanceMatrix * vec4(in_position, 1.0);
	vec4 positionRelativeCamera = viewMatrix * worldPosition;
	vec4 clipSpace = projMatrix * positionRelativeCamera;
	gl_Position = clipSpace;
	gl_ClipDistance[0] = dot(worldPosition, clippingDistance);
	float inRows = textureAtlasRows;
	if(inRows < 1){
		inRows = 1;
	}
	textureCoords = in_texCoords / inRows + textureAtlasOffset;
	//FAKE LIGHTING
	
	vec3 normal = in_normal;
	if(useFakeLighting > 0.5){
		normal = vec3(0, 1, 0);
	}
	
	//LIGHTING
	
	surfaceNormal = calculateSurfaceNormal(in_instanceMatrix, normal);

//~ normalMapTBN
	
	for(int i = 0; i < numberOfLights; i ++){
		lightDirection[i] = lightPosition[i] - worldPosition.xyz;
		
//~ normalMapLightVector

	}
	toCameraVec = (inverse(viewMatrix) * vec4(0, 0, 0, 1)).xyz - worldPosition.xyz;
	
//~ normalMapCameraVec
	numOfLights = numberOfLights;
	//FOG
	
	float distance = length(positionRelativeCamera.xyz);
	visibility = exp(-pow((distance * fogDensity), fogGradient));
	visibility = clamp(visibility, 0, 1);
	
//~ shadowMapVertex


}