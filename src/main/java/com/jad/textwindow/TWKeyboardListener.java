package com.jad.textwindow;

record TWKeyboardListener(int keyEvent, TWBooleanActionState state) {
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
