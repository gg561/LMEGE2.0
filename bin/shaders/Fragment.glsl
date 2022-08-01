#version 330 core

const int MAX_NUMBER_OF_LIGHTS = 4;

in vec3 color;
in vec3 surfaceNormal;
in vec3 lightDirection[MAX_NUMBER_OF_LIGHTS];
in vec2 textureCoords;
in vec3 toCameraVec;
in float visibility;
in float numOfLights;

out vec4 outColor;

uniform vec3 lightColor[MAX_NUMBER_OF_LIGHTS];
uniform vec3 lightAttenuation[MAX_NUMBER_OF_LIGHTS];
uniform sampler2D sampleTexture;
uniform float shineDamper;
uniform float reflectivity;
uniform vec3 skyColor;

//SIGNAL_DEFAULT

void main(void){

	//VARIABLES
	
	vec4 diffuseColor = vec4(0.0);
	vec4 simpleColor = vec4(1, 1, 1, 1);
	vec4 textureColor;
	vec4 specularColor = vec4(0.0);
	
	//LIGHTING
	
	vec3 unitNormal = normalize(surfaceNormal);
	vec3 unitCamVec = normalize(toCameraVec);
	for(int i = 0; i < numOfLights; i++){
		float distance = length(lightDirection[i]);
		float attFactor = lightAttenuation[i].x + (lightAttenuation[i].y * distance) + (lightAttenuation[i].z * distance * distance);
	
		vec3 unitDirection = normalize(lightDirection[i]);
		
		vec3 reflectingDirection = -unitDirection;
		vec3 reflectedLightDirection = reflect(reflectingDirection, unitNormal);
		
		//LIGHTING-SPECULAR
		
		float specularFactor = dot(reflectedLightDirection, unitCamVec);
		specularFactor = max(specularFactor, 0);
		float dampedSpecular = pow(specularFactor, shineDamper);
//~ specularCel
		vec3 finalSpecular = (dampedSpecular * reflectivity * lightColor[i]) / attFactor; 
		specularColor = vec4(specularColor.xyz + finalSpecular, 1);
		
		//LIGHTING-DIFFUSE
		
		float light = dot(unitNormal, unitDirection);
		float brightness = max(light, 0.0);
//~ diffuseCel
		vec3 diffuse = (brightness * lightColor[i]) / attFactor;
		diffuseColor = vec4(diffuseColor.xyz + diffuse, 1);
	}
	diffuseColor = max(diffuseColor, 0.2);
	textureColor = texture(sampleTexture, textureCoords);
	//TRANSPARENCY
//~ transparency
	
	//OUTPUT
	vec4 fragColor = diffuseColor * simpleColor * textureColor + specularColor;
	vec4 fogColor = mix(vec4(skyColor, 1), fragColor, visibility);
//~ multiTexture


	//outColor = fragColor;
	outColor = fogColor;
}