package com.jad.textwindow;

import java.awt.*;

final class TWMouseActionState extends TWActionState<Point> {
    public TWMouseActionState(final String key) {
        super(key);
        this.setValue(null);
    }

    @Override
    void release() {
        this.setValue(null);
    }
}
