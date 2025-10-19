package com.jad.textwindow;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.util.List;

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
    private final List<TWBooleanActionState> actionStates = new java.util.ArrayList<>();
    private final List<TWMouseActionState> mouseStates = new java.util.ArrayList<>();
    private final JComponent glassPane;
    private Point mousePosition = new Point(0, 0);
    private Point lastMousePressedPosition = null;

    /**
     * Default constructor.
     * <p>Creates a new text window with the default settings.</p>
     * <p>See {@link TextWindowSettings} for default settings.</p>
     */
    public TextWindow() {
        this(new TextWindowSettings());
    }

    /**
     * <p>Constructor with settings.</p>
     * <p>Creates a new text window with the specified settings.</p>
     * <p>See {@link TextWindowSettings} for settings.</p>
     *
     * @param settings - the settings for the text window
     */
    public TextWindow(TextWindowSettings settings) {
        super(settings.getTitle());

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setExtendedState(this.getExtendedState() | JFrame.MAXIMIZED_BOTH);
        this.setResizable(true);
        this.setLocationRelativeTo(null);
        this.setLayout(new BorderLayout());

        this.textArea = this.createTextArea(settings);

        this.fontWidth = this.textArea.getFontMetrics(this.textArea.getFont()).charWidth('M');
        this.fontHeight = this.textArea.getFontMetrics(this.textArea.getFont()).getHeight();
        this.screenSize = new Dimension(settings.getScreenWidth(), settings.getScreenHeight());
        final Point temporary = this.textAreaPositionToRealPosition(
                new Point(settings.getScreenWidth(), settings.getScreenHeight()));
        this.textArea.setSize(temporary.x, temporary.y);
        this.createPanel(settings);

        this.glassPane = (JComponent) this.getGlassPane();
        this.glassPane.setVisible(true);
        this.glassPane.setBackground(Color.PINK);
        this.glassPane.setOpaque(false);

        this.pack();
        this.setLocationRelativeTo(null);

        if (settings.isListenMouse()) {
            this.glassPane.addMouseMotionListener(new MouseMotionHandler());
            this.glassPane.addMouseListener(new MouseClickHandler());
            this.createMouseStates();
        }

        if (settings.isListenKeyboard()) {
            this.createKeyboardActionListeners(settings.getKeyboardListeners());
            this.createActionPerformers(settings.getKeyboardListeners());
            this.createActionStates(settings.getKeyboardListeners());
        }

        if (!settings.isMouseVisible()) {
            this.setMouseCursorInvisible();
        }
    }

    private JTextArea createTextArea(TextWindowSettings settings) {
        JTextArea textArea = new JTextArea(settings.getScreenHeight(), settings.getScreenWidth());
        textArea.setFont(settings.getFont());
        textArea.setEditable(false);
        textArea.setEnabled(false);
        textArea.setForeground(settings.getForegroundColor());
        textArea.setBackground(settings.getBackgroundColor());
        textArea.setDisabledTextColor(settings.getForegroundColor());
        textArea.addMouseListener(null);
        textArea.addMouseMotionListener(null);
        textArea.addKeyListener(null);
        return textArea;
    }

    private Point textAreaPositionToRealPosition(final Point point) {
        int x = point.x * this.fontWidth;
        int y = point.y * this.fontHeight;
        return new Point(x, y);
    }

    private void createPanel(final TextWindowSettings settings) {
        final JPanel panel = new JPanel();
        panel.setBackground(settings.getBackgroundColor());
        panel.addMouseListener(null);
        panel.addMouseMotionListener(null);
        panel.addKeyListener(null);
        panel.add(this.textArea, BorderLayout.CENTER);
        this.setContentPane(panel);
    }

    private void createMouseStates() {
        this.mouseStates.add(new TWMouseActionState("button1"));
        this.mouseStates.add(new TWMouseActionState("button2"));
        this.mouseStates.add(new TWMouseActionState("button3"));
    }

    private void createKeyboardActionListeners(final List<TWKeyboardListener> keyboardListeners) {
        final InputMap inputMap = this.glassPane.getInputMap();
        for (TWKeyboardListener listener : keyboardListeners) {
            inputMap.put(KeyStroke.getKeyStroke(listener.keyEvent(), 0, false),
                         listener.getKey() + "-pressed");
            inputMap.put(KeyStroke.getKeyStroke(listener.keyEvent(), 0, true),
                         listener.getKey() + "-released");
        }
    }

    private void createActionPerformers(final List<TWKeyboardListener> keyboardListeners) {
        final ActionMap actionMap = this.glassPane.getActionMap();
        for (TWKeyboardListener listener : keyboardListeners) {
            actionMap.put(listener.getKey() + "-pressed",
                          new AbstractAction() {
                              @Override
                              public void actionPerformed(final ActionEvent actionEvent) {
                                  listener.press();
                              }
                          });
            actionMap.put(listener.getKey() + "-released",
                          new AbstractAction() {
                              @Override
                              public void actionPerformed(final ActionEvent actionEvent) {
                                  listener.release();
                              }
                          });
        }
    }

    private void createActionStates(final List<TWKeyboardListener> keyboardListeners) {
        for (TWKeyboardListener listener : keyboardListeners) {
            this.actionStates.add(listener.state());
        }
    }

    private void setMouseCursorInvisible() {
        final BufferedImage cursorImg = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
        final Cursor blankCursor = Toolkit.getDefaultToolkit().createCustomCursor(
                cursorImg, new Point(0, 0), "blank cursor");
        this.glassPane.setCursor(blankCursor);
    }

    /**
     * Closes the text window.
     */
    public void close() {
        this.dispose();
    }

    /**
     * Displays the text in the window.
     *
     * @param text - the text to be displayed
     */
    public void display(final String text) {
        StringBuilder stringBuilder = new StringBuilder();
        String[] lines = text.split("\n");
        for (int i = 0; i < this.screenSize.height; i++) {
            stringBuilder
                    .append(TextWindowUtils.formatString((i < lines.length) ? lines[i] : "", this.screenSize.width))
                    .append(((i + 1) == this.screenSize.height) ? "" : "\n");
        }
        this.textArea.setText(stringBuilder.toString());
    }

    @Override
    public Point getMousePosition() {
        return this.realPositionToTextAreaPosition(this.mousePosition);
    }

    private Point realPositionToTextAreaPosition(final Point position) {
        Point textAreaPosition = this.textArea.getLocationOnScreen();
        Point glassPanePosition = this.glassPane.getLocationOnScreen();
        int deltaX = textAreaPosition.x - glassPanePosition.x;
        int deltaY = textAreaPosition.y - glassPanePosition.y;
        final int x = Math.floorDiv(position.x - deltaX, this.fontWidth);
        final int y = Math.floorDiv(position.y - deltaY, this.fontHeight);
        return new Point(x, y);
    }

    @Override
    public void setForeground(Color foreground) {
        super.setForeground(foreground);
        if (this.textArea != null) {
            this.textArea.setForeground(foreground);
            this.textArea.setDisabledTextColor(foreground);
        }
    }

    @Override
    public void setBackground(Color background) {
        super.setBackground(background);
        if (this.textArea != null) {
            this.textArea.setBackground(background);
        }
    }

    /**
     * Returns if the action is off.
     *
     * @param action - the action to check
     *
     * @return true if the action is off, false otherwise
     */
    public boolean isOff(final String action) {
        return !this.isOn(action);
    }

    /**
     * Returns if the action is on.
     *
     * @param action - the action to check
     *
     * @return true if the action is on, false otherwise
     */
    public boolean isOn(final String action) {
        for (TWBooleanActionState state : this.actionStates) {
            if (state.is(action)) return state.getValue();
        }
        return false;
    }

    private void setMouseClic(final int button, final Point point) {
        TWMouseActionState state = this.getMouseState(button);
        if (state != null) state.setValue(this.realPositionToTextAreaPosition(point));
    }

    private TWMouseActionState getMouseState(final int button) {
        for (TWMouseActionState state : this.mouseStates) {
            if (state.is("button" + button)) return state;
        }
        return null;
    }

    /**
     * Returns the position of the mouse when it was clicked.
     *
     * @param button - the button that was clicked
     *
     * @return the position of the mouse when it was clicked
     */
    public Point getMouseClickedPosition(final int button) {
        TWMouseActionState state = this.getMouseState(button);
        if (state != null) return state.getValue();
        return null;
    }

    private class MouseMotionHandler implements MouseMotionListener {
        @Override
        public void mouseDragged(final MouseEvent event) {
            if (TextWindow.this.lastMousePressedPosition == null) {
                TextWindow.this.lastMousePressedPosition = TextWindow.this.realPositionToTextAreaPosition(
                        event.getPoint());
            }
        }

        @Override
        public synchronized void mouseMoved(final MouseEvent event) {
            TextWindow.this.mousePosition = event.getPoint();
        }
    }

    private class MouseClickHandler implements MouseListener {
        @Override
        public void mouseClicked(final MouseEvent event) {
            TextWindow.this.setMouseClic(event.getButton(), event.getPoint());
        }

        @Override
        public void mousePressed(final MouseEvent event) {
        }

        @Override
        public void mouseReleased(final MouseEvent event) {
            if ((TextWindow.this.lastMousePressedPosition != null) &&
                    (TextWindow.this.lastMousePressedPosition.equals(
                            TextWindow.this.realPositionToTextAreaPosition(event.getPoint())))) {
                TextWindow.this.setMouseClic(event.getButton(), event.getPoint());
            }
            TextWindow.this.lastMousePressedPosition = null;
        }

        @Override
        public void mouseEntered(final MouseEvent e) {

        }

        @Override
        public void mouseExited(final MouseEvent e) {

        }
    }
}
