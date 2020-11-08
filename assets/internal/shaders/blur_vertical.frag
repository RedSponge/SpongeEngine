#define __VERSION__

#ifdef GL_ES
    precision mediump float;
    #define _in varying
#else
    #define _in in
#endif

_in vec4 v_color;
_in vec2 v_texCoords;
_in vec2 v_offset;

uniform sampler2D u_texture;

void main() {
    vec2 offset = v_offset;


    float kernel[11];


    kernel[0] = 0.000003;
    kernel[1] = 0.000229;
    kernel[2] = 0.005977;
    kernel[3] = 0.060598;
    kernel[4] = 0.24173;
    kernel[5] = 0.382925;
    kernel[6] = 0.24173;
    kernel[7] = 0.060598;
    kernel[8] = 0.005977;
    kernel[9] = 0.000229;
    kernel[10] = 0.000003;

    vec3 result = vec3(0);
    for (int i = -5; i <= 5; i++) {
        result += texture2D(u_texture, v_texCoords + offset * float(i)).rgb * float(kernel[i + 5]);
    }
    gl_FragColor = vec4(result, 1.0);
}