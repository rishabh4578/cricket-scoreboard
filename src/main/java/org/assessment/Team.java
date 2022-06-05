package org.assessment;

import jdk.jshell.execution.Util;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Team {

    private String name;
    private Player[] players;
    private Map<String, Player> playerInfoMap;
    private int strikerIdx;
    private int nonStrikerIdx;
    private int ballsCount;
    private int teamScore;
    private int oversCount;
    private int targetScore;
    private int extrasCount;
    private Player bowler;

    public Team(String name, String[] playerNames, int oversCount) {
        this.name = name;
        players = new Player[playerNames.length];
        playerInfoMap = new HashMap<>();
        for (int i = 0; i < playerNames.length; i++) {
            PlayerStatus playerStatus = (i < 2) ? PlayerStatus.PLAYING : PlayerStatus.WAITING;
            players[i] = new Player(playerNames[i], playerStatus);
            playerInfoMap.put(playerNames[i], players[i]);
        }
        this.oversCount = oversCount;
        teamScore = 0;
        strikerIdx = 0;
        nonStrikerIdx = 1;
        targetScore = -1;
        extrasCount = 0;
    }

    public MatchState registerBall(Ball ball) {
        //team level updates
        teamScore += ball.getTeamScoreContribution();
        if (ball.isContributesToOver()) ballsCount++;
        if (ball.isExtra()) extrasCount++;

        //batsman level updates
        players[strikerIdx].registerBall(ball);
        boolean concludeInning = false;
        if (Ball.WICKET.equals(ball)) {
            players[strikerIdx].setPlayerStatus(PlayerStatus.OUT);
            boolean strikerReset = setNextAvailablePlayerAsStriker();
            if (!strikerReset) {
                concludeInning = true;
            }
        }
        boolean currentOverComplete = ballsCount > 0 && ballsCount % 6 == 0;
        if ((ball.isSwitchStriker() && !currentOverComplete) || (currentOverComplete && !ball.isSwitchStriker()))
            switchStriker();
        if (concludeInning || currentOverComplete || ballsCount / 6 == oversCount || (targetScore != -1 && teamScore >= targetScore)) {
            //current over complete or max overs complete or target met
            printScoreBoard();
            if (ballsCount / 6 == oversCount || (targetScore != -1 && teamScore >= targetScore))
                concludeInning = true;
        }

        //bowler level updates
        bowler.registerBowl(ball);
        if (currentOverComplete)
            bowler.checkAndRegisterMaidenOver();

        //debugging
        if (System.getProperty("enableDebugLogs") != null && System.getProperty("enableDebugLogs").equals("on")) {
            System.out.println(String.format("Batsmen: %s %s(%s), %s %s(%s)", players[strikerIdx].getName(),
                    players[strikerIdx].getScore(), players[strikerIdx].getBallsPlayed(),
                    players[nonStrikerIdx].getName(), players[nonStrikerIdx].getScore(), players[nonStrikerIdx].getBallsPlayed()));
            System.out.println(String.format("Team: %s/%s", teamScore, getWicketsDown()));
        }

        MatchState matchState;
        if (concludeInning) {
            matchState = MatchState.INNING_CONCLUDED;
        } else if (currentOverComplete) {
            matchState = MatchState.OVER_CONCLUDED;
        } else {
            matchState = MatchState.OTHER;
        }
        return matchState;
    }

    private void switchStriker() {
        int temp = strikerIdx;
        strikerIdx = nonStrikerIdx;
        nonStrikerIdx = temp;
    }

    public void printScoreBoard() {
        String newLine = "\n";
        String tabSpace = "\t";
        StringBuilder sb = new StringBuilder(String.format("Scoreboard for team %s:", name));
        int totalTabsReq = Utility.getTotalTabsReqd(Stream.concat(Stream.of("Player Name"), Arrays.stream(players).map(player -> player.getName())));
        sb.append(newLine)
                .append(Utility.appendTabs("Player Name", totalTabsReq))
                .append("Score").append(tabSpace)
                .append("4s").append(tabSpace)
                .append("6s").append(tabSpace)
                .append("Balls").append(newLine);
        for (int i = 0; i < players.length; i++) {
            Player player = players[i];
            String displayName = (i == strikerIdx || i == nonStrikerIdx) ? String.format("%s*", player.getName()) : player.getName();
            sb.append(Utility.appendTabs(displayName, totalTabsReq))
                    .append(player.getScore()).append(tabSpace).append(tabSpace)
                    .append(player.getFoursCount()).append(tabSpace)
                    .append(player.getSixesCount()).append(tabSpace)
                    .append(player.getBallsPlayed()).append(newLine);
        }
        sb.append("--------------------").append(newLine)
                .append("Total: ").append(String.format("%s/%s", teamScore, getWicketsDown()))
                .append(newLine)
                .append("Overs: ").append(Utility.getOversByBalls(ballsCount)).append(newLine)
                .append("Team extras: ").append(extrasCount).append(newLine)
                .append("--------------------");
        System.out.println(sb);
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

    public void printPlayerSummary(String playerName) {
        if (playerInfoMap.containsKey(playerName))
            playerInfoMap.get(playerName).printPlayerSummary();
        else System.out.println(String.format("No records found by player name '%s'", playerName));
    }

    public Player getPlayerByName(String playerName) {
        if (playerInfoMap.containsKey(playerName))
            return playerInfoMap.get(playerName);
        else return null;
    }

    public void setBowler(Player bowler) {
        this.bowler = bowler;
    }

    public Player getBowler() {
        return bowler;
    }

    public String[] getPlayerNames() {
        return Arrays.stream(players).map(player -> player.getName()).toArray(String[]::new);
    }
}
