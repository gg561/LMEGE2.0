#version 400 core

in vec2 textureCoords;

out vec4 outColor;

//uniform sampler2D modelTexture;//will use this next week
uniform sampler2D depthMap;

void main(void){

	//float alpha = texture(modelTexture, textureCoords).a;
	//if(alpha < 0.5){
	//	discard;
	//}

	outColor = vec4(gl_FragCoord.z);
	
}