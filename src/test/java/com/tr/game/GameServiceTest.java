package com.tr.game;

import com.tr.exception.InvalidMoveException;
import com.tr.game.GameBoard.Piece;
import com.tr.utils.Constants;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

@RunWith(JUnit4.class)
public class GameServiceTest {

    private GameService gameService = new GameService();

    @Test
    public void canInitGame() {
        GameBoard gameBoard = gameService.initGame(3, Piece.X);

        assertNotNull(gameBoard);
        assertEquals(Piece.X, gameBoard.getTurn());
        assertNull(gameBoard.getWinner());
        assertNotNull(gameBoard.toString());
    }

    @Test (expected = InvalidMoveException.class)
    public void cannotPlayMoveWhenOutOfTurn() throws InvalidMoveException {
        GameBoard gameBoard = gameService.initGame(3, Piece.X);
        gameService.playMove(gameBoard, Piece.O, new Position(1,1));
    }

    @Test (expected = InvalidMoveException.class)
    public void cannotPlayMoveOutOfBoard() throws InvalidMoveException {
        GameBoard gameBoard = gameService.initGame(3, Piece.X);
        gameService.playMove(gameBoard, Piece.O, new Position(4,1));
    }

    @Test (expected = InvalidMoveException.class)
    public void cannotPlayMoveOnAlreadyUsedPosition() throws InvalidMoveException {
        GameBoard gameBoard = gameService.initGame(3, Piece.X);
        gameService.playMove(gameBoard, Piece.X, new Position(1,1));

        gameService.playMove(gameBoard, Piece.O, new Position(1,1));
    }

    @Test
    public void canPlayMoveAndWinRow() throws InvalidMoveException {
        GameBoard gameBoard = gameService.initGame(3, Piece.X);

        assertFalse(gameService.playMove(gameBoard, Piece.X, new Position(1,1)));
        assertFalse(gameService.playMove(gameBoard, Piece.O, new Position(0,1)));
        assertFalse(gameService.playMove(gameBoard, Piece.X, new Position(1,0)));
        assertFalse(gameService.playMove(gameBoard, Piece.O, new Position(0,0)));
        assertTrue(gameService.playMove(gameBoard, Piece.X, new Position(1,2)));
        assertEquals(Piece.X.toString(), gameBoard.getWinner());
    }


    @Test(expected = InvalidMoveException.class)
    public void cannotPlayMoveAfterWinRow() throws InvalidMoveException {
        GameBoard gameBoard = gameService.initGame(3, Piece.X);

        assertFalse(gameService.playMove(gameBoard, Piece.X, new Position(1,1)));
        assertFalse(gameService.playMove(gameBoard, Piece.O, new Position(0,1)));
        assertFalse(gameService.playMove(gameBoard, Piece.X, new Position(1,0)));
        assertFalse(gameService.playMove(gameBoard, Piece.O, new Position(0,0)));
        assertTrue(gameService.playMove(gameBoard, Piece.X, new Position(1,2)));
        assertEquals(Piece.X.toString(), gameBoard.getWinner());
        assertTrue(gameService.playMove(gameBoard, Piece.X, new Position(2,2)));
    }

    @Test
    public void canPlayMoveAndWinColumn() throws InvalidMoveException {
        GameBoard gameBoard = gameService.initGame(3, Piece.X);

        assertFalse(gameService.playMove(gameBoard, Piece.X, new Position(0,2)));
        assertFalse(gameService.playMove(gameBoard, Piece.O, new Position(0,1)));
        assertFalse(gameService.playMove(gameBoard, Piece.X, new Position(1,2)));
        assertFalse(gameService.playMove(gameBoard, Piece.O, new Position(0,0)));
        assertTrue(gameService.playMove(gameBoard, Piece.X, new Position(2,2)));
        assertEquals(Piece.X.toString(), gameBoard.getWinner());
    }

    @Test
    public void canPlayMoveAndWinDiagonal() throws InvalidMoveException {
        GameBoard gameBoard = gameService.initGame(3, Piece.X);

        assertFalse(gameService.playMove(gameBoard, Piece.X, new Position(0,0)));
        assertFalse(gameService.playMove(gameBoard, Piece.O, new Position(0,1)));
        assertFalse(gameService.playMove(gameBoard, Piece.X, new Position(2,2)));
        assertFalse(gameService.playMove(gameBoard, Piece.O, new Position(0,2)));
        assertTrue(gameService.playMove(gameBoard, Piece.X, new Position(1,1)));
        assertEquals(Piece.X.toString(), gameBoard.getWinner());
    }

    @Test
    public void canPlayMoveAndWinDiagonal_Reverse() throws InvalidMoveException {
        GameBoard gameBoard = gameService.initGame(3, Piece.X);

        assertFalse(gameService.playMove(gameBoard, Piece.X, new Position(0,2)));
        assertFalse(gameService.playMove(gameBoard, Piece.O, new Position(0,1)));
        assertFalse(gameService.playMove(gameBoard, Piece.X, new Position(2,0)));
        assertFalse(gameService.playMove(gameBoard, Piece.O, new Position(1,2)));
        assertTrue(gameService.playMove(gameBoard, Piece.X, new Position(1,1)));
        assertEquals(Piece.X.toString(), gameBoard.getWinner());
    }

    @Test
    public void canPlayMoveAndDraw() throws InvalidMoveException {
        GameBoard gameBoard = gameService.initGame(3, Piece.X);

        assertFalse(gameService.playMove(gameBoard, Piece.X, new Position(1,1)));
        assertFalse(gameService.playMove(gameBoard, Piece.O, new Position(0,0)));
        assertFalse(gameService.playMove(gameBoard, Piece.X, new Position(0,1)));
        assertFalse(gameService.playMove(gameBoard, Piece.O, new Position(2,1)));
        assertFalse(gameService.playMove(gameBoard, Piece.X, new Position(1,0)));
        assertFalse(gameService.playMove(gameBoard, Piece.O, new Position(1,2)));
        assertFalse(gameService.playMove(gameBoard, Piece.X, new Position(0,2)));
        assertFalse(gameService.playMove(gameBoard, Piece.O, new Position(2,0)));
        assertTrue(gameService.playMove(gameBoard, Piece.X, new Position(2,2)));
        assertEquals(Constants.DRAW, gameBoard.getWinner());
    }
}
