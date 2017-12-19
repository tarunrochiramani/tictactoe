package com.tr.game;

import com.tr.exception.InvalidMoveException;
import com.tr.game.GameBoard;
import com.tr.game.GameBoard.Piece;
import com.tr.game.Position;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
public class GameService {

    public GameBoard initGame(int size, Piece turn) {
        return new GameBoard(size, turn);
    }

    public boolean playMove(GameBoard gameBoard, Piece piece, Position position) throws InvalidMoveException {
        if (gameBoard.isBoardFull() || gameBoard.getWinner() != null) {
            throw new InvalidMoveException("Game Over. " + gameBoard.getWinner());
        }

        if (gameBoard.getTurn() != piece) {
            throw new InvalidMoveException("Not your turn.");
        }

        gameBoard.playMove(piece, position);
        boolean gameOver = gameBoard.gameOver(position);
        if (!gameOver) {
            gameBoard.nextTurn();
        }

        return gameOver;
    }
}
