#define __VERSION__

#ifdef GL_ES
    precision mediump float;
    #define _in attribute
    #define _out varying
#else
    #define _in in
    #define _out out
#endif

_in vec4 a_color;
_in vec4 a_position;
_in vec2 a_texCoord0;

uniform mat4 u_projTrans;
uniform float u_width;

_out vec4 v_color;
_out vec2 v_texCoords;
_out vec2 v_offset;

void main() {
    v_color = a_color;
    v_color.a = v_color.a * (255.0/254.0);
    v_texCoords = a_texCoord0;
    v_offset = vec2(0, 1.0 / u_width);
    gl_Position = u_projTrans * a_position;
}
