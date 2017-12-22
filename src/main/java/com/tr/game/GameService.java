package com.tr.game;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.ArrayType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.tr.builder.GameBoardMediaTypeBuilder;
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

import static com.tr.builder.GameBoardMediaTypeBuilder.aGameBoardMediaTypeBuilder;

@Component
public class GameService {
    private static Logger logger = LoggerFactory.getLogger(GameService.class);
    private ObjectMapper objectMapper = new ObjectMapper();

    @Autowired private Helper helper;
    @Autowired private SlackMessagePostService slackMessagePostService;

    // Channel ID : GameBoard
    private Map<String, GameBoard> games = new HashMap<>();

    // Channel ID : Initiator ID , Second Player ID
    private Map<String, Pair<String, String>> channelGamePlayers = new HashMap<>();

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

    public GameBoardMediaType playMove(String channelId, String initiatorUserId, String text) {
        logger.info("Received Request for game - channelId: " + channelId + " initiatorUserID: " +initiatorUserId + " text: " + text);
        if (!games.containsKey(channelId)) {
            logger.warn("No Game running on channel: " + channelId);
            return aGameBoardMediaTypeBuilder().withText("Unable to play move. No game running on this channel \n To start a game /ttt @user").build();
        }

        GameBoard gameBoard = games.get(channelId);
        Piece piece = assignedPiece.get(Pair.of(channelId, initiatorUserId));
        StringTokenizer stringTokenizer = new StringTokenizer(text);
        int row = Integer.valueOf(stringTokenizer.nextToken());
        int col = Integer.valueOf(stringTokenizer.nextToken());
        Position position = new Position(row, col);

        boolean gameOver;
        try {
            gameOver = playMove(gameBoard, piece, position);
        } catch (InvalidMoveException e) {
            return aGameBoardMediaTypeBuilder().withText(e.getMessage()).build();
        }

        Pair<String, String> players = channelGamePlayers.get(channelId);
        String playerOneId = players.getLeft();
        String playerTwoId = players.getRight();
        GameBoardMediaTypeBuilder gameBoardMediaTypeBuilder = aGameBoardMediaTypeBuilder().withText("\n<@" + playerOneId + "> : " + assignedPiece.get(Pair.of(channelId, playerOneId)))
                .addText("\n<@" + playerTwoId + "> : " + assignedPiece.get(Pair.of(channelId, playerTwoId)))
                .withResponseType(true)
                .withGameBoard(gameBoard);

        StringBuilder stringBuilder = new StringBuilder();
        if (gameOver) {
            games.remove(channelId);
            if (!Constants.DRAW.equals(gameBoard.getWinner())) {
                stringBuilder.append("\n\nWinner <@" + initiatorUserId + ">");
            } else {
                stringBuilder.append(Constants.DRAW);
            }
            stringBuilder.append("\n\nGAME OVER");
        }

        gameBoardMediaTypeBuilder.addText(stringBuilder.toString());

        return gameBoardMediaTypeBuilder.build();
    }

    protected boolean playMove(GameBoard gameBoard, Piece piece, Position position) throws InvalidMoveException {
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
            String fallback = "Unable to request game. A channel can have only one game at one time \n Checkout the current game /ttt-current";
            String pretext = "Unable to request game. A channel can have only one game at one time";
            String title = "Checkout the current game using /ttt-current";
            String title_link = "";
            String attachmentText = "Try once the game is over";
            return aGameBoardMediaTypeBuilder().withAttachment(fallback, pretext, title, title_link, attachmentText).build();
        }

        String secondPlayerId = helper.getUserId(helper.tokenizeEscapedUser(text).get(0));
        gameRequestMapping.put(channelId, Pair.of(initiatorUserId, secondPlayerId ));
        responseURLMapping.put(Pair.of(channelId, initiatorUserId), responseURL);
        slackMessagePostService.sendEphemeralMessageToConfirmGame(channelId, initiatorUserId, secondPlayerId);


        logger.info("Game request initiated on channelId: " + channelId + " initiatorUserID: " +initiatorUserId + " secondPlayerId: " + secondPlayerId);

        return aGameBoardMediaTypeBuilder().withResponseType(true).withText("User: <@" + initiatorUserId + "> has sent Game Request to <@" +  secondPlayerId + ">").build();
    }

    public void processReply(String payload){
        logger.info("Processing interactive Slack payload - " + payload);
        JavaType mapType = TypeFactory.defaultInstance().constructMapType(Map.class, String.class, Object.class);
        try {
            Map<String, Object> payloadMap = objectMapper.readValue(payload, mapType);

            StringTokenizer stringTokenizer = new StringTokenizer((String)payloadMap.get(Constants.SLACK_ATTACHMENT_CALLBACK_ID), "-");
            String channel = stringTokenizer.nextToken();
            String initiator = stringTokenizer.nextToken();
            String secondPlayer = stringTokenizer.nextToken();

            String initiatorResponseURL = responseURLMapping.get(Pair.of(channel, initiator));
            ArrayType actionType = TypeFactory.defaultInstance().constructArrayType(SlackMessageAction.class);
            SlackMessageAction[] actions = objectMapper.convertValue(payloadMap.get(Constants.SLACK_ACTIONS), actionType);
            SlackMessageAction action = actions[0];

            if (Constants.REJECT.equals(action.getValue())) {
                slackMessagePostService.sendMessage(aGameBoardMediaTypeBuilder().withResponseType(true).withText("Challenge rejected by <@" + secondPlayer + ">").build(), initiatorResponseURL);
            } else {
                assignedPiece.put(Pair.of(channel, secondPlayer), Piece.X);
                assignedPiece.put(Pair.of(channel, initiator), Piece.O);
                GameBoard gameBoard = initGame(Piece.X);
                games.put(channel, gameBoard);
                channelGamePlayers.put(channel, Pair.of(initiator, secondPlayer));

                slackMessagePostService.sendMessage(aGameBoardMediaTypeBuilder().withText("\nLets Play!!\n\n<@" + initiator + "> : " + assignedPiece.get(Pair.of(channel, initiator)))
                        .addText("\n<@" + secondPlayer + "> : " + assignedPiece.get(Pair.of(channel, secondPlayer))).withGameBoard(gameBoard).withResponseType(true).build(), (String)payloadMap.get(Constants.SLACK_REQUEST_PARAM_RESPONSE_URL));
                slackMessagePostService.sendMessage(aGameBoardMediaTypeBuilder().withText("\nLets Play!!\n\n<@" + initiator + "> : " + assignedPiece.get(Pair.of(channel, initiator)))
                        .addText("\n<@" + secondPlayer + "> : " + assignedPiece.get(Pair.of(channel, secondPlayer))).withGameBoard(gameBoard).build(), initiatorResponseURL);
            }
        } catch (IOException e) {
            logger.error("Error", e);
        }

    }

    public GameBoardMediaType currentGame(String channelId) {
        logger.info("Received Request for game - channelId: " + channelId);
        if (!games.containsKey(channelId)) {
            logger.warn("No Game running on channel: " + channelId);
            return aGameBoardMediaTypeBuilder().withText("No game running on this channel \n To start a game /ttt @user").build();
        }

        return aGameBoardMediaTypeBuilder().withGameBoard(games.get(channelId)).build();
    }

    public GameBoardMediaType abortGame(String channelId, String userid) {
        logger.info("Received Request for game - channelId: " + channelId);
        if (!games.containsKey(channelId)) {
            logger.warn("No Game running on channel: " + channelId);
            return aGameBoardMediaTypeBuilder().withText("No game running on this channel that could be aborted").build();
        }

        GameBoard gameBoard = games.get(channelId);
        if (!assignedPiece.get(Pair.of(channelId, userid)).equals(gameBoard.getTurn())) {
            return aGameBoardMediaTypeBuilder().withText("Cannot Abort the game, as its not your turn").build();
        }

        games.remove(channelId);
        return aGameBoardMediaTypeBuilder().withGameBoard(gameBoard).addText("\nGame Aborted by <@" + userid + ">").withResponseType(true).build();
    }
}
