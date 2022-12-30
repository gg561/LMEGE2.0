#version 400

const int MAX_NUMBER_OF_LIGHTS = 4;

in vec4 clipSpace;
in vec2 textureCoords;
in vec3 toCameraVec;
in vec3 toLightVec[MAX_NUMBER_OF_LIGHTS];
in float numOfLights;

out vec4 outColor;

uniform sampler2D reflection;
uniform sampler2D refraction;
uniform float distortionMove;

uniform sampler2D normalMap;

uniform sampler2D dudvMap;
const float waveStrength = 0.02;

uniform vec3 lightColor[MAX_NUMBER_OF_LIGHTS];

const float shineDamper = 20;
const float reflectivity = 0.6;

uniform sampler2D depthMap;

const float near = 0.001;
const float far = 1000;



float getSoftEdgeOffset(float value, vec2 tc){
	float depth = texture(depthMap, tc).r;
	float floorDistance = 2.0 * near * far / (far + near - (2.0 * depth - 1.0) * (far - near));
	float ground = gl_FragCoord.z;
	float waterDistance = 2.0 * near * far / (far + near - (2.0 * ground - 1.0) * (far - near));
	depth = floorDistance - waterDistance;
	return clamp(depth / value, 0, 1);
}

void main(void) {

	vec2 ndc = (clipSpace.xy / clipSpace.w) / 2 + 0.5;
	float alpha = getSoftEdgeOffset(5, ndc);
	vec2 normalizedDeviceCoordinates = clamp(ndc, 0.001, 0.999);
	
	//DISTORTION
	vec2 dudvShift = (texture(dudvMap, vec2(textureCoords.x + distortionMove, textureCoords.y)).rg * 2 - 1) * waveStrength;
	//SCDN DISORTION LAYER
	vec2 dudvShift2 = (texture(dudvMap, vec2(-textureCoords.x + distortionMove, textureCoords.y + distortionMove)).rg * 2 - 1) * waveStrength;
	
	vec2 finalCoords = clamp(normalizedDeviceCoordinates + (dudvShift + dudvShift2), 0.001, 0.999);
	
	vec2 reflectionTextureCoords = vec2(finalCoords.x, -finalCoords.y);
	
	vec2 refractionTextureCoords = vec2(finalCoords.x, finalCoords.y);
	
	vec4 reflectionTex = texture(reflection, reflectionTextureCoords);
	vec4 refractionTex = texture(refraction, refractionTextureCoords);
	
	
	vec3 normalColors = texture(normalMap, dudvShift + dudvShift2).rgb;
	vec3 normal = vec3(normalColors.r * 2 - 1, normalColors.b, normalColors.g * 2 - 1);
	normal = normalize(normal);
	
	vec3 unitCamVec = normalize(toCameraVec);
	float refractiveFactor = dot(unitCamVec, normal);
	refractiveFactor = clamp(refractiveFactor, 0, 1);
	
	vec3 specularColor = vec3(0);
	
	for(int i = 0; i < numOfLights; i++){
		vec3 unitLightVec = normalize(toLightVec[i]);
		vec3 reflectedVec = reflect(unitLightVec, normal);
		float reflectiveness = dot(reflectedVec, unitCamVec);
		reflectiveness = max(reflectiveness, 0);
		float dampedSpecular = pow(reflectiveness, shineDamper);
		specularColor += dampedSpecular * reflectivity * lightColor[i];
	}
	
	specularColor *= alpha;

	outColor = mix(reflectionTex, refractionTex, refractiveFactor) + vec4(specularColor, 0);
	outColor.a = alpha;
	//outColor = mix(outColor, vec4(0.0, 0.3, 0.5, 1.0), 0.2);

}