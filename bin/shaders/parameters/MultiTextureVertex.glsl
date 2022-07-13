#version 330 core//
uniform float tiles;
uniform sampler2D blendMap;
uniform sampler2D bTexture;
uniform sampler2D gTexture;
uniform sampler2D rTexture;
uniform sampler2D backgroundTexture;
//
in vec3 color;//
in vec3 surfaceNormal;//
in vec3 lightDirection;//
in vec2 textureCoords;//
in vec3 toCameraVec;//
in float visibility;//
//
out vec4 outColor;//
//
uniform vec3 lightColor;//
uniform sampler2D sampleTexture;//
uniform float shineDamper;//
uniform float reflectivity;//
uniform vec3 skyColor;//
//
void main(void){//
	//
	//LIGHTING//
	//
	vec3 unitNormal = normalize(surfaceNormal);//
	vec3 unitDirection = normalize(lightDirection);//
	vec3 unitCamVec = normalize(toCameraVec);//
	//
	vec3 reflectingDirection = -unitDirection;//
	vec3 reflectedLightDirection = reflect(reflectingDirection, unitNormal);//
	//
	//LIGHTING-SPECULAR//
	//
	float specularFactor = dot(reflectedLightDirection, unitCamVec);//
	specularFactor = max(specularFactor, 0);//
	float dampedSpecular = pow(specularFactor, shineDamper);//
	vec3 finalSpecular = dampedSpecular * lightColor; //
	//
	//LIGHTING-DIFFUSE//
	//
	float light = dot(unitNormal, unitDirection);//
	float brightness = max(light, 0.2);//
	vec3 diffuse = brightness * reflectivity * lightColor;//
//
	//TRANSPARENCY//
//
	vec4 textureColor = texture(sampleTexture, textureCoords);//
	if(textureColor.a < 0.5){//
		discard;//
	}//
	//
	//OUTPUT//
	//
      
      
      vec4 blendMapColor = texture(blendMap, textureCoords);
      float backTextureAmount = 1 - (blendMapColor.r + blendMapColor.g + blendMapColor.b);
      vec4 backgroundTextureColor = texture(backgroundTexture, textureCoords * tiles) * backTextureAmount;
      vec4 rTextureColor = texture(rTexture, textureCoords * tiles) * blendMapColor.r;
      vec4 gTextureColor = texture(gTexture, textureCoords * tiles) * blendMapColor.g;
      vec4 bTextureColor = texture(bTexture, textureCoords * tiles) * blendMapColor.b;
      
      vec4 totalColor = backGroundTextureColor + rTextureColor = gTextureColor + bTextureColor;
      outColor = vec4(diffuse, 1.0) * vec4(1, 1, 1, 1) * totalColor + vec4(finalSpecular, 1);
	outColor = vec4(diffuse, 1.0) * vec4(1, 1, 1, 1) * textureColor + vec4(finalSpecular, 1);//
	outColor = mix(vec4(skyColor, 1), outColor, visibility);//
}//