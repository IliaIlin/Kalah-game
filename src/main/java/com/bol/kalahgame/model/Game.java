package com.bol.kalahgame.model;

import java.util.Arrays;
import java.util.stream.IntStream;

import static com.bol.kalahgame.model.PitType.*;

public class Game {
    public final Player player1;
    public final Player player2;
    public final Pit[] pits;

    private Player activePlayer;
    private boolean isFinished;

    private static final int DEFAULT_NUMBER_OF_STONES = 6;

    public Game() {
        this(DEFAULT_NUMBER_OF_STONES);
    }

    public Game(int numberOfStones) {
        player1 = new Player("player 1");
        player2 = new Player("player 2");

        activePlayer = player1;

        pits = new Pit[14];
        //anticlockwise starting from large pit of first player
        pits[0] = new Pit(LARGE, player1);
        IntStream.range(1, 7).forEach((i) -> pits[i] = new Pit(SMALL, player2, numberOfStones));
        pits[7] = new Pit(LARGE, player2);
        IntStream.range(8, 14).forEach((i) -> pits[i] = new Pit(SMALL, player1, numberOfStones));
    }

    public Player getActivePlayer() {
        return activePlayer;
    }

    public void setActivePlayer(Player activePlayer) {
        this.activePlayer = activePlayer;
    }

    public boolean isFinished() {
        return isFinished;
    }

    public void finishGame() {
        isFinished = true;
    }

    @Override
    public String toString() {
        return "Game{" +
                "player1=" + player1 +
                ", player2=" + player2 +
                ", pits=" + Arrays.toString(pits) +
                ", activePlayer=" + activePlayer +
                ", isFinished=" + isFinished +
                '}';
    }
}
