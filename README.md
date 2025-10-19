# TextWindow Class

[![License: GPL v3](https://img.shields.io/badge/License-GPLv3-blue.svg)](https://www.gnu.org/licenses/gpl-3.0)

# Made with ❤️ by [JAD](mailto:jeanaymeric@gmail.com)

---

## Description

The `TextWindow` class is a custom GUI component designed to display and manage text content within a window.


---

## Features

- Create a text window with customizable title, size, and colors.
- Display text content with support for multiple lines.
- Change foreground and background colors.
- Adjust font size.
- Handle key and mouse events with character position coordinates.

## Example Simple

```java
public static void main(String[] args) {
    TextWindowSettings settings = new TextWindowSettings();
    settings.setTitle("My Text Window");
    settings.setWidth(400);
    settings.setHeight(300);
    settings.setBackgroundColor(Color.WHITE);
    settings.setForegroundColor(Color.BLACK);
    settings.setFontSize(14);

    TextWindow textWindow = new TextWindow(settings);
    textWindow.setVisible(true);
    textWindow.display("Hello, World!\nThis is a sample text window.");
}
```

## Example with Event Handling

```java
    public static void main(String[] args) {
    TextWindowSettings settings = new TextWindowSettings();
    settings.addKeyboardListener(KeyEvent.VK_ESCAPE, "exit");
    settings.addKeyboardListener(KeyEvent.VK_M, "mouseCoordinateDisplay");
    settings.addKeyboardListener(KeyEvent.VK_F, "changeForegroundColor");
    settings.addKeyboardListener(KeyEvent.VK_B, "changeBackgroundColor");
    settings.setListenMouse(true);
    settings.setMouseVisible(true);
    TextWindow textWindow = new TextWindow(settings);
    textWindow.setVisible(true);

    Point lastMousePosition;
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
            textWindow.setBackground(Color.LIGHT_GRAY);
        } else {
            textWindow.setBackground(Color.WHITE);
        }

        for (int numButton = 1; numButton < 4; numButton++) {
            Point clickedPosition = textWindow.getMouseClickedPosition(numButton);
            if (clickedPosition != null) {
                message.append("\nMouse button ").append(numButton).append(" clicked at: ").append(clickedPosition);
            }
        }
        textWindow.display(message.toString());
    }
    textWindow.close();
}
```

## Licence

This project is licensed under the GNU General Public License v3.0 or later.
See the LICENSE file for details.