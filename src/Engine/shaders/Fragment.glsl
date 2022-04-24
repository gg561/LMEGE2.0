#version 330 core

in vec3 color;
in vec3 surfaceNormal;
in vec3 lightDirection;
in vec2 textureCoords;
in vec3 toCameraVec;

out vec4 outColor;

uniform vec3 lightColor;
uniform sampler2D sampleTexture;
uniform float shineDamper;
uniform float reflectivity;

void main(void){
	
	vec3 unitNormal = normalize(surfaceNormal);
	vec3 unitDirection = normalize(lightDirection);
	vec3 unitCamVec = normalize(toCameraVec);
	vec3 reflectingDirection = -unitDirection;
	
	vec3 reflectedLightDirection = reflect(reflectingDirection, unitNormal);
	
	float specularFactor = dot(reflectedLightDirection, unitCamVec);
	specularFactor = max(specularFactor, 0);
	float dampedSpecular = pow(specularFactor, shineDamper);
	vec3 finalSpecular = dampedSpecular * lightColor; 
	
	float light = dot(unitNormal, unitDirection);
	float brightness = max(light, 0.2);
	vec3 diffuse = brightness * reflectivity * lightColor;

	vec4 textureColor = texture(sampleTexture, textureCoords);
	if(textureColor.a < 0.5){
		discard;
	}
	
	outColor = vec4(diffuse, 1.0) * vec4(1, 1, 1, 1) * textureColor; + vec4(finalSpecular, 1);
}