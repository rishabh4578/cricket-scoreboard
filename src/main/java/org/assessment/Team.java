package org.assessment;

import java.util.Arrays;
import java.util.Comparator;

public class Team {

    private String name;
    private Player[] players;
    private int strikerIdx;
    private int nonStrikerIdx;
    private int ballsCount;
    private int teamScore;
    private int oversCount;
    private int targetScore;

    public Team(String name, String[] playerNames, int oversCount) {
        this.name = name;
        players = new Player[playerNames.length];
        for (int i = 0; i < playerNames.length; i++) {
            PlayerStatus playerStatus = (i < 2) ? PlayerStatus.PLAYING : PlayerStatus.WAITING;
            players[i] = new Player(playerNames[i], playerStatus);
        }
        this.oversCount = oversCount;
        teamScore = 0;
        strikerIdx = 0;
        nonStrikerIdx = 1;
        targetScore = -1;
    }

    public boolean registerBall(Ball ball) {
        teamScore += ball.getScoreContribution();
        players[strikerIdx].registerBall(ball);
        boolean concludeInning = false;
        if (Ball.WICKET.equals(ball)) {
            players[strikerIdx].setPlayerStatus(PlayerStatus.OUT);
            boolean strikerReset = setNextAvailablePlayerAsStriker();
            if (!strikerReset) {
                concludeInning = true;
            }
        }
        if (ball.isSwitchStriker()) {
            int temp = strikerIdx;
            strikerIdx = nonStrikerIdx;
            nonStrikerIdx = temp;
        }
        if (ball.isContributesToOver()) ballsCount++;
        if (concludeInning || ballsCount % 6 == 0 || ballsCount / 6 == oversCount || (targetScore != -1 && teamScore >= targetScore)) {
            //current over complete or max overs complete or target met
            printScoreBoard();
            if (ballsCount / 6 == oversCount || (targetScore != -1 && teamScore >= targetScore))
                concludeInning = true;
        }
        return concludeInning;
    }

    private void printScoreBoard() {
        String newLine = "\n";
        String tabSpace = "\t";
        StringBuilder sb = new StringBuilder(String.format("Scoreboard for team %s:", name));
        int maxNameLength = Arrays.stream(players).map(player -> player.getName().length()).max(Comparator.naturalOrder()).get();
        maxNameLength = Math.max(maxNameLength, 11); //length of header column
        int totalTabsReq = (maxNameLength / 4) + 1;//2nd element to add extra tab if max name covers all spaces in max tabs
        sb.append(newLine)
                .append("Player Name").append(getTabSpaces(11, totalTabsReq))
                .append("Score").append(tabSpace)
                .append("4s").append(tabSpace)
                .append("6s").append(tabSpace)
                .append("Balls").append(newLine);
        for (int i = 0; i < players.length; i++) {
            Player player = players[i];
            String displayName = (i == strikerIdx || i == nonStrikerIdx) ? String.format("%s*", player.getName()) : player.getName();
            sb.append(displayName).append(getTabSpaces(displayName.length(), totalTabsReq))
                    .append(player.getScore()).append(tabSpace).append(tabSpace)
                    .append(player.getFoursCount()).append(tabSpace)
                    .append(player.getSixesCount()).append(tabSpace)
                    .append(player.getBallsPlayed()).append(newLine);
        }
        sb.append(String.format("Overs completed: %s", ballsCount / 6));
        System.out.println(sb);
    }

    private String getTabSpaces(int len, int totalTabsReq) {
        String[] arr = new String[totalTabsReq - (len / 4)];
        Arrays.fill(arr, "\t");
        return String.join("", arr);
    }

    private boolean setNextAvailablePlayerAsStriker() {
        int idx = strikerIdx + 1;
        while (idx < players.length) {
            if (players[idx].getPlayerStatus().equals(PlayerStatus.WAITING)) {
                strikerIdx = idx;
                players[strikerIdx].setPlayerStatus(PlayerStatus.PLAYING);
                return true;
            }
            idx++;
        }
        return false;
    }

    public int getTeamScore() {
        return teamScore;
    }

    public String getName() {
        return name;
    }

    public void setTargetScore(int targetScore) {
        this.targetScore = targetScore;
    }

    public int getWicketsDown() {
        return Math.max(strikerIdx, nonStrikerIdx) + 1 - 2;
    }

    public int getPlayersCount() {
        return players.length;
    }
}
