package com.jad.textwindow;


import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
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
    private final int fontWidth;
    private final int fontHeight;
    private final JTextArea textArea;
    private final Dimension screenSize;
    private Point mousePosition;

    /**
     * Default constructor.
     * Creates a new text window with the title "No title", font size 12f, background color white, and foreground color black.
     */
    public TextWindow() {
        this(new TextWindowSettings());
    }

    /**
     * Constructor with title, font size, background color, and foreground color.
     */
    public TextWindow(TextWindowSettings settings) {
        super(settings.getTitle());
        final float fixFontSize = Math.min(40f, Math.max(1f, settings.getFontSize()));
        final Color fixBackgroundColor =
                settings.getBackgroundColor() == null ? TextWindowUtils.DEFAULT_BACKGROUND_COLOR :
                        settings.getBackgroundColor();
        final Color fixForegroundColor =
                settings.getForegroundColor() == null ? TextWindowUtils.DEFAULT_FOREGROUND_COLOR :
                        settings.getForegroundColor();
        final int fixScreenWidth = Math.max(20, settings.getScreenWidth());
        final int fixScreenHeight = Math.max(10, settings.getScreenHeight());

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
        this.mousePosition = new Point((int) (TextWindowUtils.SCREEN_DIMENSION.getWidth() / 2),
                                       (int) (TextWindowUtils.SCREEN_DIMENSION.getHeight() / 2));
        this.setLayout(new BorderLayout());
        this.textArea = new JTextArea(fixScreenHeight, fixScreenWidth);
        this.textArea.setFont(font);
        this.textArea.setEditable(false);
        this.textArea.setEnabled(false);
        this.textArea.setForeground(fixForegroundColor);
        this.textArea.setBackground(fixBackgroundColor);
        this.textArea.setDisabledTextColor(fixForegroundColor);
        this.fontWidth = this.textArea.getFontMetrics(this.textArea.getFont()).charWidth('M');
        this.fontHeight = this.textArea.getFontMetrics(this.textArea.getFont()).getHeight();
        this.screenSize = new Dimension(fixScreenWidth, fixScreenHeight);
        final Point temporary = this.textAreaPositionToRealPosition(new Point(fixScreenWidth, fixScreenHeight));
        this.textArea.setSize(temporary.x, temporary.y);
        final JPanel panel = new JPanel();
        this.setContentPane(panel);
        panel.setBackground(Color.PINK);
        panel.add(this.textArea, BorderLayout.CENTER);
        if (settings.isListenMouseMotion()) this.textArea.addMouseMotionListener(new MouseMotionHandler());
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    private Point textAreaPositionToRealPosition(final Point point) {
        final int fontWidth = this.textArea.getFontMetrics(this.textArea.getFont()).charWidth('M');
        final int fontHeight = this.textArea.getFontMetrics(this.textArea.getFont()).getHeight();
        final int x = point.x * fontWidth;
        final int y = point.y * fontHeight;
        return new Point(x, y);
    }


    private void setMouseCursorInvisible() {
        final BufferedImage cursorImg = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
        final Cursor blankCursor = Toolkit.getDefaultToolkit().createCustomCursor(
                cursorImg, new Point(0, 0), "blank cursor");
        this.getContentPane().setCursor(blankCursor);
    }

    /**
     * Displays the text in the window.
     *
     * @param text - the text to be displayed
     */
    public void display(final String text) {
        StringBuilder stringBuilder = new StringBuilder();
        int maxLines = this.screenSize.height;
        String[] lines = text.split("\n");
        for (int i = 0; i < this.screenSize.height; i++) {
            stringBuilder
                    .append(TextWindowUtils.formatString((i < lines.length) ? lines[i] : "", this.screenSize.width))
                    .append(((i + 1) == this.screenSize.height) ? "" : "\n");
        }
        this.textArea.setText(stringBuilder.toString());
    }


    /**
     * Closes the text window.
     */

    public void close() {
        this.dispose();
    }

    @Override
    public Point getMousePosition() {
        return this.realPositionToTextAreaPosition(this.mousePosition);
    }

    private Point realPositionToTextAreaPosition(final Point position) {
        final int x = position.x / this.fontWidth;
        final int y = position.y / this.fontHeight;
        return new Point(x, y);
    }

    private class MouseMotionHandler implements MouseMotionListener {
        @Override
        public void mouseDragged(final MouseEvent mouseEvent) {

        }

        @Override
        public synchronized void mouseMoved(final MouseEvent mouseEvent) {
            TextWindow.this.mousePosition = mouseEvent.getPoint();
        }
    }
}
