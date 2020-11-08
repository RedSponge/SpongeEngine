package com.redsponge.sponge.util;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.GdxRuntimeException;

import java.util.Stack;

public final class UGL {

    private static final float[] tmp = new float[4];
    private static final FrameBufferStack fboStack = new FrameBufferStack();

    public static FrameBufferStack getFboStack() {
        return fboStack;
    }

    public static FrameBuffer createFrameBuffer(int width, int height, boolean depth, boolean stencil) {
        try {
            return new FrameBuffer(Format.RGBA8888, width, height, depth, stencil);
        } catch (GdxRuntimeException e) {
            Logger.warn(UGL.class, "Tried to create FBO in GWT - falling back on Format.RGBA");
            return new FrameBuffer(Format.RGB565, width, height, depth, stencil);
        }
    }

    /**
     * Sets the uniform according to the object
     */
    public static void setUniformObject(ShaderProgram prog, String name, Object obj) {
        if(obj instanceof Float) {
            prog.setUniformf(name, (float) obj);
        }
        else if(obj instanceof Integer) {
            prog.setUniformi(name, (int) obj);
        }
        else if(obj instanceof Boolean) {
            prog.setUniformi(name, (boolean) obj ? 1 : 0);
        }
        else if(obj instanceof Double) {
            prog.setUniformf(name, (float) (double) obj);
        }
        else if(obj instanceof Vector2) {
            prog.setUniformf(name, (Vector2) obj);
        }
        else if(obj instanceof Vector3) {
            prog.setUniformf(name, (Vector3) obj);
        }
        else if(obj instanceof Color) {
            prog.setUniformf(name, (Color) obj);
        }
        else if(obj instanceof float[]) {
            float[] arr = (float[]) obj;
            switch (arr.length) {
                case 1:
                    prog.setUniformf(name, arr[0]);
                    break;
                case 2:
                    prog.setUniform2fv(name, arr, 0, 2);
                    break;
                case 3:
                    prog.setUniform3fv(name, arr, 0, 3);
                    break;
                case 4:
                    prog.setUniform4fv(name, arr, 0, 4);
                    break;
                default:
                    Logger.error(UGL.class, "Failed to set uniform", name, "since it's a", arr.length, "long float array!");
            }
        }
    }

    public static class FrameBufferStack {
        private final Stack<FrameBuffer> stack;

        public FrameBufferStack() {
            stack = new Stack<>();
        }

        public void push(FrameBuffer buffer) {
            if(stack.size() != 0) {
                stack.peek().end();
            }
            stack.push(buffer);
            buffer.begin();
        }

        public FrameBuffer pop() {
            FrameBuffer fbo = stack.pop();
            fbo.end();
            if(stack.size() != 0) {
                stack.peek().begin();
            }
            return fbo;
        }
    }

}
