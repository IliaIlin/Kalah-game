package com.bol.kalahgame.service;

import com.bol.kalahgame.model.Game;
import com.bol.kalahgame.model.Pit;
import com.bol.kalahgame.model.Player;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.bol.kalahgame.model.PitType.*;

@Service
public class GameService {

    public void processNextTurn(final Game game, final int indexOfChosenPit) {
        int indexOfLastChangedPit = distributeStones(game, indexOfChosenPit);
        if (isCaptureCase(game, indexOfLastChangedPit)) {
            performCapture(game, indexOfLastChangedPit);
        }
        if (!activePlayerHasOneMoreTurn(game, indexOfLastChangedPit)) {
            updateActivePlayer(game);
        }
        if (isGameFinished(game)) {
            finishGame(game);
        }
    }

    private int distributeStones(final Game game, final int indexOfChosenPit) {
        int stonesToDistribute = emptyChosenPit(game, indexOfChosenPit);
        Pit[] pits = game.pits;
        int indexOfNextPit = indexOfChosenPit + 1;
        int stonesDistributed = 0;
        while (stonesToDistribute != stonesDistributed) {
            if (indexOfNextPit > 13) {
                indexOfNextPit = 0;
            }
            boolean stoneDistributed = distributeStonesToSpecificPit(pits[indexOfNextPit], game.getActivePlayer());
            if (stoneDistributed) {
                stonesDistributed++;
            }
            indexOfNextPit++;
        }
        return --indexOfNextPit;
    }

    private int emptyChosenPit(final Game game, final int indexOfChosenPit) {
        Pit chosenPit = game.pits[indexOfChosenPit];
        return chosenPit.getType() == SMALL ? chosenPit.emptyPit() : 0;
    }

    private boolean distributeStonesToSpecificPit(final Pit pit, final Player activePlayer) {
        boolean stoneDistributed = false;
        if (pit.getType() == SMALL) {
            pit.addStones(1);
            stoneDistributed = true;
        } else if (pit.getBelongsToPlayer().equals(activePlayer)) {
            pit.addStones(1);
            pit.getBelongsToPlayer().addScore(1);
            stoneDistributed = true;
        }
        return stoneDistributed;
    }

    private boolean isCaptureCase(final Game game, final int indexOfLastChangedPit) {
        Pit[] pits = game.pits;
        Pit lastChangedPit = pits[indexOfLastChangedPit];
        int oppositePit = pits.length - indexOfLastChangedPit;
        return lastChangedPit.getType() == SMALL
                && lastChangedPit.getBelongsToPlayer().equals(game.getActivePlayer())
                && lastChangedPit.getStonesCount() == 1
                && pits[oppositePit].getStonesCount() != 0;
    }

    private void performCapture(final Game game, final int indexOfLastChangedPit) {
        Pit[] pits = game.pits;
        Pit lastChangedPit = pits[indexOfLastChangedPit];

        lastChangedPit.emptyPit();
        int indexOfTheOppositePit = pits.length - indexOfLastChangedPit;
        int stonesPickedFromTheOppositePit = pits[indexOfTheOppositePit].emptyPit();
        Player activePlayer = game.getActivePlayer();
        Optional<Pit> largePitCorrespondingToActivePlayer = Stream.of(pits)
                .filter(pit -> pit.getType() == LARGE && pit.getBelongsToPlayer().equals(activePlayer))
                .findFirst();

        largePitCorrespondingToActivePlayer.orElseThrow()
                .addStones(1 + stonesPickedFromTheOppositePit);
        activePlayer.addScore(1 + stonesPickedFromTheOppositePit);
    }

    private boolean activePlayerHasOneMoreTurn(final Game game,
                                               final int indexOfLastChangedPit) {
        Pit lastChangedPit = game.pits[indexOfLastChangedPit];
        return lastChangedPit.getType() == LARGE
                && lastChangedPit.getBelongsToPlayer().equals(game.getActivePlayer());
    }

    private void updateActivePlayer(final Game game) {
        Player newActivePlayer = game.getActivePlayer().equals(game.player1) ? game.player2 : game.player1;
        game.setActivePlayer(newActivePlayer);
    }

    private boolean isGameFinished(final Game game) {
        return Stream.of(game.pits)
                .filter(pit -> pit.getType() == SMALL)
                .collect(
                        Collectors.groupingBy(Pit::getBelongsToPlayer,
                                Collectors.summingInt(Pit::getStonesCount)))
                .values()
                .stream()
                .anyMatch(stonesByPlayer -> stonesByPlayer == 0);
    }

    private void finishGame(final Game game) {
        updateFinalScores(game);
        game.finishGame();
    }

    private void updateFinalScores(final Game game) {
        Map<Player, Integer> playerToFinalScore = Stream.of(game.pits)
                .collect(
                        Collectors.groupingBy(
                                Pit::getBelongsToPlayer,
                                Collectors.summingInt(Pit::getStonesCount)));
        game.player1.setScore(playerToFinalScore.get(game.player1));
        game.player2.setScore(playerToFinalScore.get(game.player2));
    }
}
