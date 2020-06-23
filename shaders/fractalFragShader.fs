#version 330
in vec2 st;

uniform vec3 zoomInfo;
uniform float fractalPower;
uniform float iterationNum;
uniform vec2 aspectScale;

out vec4 fragColor;

void main () {
    vec4 color = vec4(0.0, 0.0, 0.0, 0.0);

    vec2 offset = vec2(zoomInfo.x, zoomInfo.y);
    vec2 coords = ((st - vec2(0.5f))*(zoomInfo.z) - offset) * aspectScale;

    vec2 z = vec2(0.0f);

    for (float i = 0; i < iterationNum; i++) {
        z = vec2( z.x*z.x - z.y*z.y, fractalPower*z.x*z.y ) + coords;
        
        if (z.x*z.x + z.y*z.y > 5) {
            if (i - (2 * floor(i/2)) == 0) {
                color = vec4(0.0, 0.0, 0.0, 1.0);
            }
            else {
                color = vec4(1.0);
            }
            //color = vec4((float(i) / 100), 0.0, 0.0, 1.0);
        }
    }

    fragColor = color;// vec4(texture(img, st).rgb, 1.0);
}
