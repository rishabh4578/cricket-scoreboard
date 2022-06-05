package org.assessment;

import java.util.Arrays;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Driver {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Match match = setupMatch(sc);
        System.out.println("Type 'exit' and press enter to end the match at any time.");
        System.out.println("Type ball type and press enter. Supported ball types: " + Ball.getSupportedBallTypes());
        while (true) {
            String ip = sc.nextLine();
            if ("exit".equals(ip)) break;
            Ball ball = Ball.getByDisplayCode(ip);
            if (null == ball) {
                System.out.println("Ball type not recognized. Please try again.");
                continue;
            }
            match.registerBall(ball);
        }
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
                assert ip != null && !ip.isBlank();
                if (takenName != null)
                    assert !ip.equals(takenName) : String.format("Team name %s is already used", takenName);
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
                assert Arrays.stream(teamPlayerNames).filter(name -> !name.trim().isEmpty()).distinct().count() == teamSize :
                        String.format("Enter %s distinct non blank player names", teamSize);
                if (existingTeamPlayerNames != null) {
                    String commonNames = Arrays.stream(teamPlayerNames).filter(name -> Arrays.stream(existingTeamPlayerNames).toList().contains(name)).collect(Collectors.joining(", "));
                    assert commonNames.isEmpty() : String.format("Player names [%s] are already taken", commonNames);
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
                if (minValue != null)
                    assert integerInput >= minValue : String.format("A minimum value of %s is required", minValue);
                return integerInput;
            } catch (Throwable e) {
                System.out.println(String.format("%s: %s. Please try again with a valid input.", e.getClass().getSimpleName(), e.getLocalizedMessage()));
            }
        }
    }

}
