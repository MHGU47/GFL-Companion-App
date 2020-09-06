package com.shikikan.gflcompanionapp;

import java.util.List;

public class Queue {
    private Effect queueObject_Effect;
    private Passive queueObject_Passive;
    private Buff queueObject_Buff;
    private Timer queueObject_Timer;

    Queue(Effect effect){
        queueObject_Effect = effect;
    }

    Queue(Passive passive){
        queueObject_Passive = passive;
    }

    Queue(Buff buff){
        queueObject_Buff = buff;
    }

    Queue(Timer timer){
        queueObject_Timer = timer;
    }

    public Buff getQueueObject_Buff() {
        return queueObject_Buff;
    }

    public Effect getQueueObject_Effect() {
        return queueObject_Effect;
    }

    public Passive getQueueObject_Passive() {
        return queueObject_Passive;
    }

    public Timer getQueueObject_Timer() {
        return queueObject_Timer;
    }


}
