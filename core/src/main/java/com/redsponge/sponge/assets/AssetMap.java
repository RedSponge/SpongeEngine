package com.redsponge.sponge.assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.JsonValue.ValueType;

import java.util.Arrays;

public class AssetMap {

    private static final String FILES_KEY = "~~files~~";
    private JsonValue root;

    public AssetMap(FileHandle descriptorFile) {
        root = new JsonValue(ValueType.object);
        parseFile(descriptorFile);
    }

    private void parseFile(FileHandle file) {
        String[] lines = file.readString().split("\n");
        for (String line : lines) {
            String[] path = line.split(":")[0].split("\\\\");
            String valuesWithBrackets = line.split(":")[1];
            String[] valuesArr = valuesWithBrackets.substring(1, valuesWithBrackets.length() - 1).replace(" ", "").trim().split(",");

            JsonValue tunnel = root;
            for (String part : path) {
                if(!tunnel.has(part)) {
                    tunnel.addChild(part, new JsonValue(ValueType.object));
                }
                tunnel = tunnel.get(part);
            }
            JsonValue jsonArray = new JsonValue(ValueType.array);
            for (String value : valuesArr) {
                if(!value.isEmpty()) {
                    jsonArray.addChild(new JsonValue(value));
                }
            }
            tunnel.addChild(FILES_KEY, jsonArray);
        }
    }

    public FileHandle[] list(FileHandle file) {
        String name = file.path();
        String[] parts = name.split("/");
        JsonValue tunnel = root;
        for (String part : parts) {
            tunnel = tunnel.get(part);
        }
        String[] files = tunnel.get(FILES_KEY).asStringArray();
        FileHandle[] handles = new FileHandle[files.length];
        for (int i = 0; i < files.length; i++) {
            handles[i] = file.child(files[i]);
        }
        return handles;
    }
}
