package com.bol.kalahgame.controller;

import com.bol.kalahgame.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class GameController {

    private final GameHolderController gameHolderController;
    private final GameService gameService;

    @Autowired
    public GameController(GameHolderController gameHolderController, GameService gameService) {
        this.gameHolderController = gameHolderController;
        this.gameService = gameService;
    }

    @GetMapping("/start")
    public String startGame(Model model) {
        gameHolderController.startGame();
        model.addAttribute("game", gameHolderController.getGame());
        return "board";
    }

    @PostMapping("/turn")
    public String turn(Model model, @RequestParam String index) {
        final int startingIndex = Integer.parseInt(index);
        gameService.processNextTurn(gameHolderController.getGame(), startingIndex);
        model.addAttribute("game", gameHolderController.getGame());
        return "board";
    }
}
