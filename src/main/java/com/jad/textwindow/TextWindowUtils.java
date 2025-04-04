package com.jad.textwindow;

import java.awt.*;

enum TextWindowUtils {
    ;
    public static final int DEFAULT_SCREEN_WIDTH = 137;
    public static final int DEFAULT_SCREEN_HEIGHT = 32;
    public static final String DEFAULT_TITLE = "Made with love by JAD (jeanaymeric@gmlail.com)";
    public static final boolean DEFAULT_MOUSE_VISIBILITY = true;
    static final float DEFAULT_FONT_SIZE = 14f;
    static final Color DEFAULT_BACKGROUND_COLOR = Color.WHITE;
    static final Color DEFAULT_FOREGROUND_COLOR = Color.BLACK;
    static final boolean DEFAULT_LISTEN_MOUSE_MOTION = false;
    static final boolean DEFAULT_LISTEN_KEYBOARD = false;
    static final String DEFAULT_FONT = "CascadiaMono.ttf";
    static final int MIN_SCREEN_WIDTH = 20;
    static final int MIN_SCREEN_HEIGHT = 20;

    public static String formatString(String str, int length) {
        if (str.length() > length) return str.substring(0, length);
        return String.format("%-" + length + "s", str);
    }
}
