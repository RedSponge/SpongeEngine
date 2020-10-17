package com.redsponge.sponge.assets;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.Disposable;
import com.redsponge.sponge.util.Logger;

import java.util.HashMap;

public class ShaderLoader implements Disposable {

    private static final String VERSION_LOCATION = "#define __VERSION__";
    private static final HashMap<ApplicationType, String> VERSION_MAP = new HashMap<>();

    static {
        VERSION_MAP.put(ApplicationType.Desktop, "#version 330 core");
        VERSION_MAP.put(ApplicationType.WebGL, "");
    }

    private HashMap<String, ShaderProgram> shaders;
    public ShaderLoader() {
        shaders = new HashMap<>(); }

    public ShaderProgram load(String path, String name) {
        String vertexCode = Gdx.files.internal(path + name + ".vert").readString();
        String fragmentCode = Gdx.files.internal(path + name + ".frag").readString();

        String ver = VERSION_MAP.get(Gdx.app.getType());
        vertexCode = vertexCode.replace(VERSION_LOCATION, ver);
        fragmentCode = fragmentCode.replace(VERSION_LOCATION, ver);
        ShaderProgram sp = new ShaderProgram(vertexCode, fragmentCode);
        if(!sp.isCompiled()) {
            Logger.error(this, "Failed to compile shader", name, "\n ---- LOG: ----\n" + sp.getLog());
        }
        shaders.put(name, sp);
        return sp;
    }

    @Override
    public void dispose() {
        for (ShaderProgram value : shaders.values()) {
            value.dispose();
        }
        shaders.clear();
    }

    public ShaderProgram getShader(String shader) {
        return shaders.get(shader);
    }
}
