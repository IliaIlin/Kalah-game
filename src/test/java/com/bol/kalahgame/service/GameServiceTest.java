package com.bol.kalahgame.service;

import com.bol.kalahgame.model.Game;
import com.bol.kalahgame.model.Pit;
import org.junit.jupiter.api.Test;

import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumeFalse;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

public class GameServiceTest {
    private static final GameService gameService = new GameService();

    @Test
    void processNextTurn_enoughStonesForRoundTrip_stonesDistributedCorrectly() {
        Game game = new Game(12);
        game.setActivePlayer(game.player2);

        gameService.processNextTurn(game, 3);

        Pit[] pits = game.pits;
        Stream.of(1, 2, 4, 5, 6, 8, 9, 10, 11, 12, 13)
                .forEach(i -> assertEquals(13, pits[i].getStonesCount()));
        assertEquals(0, pits[0].getStonesCount());
        assertEquals(0, pits[3].getStonesCount());
        assertEquals(1, pits[7].getStonesCount());
    }

    @Test
    void processNextTurn_defaultFirstPerformsTurn_activePlayerIsChanged() {
        Game game = new Game(3);

        assumeTrue(game.getActivePlayer().equals(game.player1));

        gameService.processNextTurn(game, 8);

        assertEquals(game.player2, game.getActivePlayer());
    }

    @Test
    void processNextTurn_defaultFirstPerformsTurn_lastPitFilledIsLargePit_activePlayerNoChange() {
        Game game = new Game(6);

        assumeTrue(game.getActivePlayer().equals(game.player1));

        gameService.processNextTurn(game, 8);

        assertEquals(game.player1, game.getActivePlayer());
    }

    @Test
    void processNextTurn_stonesAddedToLargePit_ScoreOfActivePlayerIncreases() {
        Game game = new Game(6);

        assumeTrue(game.getActivePlayer().equals(game.player1));
        assumeTrue(game.getActivePlayer().getScore()==0);

        gameService.processNextTurn(game, 8);
        assertEquals(1, game.player1.getScore());
    }

    @Test
    void processNextTurn_enoughStonesForReturningToTheChosenPit_captureWasPerformed() {
        Game game = new Game(12);
        game.setActivePlayer(game.player2);
        game.pits[3].addStones(1);

        gameService.processNextTurn(game, 3);

        Pit[] pits = game.pits;
        Stream.of(1, 2, 4, 5, 6, 8, 9, 10, 12, 13)
                .forEach(i -> assertEquals(13, pits[i].getStonesCount()));
        assertEquals(0, pits[0].getStonesCount());
        assertEquals(0, pits[3].getStonesCount());
        assertEquals(15, pits[7].getStonesCount());
        assertEquals(0, pits[11].getStonesCount());

        assertEquals(15, game.player2.getScore());
    }

    @Test
    void processNextTurn_gameFinishes_gameStatusChanges_playersGetsCorrespondingPoints() {
        Game game = new Game(6);
        Pit[] pits = game.pits;
        pits[0].addStones(10);
        pits[7].addStones(5);
        IntStream.range(8, 13).forEach(i -> pits[i].emptyPit());

        assumeFalse(game.isFinished());
        gameService.processNextTurn(game, 13);

        assertTrue(game.isFinished());
        assertEquals(11, game.player1.getScore());
        assertEquals(46, game.player2.getScore());
    }
}
