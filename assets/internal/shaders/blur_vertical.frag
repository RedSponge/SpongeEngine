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


    float kernel[7];

    kernel[0] = 0.00598;
    kernel[1] = 0.060626;
    kernel[2] = 0.241843;
    kernel[3] = 0.383103;
    kernel[4] = 0.241843;
    kernel[5] = 0.060626;
    kernel[6] = 0.00598;

    vec3 result = vec3(0);
    for (int i = -3; i <= 3; i++) {
        result += texture2D(u_texture, v_texCoords + offset * float(i)).rgb * float(kernel[i + 3]);
    }
    gl_FragColor = vec4(result, 1.0);
}