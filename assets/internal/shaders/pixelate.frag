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

uniform float u_pixelation;

void main() {
    vec2 uv = v_texCoords;
    vec2 snaps = vec2(u_pixelation / u_width, u_pixelation / u_height);
//    vec2 pos = floor(uv / snaps) * snaps;
    vec2 centerOffset = snaps / 2;
//    vec2 col = vec2(mod(uv.x, snaps.x), mod(uv.y, snaps.y));
//    gl_FragColor = vec4(col * 100, 0, 1);

    uv.x -= mod(uv.x, snaps.x);
    uv.y -= mod(uv.y, snaps.y);
//    uv.x -= uv.x % divAmount.x; // Snap to chunk multiples
//    uv.y -= uv.y ^ divAmount.y; // Snap to chunk multiples

    vec2 snappedTexCoords = uv;
    vec4 avg = vec4(0);
    for(int i = 0; i < u_pixelation; i++) {
        for(int j = 0; j < u_pixelation; j++) {
            avg += texture2D(u_texture, snappedTexCoords + vec2(i / u_width, i / u_height));
        }
    }
    float tally = u_pixelation * u_pixelation;

    vec4 col = v_color * (avg / tally);
    gl_FragColor = vec4(col);
}