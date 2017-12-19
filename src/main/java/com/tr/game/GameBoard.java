package com.tr.game;

import com.tr.exception.InvalidMoveException;
import com.tr.utils.Constants;
import org.springframework.stereotype.Component;

public final class GameBoard {
    private int size;
    private int[][] board;
    private int numberOfMoves = 0;
    private String winner = null;
    private int turn;

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

    public GameBoard(int size, Piece turn) {
        this.size = size;
        this.board = new int[size][size];
        initializeBoard();
        this.turn = turn.getValue();
    }

    public String getWinner() {
        return winner;
    }

    public Piece getTurn() {
        return Piece.fromValue(turn);
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (int row=0; row < size; row++) {
            stringBuilder.append("\n|");
            for (int col=0; col < size; col++) {
                if (board[row][col] == Constants.EMPTY) {
                    stringBuilder.append(" -");
                } else {
                    stringBuilder.append(" " + Piece.fromValue(board[row][col]));
                }
                stringBuilder.append(" |");
            }
        }

        return stringBuilder.toString();
    }

    public boolean isBoardFull() {
        if (numberOfMoves == (size * size)) {
            return true;
        }
        return false;
    }

    protected void playMove(Piece piece, Position position) throws InvalidMoveException {
        validatePositionOnBoard(position);
        if (winner != null) {
            return;
        }

        if (board[position.getRow()][position.getColumn()] != Constants.EMPTY) {
            throw new InvalidMoveException("Position already taken");
        }

        board[position.getRow()][position.getColumn()] = piece.getValue();
        numberOfMoves++;
    }

    protected boolean gameOver(Position lastPosition) throws InvalidMoveException {
        validatePositionOnBoard(lastPosition);
        if (winner != null) {
            return true;
        }

        int value = board[lastPosition.getRow()][lastPosition.getColumn()];
        if (checkRow(lastPosition.getRow(), value) || checkColumn(lastPosition.getColumn(), value) || checkDiagonal(0, 0, 1, value) || checkDiagonal(0, size-1, -1, value)) {
            winner = Piece.fromValue(value).toString();
            return true;
        }

        if (isBoardFull()) {
            winner = Constants.DRAW;
            return true;
        }

        return false;
    }

    public Piece nextTurn() {
        turn ^= 1;
        return Piece.fromValue(turn);
    }

    private boolean checkDiagonal(int xCoordinate, int yCoordinate, int incrementSize, int value) {
        for (int count=0; count<size; count++) {
            if (board[xCoordinate][yCoordinate] != value) {
                return false;
            }
            xCoordinate += 1;
            yCoordinate += incrementSize;
        }
        return true;
    }

    private boolean checkColumn(int column, int value) {
        for (int row=0; row<size; row++) {
            if (board[row][column] != value) {
                return false;
            }
        }
        return true;
    }

    private boolean checkRow(int row, int value) {
        for (int col=0; col<size; col++) {
            if (board[row][col] != value) {
                return false;
            }
        }
        return true;
    }

    private void validatePositionOnBoard(Position position) throws InvalidMoveException {
        if ((position.getRow() < 0 || position.getRow() >= size) || (position.getColumn() < 0 || position.getColumn() >= size)) {
            throw new InvalidMoveException("Invalid position");
        }
    }

    private void initializeBoard() {
        for (int row=0; row<size; row++) {
            for (int col=0; col<size; col++) {
                board[row][col] = Constants.EMPTY;
            }
        }
    }
}
