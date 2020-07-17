package com.shikikan.gflcompanionapp;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BattleStats {
    private int hp, fp, acc, eva, rof, crit, critDmg, ap, rounds, currentRounds, armour, nightView, targets;

    private float CD;
    float[] maxStats, minStats, skillBonus;

    private List<Float> preBattleStats, BattleStats_;
    private Map <String, Float> preBattleStats_, BattleStats__;

    private Timer normalAttackTimer, skillTimer;
    private Timer[] timers;

    private Shots shots;

    private Utils u;

    private Doll doll;

    private Skill skill_1, skill_2;

    private Effect[] skill_1Effects, skill_2Effects;

    private Passive[] passives;

    private Buff[] buffs;

    int reserveAmmo;

    boolean reserveAmmoMode;

    public BattleStats(Doll doll, List<Integer> stats) {
        this.doll = doll;
        u = new Utils();
        BattleStats__ = new HashMap<>();
        preBattleStats_ = new HashMap<>();

        hp = stats.get(0);
        fp = stats.get(1);
        acc = stats.get(2);
        eva = stats.get(3);
        rof = stats.get(4);
        critDmg = stats.get(5);
        crit = stats.get(6);
        rounds = stats.get(7);
        currentRounds = stats.get(7);
        armour = stats.get(8);
        ap = stats.get(9);
        nightView = stats.get(10);

        targets = doll.getTargets();

        doll.setCooldown(doll.getTileBuff("cd"));//Automatically alters the CD for use in calculations
        CD = doll.getCooldown(); //Altered CD, not raw CD

        //for(int i = 0; i < stats.size(); i++) preBattleStats.add((float)stats.get(i));
        preBattleStats_.put("HP", (float) stats.get(0));
        preBattleStats_.put("FP", (float) stats.get(1));
        preBattleStats_.put("Acc", (float) stats.get(2));
        preBattleStats_.put("Eva", (float) stats.get(3));
        preBattleStats_.put("Rof", (float) stats.get(4));
        preBattleStats_.put("CritDmg", (float) stats.get(5));
        preBattleStats_.put("Crit", (float) stats.get(6));
        preBattleStats_.put("Rounds", (float) stats.get(7));
        preBattleStats_.put("CurrentRounds", (float) stats.get(7));
        preBattleStats_.put("Armour", (float) stats.get(8));
        preBattleStats_.put("AP", (float) stats.get(9));
        preBattleStats_.put("NightView", (float) stats.get(10));




        //preBattleStats = new ArrayList<>();
        //for(int stat : stats) preBattleStats.add((float) stat);
        //preBattleStats.add(CD);

        if(doll.getType() == 6){
            if(doll.hasSlug()){
                doll.setTargets(6);
                //preBattleStats.set(1, preBattleStats.get(1) * 3);
                preBattleStats_.put("FP", preBattleStats_.get("FP") * 3);
            }
            else doll.setTargets(3);
        }
        //preBattleStats.add((float) targets);//TODO: Alter this so targets are only ever saved if the selected T-Doll is an SG

        preBattleStats_.put("CD", doll.getCooldown());
        preBattleStats_.put("Targets", (float) doll.getTargets());

        preBattleStats_.put("FP", Math.max(0, preBattleStats_.get("FP")));//Cap FP
        preBattleStats_.put("Acc", Math.max(1, preBattleStats_.get("Acc")));//Cap Acc
        preBattleStats_.put("Eva", Math.max(0, preBattleStats_.get("Eva")));//Cap Eva
        preBattleStats_.put("CritDmg", Math.max(0, preBattleStats_.get("CritDmg")));//Cap CritDmg
        preBattleStats_.put("AP", Math.max(0, preBattleStats_.get("AP")));//Cap AP
        preBattleStats_.put("Armour", Math.max(0, preBattleStats_.get("Armour")));//Cap Armour

        //preBattleStats.set(1, Math.max(0, preBattleStats.get(1)));//Cap FP
        //preBattleStats.set(2, Math.max(1, preBattleStats.get(2)));//Cap Acc
        //preBattleStats.set(3, Math.max(0, preBattleStats.get(3)));//Cap Eva
        //preBattleStats.set(5, Math.max(0, preBattleStats.get(5)));//Cap CritDmg
        //preBattleStats.set(8, Math.max(0, preBattleStats.get(8)));//Cap AP
        //preBattleStats.set(9, Math.max(0, preBattleStats.get(9)));//Cap Armour

        //if (isNight) {//Calculate Night Battle Accuracy penalty
            //preBattleStats.set(2, (float)Math.floor(preBattleStats.get(2) * (1 - (.9 - .9 * preBattleStats.get(10) / 100))));
            preBattleStats_.put("Acc", (float)Math.floor(preBattleStats_.get("Acc") * (1 - (.9 - .9 * preBattleStats_.get("Acc") / 100))));
        //}

        //preBattleStats.add(u.getCapRof(doll, preBattleStats.get(4)));
        //preBattleStats.add(u.getCapCrit(preBattleStats.get(6)));

        preBattleStats_.put("CapRof", u.getCapRof(doll, preBattleStats_.get("Rof")));
        preBattleStats_.put("CapCrit", u.getCapCrit(preBattleStats_.get("Crit")));
        setBattleStats();
    }



    public float getPreBattleStat(String stat) {
        /*switch(stat){
            case "hp": return preBattleStats.get(0);
            case "fp": return preBattleStats.get(1);
            case "acc": return preBattleStats.get(2);
            case "eva": return preBattleStats.get(3);
            case "rof": return preBattleStats.get(4);
            case "critDmg": return preBattleStats.get(5);
            case "crit": return preBattleStats.get(6);
            case "rounds":
            case "currentRounds": return preBattleStats.get(7);
            case "armour": return preBattleStats.get(8);
            case "ap": return preBattleStats.get(9);
            case "nightView": return preBattleStats.get(10);
            case "CD": return preBattleStats.get(11);
            case "targets": return preBattleStats.get(12);
            case "cappedRof": return preBattleStats.get(13);
            case "cappedCrit": return preBattleStats.get(14);
            default: return 0;
        }*/
        return preBattleStats_.get(stat);
    }
    public List<Float> getPreBattleStats(){
        return preBattleStats;
    }

    public void setBattleStats() {
        /*
         * Order of 'BattleStats_'
         * -HP
         * -FP
         * -Acc
         * -Eva
         * -Rof
         * -CritDmg
         * -Crit
         * -Rounds
         * -CurrentRounds
         * -Armour
         * -AP
         * -NightView
         * -CD
         * -Targets (if SG)
         * -FramesPerAttack (if MG(?))
         * -BusyLinks
         * -SkillDamage
         */
        shots = new Shots();


//        BattleStats_ = new ArrayList<>(preBattleStats);
//        if (doll.getType() != 6) BattleStats_.remove(13);
//        BattleStats_.remove(14);
//        BattleStats_.remove(15);
//        if (doll.getFramesPerAttack() != 0) BattleStats_.add((float) doll.getFramesPerAttack());
//        BattleStats_.add(0f);//SKill Damage


        //Hash Map Method
        BattleStats__ = new HashMap<>();
        BattleStats__.putAll(preBattleStats_);
        BattleStats__.remove("HP");
        if (doll.getType() != 6) BattleStats__.remove("Targets");
        if (doll.getFramesPerAttack() != 0) BattleStats__.put("FramesPerAttack", (float) doll.getFramesPerAttack());
        BattleStats__.put("Skill Damage", 0f);

        skill_1 = new Skill(doll.getSkill_1());
        skill_2 = new Skill(doll.getSkill_2());
        skill_1Effects = new Effect[skill_1.getEffects().length];
        skill_2Effects = new Effect[skill_2.getEffects().length];
        for(int i = 0; i < skill_1.getEffects().length; i++) skill_1Effects[i] = new Effect(skill_1.getEffects()[i]);
        for(int i = 0; i < skill_2.getEffects().length; i++) skill_2Effects[i] = new Effect(skill_2.getEffects()[i]);


        skillBonus = new float[]{
                1,//FP
                1,//Acc
                1,//Eva
                1,//Rof
                1,//CritDmg
                1,//Crit
                0,//Rounds
                1,//Armour
                1,//AP
                1,//Skill CD
        };

        maxStats = new float[]{
                //preBattleStats.get(1),//FP
                //preBattleStats.get(2),//Acc
                //preBattleStats.get(3),//Eva
                //u.getCapRof(doll, preBattleStats.get(4)),//Capped Rof
                //preBattleStats.get(4),//Uncapped Rof
                //preBattleStats.get(5),//CritDmg
                //u.getCapCrit(preBattleStats.get(6)),//Capped Crit
                //preBattleStats.get(6),//Uncapped Crit
                //preBattleStats.get(7),//Rounds
                //preBattleStats.get(8),//Armour
                //preBattleStats.get(9)//AP

                preBattleStats_.get("FP"),//FP
                preBattleStats_.get("Acc"),//Acc
                preBattleStats_.get("Eva"),//Eva
                u.getCapRof(doll, preBattleStats_.get("CapRof")),//Capped Rof
                preBattleStats_.get("Rof"),//Uncapped Rof
                preBattleStats_.get("CritDmg"),//CritDmg
                u.getCapCrit(preBattleStats_.get("CapCrit")),//Capped Crit
                preBattleStats_.get("Crit"),//Uncapped Crit
                preBattleStats_.get("Rounds"),//Rounds
                preBattleStats_.get("Armour"),//Armour
                preBattleStats_.get("AP")//AP
        };

        minStats = new float[]{
                //preBattleStats.get(1),//FP
                //preBattleStats.get(2),//Acc
                //preBattleStats.get(3),//Eva
                //u.getCapRof(doll, preBattleStats.get(4)),//Capped Rof
                //preBattleStats.get(4),//Uncapped Rof
                //preBattleStats.get(5),//CritDmg
                //u.getCapCrit(preBattleStats.get(6)),//Capped Crit
                //preBattleStats.get(6),//Uncapped Crit
                //preBattleStats.get(7),//Rounds
                //preBattleStats.get(8),//Armour
                //preBattleStats.get(9)//AP

                preBattleStats_.get("FP"),//FP
                preBattleStats_.get("Acc"),//Acc
                preBattleStats_.get("Eva"),//Eva
                u.getCapRof(doll, preBattleStats_.get("CapRof")),//Capped Rof
                preBattleStats_.get("Rof"),//Uncapped Rof
                preBattleStats_.get("CritDmg"),//CritDmg
                u.getCapCrit(preBattleStats_.get("CapCrit")),//Capped Crit
                preBattleStats_.get("Crit"),//Uncapped Crit
                preBattleStats_.get("Rounds"),//Rounds
                preBattleStats_.get("Armour"),//Armour
                preBattleStats_.get("AP")//AP
        };

        if(doll.getPassives() != null){
            for(int i = 0; i < doll.getPassives().length; i++) passives[i] = new Passive(doll.getPassives()[i]);
            for(Passive passive : passives){
                if(passive.getInterval() != null) passive.startTime = 1;
                passive.level = passive.isSkill2Passive() ? doll.getSkill_2Level() : doll.getLevel();
                passive.setEffects(u.getUsableSkillEffects(passive.getEffects()));
                for(Effect effect : passive.getEffects()){
                    effect.level = passive.level;
                }
            }
        }
        else{
            passives = new Passive[]{new Passive()};
        }

        BattleStats__.put("NumOfAttacks" , 1f);

        if (doll.getFramesPerAttack() != 0) normalAttackTimer = new Timer("Normal Attack", doll.getFramesPerAttack());
        else normalAttackTimer = new Timer("Normal Attack", (int) Math.floor(50 * (30f / doll.getRof())));

        timers = new Timer[3];
        timers[0] = normalAttackTimer;//TODO: This is only supposed to be added if the user has set the skill as active

        skillTimer = new Timer("Skill 1", Math.round(skill_1.getInitialCD() * 30 * (1 - preBattleStats_.get("CD") / 100)));

        //TODO: Skill 2 timer set up
        /*if (doll.mod) {
            doll.battle.skill2 = $.extend(true, {}, doll.skill2);
            doll.battle.skill2.effects = getUsableSkillEffects(doll.battle.skill2.effects);
            let skill2Timer = {
                    type: 'skill2',
                    timeLeft: Math.round(doll.battle.skill2.icd * 30 * (1 - doll.pre_battle.skillcd / 100))
      };
            if (doll.useSkill) {
                doll.battle.timers.push(skill2Timer);
            }
        }*/
    }

    public float[] getMaxStats() {
        return maxStats;
    }

    public float[] getMinStats() {
        return minStats;
    }

    public void setBuffs(Buff[] buffs){
        this.buffs = buffs;
    }

    public Skill getSkill_1() {
        return skill_1;
    }

    public Effect[] getSkill_1Effects() {
        return skill_1Effects;
    }

    public void setSkill_1Effects(Effect[] effects) {
        /*Effect[] temp;
        int index = 0;
        for(Effect effect : effects) if(effect != null) index++;

        temp = new Effect[index];
        System.arraycopy(effects, 0, temp, 0, temp.length);
        this.effects = temp;*/
        this.skill_1Effects = effects;
    }

    public Effect[] getSkill_2Effects() {
        return skill_2Effects;
    }

    public void setSkill_2Effects(Effect[] effects) {
        /*Effect[] temp;
        int index = 0;
        for(Effect effect : effects) if(effect != null) index++;

        temp = new Effect[index];
        System.arraycopy(effects, 0, temp, 0, temp.length);
        this.effects = temp;*/
        this.skill_2Effects = effects;
    }

    public void setPassives(Passive[] passives){
        this.passives = passives;
    }
    public Passive[] getPassives() {
        return passives;
    }

    public List<Float> getBattleStats_() {
        return BattleStats_;
    }

    public void setHits(int hits){
        shots.hits = hits;
    }

    public void setMisses(int misses){
        shots.misses = misses;
    }

    public void setTotalShots(int totalShots){
        shots.total = totalShots;
    }

    public void setTargets(int targets) {
        this.targets = targets;
    }
    public int getTargets() {
        return targets;
    }







//    public void setBusyLinks(int busyLinks) {
//        this.busyLinks = busyLinks;
//    }
//    public int getBusyLinks() {
//        return busyLinks;
//    }
//
//    public void setSkillDamage(int skillDamage) {
//        this.skillDamage = skillDamage;
//    }
//    public int getSkillDamage() {
//        return skillDamage;
//    }
//
//    public void setNumOfAttacks(int numOfAttacks) {
//        this.numOfAttacks = numOfAttacks;
//    }
//    public int getNumOfAttacks() {
//        return numOfAttacks;
//    }
//
//    public void setNormalAttackTimer(int timeLeft){
//        normalAttackTimer.setTimeLeft(timeLeft);
//    }
//    public int getNormalAttackTimer(){
//        return normalAttackTimer.getTimeLeft();
//    }
//
//    public void setSkillTimer(int timeLeft){
//        skillTimer.setTimeLeft(timeLeft);
//    }
//    public int getSkillTimer(){
//        return skillTimer.getTimeLeft();
//    }
//
//    public Timer[] getTimers(){
//        return new Timer[] {normalAttackTimer, skillTimer};
//    }
//
//    public Timer getTimer(String type){
//        for(Timer timer : getTimers()){
//            if(timer.getType().contains(type)) return timer;
//        }
//        return new Timer("null", 0);
//    }
//
//    public boolean findTimer(String type){
//        for(Timer timer : getTimers()){
//            if(timer.getType().equals(type)) return true;
//        }
//        return false;
//    }
//
//    public void addToActionQueue(Object object){
//        actionQueue.add(object);
//    }
//
//    public Vector<Object> getActionQueue(){
//        return actionQueue;
//    }
//
//    public void addToEffectQueue(Object object){
//        effectQueue.add(object);
//    }
//
//    public Vector<Object> getEffectQueue(){
//        return effectQueue;
//    }
//
//    public void addBuff(Buff buff){
//        buffs.add(buff);
//    }
//
//    public List<Buff> getBuffs(){
//        return buffs;
//    }
}
