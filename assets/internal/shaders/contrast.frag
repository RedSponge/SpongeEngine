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
uniform float u_contrast;

void main() {
    vec4 col = v_color * texture2D(u_texture, v_texCoords);
    float factor = (1.17 * (u_contrast + 1.0)) / (1.0 * (1.01 - u_contrast)); // Formula for contrast factor
    vec3 rgbCol = col.rgb;
    rgbCol = (factor * (rgbCol - 0.5) + 0.5);

    gl_FragColor = vec4(rgbCol, col.a);
}