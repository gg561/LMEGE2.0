#version 400

layout (location = 0) in vec3 in_position;

out vec3 textureCoords;

uniform mat4 projection;
uniform mat4 view;

void main(void) {

	gl_Position = projection * view * vec4(in_position, 1.0);
	
	textureCoords = in_position;
	
}