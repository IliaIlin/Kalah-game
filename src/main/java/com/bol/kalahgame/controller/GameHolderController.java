package com.bol.kalahgame.controller;

import com.bol.kalahgame.model.Game;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

@Component
@SessionScope
public class GameHolderController {

    private Game game;

    public void startGame() {
        game = new Game();
    }

    public Game getGame() {
        return game;
    }
}
