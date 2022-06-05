package org.assessment;

import java.util.Arrays;
import java.util.Comparator;
import java.util.stream.Stream;

public class Utility {

    public static String getOversByBalls(int balls) {
        return String.format("%s%s", balls / 6, (balls % 6 != 0 ? "." + balls % 6 : ""));
    }

    public static int getTotalTabsReqd(Stream<String> stream) {
        int maxLen = stream.map(item -> item.length()).max(Comparator.naturalOrder()).get();
        return (maxLen / 4) + 1;
    }

    public static String appendTabs(String s, int totalTabsReq) {
        String[] arr = new String[totalTabsReq - (s.length() / 4)];
        Arrays.fill(arr, "\t");
        String tabsStr = String.join("", arr);
        return String.format("%s%s", s, tabsStr);
    }
}
