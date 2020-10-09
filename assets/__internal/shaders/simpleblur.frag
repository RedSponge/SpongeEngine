#version 330 core

#ifdef GL_ES
precision mediump float;
#endif

in vec4 v_color;
in vec2 v_texCoords;

uniform sampler2D u_texture;

const float offset = 1.0 / 300.0f;
void main() {
    vec2 offsets[9] = vec2[](
        vec2(-offset,  offset), // top-left
        vec2( 0.0f,    offset), // top-center
        vec2( offset,  offset), // top-right
        vec2(-offset,  0.0f),   // center-left
        vec2( 0.0f,    0.0f),   // center-center
        vec2( offset,  0.0f),   // center-right
        vec2(-offset, -offset), // bottom-left
        vec2( 0.0f,   -offset), // bottom-center
        vec2( offset, -offset)  // bottom-right
    );

    float kernel[9] = {
        0.077847,	0.123317,	0.077847,
        0.123317,	0.195346,	0.123317,
        0.077847,	0.123317,	0.077847,
    };

    vec3 result = vec3(0);
    for(int i = 0; i < 9; i++) {
        result += texture2D(u_texture, v_texCoords + offsets[i]).rgb * kernel[i];
    }
    gl_FragColor = vec4(result, 1.0f);
}