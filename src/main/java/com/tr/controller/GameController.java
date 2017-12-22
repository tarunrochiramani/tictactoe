package com.tr.controller;


import java.util.List;
import java.util.Map;

import com.tr.game.GameBoard;
import com.tr.game.GameService;
import com.tr.game.Piece;
import com.tr.mediaType.GameBoardMediaType;
import com.tr.utils.Constants;
import com.tr.utils.SlackMessageAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static com.tr.builder.GameBoardMediaTypeBuilder.aGameBoardMediaTypeBuilder;

@RestController
public class GameController {
    @Autowired private GameService gameService;

    @RequestMapping("/")
    public String index() {
        return "Greetings from TR's TicTacToe running on Spring Boot!";
    }

    @RequestMapping(value = "/rest/requestGame", method = RequestMethod.POST, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public GameBoardMediaType requestGame(@RequestParam(value = Constants.SLACK_REQUEST_PARAM_CHANNEL_ID) String channelId,
                                          @RequestParam(value = Constants.SLACK_REQUEST_PARAM_USER_ID) String initiatorUserId,
                                          @RequestParam(value = Constants.SLACK_REQUEST_PARAM_TEXT) String text,
                                          @RequestParam(value = Constants.SLACK_REQUEST_PARAM_RESPONSE_URL) String responseURL) {
        return gameService.requestGame(channelId, initiatorUserId, text, responseURL);
    }

    @RequestMapping(value = "/rest/slack/interactive", method = RequestMethod.POST, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity interactiveResponse(@RequestParam(value = Constants.PAYLOAD) String payload) {

        gameService.processReply(payload);

        return new ResponseEntity(HttpStatus.OK);
    }

    @RequestMapping(value = "/rest/initGame", method = RequestMethod.POST, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public GameBoardMediaType initGame() {
        GameBoard gameBoard = gameService.initGame(Piece.X);
        return aGameBoardMediaTypeBuilder().withGameBoard(gameBoard).build();
    }

    @RequestMapping(value = "/rest/currentGame", method = RequestMethod.POST, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public GameBoardMediaType currentGame(@RequestParam(value = Constants.SLACK_REQUEST_PARAM_CHANNEL_ID) String channelId,
                                          @RequestParam(value = Constants.SLACK_REQUEST_PARAM_USER_ID) String initiatorUserId,
                                          @RequestParam(value = Constants.SLACK_REQUEST_PARAM_TEXT) String text) {
        return gameService.currentGame(channelId, initiatorUserId, text);
    }

    @RequestMapping(value = "/rest/playMove", method = RequestMethod.POST, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public GameBoardMediaType playMove(@RequestParam(value = Constants.SLACK_REQUEST_PARAM_CHANNEL_ID) String channelId,
                                          @RequestParam(value = Constants.SLACK_REQUEST_PARAM_USER_ID) String initiatorUserId,
                                          @RequestParam(value = Constants.SLACK_REQUEST_PARAM_TEXT) String text) {
        return gameService.playMove(channelId, initiatorUserId, text);
    }
}