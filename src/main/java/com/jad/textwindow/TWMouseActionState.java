package com.jad.textwindow;

import javafx.geometry.Point2D;

final class TWMouseActionState extends TWActionState<Point2D> {
    public TWMouseActionState(final String key) {
        super(key);
        this.setValue(null);
    }

    @Override
    void release() {
        this.setValue(null);
    }
}
