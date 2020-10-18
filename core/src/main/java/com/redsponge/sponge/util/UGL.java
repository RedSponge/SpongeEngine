package com.redsponge.sponge.util;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.GdxRuntimeException;

public class UGL {

    private static final float[] colorTmp = new float[4];

    public static FrameBuffer createFrameBuffer(int width, int height, boolean depth, boolean stencil) {
        try {
            return new FrameBuffer(Format.RGBA8888, width, height, depth, stencil);
        } catch (GdxRuntimeException e) {
            Logger.warn(UGL.class, "Tried to create FBO in GWT - falling back on Format.RGBA");
            return new FrameBuffer(Format.RGB565, width, height, depth, stencil);
        }
    }


    public static void setUniformColour(ShaderProgram prog, String name, Color color) {
        colorTmp[0] = color.r;
        colorTmp[1] = color.g;
        colorTmp[2] = color.b;
        colorTmp[3] = color.a;
        prog.setUniform4fv(name, colorTmp, 0, 4);
    }

}
