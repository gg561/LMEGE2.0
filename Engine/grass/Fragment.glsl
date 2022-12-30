#version 330 core

layout (location = 0) out vec4 outColor;

in GS_OUT {
	vec4 color;
} fs_in;

void main(void) {
	outColor = fs_in.color;
}