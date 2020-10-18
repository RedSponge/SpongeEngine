#define __VERSION__

#ifdef GL_ES
    precision mediump float;
    #define _in attribute
    #define _out varying
#else
    #define _in in
    #define _out out
#endif

_in vec4 a_position;
_in vec4 a_color;
_in vec2 a_texCoord0;

uniform mat4 u_projTrans;

_out vec4 v_color;
_out vec2 v_texCoords;


void main() {
    v_color = a_color;
    v_texCoords = a_texCoord0;
    gl_Position = u_projTrans * a_position;
}
