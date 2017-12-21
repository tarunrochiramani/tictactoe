package com.tr.game;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.tr.exception.InvalidMoveException;
import com.tr.mediaType.GameBoardMediaType;
import com.tr.service.SlackMessagePostService;
import com.tr.utils.Constants;
import com.tr.utils.Helper;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.tr.builder.GameBoardMediaTypeBuilder.aGameBoardMediaTypeBuilder;

@Component
public class GameService {
    private static Logger logger = LoggerFactory.getLogger(GameService.class);

    @Autowired private Helper helper;
    @Autowired private SlackMessagePostService slackMessagePostService;

    // Channel ID : GameBoard
    private Map<String, GameBoard> games = new HashMap<>();

    // Channel ID ,Player ID : Piece
    private Map<Pair<String, String>, Piece> assignedPiece = new HashMap<>();

    // Channel ID : Initiator ID , Second Player ID
    private Map<String, Pair<String, String>> gameRequestMapping = new HashMap<>();

    // Channel ID, Initiator ID : responseURL
    private Map<Pair<String, String>, String> responseURLMapping = new HashMap<>();

    public GameBoard initGame(Piece turn) {
        return new GameBoard(Constants.SIZE, turn);

    }

    public Map<Pair<String, String>, Piece> getAssignedPiece() {
        return assignedPiece;
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

    public GameBoardMediaType requestGame(String channelId, String initiatorUserId, String text, String responseURL) {
        logger.info("Received Request for game - channelId: " + channelId + " initiatorUserID: " +initiatorUserId + " text: " + text);
        if (games.containsKey(channelId)) {
            logger.warn("Game already running on channel: " + channelId);
            String fallback = "Unable to request game. A channel can have only one game at one time \n Checkout the current game /currentTTTGame";
            String pretext = "Unable to request game. A channel can have only one game at one time";
            String title = "Checkout the current game";
            String title_link = "http://localhost:8080";
            String attachmentText = "Try once the game is over";
            return aGameBoardMediaTypeBuilder().withAttachment(fallback, pretext, title, title_link, attachmentText).build();
        }

        String secondPlayerId = helper.getUserId(helper.tokenizeEscapedUser(text).get(0));
        gameRequestMapping.put(channelId, Pair.of(initiatorUserId, secondPlayerId ));
        responseURLMapping.put(Pair.of(channelId, initiatorUserId), responseURL);
        slackMessagePostService.sendEphermalMessageToConfirmGame(channelId, initiatorUserId, secondPlayerId);


        logger.info("Game request initiated on channelId: " + channelId + " initiatorUserID: " +initiatorUserId + " secondPlayerId: " + secondPlayerId);

        return aGameBoardMediaTypeBuilder().withResponseType(true).withText("User: " + initiatorUserId + " has sent Game Request to " +  secondPlayerId).build();
    }
}
