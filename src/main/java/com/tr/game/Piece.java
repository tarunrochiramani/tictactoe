package com.tr.game;

public enum Piece {
    X (1), O (0);
    private int value;

    Piece(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static Piece fromValue(int value) {
        if (X.value == value) {
            return X;
        }
        return O;
    }
}
