#version 330 core

in vec2 textureCoords;

out vec4 outColor;

uniform sampler2D sampleTexture;
uniform vec3 color;

void main(void) {
	
	vec4 textureColor = texture(sampleTexture, textureCoords);
	outColor = vec4(textureColor.rgb * color, textureColor.a);
	//outColor = vec4(textureCoords.x, 0, gl_FragCoord.x * textureCoords.y, 1);
	
}