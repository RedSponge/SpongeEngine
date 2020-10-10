#version 330 core

#ifdef GL_ES
precision mediump float;
#endif

in vec4 v_color;
in vec2 v_texCoords;

uniform sampler2D u_texture;
const vec2 offset = vec2(0, 1.0 / 300.0f);
const float kernel[] = {
    0.06136, 	0.24477, 	0.38774, 	0.24477, 	0.06136,
};
void main() {

    vec3 result = vec3(0);
    for (int i = -2; i <= 2; i++) {
        result += texture2D(u_texture, v_texCoords + offset * i).rgb * kernel[i + 3];
    }
    gl_FragColor = vec4(result, 1.0f);
}