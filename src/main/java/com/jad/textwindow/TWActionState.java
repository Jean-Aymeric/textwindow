package com.jad.textwindow;

abstract class TWActionState<E> {
    private final String key;
    private E value;

    TWActionState(final String key) {
        this.key = key;
    }

    E getValue() {
        return this.value;
    }

    void setValue(final E value) {
        this.value = value;
    }

    String getKey() {
        return this.key;
    }

    abstract void release();

    boolean is(final String key) {
        return this.key.equals(key);
    }
}
