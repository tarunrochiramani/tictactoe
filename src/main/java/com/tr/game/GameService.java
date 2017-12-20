package com.tr.game;

import com.tr.exception.InvalidMoveException;
import com.tr.utils.Constants;
import org.springframework.stereotype.Component;

@Component
public class GameService {


    public GameBoard initGame(Piece turn) {
        return new GameBoard(Constants.SIZE, turn);

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
