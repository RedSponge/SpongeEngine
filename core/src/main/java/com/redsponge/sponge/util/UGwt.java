package com.redsponge.sponge.util;

import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;

public class UGwt {

    public static FrameBuffer createFrameBuffer(int width, int height) {
        try {
            return new FrameBuffer(Format.RGBA8888, width, height, true, true);
        } catch (IllegalStateException e) {
            Logger.warn(UGwt.class, "Tried to create FBO in GWT - falling back on Format.RGBA");
            return new FrameBuffer(Format.RGBA4444, width, height, true, true);
        }
    }

}
