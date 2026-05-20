import com.jad.textwindow.TextWindow;
import com.jad.textwindow.TextWindowSettings;

import javafx.geometry.Point2D;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;

public enum Main {
    ;

    public static void main(String[] args) {
        TextWindowSettings settings = new TextWindowSettings();
        settings.addKeyboardListener(KeyCode.ESCAPE, "exit");
        settings.addKeyboardListener(KeyCode.M, "mouseCoordinateDisplay");
        settings.addKeyboardListener(KeyCode.F, "changeForegroundColor");
        settings.addKeyboardListener(KeyCode.B, "changeBackgroundColor");
        settings.setListenMouse(true);
        settings.setMouseVisible(true);
        TextWindow textWindow = new TextWindow(settings);
        textWindow.setVisible(true);

        Point2D lastMousePosition;
        while (textWindow.isOff("exit")) {
            lastMousePosition = textWindow.getMousePosition();
            StringBuilder message = new StringBuilder("Press ESC to exit.\n");
            message.append("Press M to toggle mouse coordinate display.\n");
            message.append("Press F to change foreground color.\n");
            message.append("Press B to change background color.\n");
            if (textWindow.isOn("mouseCoordinateDisplay")) {
                message.append("Mouse position: ").append(lastMousePosition);
            }
            if (textWindow.isOn("changeForegroundColor")) {
                textWindow.setForeground(Color.GREEN);
            } else {
                textWindow.setForeground(Color.BLACK);
            }
            if (textWindow.isOn("changeBackgroundColor")) {
                textWindow.setBackground(Color.LIGHTGRAY);
            } else {
                textWindow.setBackground(Color.WHITE);
            }

            for (int numButton = 1; numButton < 4; numButton++) {
                Point2D clickedPosition = textWindow.getMouseClickedPosition(numButton);
                if (clickedPosition != null) {
                    message.append("\nMouse button ").append(numButton).append(" clicked at: ").append(clickedPosition);
                }
            }
            textWindow.display(message.toString());
        }
        textWindow.close();
    }
}