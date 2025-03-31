package com.jad.textwindow;

import java.awt.*;

/**
 * This class contains the settings for the TextWindow class.
 * <p>It allows to set the title, font size, background color,
 * foreground color, whether to listen to mouse motion and keyboard events,
 * and the screen width and height.</p
 * <p>The screen width and height are in characters.</p>
 * <p>The default values are:
 * <ul>
 * <li>title: {@value TextWindowUtils#DEFAULT_TITLE}</li>
 * <li>font size: {@value TextWindowUtils#DEFAULT_FONT_SIZE}</li>
 * <li>background color: {@link TextWindowUtils#DEFAULT_BACKGROUND_COLOR}</li>
 * <li>foreground color: {@link TextWindowUtils#DEFAULT_FOREGROUND_COLOR}</li>
 * <li>listen mouse motion: {@value TextWindowUtils#DEFAULT_LISTEN_MOUSE_MOTION}</li>
 * <li>listen keyboard: {@value TextWindowUtils#DEFAULT_LISTEN_KEYBOARD}</li>
 * <li>screen width: {@value TextWindowUtils#DEFAULT_SCREEN_WIDTH}</li>
 * <li>screen height: {@value TextWindowUtils#DEFAULT_SCREEN_HEIGHT}</li>
 * </ul></p>
 */

public final class TextWindowSettings {

    private String title = TextWindowUtils.DEFAULT_TITLE;
    private float fontSize = TextWindowUtils.DEFAULT_FONT_SIZE;
    private Color backgroundColor = TextWindowUtils.DEFAULT_BACKGROUND_COLOR;
    private Color foregroundColor = TextWindowUtils.DEFAULT_FOREGROUND_COLOR;
    private boolean listenMouseMotion = TextWindowUtils.DEFAULT_LISTEN_MOUSE_MOTION;
    private boolean listenKeyboard = TextWindowUtils.DEFAULT_LISTEN_KEYBOARD;
    private int screenWidth = TextWindowUtils.DEFAULT_SCREEN_WIDTH;
    private int screenHeight = TextWindowUtils.DEFAULT_SCREEN_HEIGHT;

    /**
     * Returns the title of the text window.
     *
     * @return - the title of the text window
     */
    public String getTitle() {
        return this.title;
    }

    /**
     * Sets the title of the text window.
     *
     * @param title - the title of the text window
     */
    public void setTitle(final String title) {
        this.title = title;
    }

    /**
     * Returns the font size of the text window.
     *
     * @return - the font size of the text window
     */
    public float getFontSize() {
        return this.fontSize;
    }

    /**
     * Sets the font size of the text window.
     *
     * @param fontSize - the font size of the text window
     */
    public void setFontSize(final float fontSize) {
        this.fontSize = fontSize;
    }

    /**
     * Returns the background color of the text window.
     *
     * @return - the background color of the text window
     */
    public Color getBackgroundColor() {
        return this.backgroundColor;
    }

    /**
     * Sets the background color of the text window.
     *
     * @param backgroundColor - the background color of the text window
     */
    public void setBackgroundColor(final Color backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    /**
     * Returns the foreground color of the text window.
     *
     * @return - the foreground color of the text window
     */
    public Color getForegroundColor() {
        return this.foregroundColor;
    }

    /**
     * Sets the foreground color of the text window.
     *
     * @param foregroundColor - the foreground color of the text window
     */
    public void setForegroundColor(final Color foregroundColor) {
        this.foregroundColor = foregroundColor;
    }

    /**
     * Returns true if the text window listens to mouse motion events.
     *
     * @return - true if the text window listens to mouse motion events
     */
    public boolean isListenMouseMotion() {
        return this.listenMouseMotion;
    }

    /**
     * Sets whether the text window should listen to mouse motion events.
     *
     * @param listenMouseMotion - true if the text window should listen to mouse motion events
     */
    public void setListenMouseMotion(final boolean listenMouseMotion) {
        this.listenMouseMotion = listenMouseMotion;
    }

    /**
     * Returns true if the text window listens to keyboard events.
     *
     * @return - true if the text window listens to keyboard events
     */
    public boolean isListenKeyboard() {
        return this.listenKeyboard;
    }

    /**
     * Sets whether the text window should listen to keyboard events.
     *
     * @param listenKeyboard - true if the text window should listen to keyboard events
     */
    public void setListenKeyboard(final boolean listenKeyboard) {
        this.listenKeyboard = listenKeyboard;
    }

    /**
     * Returns the width of the text window in characters.
     *
     * @return - the width of the text window in characters
     */
    public int getScreenWidth() {
        return this.screenWidth;
    }

    /**
     * Sets the width of the text window in characters.
     *
     * @param screenWidth - the width of the text window in characters
     */
    public void setScreenWidth(final int screenWidth) {
        this.screenWidth = screenWidth;
    }

    /**
     * Returns the height of the text window in characters.
     *
     * @return - the height of the text window in characters
     */
    public int getScreenHeight() {
        return this.screenHeight;
    }

    /**
     * Sets the height of the text window in characters.
     *
     * @param screenHeight - the height of the text window in characters
     */
    public void setScreenHeight(final int screenHeight) {
        this.screenHeight = screenHeight;
    }
}
