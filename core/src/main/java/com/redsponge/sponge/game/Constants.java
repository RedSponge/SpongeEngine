package com.redsponge.sponge.game;

import java.util.HashMap;

public class Constants {

    public static final String[] OBSTACLE_OPTIONS = new String[] {
            "lamp",
            "lamp",
            "lamp",
            "drawer",
            "drawer",
            "drawer",
            "book_badlogic",
            "book_dashni",
            "book_heart",
            "book_sponge_toast"
    };

    public static final HashMap<String, String> OBSTACLE_SOUNDS = new HashMap<String, String>() {{
        put("lamp", "glass_break.ogg");
        put("drawer", "wood_break.ogg");
        put("book_badlogic", "book_torn.ogg");
        put("book_dashni", "book_torn.ogg");
        put("book_heart", "book_torn.ogg");
        put("book_sponge_toast", "book_torn.ogg");
    }};

}
