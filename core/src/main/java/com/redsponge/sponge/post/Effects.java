package com.redsponge.sponge.post;

public final class Effects {

    public static void addPassthrough(PostProcessingPipeline pipeline) {
        pipeline.addEffect(new ShaderEffect("passthrough"));
    }

    public static void addInvert(PostProcessingPipeline pipeline) {
        pipeline.addEffect(new ShaderEffect("invert"));
    }

    public static void addGreyscale(PostProcessingPipeline pipeline) {
        pipeline.addEffect(new ShaderEffect("greyscale"));
    }

    public static void addGaussianBlur(PostProcessingPipeline pipeline) {
        pipeline.addEffect(new ShaderEffect("blur_horizontal"));
        pipeline.addEffect(new ShaderEffect("blur_vertical"));
    }

}
