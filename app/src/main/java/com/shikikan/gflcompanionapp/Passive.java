package com.shikikan.gflcompanionapp;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Passive {

    private String type, target, name, trigger, modifySkill;
    private int busyLinks, attacksLeft, tick, targets, fixedTime, maxStacks, stacks,
            victories, uses, hits, dollID;
    private float delay, radius, aoe_radius, aoe_multiplier;
    private boolean stun, night, boss, requirements, sureCrit, refreshDuration, armoured, ignoreArmour,
            piercing, stackable, canCrit, aoe, aoe_canCrit, aoe_sureCrit, triggerPythonPassive,
            skill2Passive;

    private int[] rounds, hitCount, fixedDamage, armour, vulnerability, stacksToAdd, stackChance,
            skillDamageBonus, ap, butterCream, extraCritDamage, stacksRequired;
    private float[] fp, acc, eva, rof, crit, critDmg, movespeed, duration, interval, multiplier;

    private Effect effect;
    private Effect[] effects;

    float startTime;
    int level, timeleft;

    //stat, after(another skillEffects class)



    //private String skillName_1, skillName_2, iconName_1, iconName_2, tooltipSkill_1, tooltipSkill_2,
    //trigger, modeName;

    Passive(JSONObject rawPassive){
        try {
            JSONArray rawPassiveNames = rawPassive.names();
            if (rawPassiveNames == null) return;
            for (int i = 0; i < rawPassiveNames.length(); i++) {
                switch (rawPassiveNames.getString(i)) {
                    case "type": type = rawPassive.getString("type"); break;
                    case "target": target = rawPassive.getString("target"); break;
                    case "name": name = rawPassive.getString("name"); break;
                    case "trigger": trigger = rawPassive.getString("trigger"); break;
                    case "modifySkill;": modifySkill = rawPassive.getString("modifySkill"); break;

                    case "busyLinks": busyLinks = rawPassive.getInt("busylinks"); break;
                    case "tick": tick = rawPassive.getInt("tick"); break;
                    case "targets": targets = rawPassive.getInt("targets"); break;
                    case "fixedTime": fixedTime = rawPassive.getInt("fixedTime"); break;
                    case "maxStacks": maxStacks = rawPassive.getInt("maxStacks"); break;
                    case "stacks": stacks = rawPassive.getInt("stacks"); break;
                    case "stacksRequired":
                        JSONArray rawStacksRequired = rawPassive.getJSONArray("stacksRequired");
                        stacksRequired = new int[rawStacksRequired.length()];
                        for (int index = 0; index < rawStacksRequired.length(); index++) {
                            stacksRequired[index] = rawStacksRequired.getInt(index);
                        }
                        break;
                    case "attacksLeft": attacksLeft = rawPassive.getInt("attacksLeft"); break;
                    case "victories": victories = rawPassive.getInt("victories"); break;
                    case "hits": hits = rawPassive.getInt("hits"); break;
                    case "uses": uses = rawPassive.getInt("uses"); break;
                    case "dollid": dollID = rawPassive.getInt("dollid"); break;

                    case "delay": delay = (float) rawPassive.getDouble("delay"); break;
                    case "radius": radius = (float) rawPassive.getDouble("radius"); break;
                    case "aoe_radius": aoe_radius = (float) rawPassive.getDouble("aoe_radius"); break;
                    case "aoe_multiplier": aoe_multiplier = (float) rawPassive.getDouble("aoe_multiplier"); break;


                    case "stun": stun = rawPassive.getBoolean("stun"); break;
                    case "sureCrit": sureCrit = rawPassive.getBoolean("sureCrit"); break;
                    case "armored": armoured = rawPassive.getBoolean(("armored")); break;
                    case "ignoreArmor": ignoreArmour = rawPassive.getBoolean("ignoreArmor"); break;
                    case "piercing": piercing = rawPassive.getBoolean("piercing"); break;
                    case "stackable": stackable = rawPassive.getBoolean("stackable"); break;
                    case "refreshDuration": refreshDuration = rawPassive.getBoolean("refreshDuration"); break;
                    case "canCrit": canCrit = rawPassive.getBoolean("canCrit"); break;
                    case "aoe": aoe = rawPassive.getBoolean("aoe"); break;
                    case "aoe_canCrit": aoe_canCrit = rawPassive.getBoolean("aoe_canCrit"); break;
                    case "aoe_sureCrit": aoe_sureCrit = rawPassive.getBoolean("aoe_sureCrit"); break;
                    case "triggerPythonPassive": triggerPythonPassive = rawPassive.getBoolean("triggerPythonPassive"); break;
                    case "skill2passive": skill2Passive = rawPassive.getBoolean("skill2passive"); break;

                    case "stat":
                        JSONObject rawStat = rawPassive.getJSONObject("stat");
                        JSONArray names = rawStat.names();
                        if (names == null) break;
                        for (int index = 0; index < rawStat.length(); index++) {
                            switch (names.getString(index)) {
                                case "fp":
                                    try {
                                        JSONArray fp_ = rawStat.getJSONArray("fp");
                                        fp = new float[fp_.length()];
                                        for (int stat = 0; stat < fp_.length(); stat++) {
                                            fp[stat] = (float) fp_.getDouble(stat);
                                        }
                                    }
                                    catch (Exception e){
                                        fp = new float[]{(float)rawPassive.getDouble("fp")};
                                    }
                                    break;
                                case "acc":
                                    try {
                                        JSONArray acc_ = rawStat.getJSONArray("acc");
                                        acc = new float[acc_.length()];
                                        for (int stat = 0; stat < acc_.length(); stat++) {
                                            acc[stat] = (float) acc_.getDouble(stat);
                                        }
                                    }
                                    catch (Exception e){
                                        acc = new float[]{(float)rawPassive.getDouble("acc")};
                                    }
                                    break;
                                case "eva":
                                    try {
                                        JSONArray eva_ = rawStat.getJSONArray("eva");
                                        eva = new float[eva_.length()];
                                        for (int stat = 0; stat < eva_.length(); stat++) {
                                            eva[stat] = (float) eva_.getDouble(stat);
                                        }
                                    }
                                    catch (Exception e){
                                        acc = new float[]{(float)rawPassive.getDouble("acc")};
                                    }
                                    break;
                                case "rof":
                                    try {
                                        JSONArray rof_ = rawStat.getJSONArray("rof");
                                        rof = new float[rof_.length()];
                                        for (int stat = 0; stat < rof_.length(); stat++) {
                                            rof[stat] = (float) rof_.getDouble(stat);
                                        }
                                    }
                                    catch (Exception e){
                                        rof = new float[]{(float)rawPassive.getDouble("rof")};
                                    }
                                    break;
                                case "crit":
                                    try {
                                        JSONArray crit_ = rawStat.getJSONArray("crit");
                                        crit = new float[crit_.length()];
                                        for (int stat = 0; stat < crit_.length(); stat++) {
                                            crit[stat] = (float) crit_.getDouble(stat);
                                        }
                                    }
                                    catch (Exception e){
                                        crit = new float[]{(float)rawPassive.getDouble("crit")};
                                    }
                                    break;
                                case "critdmg":
                                    try {
                                        JSONArray critdmg_ = rawStat.getJSONArray("critdmg");
                                        critDmg = new float[critdmg_.length()];
                                        for (int stat = 0; stat < critdmg_.length(); stat++) {
                                            critDmg[stat] = (float) critdmg_.getDouble(stat);
                                        }
                                    }
                                    catch (Exception e){
                                        critDmg = new float[]{(float)rawPassive.getDouble("critdmg")};
                                    }
                                    break;
                                case "movespeed":
                                    try {
                                        JSONArray movespeed_ = rawStat.getJSONArray("movespeed");
                                        movespeed = new float[movespeed_.length()];
                                        for (int stat = 0; stat < movespeed_.length(); stat++) {
                                            movespeed[stat] = (float) movespeed_.getDouble(stat);
                                        }
                                    }
                                    catch (Exception e){
                                        movespeed = new float[]{(float)rawPassive.getDouble("movespeed")};
                                    }
                                    break;
                                case "ap":
                                    /*JSONArray ap_ = rawStat.getJSONArray("ap");
                                    ap = new int[ap_.length()];
                                    for (int stat = 0; stat < ap_.length(); stat++) {
                                        ap[stat] = ap_.getInt(stat);
                                    }*/

                                    ap = new int[]{rawPassive.getInt("ap")};
                                    break;
                                case "armor":
                                    try {
                                        JSONArray armour_ = rawStat.getJSONArray("armor");
                                        armour = new int[armour_.length()];
                                        for (int stat = 0; stat < armour_.length(); stat++) {
                                            armour[stat] = armour_.getInt(stat);
                                        }
                                    }
                                    catch (Exception e){
                                        armour = new int[]{rawPassive.getInt("armor")};
                                    }
                                    break;
                            }
                        }
                        break;
                    case "effects":
                        JSONArray rawEffects = rawPassive.getJSONArray("effects");
                        effects = new Effect[rawEffects.length()];
                        for(int ii = 0; ii < rawEffects.length(); ii++){
                            effect = new Effect(rawEffects.getJSONObject(ii));
                            effects[ii] = effect;
                        }
                        break;
//                    case "after":
//                        JSONObject rawAfter = rawPassive.getJSONObject("after");
//                        afterEffect = new Passive(rawAfter);
//                        break;
                    case "buttercream":
                        JSONArray rawButterCream = rawPassive.getJSONArray("buttercream");
                        butterCream = new int[rawButterCream.length()];
                        for (int index = 0; index < rawButterCream.length(); index++) {
                            butterCream[index] = rawButterCream.getInt(index);
                        }
                        break;
                    case "skillDamageBonus":
                        JSONArray rawSkillDamageBonus = rawPassive.getJSONArray("skillDamageBonus");
                        skillDamageBonus = new int[rawSkillDamageBonus.length()];
                        for (int index = 0; index < rawSkillDamageBonus.length(); index++) {
                            skillDamageBonus[index] = rawSkillDamageBonus.getInt(index);
                        }
                        break;
                    case "extraCritDamage":
                        try {
                            JSONArray rawExtraCritDamage = rawPassive.getJSONArray("extraCritDamage");
                            extraCritDamage = new int[rawExtraCritDamage.length()];
                            for (int index = 0; index < rawExtraCritDamage.length(); index++) {
                                extraCritDamage[index] = rawExtraCritDamage.getInt(index);
                            }
                        }
                        catch (Exception e){
                            extraCritDamage = new int[]{rawPassive.getInt("extraCritDamage")};
                        }
                        break;
                    case "vulnerability":
                        try {
                            JSONArray rawVulnerability = rawPassive.getJSONArray("vulnerability");
                            vulnerability = new int[rawVulnerability.length()];
                            for (int index = 0; index < rawVulnerability.length(); index++) {
                                vulnerability[index] = rawVulnerability.getInt(index);
                            }
                        }
                        catch (Exception e){
                            vulnerability = new int[]{rawPassive.getInt("vulnerability")};
                        }
                        break;
                    case "hitCount":
                        JSONArray rawHitCount = rawPassive.getJSONArray("hitCount");
                        hitCount = new int[rawHitCount.length()];
                        for (int index = 0; index < rawHitCount.length(); index++) {
                            hitCount[index] = rawHitCount.getInt(index);
                        }
                        break;
                    case "fixedDamage":
                        JSONArray rawFixedDamage = rawPassive.getJSONArray("fixedDamage");
                        fixedDamage = new int[rawFixedDamage.length()];
                        for (int index = 0; index < rawFixedDamage.length(); index++) {
                            fixedDamage[index] = rawFixedDamage.getInt(index);
                        }
                        break;
                    case "rounds":
                        JSONArray rawRounds = rawPassive.getJSONArray("rounds");
                        rounds = new int[rawRounds.length()];
                        for (int index = 0; index < rawRounds.length(); index++) {
                            rounds[index] = rawRounds.getInt(index);
                        }
                        break;
                    case "interval":
                        try {
                            JSONArray rawInterval = rawPassive.getJSONArray("interval");
                            interval = new float[rawInterval.length()];
                            for (int index = 0; index < rawInterval.length(); index++) {
                                interval[index] = rawInterval.getInt(index);
                            }
                        }
                        catch (Exception e){
                            interval = new float[]{rawPassive.getInt("interval")};
                        }
                        break;
                    case "multiplier":
                        try {
                            JSONArray rawMultiplier = rawPassive.getJSONArray("multiplier");
                            multiplier = new float[rawMultiplier.length()];
                            for (int index = 0; index < rawMultiplier.length(); index++) {
                                multiplier[index] = (float) rawMultiplier.getDouble(index);
                            }
                        }
                        catch (Exception e){
                            multiplier = new float[]{rawPassive.getInt("multiplier")};
                        }
                        break;
                    case "stacksToAdd":
                        try {
                            JSONArray rawStacksToAdd = rawPassive.getJSONArray("stacksToAdd");
                            stacksToAdd = new int[rawStacksToAdd.length()];
                            for (int index = 0; index < rawStacksToAdd.length(); index++) {
                                stacksToAdd[index] = rawStacksToAdd.getInt(index);
                            }
                        }
                        catch (Exception e){
                            stacksToAdd = new int[]{rawPassive.getInt("stacksToAdd")};
                        }
                        break;
                    case "stackChance":
                        JSONArray rawStackChance = rawPassive.getJSONArray("stackChance");
                        stackChance = new int[rawStackChance.length()];
                        for (int index = 0; index < rawStackChance.length(); index++) {
                            stackChance[index] = rawStackChance.getInt(index);
                        }
                        break;
                    case "duration":
                        try {
                            JSONArray rawDuration = rawPassive.getJSONArray("duration");
                            duration = new float[rawDuration.length()];
                            for (int index = 0; index < rawDuration.length(); index++) {
                                duration[index] = (float) rawDuration.getDouble(index);
                            }
                        }
                        catch (Exception e){
                            duration = new float[]{rawPassive.getInt("duration")};
                        }
                        break;
                    case "requirements":
                        JSONObject rawRequirements = rawPassive.getJSONObject("requirements");
                        requirements = true;

                        night = rawRequirements.has("night") && rawRequirements.getBoolean("night");
                        boss = rawRequirements.has("boss") && rawRequirements.getBoolean("boss");
                }
            }
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }
    Passive(Passive passive){
        //Strings
        this.type = passive.getType();
        this.target = passive.getTarget();
        this.name = passive.getName();
        this.trigger = passive.getTrigger();
        this.modifySkill = passive.getModifySkill();

        //ints
        this.busyLinks = passive.getBusyLinks();
        this.attacksLeft = passive.getAttacksLeft();
        this.tick = passive.getTick();
        this.targets = passive.getTargets();
        this.fixedTime = passive.getFixedTime();
        this.maxStacks = passive.getMaxStacks();
        this.stacks = passive.getStacks();
        this.victories = passive.getVictories();
        this.stacksRequired = passive.getStacksRequired();
        this.uses = passive.getUses();
        this.hits = passive.getHits();
        this.dollID = passive.getDollID();

        //floats
        this.delay = passive.getDelay();
        this.radius = passive.getRadius();
        this.aoe_radius = passive.getAoe_radius();
        this.aoe_multiplier = passive.getAoe_multiplier();

        //booleans
        this.stun = passive.isStun();
        this.night = passive.isNight();
        this.boss = passive.isBoss();
        this.requirements = passive.isRequirements();
        this.sureCrit = passive.isSureCrit();
        this.refreshDuration = passive.isRefreshDuration();
        this.armoured = passive.isArmoured();
        this.ignoreArmour = passive.isIgnoreArmour();
        this.piercing = passive.isPiercing();
        this.stackable =  passive.isStackable();
        this.canCrit = passive.isCanCrit();
        this.aoe = passive.isAoe();
        this.aoe_canCrit = passive.isAoe_canCrit();
        this.aoe_sureCrit = passive.isAoe_sureCrit();
        this.triggerPythonPassive = passive.isTriggerPythonPassive();
        this.skill2Passive = passive.isSkill2Passive();

        //int arrays
        this.rounds = passive.getRounds();
        this.hitCount = passive.getHitCount();
        this.fixedDamage = passive.getFixedDamage();
        this.interval = passive.getInterval();
        this.armour = passive.getArmour();
        this.vulnerability = passive.getVulnerability();
        this.stacksToAdd = passive.getStacksToAdd();
        this.stackChance = passive.getStackChance();
        this.skillDamageBonus = passive.getSkillDamageBonus();
        this.ap = passive.getAp();
        this.butterCream = passive.getButterCream();
        this.extraCritDamage = passive.getExtraCritDamage();

        //float arrays
        this.fp = passive.getFp();
        this.acc = passive.getAcc();
        this.eva = passive.getEva();
        this.rof = passive.getRof();
        this.crit = passive.getCrit();
        this.critDmg = passive.getCritDmg();
        this.movespeed = passive.getMovespeed();
        this.duration = passive.getDuration();
        this.multiplier = passive.getMultiplier();

        this.effects = passive.getEffects();

        this.effect = passive.effect;
        this.startTime = passive.startTime;
        this.level = passive.level;
    }
    Passive(){

    }

    public String getType(){
        return type;
    }
    public String getTarget(){
        return target;
    }
    public String getName(){
        return name;
    }
    public String getTrigger() {
        return trigger;
    }
    public String getModifySkill() {
        return modifySkill;
    }

    public int getBusyLinks() {
        return busyLinks;
    }
    public int getAttacksLeft() {
        return attacksLeft;
    }
    public int getTick() {
        return tick;
    }
    public int getTargets() {
        return targets;
    }
    public int getFixedTime() {
        return fixedTime;
    }
    public int getMaxStacks() {
        return maxStacks;
    }
    public int getStacks() {
        return stacks;
    }
    public int[] getStacksRequired() {
        return stacksRequired;
    }
    public int getVictories() {
        return victories;
    }
    public int getHits() {
        return hits;
    }
    public int getUses() {
        return uses;
    }
    public int getDollID() {
        return dollID;
    }

    public float getDelay() {
        return delay;
    }
    public float getRadius() {
        return radius;
    }
    public float getAoe_radius() {
        return aoe_radius;
    }
    public float getAoe_multiplier() {
        return aoe_multiplier;
    }

    public boolean isStun() {
        return stun;
    }
    public boolean isNight() {
        return night;
    }
    public boolean isBoss() {
        return boss;
    }
    public boolean isRequirements() {
        return requirements;
    }
    public boolean isSureCrit() {
        return sureCrit;
    }
    public boolean isRefreshDuration() {
        return refreshDuration;
    }
    public boolean isArmoured() {
        return armoured;
    }
    public boolean isIgnoreArmour() {
        return ignoreArmour;
    }
    public boolean isPiercing() {
        return piercing;
    }
    public boolean isStackable() {
        return stackable;
    }
    public boolean isCanCrit() {
        return canCrit;
    }
    public boolean isAoe() {
        return aoe;
    }
    public boolean isAoe_canCrit() {
        return aoe_canCrit;
    }
    public boolean isAoe_sureCrit() {
        return aoe_sureCrit;
    }
    public boolean isTriggerPythonPassive() {
        return triggerPythonPassive;
    }
    public boolean isSkill2Passive() {
        return skill2Passive;
    }

    public int[] getRounds(){
        return rounds;
    }
    public int[] getHitCount(){
        return hitCount;
    }
    public int[] getFixedDamage(){
        return fixedDamage;
    }
    public int[] getArmour(){
        return armour;
    }
    public int[] getVulnerability(){
        return vulnerability;
    }
    public int[] getStacksToAdd(){
        return stacksToAdd;
    }
    public int[] getStackChance(){
        return stackChance;
    }
    public int[] getSkillDamageBonus(){
        return skillDamageBonus;
    }
    public int[] getAp(){
        return ap;
    }
    public int[] getButterCream(){
        return butterCream;
    }
    public int[] getExtraCritDamage() {
        return extraCritDamage;
    }

    public float[] getFp(){
        return fp;
    }
    public float[] getAcc(){
        return acc;
    }
    public float[] getEva(){
        return eva;
    }
    public float[] getRof(){
        return rof;
    }
    public float[] getCrit(){
        return crit;
    }
    public float[] getCritDmg(){
        return critDmg;
    }
    public float[] getMovespeed(){
        return movespeed;
    }
    public float[] getDuration(){
        return duration;
    }
    public float[] getMultiplier(){
        return multiplier;
    }
    public float[] getInterval() {
        return interval;
    }

//    public Passive getAfterEffect() {
//        return afterEffect;
//    }

    public void setEffects(Effect[] effects) {
        this.effects = effects;
    }

    public Effect[] getEffects() {
        return effects;
    }
}
