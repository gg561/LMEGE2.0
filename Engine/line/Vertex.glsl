#version 400 core

layout (location = 0) in vec3 in_positions;

out vec3 out_color;

uniform mat4 projMatrix;
uniform mat4 viewMatrix;
uniform mat4 modelMatrix;

void main(void) {
	vec4 worldPosition = modelMatrix * vec4(in_positions, 1);

	gl_Position = projMatrix * viewMatrix * worldPosition;
	
}