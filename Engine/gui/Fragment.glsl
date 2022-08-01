#version 140

in vec2 textureCoords;

out vec4 outColor;

uniform sampler2D sampleTexture;

void main(void){

	outColor = texture(sampleTexture, textureCoords);
}