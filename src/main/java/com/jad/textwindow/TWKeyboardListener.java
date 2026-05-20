package com.jad.textwindow;

import javafx.scene.input.KeyCode;

record TWKeyboardListener(KeyCode keyCode, TWBooleanActionState state) {
    public String getKey() {
        return this.state.getKey();
    }

    public void press() {
        this.state.setValue(true);
    }

    public void release() {
        this.state.release();
    }
}
