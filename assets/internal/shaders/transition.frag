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
uniform sampler2D u_transitionTexture;
uniform float u_progress;

void main() {
    vec4 col = texture2D(u_texture, v_texCoords);
    vec4 maskCol = texture2D(u_transitionTexture, v_texCoords);
    gl_FragColor = col;
}
    /*
    if(maskCol.r > u_progress) {
        gl_FragColor = col;
    } else {
        gl_FragColor = vec4(1, 0, 0, 1);
    }
}