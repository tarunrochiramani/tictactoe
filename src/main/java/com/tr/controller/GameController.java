package com.tr.controller;


import com.tr.game.GameBoard;
import com.tr.game.GameService;
import com.tr.game.Piece;
import com.tr.mediaType.GameBoardMediaType;
import com.tr.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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
    public GameBoardMediaType requestGame(@RequestHeader(value = Constants.SLACK_HEADER_CHANNEL_ID) String channelId,
                                          @RequestHeader(value = Constants.SLACK_HEADER_USER_ID) String initiatorUserId,
                                          @RequestHeader(value = Constants.SLACK_HEADER_TEXT) String text,
                                          @RequestHeader(value = Constants.SLACK_HEADER_RESPONSE_URL) String responseURL) {
        return gameService.requestGame(channelId, initiatorUserId, text, responseURL);
    }

    @RequestMapping(value = "/rest/initGame", method = RequestMethod.POST, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public GameBoardMediaType initGame() {
        GameBoard gameBoard = gameService.initGame(Piece.X);
        return aGameBoardMediaTypeBuilder().withGameBoard(gameBoard).build();
    }

}