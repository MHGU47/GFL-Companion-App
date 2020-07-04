package com.shikikan.gflcompanionapp;

public class Buff {

    private int timeLeft;

    Buff(int timeLeft){
        this.timeLeft = timeLeft;
    }

    public void setTimeLeft(int timeLeft){
        this.timeLeft = timeLeft;
    }
    public int getTimeLeft() {
        return timeLeft;
    }
}
