package com.shikikan.gflcompanionapp;

public class Buff {

    private int timeLeft, level, duration, attacksLeft, stacks, stacksToAdd, maxStacks, hitCount;
    private int[] setTime;
    private float fp, rof, eva;
    private float[] multiplier;
    private String type, target, name;
    private boolean stackable;

    private Passive passive;
    private Effect effect;

    Buff(int timeLeft){
        this.timeLeft = timeLeft;
    }
    Buff(Passive passive){
        this.passive = passive;
    }
    Buff(Effect effect) {
        this.effect = effect;
    }

    public void setTimeLeft(int timeLeft){
        this.timeLeft = timeLeft;
    }
    public int getTimeLeft() {
        return timeLeft;
    }

    Passive getPassiveBuff(){
        return passive;
    }

    public Effect getEffect() {
        return effect;
    }
}
