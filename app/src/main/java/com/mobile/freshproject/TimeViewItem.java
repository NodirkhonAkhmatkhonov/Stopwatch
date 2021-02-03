package com.mobile.freshproject;

public class TimeViewItem {

    private int order;
    private long millis;
    private long lastTimeMillis;

    public TimeViewItem(int order, long millis, long lastTimeMillis) {
        this.order = order;
        this.millis = millis;
        this.lastTimeMillis = lastTimeMillis;
    }

    public String getOrder() {
        return String.valueOf(setTimeFormat(order));
    }

    public String getDifference() {
            return "+ "+ getReadyTime(millis - lastTimeMillis);
    }

    public String getCurrentTime() {
        return getReadyTime(millis);
    }

    private String getReadyTime(long millis) {
        long seconds = millis/1000;

        if (millis < 3600000)
            return String.format("%s:%s.%s",
                    setTimeFormat((seconds / 60) % 60),
                    setTimeFormat(seconds % 60),
                    setTimeFormat((millis / 10) % 100));
        else
        return String.format("%s:%s:%s.%s",
                setTimeFormat((seconds / 60) / 60),
                setTimeFormat((seconds / 60) % 60),
                setTimeFormat(seconds % 60),
                setTimeFormat((millis / 10) % 100));
    }

    // returns String in the form like 01 or 11
    private String setTimeFormat(long time) {
        return time < 10 ? String.format("0%d", time) : (String.valueOf(time));
    }

}