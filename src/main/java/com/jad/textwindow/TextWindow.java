package com.jad.textwindow;


import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

/**
 * A simple text window that displays text using JavaFX.
 * The text is displayed in a TextArea with a specified font size.
 * The window is maximized and resizable.
 * The text area is not editable and has a white background with black text.
 * The font is set to Cascadia Mono.
 * The window can be closed by clicking the close button.
 * The window can be displayed with a title, font size, background color, and foreground color.
 * The default font size is 14f, the default background color is white, and the default foreground color is black.
 */
public class TextWindow {
    private final int fontWidth;
    private final int fontHeight;
    private TextArea textArea;
    private final int screenWidth;
    private final int screenHeight;
    private final List<TWBooleanActionState> actionStates = new java.util.ArrayList<>();
    private final List<TWMouseActionState> mouseStates = new java.util.ArrayList<>();
    private Stage stage;
    private volatile Point2D mousePosition = new Point2D(0, 0);
    private volatile Point2D lastMousePressedPosition = null;

    /** Holds the latest text to display; coalesces rapid display() calls. */
    private final AtomicReference<String> pendingText = new AtomicReference<>();
    private final AtomicBoolean updateScheduled = new AtomicBoolean(false);

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
    public TextWindow(final TextWindowSettings settings) {
        try {
            Platform.startup(() -> {});
        } catch (IllegalStateException ignored) {
            // JavaFX platform already initialized
        }

        this.screenWidth = settings.getScreenWidth();
        this.screenHeight = settings.getScreenHeight();

        final int[] fontDims = new int[2];
        final CountDownLatch initLatch = new CountDownLatch(1);

        Platform.runLater(() -> {
            final Font font = settings.getFont();

            // Measure monospace character dimensions
            final Text measurer = new Text("M");
            measurer.setFont(font);
            fontDims[0] = (int) Math.ceil(measurer.getBoundsInLocal().getWidth());
            fontDims[1] = (int) Math.ceil(measurer.getBoundsInLocal().getHeight());

            this.textArea = new TextArea();
            this.textArea.setFont(font);
            this.textArea.setEditable(false);
            this.textArea.setWrapText(false);
            this.textArea.setStyle(this.buildStyle(settings.getBackgroundColor(), settings.getForegroundColor()));

            final Scene scene = new Scene(this.textArea);
            this.stage = new Stage();
            this.stage.setTitle(settings.getTitle());
            this.stage.setScene(scene);
            this.stage.setMaximized(true);
            this.stage.setOnCloseRequest(e -> {
                Platform.exit();
                System.exit(0);
            });

            if (settings.isListenMouse()) {
                this.createMouseStates();
                scene.setOnMouseMoved(this::handleMouseMoved);
                scene.setOnMouseDragged(this::handleMouseDragged);
                scene.setOnMouseClicked(this::handleMouseClicked);
                scene.setOnMouseReleased(this::handleMouseReleased);
            }

            if (settings.isListenKeyboard()) {
                this.registerKeyHandlers(scene, settings.getKeyboardListeners());
                this.createActionStates(settings.getKeyboardListeners());
            }

            if (!settings.isMouseVisible()) {
                scene.setCursor(Cursor.NONE);
            }

            initLatch.countDown();
        });

        try {
            initLatch.await();
        } catch (final InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        this.fontWidth = fontDims[0];
        this.fontHeight = fontDims[1];
    }

    private String buildStyle(final Color bg, final Color fg) {
        return "-fx-control-inner-background: " + toWebColor(bg) + ";" +
               "-fx-text-fill: " + toWebColor(fg) + ";" +
               "-fx-highlight-fill: transparent;" +
               "-fx-highlight-text-fill: " + toWebColor(fg) + ";";
    }

    private String toWebColor(final Color color) {
        return String.format("rgba(%d,%d,%d,%.2f)",
                             (int) (color.getRed() * 255),
                             (int) (color.getGreen() * 255),
                             (int) (color.getBlue() * 255),
                             color.getOpacity());
    }

    private void createMouseStates() {
        this.mouseStates.add(new TWMouseActionState("button1"));
        this.mouseStates.add(new TWMouseActionState("button2"));
        this.mouseStates.add(new TWMouseActionState("button3"));
    }

    private void registerKeyHandlers(final Scene scene, final List<TWKeyboardListener> listeners) {
        scene.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            for (final TWKeyboardListener listener : listeners) {
                if (listener.keyCode() == event.getCode()) {
                    listener.press();
                    break;
                }
            }
        });
        scene.addEventFilter(KeyEvent.KEY_RELEASED, event -> {
            for (final TWKeyboardListener listener : listeners) {
                if (listener.keyCode() == event.getCode()) {
                    listener.release();
                    break;
                }
            }
        });
    }

    private void createActionStates(final List<TWKeyboardListener> keyboardListeners) {
        for (final TWKeyboardListener listener : keyboardListeners) {
            this.actionStates.add(listener.state());
        }
    }

    private void handleMouseMoved(final MouseEvent event) {
        this.mousePosition = new Point2D(event.getX(), event.getY());
    }

    private void handleMouseDragged(final MouseEvent event) {
        if (this.lastMousePressedPosition == null) {
            this.lastMousePressedPosition = this.sceneToTextPosition(event.getX(), event.getY());
        }
    }

    private void handleMouseClicked(final MouseEvent event) {
        final int button = this.toButtonNumber(event.getButton());
        if (button > 0) this.setMouseClick(button, event.getX(), event.getY());
    }

    private void handleMouseReleased(final MouseEvent event) {
        final Point2D releasePos = this.sceneToTextPosition(event.getX(), event.getY());
        if (this.lastMousePressedPosition != null && this.lastMousePressedPosition.equals(releasePos)) {
            final int button = this.toButtonNumber(event.getButton());
            if (button > 0) this.setMouseClick(button, event.getX(), event.getY());
        }
        this.lastMousePressedPosition = null;
    }

    private int toButtonNumber(final MouseButton button) {
        return switch (button) {
            case PRIMARY -> 1;
            case MIDDLE -> 2;
            case SECONDARY -> 3;
            default -> 0;
        };
    }

    /**
     * Converts scene coordinates to character-grid (column, row) position.
     * Accounts for TextArea internal insets.
     */
    private Point2D sceneToTextPosition(final double sceneX, final double sceneY) {
        final Insets insets = this.textArea.getInsets();
        final double contentX = sceneX - insets.getLeft();
        final double contentY = sceneY - insets.getTop();
        final int col = (int) Math.max(0, Math.floor(contentX / this.fontWidth));
        final int row = (int) Math.max(0, Math.floor(contentY / this.fontHeight));
        return new Point2D(col, row);
    }

    private void setMouseClick(final int button, final double x, final double y) {
        final TWMouseActionState state = this.getMouseState(button);
        if (state != null) state.setValue(this.sceneToTextPosition(x, y));
    }

    private TWMouseActionState getMouseState(final int button) {
        for (final TWMouseActionState state : this.mouseStates) {
            if (state.is("button" + button)) return state;
        }
        return null;
    }

    /**
     * Makes the window visible or hidden. Blocks until the JavaFX stage has processed the request,
     * ensuring the window is ready to receive events before returning.
     *
     * @param visible - true to show, false to hide
     */
    public void setVisible(final boolean visible) {
        final CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            if (visible) {
                this.stage.show();
                this.stage.requestFocus();
            } else {
                this.stage.hide();
            }
            latch.countDown();
        });
        try {
            latch.await();
        } catch (final InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Closes the text window and exits the JavaFX platform.
     */
    public void close() {
        Platform.runLater(() -> {
            this.stage.close();
            Platform.exit();
        });
    }

    /**
     * Displays the text in the window.
     * Rapid successive calls are coalesced: only the most recent text is rendered.
     *
     * @param text - the text to be displayed
     */
    public void display(final String text) {
        final StringBuilder sb = new StringBuilder();
        final String[] lines = text.split("\n");
        for (int i = 0; i < this.screenHeight; i++) {
            sb.append(TextWindowUtils.formatString((i < lines.length) ? lines[i] : "", this.screenWidth));
            if ((i + 1) < this.screenHeight) sb.append("\n");
        }
        final String formatted = sb.toString();

        this.pendingText.set(formatted);
        if (this.updateScheduled.compareAndSet(false, true)) {
            Platform.runLater(() -> {
                this.updateScheduled.set(false);
                final String t = this.pendingText.get();
                if (t != null) this.textArea.setText(t);
            });
        }
    }

    /**
     * Returns the current mouse position as a character-grid coordinate.
     *
     * @return the column/row position of the mouse cursor
     */
    public Point2D getMousePosition() {
        return this.sceneToTextPosition(this.mousePosition.getX(), this.mousePosition.getY());
    }

    /**
     * Returns the position of the mouse when the given button was last clicked.
     *
     * @param button - the button number (1=primary, 2=middle, 3=secondary)
     *
     * @return the character-grid position of the last click, or null if not clicked
     */
    public Point2D getMouseClickedPosition(final int button) {
        final TWMouseActionState state = this.getMouseState(button);
        if (state != null) return state.getValue();
        return null;
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
        for (final TWBooleanActionState state : this.actionStates) {
            if (state.is(action)) return state.getValue();
        }
        return false;
    }

    /**
     * Sets the foreground (text) color of the window.
     *
     * @param color - the JavaFX color to use for text
     */
    public void setForeground(final Color color) {
        if (this.textArea == null) return;
        final String style = this.textArea.getStyle()
                .replaceAll("-fx-text-fill:[^;]+;", "")
                .replaceAll("-fx-highlight-text-fill:[^;]+;", "");
        final String newStyle = style +
                                "-fx-text-fill: " + toWebColor(color) + ";" +
                                "-fx-highlight-text-fill: " + toWebColor(color) + ";";
        if (Platform.isFxApplicationThread()) {
            this.textArea.setStyle(newStyle);
        } else {
            Platform.runLater(() -> this.textArea.setStyle(newStyle));
        }
    }

    /**
     * Sets the background color of the window.
     *
     * @param color - the JavaFX color to use for the background
     */
    public void setBackground(final Color color) {
        if (this.textArea == null) return;
        final String style = this.textArea.getStyle()
                .replaceAll("-fx-control-inner-background:[^;]+;", "");
        final String newStyle = style + "-fx-control-inner-background: " + toWebColor(color) + ";";
        if (Platform.isFxApplicationThread()) {
            this.textArea.setStyle(newStyle);
        } else {
            Platform.runLater(() -> this.textArea.setStyle(newStyle));
        }
    }
}
