package com.shikikan.gflcompanionapp;

public class Buff {

    private float timeLeft, level, duration, attacksLeft, stacks, stacksToAdd, maxStacks, hitCount;
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

    public void setTimeLeft(float timeLeft){
        this.timeLeft = timeLeft;
    }
    public float getTimeLeft() {
        return timeLeft;
    }

    Passive getPassiveBuff(){
        return passive;
    }

    public Effect getEffect() {
        return effect;
    }

    public boolean isStatBuff(){
        return effect.isStatBuff();
    }

    public boolean isStackable() {
        return effect.isStackable();
    }

    public int getBuffCounter(){
        return effect.buffCounter;
    }
}
