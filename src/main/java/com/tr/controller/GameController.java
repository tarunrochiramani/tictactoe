package com.tr.controller;


import com.tr.game.GameBoard;
import com.tr.game.GameService;
import com.tr.game.Piece;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GameController {
    @Autowired private GameService gameService;

    @RequestMapping("/")
    public String index() {
        return "Greetings from TR's TicTacToe running on Spring Boot!";
    }

    @RequestMapping(value = "/rest/initGame", method = RequestMethod.POST)
    public GameBoard initGame(@RequestBody Piece startingPiece) {
        return gameService.initGame(startingPiece);
    }

}