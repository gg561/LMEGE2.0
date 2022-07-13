#version 330 core

in vec3 color;
in vec3 surfaceNormal;
in vec3 lightDirection;
in vec2 textureCoords;
in vec3 toCameraVec;
in float visibility;

out vec4 outColor;

uniform vec3 lightColor;
uniform sampler2D sampleTexture;
uniform float shineDamper;
uniform float reflectivity;
uniform vec3 skyColor;

void main(void){

	//VARIABLES
	
	vec4 diffuseColor;
	vec4 simpleColor = vec4(1, 1, 1, 1);
	vec4 textureColor;
	vec4 specularColor;
	
	//LIGHTING
	
	vec3 unitNormal = normalize(surfaceNormal);
	vec3 unitDirection = normalize(lightDirection);
	vec3 unitCamVec = normalize(toCameraVec);
	
	vec3 reflectingDirection = -unitDirection;
	vec3 reflectedLightDirection = reflect(reflectingDirection, unitNormal);
	
	//LIGHTING-SPECULAR
	
	float specularFactor = dot(reflectedLightDirection, unitCamVec);
	specularFactor = max(specularFactor, 0);
	float dampedSpecular = pow(specularFactor, shineDamper);
	vec3 finalSpecular = dampedSpecular * reflectivity * lightColor; 
	specularColor = vec4(finalSpecular, 1);
	
	//LIGHTING-DIFFUSE
	
	float light = dot(unitNormal, unitDirection);
	float brightness = max(light, 0.2);
	vec3 diffuse = brightness * lightColor;
	diffuseColor = vec4(diffuse, 1);

	//TRANSPARENCY

	textureColor = texture(sampleTexture, textureCoords);
	if(textureColor.a < 0.5){
		discard;
	}
	
	//OUTPUT
	vec4 fragColor = diffuseColor * simpleColor * textureColor + specularColor;
	vec4 fogColor = mix(vec4(skyColor, 1), fragColor, visibility);
	
	//outColor = fragColor;
	outColor = fogColor;
}