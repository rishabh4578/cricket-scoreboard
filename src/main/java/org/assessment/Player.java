package org.assessment;

public class Player {

    private String name;
    private int ballsPlayed;
    private int score;
    private int foursCount;
    private int sixesCount;
    private PlayerStatus playerStatus;
    private int runsConceded;
    private int ballsBowled;
    private int wicketsTaken;
    private int runsConcededInCurrentOver;
    private int maidenOvers;

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
        this.runsConceded = 0;
        this.ballsBowled = 0;
        this.wicketsTaken = 0;
        this.runsConcededInCurrentOver = 0;
        this.maidenOvers = 0;
    }

    public void registerBall(Ball ball) {
        this.score += ball.getPlayerScoreContribution();
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

    public void printPlayerSummary() {
        String newLine = "\n";
        StringBuilder sb = new StringBuilder();
        sb.append("Player name: ").append(name).append(newLine)
                .append("BATTING SUMMARY:").append(newLine)
                .append("----------------").append(newLine)
                .append("Runs scored: ").append(score).append(newLine)
                .append("Balls played: ").append(ballsPlayed).append(newLine)
                .append("Strike rate: ").append(score / ballsPlayed * 100.0).append(newLine)
                .append("BOWLING SUMMARY:").append(newLine)
                .append("----------------").append(newLine)
                .append("Overs bowled: ").append(Utility.getOversByBalls(ballsBowled)).append(newLine)
                .append("Runs conceded: ").append(runsConceded).append(newLine)
                .append("Wickets: ").append(wicketsTaken).append(newLine)
                .append("Maiden overs: ").append(maidenOvers);
        System.out.println(sb);
    }

    public void registerBowl(Ball ball) {
        runsConceded += ball.getTeamScoreContribution();
        runsConcededInCurrentOver += ball.getTeamScoreContribution();
        if (ball.isContributesToOver()) ballsBowled++;
        if (ball.equals(Ball.WICKET)) wicketsTaken++;
    }

    public void checkAndRegisterMaidenOver() {
        if (runsConcededInCurrentOver == 0)
            maidenOvers++;
        runsConcededInCurrentOver = 0;
    }

}
