package org.assessment;

import java.sql.Struct;
import java.util.Arrays;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Driver {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Match match = setupMatch(sc);
        System.out.println("Type 'exit' and press enter to end the match at any time.");
        MatchState matchState = MatchState.OVER_CONCLUDED;
        while (!matchState.equals(MatchState.MATCH_CONCLUDED)) {
            if (matchState.equals(MatchState.OVER_CONCLUDED) || matchState.equals(MatchState.INNING_CONCLUDED)) {

                if (matchState.equals(MatchState.INNING_CONCLUDED))
                    getAdditionalInfo(match, false, sc);

                setBowler(match, sc);
                matchState = MatchState.OTHER;
                System.out.println("Enter ball type and press enter. Supported ball types: " + Ball.getSupportedBallTypes());
            }

            String ip = sc.nextLine();
            if ("exit".equals(ip)) return;
            Ball ball = Ball.getByDisplayCode(ip);
            if (null == ball) {
                System.out.println("Ball type not recognized. Please try again.");
                continue;
            }
            matchState = match.registerBall(ball);
        }

        getAdditionalInfo(match, true, sc);
    }

    private static void getAdditionalInfo(Match match, boolean matchConcluded, Scanner sc) {
        System.out.println();
        System.out.println("------------------------");
        System.out.println("ADDITIONAL INFO COMMANDS");
        System.out.println("------------------------");
        String newLine = "\n";
        StringBuilder help = new StringBuilder();
        help.append("* Print match result board: matchResultBoard").append(newLine)
                .append("* Print team scoreboard: teamScoreBoard").append(newLine)
                .append("* Print player summary: playerSummary").append(newLine)
                .append("* Help: help").append(newLine);
        if (!matchConcluded)
            help.append("* Continue to next inning: continue").append(newLine);
        help.append("* Exit: exit");
        System.out.println(help);
        while (true) {
            String ip = sc.nextLine();
            if ("exit".equals(ip)) System.exit(0);
            else if (ip.equals("matchResultBoard")) match.printResultBoard();
            else if (ip.equals("teamScoreBoard")) {
                System.out.println(String.format("Enter team name (%s): ", String.join(", ", match.getTeamNames())));
                String teamName = sc.nextLine();
                match.printTeamScoreBoard(teamName);
            } else if (ip.equals("playerSummary")) {
                System.out.println(String.format("Enter team name (%s): ", String.join(", ", match.getTeamNames())));
                String teamName = sc.nextLine();
                System.out.println(String.format("Enter player name (%s): ", String.join(", ", match.getPlayerNamesForTeam(teamName))));
                String playerName = sc.nextLine();
                match.printPlayerSummary(teamName, playerName);
            } else if (ip.equals("help")) {
                System.out.println(help);
            } else if (!matchConcluded && ip.equals("continue")) {
                break;
            } else {
                System.out.println("Command not identified. Please try again.");
            }
        }
    }

    private static void setBowler(Match match, Scanner sc) {
        boolean bowlerSet = false;
        System.out.println(String.format("Select a bowler from %s:", match.getBowlingTeam().getName()));
        while (!bowlerSet) {
            String playerName = sc.nextLine();
            bowlerSet = match.setBowler(playerName);
            if (!bowlerSet)
                System.out.println("Select a valid player from the bowling team who hasn't bowled the last over.");
        }
        System.out.println("Bowler set. Start entering scores.");
    }

    private static Match setupMatch(Scanner sc) {
        System.out.println("-----------");
        System.out.println("MATCH SETUP");
        System.out.println("-----------");
        int oversCount = getIntegerInput("No. of overs: ", null, sc);
        int teamSize = getIntegerInput("Team size: ", 2, sc);
        String team1Name = getTeamName("Team 1 name: ", null, sc);
        String[] team1PlayerNames = getStringArrayInput(String.format("Comma separated player names of %s: ", team1Name), teamSize, null, sc);
        String team2Name = getTeamName("Team 2 name: ", team1Name, sc);
        String[] team2PlayerNames = getStringArrayInput(String.format("Comma separated player names of %s: ", team2Name), teamSize, team1PlayerNames, sc);
        Match match = new Match(oversCount, team1Name, team1PlayerNames, team2Name, team2PlayerNames);
        return match;
    }


    private static String getTeamName(String message, String takenName, Scanner sc) {
        while (true) {
            try {
                System.out.print(message);
                String ip = sc.nextLine();
                if (ip.isEmpty())
                    throw new Exception("Enter a non blank team name");
                if (takenName != null && ip.equals(takenName))
                    throw new Exception(String.format("Team name %s is already used", takenName));
                return ip;
            } catch (Throwable e) {
                System.out.println(String.format("%s: %s. Please try again with a valid input.", e.getClass().getSimpleName(), e.getLocalizedMessage()));
            }
        }
    }

    private static String[] getStringArrayInput(String message, int teamSize, String[] existingTeamPlayerNames, Scanner sc) {
        while (true) {
            try {
                System.out.print(message);
                String[] teamPlayerNames = sc.nextLine().split(",");
                if (Arrays.stream(teamPlayerNames).filter(name -> !name.trim().isEmpty()).distinct().count() != teamSize)
                    throw new Exception(String.format("Enter %s distinct non blank player names", teamSize));
                if (existingTeamPlayerNames != null) {
                    String commonNames = Arrays.stream(teamPlayerNames).filter(name -> Arrays.stream(existingTeamPlayerNames).toList().contains(name)).collect(Collectors.joining(", "));
                    if (!commonNames.isEmpty())
                        throw new Exception(String.format("Player names [%s] are already taken", commonNames));
                }
                return teamPlayerNames;
            } catch (Throwable e) {
                System.out.println(String.format("%s: %s. Please try again with a valid input.", e.getClass().getSimpleName(), e.getLocalizedMessage()));
            }
        }
    }

    private static int getIntegerInput(String message, Integer minValue, Scanner sc) {
        while (true) {
            try {
                System.out.print(message);
                int integerInput = Integer.parseInt(sc.nextLine());
                if (minValue != null && integerInput < minValue)
                    throw new Exception(String.format("A minimum value of %s is required", minValue));
                return integerInput;
            } catch (Throwable e) {
                System.out.println(String.format("%s: %s. Please try again with a valid input.", e.getClass().getSimpleName(), e.getLocalizedMessage()));
            }
        }
    }

}
