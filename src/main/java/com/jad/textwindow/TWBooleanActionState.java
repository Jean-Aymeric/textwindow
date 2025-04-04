package com.jad.textwindow;

final class TWBooleanActionState extends TWActionState<Boolean> {
    public TWBooleanActionState(final String key) {
        super(key);
        this.setValue(false);
    }

    @Override
    void release() {
        this.setValue(false);
    }
}
