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

void main() {
    vec4 col = v_color * texture2D(u_texture, v_texCoords);
    float avg = (col.r + col.g + col.b) / 3.;
    gl_FragColor = vec4(vec3(avg), col.a);
}