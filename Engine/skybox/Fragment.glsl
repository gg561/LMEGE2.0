#version 400

in vec3 textureCoords;

out vec4 outColor;

uniform samplerCube samplerTexture;
uniform vec3 fogColor;
uniform float max = 30;
uniform float min = 0;

uniform samplerCube mixable;
uniform float blendFactor;

void main(void) {

	vec4 originalTexture = texture(samplerTexture, textureCoords);
	vec4 mixableTexture = texture(mixable, textureCoords);
	outColor = mix(originalTexture, mixableTexture, blendFactor);
	
	float factor = (textureCoords.y - min) / (max - min);
	factor = clamp(factor, 0, 1);
	outColor = mix(vec4(fogColor, 1), outColor, factor);
	
	
}