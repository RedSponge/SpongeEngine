#define __VERSION__


#ifdef GL_ES
    precision mediump float;
    #define _in varying
#else
    #define _in in
#endif

_in vec4 v_color;
_in vec2 v_texCoords;

uniform float flashAmount;
uniform sampler2D u_texture;

void main() {
    vec4 col = v_color * texture2D(u_texture, v_texCoords);
    gl_FragColor = vec4(mix(col.rgb, vec3(1), flashAmount), col.a);
}