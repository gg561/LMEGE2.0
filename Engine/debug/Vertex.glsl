#version 330 core
layout (location = 0) in vec3 in_position;
layout (location = 1) in vec2 textureCoords;
layout (location = 2) in vec3 in_normal;

out VS_OUT{
	vec3 out_normal;
} vs_out;

uniform mat4 view;
uniform mat4 model;

void main(void){
	gl_Position = view * model * vec4(in_position, 1);
	mat3 normalMatrix = mat3(transpose(view * model));
	vs_out.out_normal = normalize(vec3(vec4(normalMatrix * in_normal, 0)));
}