package com.tr.controller;


import com.tr.game.GameBoard;
import com.tr.game.GameService;
import com.tr.game.Piece;
import com.tr.mediaType.GameBoardMediaType;
import org.springframework.beans.factory.annotation.Autowired;
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

    @RequestMapping(value = "/rest/initGame", method = RequestMethod.POST)
    public GameBoardMediaType initGame() {
        GameBoard gameBoard = gameService.initGame(Piece.X);
        return aGameBoardMediaTypeBuilder().withGameBoard(gameBoard).build();
    }

}