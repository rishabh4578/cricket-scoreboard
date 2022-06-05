package org.assessment;

import jdk.jshell.execution.Util;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Stream;

public class Match {

    private Team team1;
    private Team team2;
    private Map<String, Team> teamInfoMap;
    private int inningInProgress;
    private Team battingTeam;
    private Team bowlingTeam;

    public Match(int oversCount, String team1Name, String[] team1PlayerNames, String team2Name, String[] team2PlayerNames) {
        this.team1 = new Team(team1Name, team1PlayerNames, oversCount);
        this.team2 = new Team(team2Name, team2PlayerNames, oversCount);
        teamInfoMap = new HashMap<>();
        teamInfoMap.put(team1Name, team1);
        teamInfoMap.put(team2Name, team2);
        this.inningInProgress = 1;
        battingTeam = team1;
        bowlingTeam = team2;
    }

    public MatchState registerBall(Ball ball) {
        MatchState matchState = battingTeam.registerBall(ball);
        if (matchState.equals(MatchState.INNING_CONCLUDED)) {
            System.out.println(String.format("Inning concluded with team score: %s/%s", battingTeam.getTeamScore(), battingTeam.getWicketsDown()));
            if (inningInProgress == 1) {
                System.out.println();
                System.out.println(String.format("Batting starts for %s:", team2.getName()));
                team2.setTargetScore(team1.getTeamScore() + 1);
                inningInProgress = 2;
                battingTeam = team2;
                bowlingTeam = team1;
                return MatchState.INNING_CONCLUDED;
            } else {
                //conclude match
                printResultBoard();
                return MatchState.MATCH_CONCLUDED;
            }
        }
        return matchState;
    }

    public void printResultBoard() {
        String newLine = "\n";
        String tabSpace = "\t";
        StringBuilder sb = new StringBuilder("------------");
        int totalTabsReq = Utility.getTotalTabsReqd(Stream.of("Team", team1.getName(), team2.getName()));
        sb.append(newLine)
                .append("RESULT BOARD").append(newLine)
                .append("------------").append(newLine)
                .append(Utility.appendTabs("Team", totalTabsReq))
                .append("Score").append(newLine)
                .append(Utility.appendTabs(team1.getName(), totalTabsReq))
                .append(team1.getTeamScore()).append(newLine)
                .append(Utility.appendTabs(team2.getName(), totalTabsReq))
                .append(team2.getTeamScore()).append(newLine);
        String result;
        if (team1.getTeamScore() > team2.getTeamScore()) {
            result = String.format("%s won by %s runs!", team1.getName(), team1.getTeamScore() - team2.getTeamScore());
        } else if (team2.getTeamScore() > team1.getTeamScore()) {
            result = String.format("%s won by %s wickets!", team2.getName(), team2.getPlayersCount() - 1 - team2.getWicketsDown());
        } else {
            result = "Its a tie!";
        }
        sb.append(result);

        System.out.println(sb);
    }

    public void printTeamScoreBoard(String teamName) {
        if (teamInfoMap.containsKey(teamName))
            teamInfoMap.get(teamName).printScoreBoard();
        else System.out.println(String.format("No records found by team name '%s'", teamName));
    }

    public void printPlayerSummary(String teamName, String playerName) {
        if (teamInfoMap.containsKey(teamName))
            teamInfoMap.get(teamName).printPlayerSummary(playerName);
        else System.out.println(String.format("No records found by team name '%s'", teamName));
    }

    public Team getBowlingTeam() {
        return bowlingTeam;
    }

    public boolean setBowler(String playerName) {
        Player player = bowlingTeam.getPlayerByName(playerName);
        if (player != null) {
            if (battingTeam.getBowler() != null && battingTeam.getBowler().getName().equals(playerName))//can't have same bowler again
                return false;
            battingTeam.setBowler(player);
            return true;
        }
        return false;
    }
}
