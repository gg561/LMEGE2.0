#version 140

in vec2 in_position;

out vec2 textureCoords;

uniform mat4 orthoMatrix;
uniform mat4 modelMatrix;

void main(void) {
	gl_Position = modelMatrix * vec4(in_position, 0.0, 1.0);
	textureCoords = vec2((in_position.x+1.0)/2.0, 1 - (in_position.y+1.0)/2.0);

}
