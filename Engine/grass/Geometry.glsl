#version 330 core

layout (triangles) in;
layout (triangle_strip, max_vertices = 44) out;

in VS_OUT {
	vec4 worldPosition;
} gs_in[];

out GS_OUT{
	vec4 color;
} gs_out;

const float GRASS_BLADES = 2;
const float GRASS_SEGMENTS = 5; 
const float GRASS_HEIGHT = 6;
const float GRASS_WIDTH = 0.26;
const float BLADE_RADIUS = 0.6;
const float BLADE_CURVATURE = 2;

const float PI = 3.1415926;

uniform mat4 projection;
uniform mat4 view;

mat3 Ang3(float angle, vec3 axis) {
        float c, s;
        s = sin(angle);
        c = cos(angle);
 
        float t = 1 - c;
        float x = axis.x;
        float y = axis.y;
        float z = axis.z;
 
        return mat3(
            t * x * x + c, t * x * y - s * z, t * x * z + s * y,
            t * x * y + s * z, t * y * y + c, t * y * z - s * x,
            t * x * z - s * y, t * y * z + s * x, t * z * z + c
            );
}

float rand(vec3 co){
	return fract(sin(dot(co.xyz, vec3(12.9898, 78.233, 53.539))) * 43758.5453);
}

void main(void){
	float forward = rand(gs_in[0].worldPosition.yyz) * BLADE_CURVATURE;
	vec3 v0 = gs_in[0].worldPosition.xyz;
	for(int i = 0; i < GRASS_BLADES; i++){
		mat3 facingRotationMatrix = Ang3(rand(gs_in[0].worldPosition.xyz * 2 * PI + i), vec3(0, 1, -0.1));
		mat3 transformationMatrix = facingRotationMatrix;
		float radius = i / GRASS_BLADES;
		float offset = (1 - radius) * BLADE_RADIUS;
		float baseHeight = 0;
		float lastY = 0;
		for(int j = 0; j < GRASS_SEGMENTS; j++){
			float t = j / GRASS_SEGMENTS;
			float segmentHeight = GRASS_HEIGHT * t;
			float segmentWidth = GRASS_WIDTH * (1 - t);
			baseHeight += segmentHeight;
			
			segmentWidth = j == 0 ? GRASS_WIDTH * 0.3 : segmentWidth;
			
			float segmentForward = pow(t, BLADE_CURVATURE) * forward;
			
			mat3 transformMatrix = j == 0 ? facingRotationMatrix : transformationMatrix;
			
			vec3 newPos = j == 0 ? v0 : v0;// + ((vec3(sphereDisp.xyz)) + t);
			float blend = j == 0 ? 1 : clamp(t, 0.5, t);
			vec4 position = vec4(newPos + transformMatrix * (vec3(segmentWidth, segmentHeight, segmentForward) + vec3(0, 0, offset)), 1);
			gl_Position = projection * view * position;
			gs_out.color = vec4(blend * vec3(0.4, 1, 0.1), 1);
			EmitVertex();
			vec4 position2 = vec4(newPos + transformMatrix * (vec3(-segmentWidth, segmentHeight, segmentForward) + vec3(0, 0, offset)), 1);
			gl_Position = projection * view * position2;
			gs_out.color = vec4(blend * vec3(0.4, 1, 0.1), 1);
			EmitVertex();
		}
		vec4 position = vec4(v0 + transformationMatrix * vec3(0, GRASS_HEIGHT, forward) + vec3(0, 0, offset), 1);
		gl_Position = projection * view * position;
		gs_out.color = vec4(0.8, 1, 0.2, 1);
		EmitVertex();
		EndPrimitive();
	}
	
}