package com.tr.game;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.tr.exception.InvalidMoveException;
import com.tr.mediaType.GameBoardMediaType;
import com.tr.service.SlackMessagePostService;
import com.tr.utils.Constants;
import com.tr.utils.Helper;
import com.tr.utils.SlackMessageAction;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;

import static com.tr.builder.GameBoardMediaTypeBuilder.aGameBoardMediaTypeBuilder;

@Component
public class GameService {
    private static Logger logger = LoggerFactory.getLogger(GameService.class);
    private ObjectMapper objectMapper = new ObjectMapper();

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
        slackMessagePostService.sendEphemeralMessageToConfirmGame(channelId, initiatorUserId, secondPlayerId);


        logger.info("Game request initiated on channelId: " + channelId + " initiatorUserID: " +initiatorUserId + " secondPlayerId: " + secondPlayerId);

        return aGameBoardMediaTypeBuilder().withResponseType(true).withText("User: " + initiatorUserId + " has sent Game Request to " +  secondPlayerId).build();
    }

    public void processReply(String payload){
        logger.info("payload - " + payload);
        JavaType mapType = TypeFactory.defaultInstance().constructMapType(Map.class, String.class, Object.class);
        Map<String, Object> payloadValue = null;
        try {
            payloadValue = objectMapper.readValue(payload, mapType);
        } catch (IOException e) {
            logger.error("Error", e);
        }
        logger.info("Payload Map - " + payloadValue.toString());
//        StringTokenizer stringTokenizer = new StringTokenizer(callbackid, "-");
//        String channel = stringTokenizer.nextToken();
//        String initiator = stringTokenizer.nextToken();
//        String secondPlayer = stringTokenizer.nextToken();
//
//        String initiatorResponseURL = responseURLMapping.get(Pair.of(channel, initiator));
//        SlackMessageAction action = actions.get(0);
//
//        if (Constants.REJECT.equals(action.getValue())) {
//            slackMessagePostService.sendMessage(aGameBoardMediaTypeBuilder().withText("Challenge rejected by - " + secondPlayer).build(), initiatorResponseURL);
//        } else {
//            assignedPiece.put(Pair.of(channel, secondPlayer), Piece.X);
//            assignedPiece.put(Pair.of(channel, initiator), Piece.O);
//            GameBoard gameBoard = initGame(Piece.X);
//            games.put(channel, gameBoard);
//
//            slackMessagePostService.sendMessage(aGameBoardMediaTypeBuilder().withGameBoard(gameBoard).withResponseType(true).build(), responseURL);
//            slackMessagePostService.sendMessage(aGameBoardMediaTypeBuilder().withGameBoard(gameBoard).withResponseType(true).build(), initiatorResponseURL);
//        }


    }
}
