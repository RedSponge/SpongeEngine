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
    return mix(colour, u_transitionColour, smoothstep(0, 1, u_fade));
}

void main() {
    vec4 final = texture2D(u_texture, v_texCoords);
    final = applyMask(final);
    final = applyFade(final);
    gl_FragColor = final;
//    vec4 col = texture2D(u_texture, v_texCoords);
//    vec4 maskCol = texture2D(u_transitionTexture, v_texCoords);
//    if(maskCol.r < u_progress) {
//        gl_FragColor = u_transitionTarget;
//    } else {
//        gl_FragColor = col;
//    }
}
    /*
    if(maskCol.r > u_progress) {
        gl_FragColor = col;
    } else {
        gl_FragColor = vec4(1, 0, 0, 1);
    }
}*/