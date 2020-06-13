#version 330
in vec2 st;

uniform vec3 zoomInfo;
uniform float fractalPower;

out vec4 fragColor;

void main () {
    vec4 color = vec4(0.0, 0.0, 0.0, 0.0);

    vec2 offset = vec2(zoomInfo.x, zoomInfo.y);
    vec2 coords = (st - vec2(0.5f))*(zoomInfo.z) - offset;

    vec2 z = vec2(0.0f);
    int limit = 500;

    for (int i = 0; i < 500; i++) {
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
