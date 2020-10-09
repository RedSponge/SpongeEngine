#version 330 core

#ifdef GL_ES
precision mediump float;
#endif

in vec4 v_color;
in vec2 v_texCoords;

uniform sampler2D u_texture;

void main() {
    vec4 col = v_color * texture2D(u_texture, v_texCoords);
    gl_FragColor = vec4(1 - col.rgb, col.a);
}