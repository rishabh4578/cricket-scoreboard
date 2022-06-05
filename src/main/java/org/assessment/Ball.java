package org.assessment;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public enum Ball {

    DOT_BALL("0", 0, 0, true, false, false),
    SINGLE("1", 1, 1, true, true, false),
    DOUBLE("2", 2, 2, true, false, false),
    TRIPLE("3", 3, 3, true, true, false),
    FOUR("4", 4, 4, true, false, false),
    SIX("6", 6, 6, true, false, false),
    WIDE("WD", 1, 0, false, false, true),
    NO_BALL("NB", 1, 0, false, false, true),
    WICKET("W", 0, 0, true, false, false);

    private String displayCode;
    private int teamScoreContribution;
    private int playerScoreContribution;
    private boolean contributesToOver;
    private boolean switchStriker;
    private boolean isExtra;

    Ball(String displayCode, int teamScoreContribution, int playerScoreContribution, boolean contributesToOver, boolean switchStriker, boolean isExtra) {
        this.displayCode = displayCode;
        this.teamScoreContribution = teamScoreContribution;
        this.playerScoreContribution = playerScoreContribution;
        this.contributesToOver = contributesToOver;
        this.switchStriker = switchStriker;
        this.isExtra = isExtra;
    }

    private static Map<String, Ball> map;

    static {
        map = new HashMap<>();
        for (Ball ball : Ball.values())
            map.put(ball.displayCode, ball);
    }

    public int getTeamScoreContribution() {
        return teamScoreContribution;
    }

    public boolean isContributesToOver() {
        return contributesToOver;
    }

    public String getDisplayCode() {
        return displayCode;
    }

    public boolean isSwitchStriker() {
        return switchStriker;
    }

    public static Ball getByDisplayCode(String displayCode) {
        if (map.containsKey(displayCode.toUpperCase()))
            return map.get(displayCode.toUpperCase());
        return null;
    }

    public int getPlayerScoreContribution() {
        return playerScoreContribution;
    }

    public boolean isExtra() {
        return isExtra;
    }

    public static String getSupportedBallTypes() {
        return Arrays.stream(Ball.values()).map(ball -> ball.displayCode).collect(Collectors.joining(", "));
    }

}
