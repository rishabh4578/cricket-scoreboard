package org.assessment;

public class Match {

    private Team team1;
    private Team team2;
    private int inningInProgress;
    private Team playingTeam;

    public Match(int oversCount, String team1Name, String[] team1PlayerNames, String team2Name, String[] team2PlayerNames) {
        this.team1 = new Team(team1Name, team1PlayerNames, oversCount);
        this.team2 = new Team(team2Name, team2PlayerNames, oversCount);
        this.inningInProgress = 1;
        this.playingTeam = team1;
    }

    public void registerBall(Ball ball) {
        boolean concludeInning = playingTeam.registerBall(ball);
        if (concludeInning) {
            System.out.println(String.format("Inning concluded with team score: %s", playingTeam.getTeamScore()));
            if (inningInProgress == 1) {
                team2.setTargetScore(team1.getTeamScore() + 1);
                inningInProgress = 2;
                playingTeam = team2;
            } else {
                //conclude match
                printResultBoard();
            }
        }
    }

    private void printResultBoard() {
        String newLine = "\n";
        String tabSpace = "\t";
        StringBuilder sb = new StringBuilder("----------");
        sb.append(newLine)
                .append("MATCH OVER").append(newLine)
                .append("----------").append(newLine)
                .append("Team").append(tabSpace)
                .append("Score").append(newLine)
                .append(team1.getName()).append(tabSpace)
                .append(team1.getTeamScore()).append(newLine)
                .append(team2.getName()).append(tabSpace)
                .append(team2.getTeamScore()).append(newLine);
        int scoreDiff = team1.getTeamScore() - team2.getTeamScore();
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
}
