package com.bol.kalahgame.model;

public class Pit {
    private final PitType type;
    private final Player belongsToPlayer;
    private int stonesCount;

    public Pit(PitType type, Player belongsToPlayer) {
        this.type = type;
        this.belongsToPlayer = belongsToPlayer;
    }

    public Pit(PitType type, Player belongsToPlayer, int initialStonesCount) {
        this.type = type;
        this.belongsToPlayer = belongsToPlayer;
        stonesCount = initialStonesCount;
    }

    public PitType getType() {
        return type;
    }

    public Player getBelongsToPlayer() {
        return belongsToPlayer;
    }

    public int getStonesCount() {
        return stonesCount;
    }

    public int emptyPit() {
        int stonesBefore = stonesCount;
        this.stonesCount = 0;
        return stonesBefore;
    }

    public void addStones(int numberOfStonesToAdd) {
        this.stonesCount += numberOfStonesToAdd;
    }

    @Override
    public String toString() {
        return "Pit{" +
                "type=" + type +
                ", belongsToPlayer=" + belongsToPlayer +
                ", stonesCount=" + stonesCount +
                '}';
    }
}
