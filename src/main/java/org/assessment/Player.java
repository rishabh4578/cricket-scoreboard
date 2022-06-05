package org.assessment;

import java.util.ArrayList;
import java.util.List;

public class Player {

    private String name;
    private int ballsPlayed;
    private int score;
    private int foursCount;
    private int sixesCount;
    private PlayerStatus playerStatus;

    public Player(String name) {
        this(name, PlayerStatus.WAITING);
    }

    public Player(String name, PlayerStatus playerStatus) {
        this.name = name;
        this.ballsPlayed = 0;
        this.score = 0;
        this.foursCount = 0;
        this.sixesCount = 0;
        this.playerStatus = playerStatus;
    }

    public void registerBall(Ball ball) {
        this.score += ball.getScoreContribution();
        if (Ball.SIX.equals(ball)) sixesCount++;
        else if (Ball.FOUR.equals(ball)) foursCount++;
        if (ball.isContributesToOver())
            this.ballsPlayed++;
    }

    public String getName() {
        return name;
    }

    public int getBallsPlayed() {
        return ballsPlayed;
    }

    public int getScore() {
        return score;
    }

    public void setPlayerStatus(PlayerStatus playerStatus) {
        this.playerStatus = playerStatus;
    }

    public PlayerStatus getPlayerStatus() {
        return playerStatus;
    }

    public int getFoursCount() {
        return foursCount;
    }

    public int getSixesCount() {
        return sixesCount;
    }
}
