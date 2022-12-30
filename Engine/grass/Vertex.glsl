#version 330 core
layout (location = 0) in vec3 in_positions;

out VS_OUT {
	vec4 worldPosition;
} vs_out;

uniform mat4 model;

void main(void) {
	vs_out.worldPosition = model * vec4(in_positions, 1);
}