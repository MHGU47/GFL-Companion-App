package com.shikikan.gflcompanionapp;

public class Passive {

    private int timeLeft, startTime;

    private int[] intervals;

    private boolean intervalCheck;

    Passive(int timeLeft, int startTime, boolean interval, int[] intervals){
        this.timeLeft = timeLeft;
        this.startTime = startTime;
        this.intervalCheck = interval;
        this.intervals = intervals;
    }

    public void setTimeLeft(int timeLeft) {
        this.timeLeft = timeLeft;
    }

    public int getTimeLeft() {
        return timeLeft;
    }

    public int getStartTime() {
        return startTime;
    }

    public boolean isInterval(){
        return intervalCheck;
    }

    public int getAmountOfIntervals(){
        return intervals.length;
    }

    public int getInterval(int level){
        return intervals[level - 1];
    }
}
