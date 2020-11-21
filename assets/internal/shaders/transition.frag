#define __VERSION__

#ifdef GL_ES
    precision mediump float;
    #define _in varying
#else
    #define _in in
#endif

_in vec4 v_color;
_in vec2 v_texCoords;
_in vec2 v_transitionTexCoords;

uniform sampler2D u_texture;
uniform sampler2D u_transitionTexture;
uniform bool u_transitionTextureActive;

uniform float u_progress;
uniform float u_fade;
uniform vec4 u_transitionColour;


vec4 applyMask(vec4 colour) {
    if(texture2D(u_transitionTexture, v_transitionTexCoords).r >= u_progress) {
        return colour;
    }
    return u_transitionColour;
}

vec4 applyFade(vec4 colour) {
    return mix(colour, u_transitionColour, smoothstep(0.0, 1.0, u_fade));
}

void main() {
    vec4 final = texture2D(u_texture, v_texCoords);
    if (u_transitionTextureActive) {
        final = applyMask(final);
    }
    final = applyFade(final);
    gl_FragColor = final;
}