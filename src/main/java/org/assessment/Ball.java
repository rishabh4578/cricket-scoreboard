package org.assessment;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public enum Ball {

    SINGLE("1", 1, true, true),
    DOUBLE("2", 2, true, false),
    TRIPLE("3", 3, true, true),
    FOUR("4", 4, true, false),
    SIX("6", 6, true, false),
    WIDE("WD", 1, false, false),
    NO_BALL("NB", 1, false, false),
    WICKET("W", 0, true, false);

    private String displayCode;
    private int scoreContribution;
    private boolean contributesToOver;
    private boolean switchStriker;

    Ball(String displayCode, int scoreContribution, boolean contributesToOver, boolean switchStriker) {
        this.displayCode = displayCode;
        this.scoreContribution = scoreContribution;
        this.contributesToOver = contributesToOver;
        this.switchStriker = switchStriker;
    }

    private static Map<String, Ball> map;

    static {
        map = new HashMap<>();
        for (Ball ball : Ball.values())
            map.put(ball.displayCode, ball);
    }

    public int getScoreContribution() {
        return scoreContribution;
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

    public static String getSupportedBallTypes() {
        return Arrays.stream(Ball.values()).map(ball -> ball.displayCode).collect(Collectors.joining(", "));
    }

}
