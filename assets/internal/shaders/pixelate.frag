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
uniform float u_width;
uniform float u_height;

uniform int u_pixelation;

const int MAX_PIXELATION = 128;

void main() {
    vec2 uv = v_texCoords;
    vec2 snaps = vec2(float(u_pixelation) / u_width, float(u_pixelation) / u_height);
    vec2 centerOffset = snaps / 2.0;

    uv.x -= mod(uv.x, snaps.x);
    uv.y -= mod(uv.y, snaps.y);

    vec2 snappedTexCoords = uv;
    vec4 avg = vec4(0);

    for(int i = 0; i < MAX_PIXELATION; i++) {
        if(i >= u_pixelation) break;
        for(int j = 0; j < MAX_PIXELATION; j++) {
            if(j >= u_pixelation) break;
            avg += texture2D(u_texture, snappedTexCoords + vec2(float(i) / u_width, float(i) / u_height));
        }
    }
    float tally = float(u_pixelation) * float(u_pixelation);

    vec4 col = v_color * (avg / tally);
    gl_FragColor = vec4(col);
}