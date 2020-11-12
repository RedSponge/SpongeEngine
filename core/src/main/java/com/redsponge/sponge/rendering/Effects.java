package com.redsponge.sponge.rendering;

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

    public static ParameterizedShaderEffect addContrast(RenderingPipeline pipeline, float contrast) {
        ParameterizedShaderEffect pse = new ParameterizedShaderEffect("contrast");
        pse.putParameter("u_contrast", contrast);
        pipeline.addEffect(pse);
        return pse;
    }

    public static ParameterizedShaderEffect addPixelation(RenderingPipeline rPipeline, float pixelation) {
        ParameterizedShaderEffect pse = new ParameterizedShaderEffect("pixelate");
        pse.putParameter("u_pixelation", pixelation);
        rPipeline.addEffect(pse);
        return pse;
    }
}
