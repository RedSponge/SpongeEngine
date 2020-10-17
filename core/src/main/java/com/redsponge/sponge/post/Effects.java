package com.redsponge.sponge.post;

public final class Effects {

    public static void addPassthrough(RenderingPipeline pipeline) {
        pipeline.addEffect(new ShaderEffect("passthrough"));
    }

    public static void addInvert(RenderingPipeline pipeline) {
        pipeline.addEffect(new ShaderEffect("invert"));
    }

    public static void addGreyscale(RenderingPipeline pipeline) {
        pipeline.addEffect(new ShaderEffect("greyscale"));
    }

    public static void addGaussianBlur(RenderingPipeline pipeline) {
        pipeline.addEffect(new ShaderEffect("blur_horizontal"));
        pipeline.addEffect(new ShaderEffect("blur_vertical"));
    }

}
