package com.jad.textwindow;


import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.Objects;

/**
 * A simple text window that displays text.
 * The text is displayed in a JTextArea with a specified font size.
 * The window is maximized and resizable.
 * The text area is not editable and has a white background with black text.
 * The font is set to Cascadia Mono.
 * The window can be closed by clicking the close button.
 * The window can be displayed with a title, font size, background color, and foreground color.
 * The default font size is 12f, the default background color is white, and the default foreground color is black.
 */
public class TextWindow extends JFrame {
    private static final float DEFAULT_FONT_SIZE = 12f;
    private static final Color DEFAULT_BACKGROUND_COLOR = Color.WHITE;
    private static final Color DEFAULT_FOREGROUND_COLOR = Color.BLACK;
    private final JTextArea textArea = new JTextArea();

    /**
     * Default constructor.
     * Creates a new text window with the title "No title", font size 12f, background color white, and foreground color black.
     */
    public TextWindow() {
        this("No title",
             TextWindow.DEFAULT_FONT_SIZE,
             TextWindow.DEFAULT_BACKGROUND_COLOR,
             TextWindow.DEFAULT_FOREGROUND_COLOR);
    }

    /**
     * Constructor with title, font size, background color, and foreground color.
     *
     * @param title           - the title of the window
     * @param fontSize        - the font size of the text
     * @param backgroundColor - the background color of the text area
     * @param foregroundColor - the foreground color of the text area
     */
    public TextWindow(final String title, final float fontSize, final Color backgroundColor,
                      final Color foregroundColor) {
        super(title);
        final float fixFontSize = Math.min(40f, Math.max(1f, fontSize));
        final Color fixBackgroundColor =
                backgroundColor == null ? TextWindow.DEFAULT_BACKGROUND_COLOR : backgroundColor;
        final Color fixForegroundColor =
                foregroundColor == null ? TextWindow.DEFAULT_FOREGROUND_COLOR : foregroundColor;
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setExtendedState(this.getExtendedState() | JFrame.MAXIMIZED_BOTH);
        this.setResizable(true);
        this.setLocationRelativeTo(null);
        final Font font;
        try {
            font = Font.createFont(Font.TRUETYPE_FONT,
                                   Objects.requireNonNull(TextWindow.class.getResourceAsStream("/CascadiaMono.ttf")))
                    .deriveFont(fixFontSize);
        } catch (final FontFormatException | IOException exception) {
            throw new RuntimeException(exception);
        }
        this.setLayout(new BorderLayout());
        this.textArea.setFont(font);
        this.textArea.setEditable(false);
        this.textArea.setForeground(fixBackgroundColor);
        this.textArea.setBackground(fixForegroundColor);
        final JPanel panel = new JPanel();
        this.add(panel, BorderLayout.CENTER);
        panel.add(this.textArea);
        this.setVisible(true);
    }

    /**
     * Displays the text in the window.
     *
     * @param text - the text to be displayed
     */
    public void display(final String text) {
        this.textArea.setText(text);
    }

    /**
     * Closes the text window.
     */

    public void close() {
        this.dispose();
    }
}
