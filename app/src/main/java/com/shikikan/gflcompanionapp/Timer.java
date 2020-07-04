package com.shikikan.gflcompanionapp;

public class Timer {

    private int timeLeft;

    private String type;

    Timer(String type, int timeLeft){
        this.type = type;
        this.timeLeft = timeLeft;
    }


    public void setTimeLeft(int timeLeft) {
        this.timeLeft = timeLeft;
    }
    public int getTimeLeft() {
        return timeLeft;
    }

    public void setType(String type) {
        this.type = type;
    }
    public String getType() {
        return type;
    }
}
