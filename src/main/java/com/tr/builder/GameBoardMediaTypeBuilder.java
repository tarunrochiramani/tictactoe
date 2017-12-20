package com.tr.builder;

import com.tr.game.GameBoard;
import com.tr.mediaType.GameBoardMediaType;
import javafx.util.Builder;

public class GameBoardMediaTypeBuilder implements Builder<GameBoardMediaType> {
    private GameBoardMediaType gameBoardMediaType = new GameBoardMediaType();

    public static GameBoardMediaTypeBuilder aGameBoardMediaTypeBuilder() {
        return new GameBoardMediaTypeBuilder();
    }

    public GameBoardMediaTypeBuilder withGameBoard(GameBoard gameBoard) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("\nBoard\n");
        stringBuilder.append(gameBoard.getBoard());
        stringBuilder.append("\nTurn - ");
        stringBuilder.append(gameBoard.getTurn());
        if (gameBoard.getWinner() != null) {
            stringBuilder.append("\nWinner - ");
            stringBuilder.append(gameBoard.getWinner());
        }
        gameBoardMediaType.setText(stringBuilder.toString());
        return this;
    }

    @Override
    public GameBoardMediaType build() {
        return gameBoardMediaType;
    }
}
