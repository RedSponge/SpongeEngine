#define __VERSION__

#ifdef GL_ES
    precision mediump float;
    #define _in varying
#else
    #define _in in
#endif

_in vec4 v_color;
_in vec2 v_texCoords;

uniform sampler2D u_texture;
const vec2 offset = vec2(1.0 / 300.0, 0);

void main() {
    float kernel[5];
    kernel[0] = 0.06136;
    kernel[1] = 0.24477;
    kernel[2] = 0.38774;
    kernel[3] = 0.24477;
    kernel[4] = 0.06136;

    vec3 result = vec3(0);
    for (int i = -2; i <= 2; i++) {
        result += texture2D(u_texture, v_texCoords + offset * float(i)).rgb * float(kernel[i + 3]);
    }
    gl_FragColor = vec4(result, 1.0);
}