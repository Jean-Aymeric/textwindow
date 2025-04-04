package com.jad.textwindow;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * This class contains the settings for the TextWindow class.
 * <p>It allows to set :<ul>
 * <li>the title</li>
 * <li>font size and type</li>
 * <li>background and foreground color</li>
 * <li>whether to listen to mouse motion and keyboard events</li>
 * <li>the screen width and height.</li>
 * <li>if the mouse cursor is visible</li>
 * </ul>
 * <p>The screen width and height are in characters.</p>
 * <p>The default values are:</p>
 * <ul>
 * <li>title: {@value TextWindowUtils#DEFAULT_TITLE}</li>
 * <li>font: {@value TextWindowUtils#DEFAULT_FONT}</li>
 * <li>font size: {@value TextWindowUtils#DEFAULT_FONT_SIZE}</li>
 * <li>background color: {@link Color#WHITE}</li>
 * <li>foreground color: {@link Color#BLACK}</li>
 * <li>listen mouse motion: {@value TextWindowUtils#DEFAULT_LISTEN_MOUSE_MOTION}</li>
 * <li>listen keyboard: {@value TextWindowUtils#DEFAULT_LISTEN_KEYBOARD}</li>
 * <li>screen width: {@value TextWindowUtils#DEFAULT_SCREEN_WIDTH}</li>
 * <li>screen height: {@value TextWindowUtils#DEFAULT_SCREEN_HEIGHT}</li>
 * <li>mouse visibility: {@value TextWindowUtils#DEFAULT_MOUSE_VISIBILITY}</li>
 * </ul>
 */
public final class TextWindowSettings {

    private final List<TWKeyboardListener> keyboardListeners = new ArrayList<>();
    private String title = TextWindowUtils.DEFAULT_TITLE;
    private float fontSize = TextWindowUtils.DEFAULT_FONT_SIZE;
    private Color backgroundColor = TextWindowUtils.DEFAULT_BACKGROUND_COLOR;
    private Color foregroundColor = TextWindowUtils.DEFAULT_FOREGROUND_COLOR;
    private boolean listenMouse = TextWindowUtils.DEFAULT_LISTEN_MOUSE_MOTION;
    private boolean listenKeyboard = TextWindowUtils.DEFAULT_LISTEN_KEYBOARD;
    private int screenWidth = TextWindowUtils.DEFAULT_SCREEN_WIDTH;
    private int screenHeight = TextWindowUtils.DEFAULT_SCREEN_HEIGHT;
    private boolean mouseVisible = TextWindowUtils.DEFAULT_MOUSE_VISIBILITY;
    private Font font;

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
        this.backgroundColor = (backgroundColor == null) ? TextWindowUtils.DEFAULT_BACKGROUND_COLOR : backgroundColor;
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
        this.foregroundColor = (foregroundColor == null) ? TextWindowUtils.DEFAULT_FOREGROUND_COLOR : foregroundColor;
    }

    /**
     * Returns true if the text window listens to mouse motion events.
     *
     * @return - true if the text window listens to mouse motion events
     */
    public boolean isListenMouse() {
        return this.listenMouse;
    }

    /**
     * Sets whether the text window should listen to mouse events.
     *
     * @param listenMouse - true if the text window should listen to mouse events
     */
    public void setListenMouse(final boolean listenMouse) {
        this.listenMouse = listenMouse;
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
        this.screenWidth = Math.max(TextWindowUtils.MIN_SCREEN_WIDTH, screenWidth);
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
        this.screenHeight = Math.max(TextWindowUtils.MIN_SCREEN_HEIGHT, screenHeight);
    }

    List<TWKeyboardListener> getKeyboardListeners() {
        return this.keyboardListeners;
    }

    /**
     * Adds a keyboard listener to the text window.
     * If at least one listener is added, the text window will listen to keyboard events. The listenKeyboard property will be set to true.
     *
     * @param keyEvent - the key event to listen to
     * @param key      - the key to store the state of the key event
     */
    public void addKeyboardListener(final int keyEvent, final String key) {
        for (final TWKeyboardListener listener : this.keyboardListeners) {
            if (listener.keyEvent() == keyEvent) {
                throw new IllegalArgumentException(
                        "Keyboard listener " + KeyEvent.getKeyText(keyEvent) + " already added.");
            }
        }
        final TWKeyboardListener keyboardListener = new TWKeyboardListener(keyEvent, new TWBooleanActionState(key));
        if (!this.listenKeyboard) this.listenKeyboard = true;
        this.keyboardListeners.add(keyboardListener);
    }

    /**
     * Returns the font of the text window.
     *
     * @return - the font of the text window
     */
    public Font getFont() {
        if (this.font == null) {
            try {
                this.font = Font.createFont(
                                Font.TRUETYPE_FONT,
                                Objects.requireNonNull(
                                        TextWindow.class.getResourceAsStream("/" + TextWindowUtils.DEFAULT_FONT)))
                        .deriveFont(this.fontSize);
            } catch (final FontFormatException | IOException exception) {
                throw new RuntimeException(exception);
            }
        }
        return this.font;
    }

    /**
     * Sets the font of the text window.
     *
     * @param font - the font of the text window
     */
    public void setFont(final Font font) {
        this.font = font;
    }

    /**
     * Returns true if the mouse cursor is visible.
     *
     * @return - true if the mouse cursor is visible
     */
    public boolean isMouseVisible() {
        return this.mouseVisible;
    }

    /**
     * Sets whether the mouse cursor should be visible.
     *
     * @param mouseVisible - true if the mouse cursor should be visible
     */
    public void setMouseVisible(final boolean mouseVisible) {
        this.mouseVisible = mouseVisible;
    }
}
