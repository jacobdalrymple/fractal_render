#version 330 core
out vec4 fragColor;
  
in vec2 texCoords;

uniform sampler2D screenTexture;

void main()
{ 
    fragColor = vec4(vec3(texture(screenTexture, texCoords).rgb), 1.0);
//texture(screenTexture, texCoords);
}