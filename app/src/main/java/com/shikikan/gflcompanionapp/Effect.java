package com.shikikan.gflcompanionapp;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Effect {
    private String type, target, name, trigger, modifySkill;
    private int busyLinks, attacksLeft, tick, targets, fixedTime, maxStacks, stacks, interval,
            victories, hits, uses;
    private float radius, aoe_radius;
    private boolean stun, night, boss, sureCrit, refreshDuration, armoured, ignoreArmour,
            piercing, stackable, canCrit, aoe, aoe_canCrit, aoe_sureCrit, triggerPythonPassive, usable;

    private List<String> requirements, allStats;
    private int[] rounds, hitCount, fixedDamage, armour, vulnerability, stacksToAdd, stackChance,
            skillDamageBonus, ap, butterCream, extraAttackChance;
    private float[] fp, acc, eva, rof, crit, critDmg, movespeed, duration, multiplier, delay,
            aoe_multiplier, setTime;

    private Effect[] afterEffects;
    private Effect effect_2;

    //For use with 'BattleStats.java'. Gives the effect object the level of the passive object it is being used with
    int level, dollID, buffCounter = 0;
    private boolean statBuff = false;

    //stat, after(another skillEffects class)



    //private String skillName_1, skillName_2, iconName_1, iconName_2, tooltipSkill_1, tooltipSkill_2,
            //trigger, modeName;

    Effect(JSONObject rawEffects){
        try {
            JSONArray rawEffectsNames = rawEffects.names();
            if (rawEffectsNames == null) return;
            for (int i = 0; i < rawEffectsNames.length(); i++) {
                switch (rawEffectsNames.getString(i)) {
                    case "type": type = rawEffects.getString("type"); break;
                    case "target": target = rawEffects.getString("target"); break;
                    case "name": name = rawEffects.getString("name"); break;
                    case "trigger": trigger = rawEffects.getString("trigger"); break;
                    case "modifySkill;": modifySkill = rawEffects.getString("modifySkill"); break;

                    case "busyLinks": busyLinks = rawEffects.getInt("busylinks"); break;
                    case "tick": tick = rawEffects.getInt("tick"); break;
                    case "targets": targets = rawEffects.getInt("targets"); break;
                    case "fixedTime": fixedTime = rawEffects.getInt("fixedTime"); break;
                    case "maxStacks": maxStacks = rawEffects.getInt("maxStacks"); break;
                    case "stacks": stacks = rawEffects.getInt("stacks"); break;
                    case "attacksLeft": attacksLeft = rawEffects.getInt("attacksLeft"); break;
                    case "interval": interval = rawEffects.getInt("interval"); break;
                    case "victories": victories = rawEffects.getInt("victories"); break;
                    case "hits": hits = rawEffects.getInt("hits"); break;
                    case "uses": hits = rawEffects.getInt("uses"); break;

                    case "delay":

                        try {
                            JSONArray rawDelay = rawEffects.getJSONArray("delay");
                            delay = new float[rawDelay.length()];
                            for (int stat = 0; stat < rawDelay.length(); stat++) {
                                delay[stat] = (float) rawDelay.getDouble(stat);
                            }
                        }
                        catch (Exception e){
                            delay = new float[]{(float)rawEffects.getDouble("delay")};
                        }

                        break;
                    case "radius": radius = (float) rawEffects.getDouble("radius"); break;
                    case "aoe_radius": aoe_radius = (float) rawEffects.getDouble("aoe_radius"); break;
                    case "aoe_multiplier":

                        try {
                            JSONArray rawAOE_Multiplier = rawEffects.getJSONArray("aoe_multiplier");
                            aoe_multiplier = new float[rawAOE_Multiplier.length()];
                            for (int stat = 0; stat < rawAOE_Multiplier.length(); stat++) {
                                aoe_multiplier[stat] = (float) rawAOE_Multiplier.getDouble(stat);
                            }
                        }
                        catch (Exception e){
                            aoe_multiplier = new float[]{(float)rawEffects.getDouble("aoe_multiplier")};
                        }

                        break;


                    case "stun": stun = rawEffects.getBoolean("stun"); break;
                    case "sureCrit": sureCrit = rawEffects.getBoolean("sureCrit"); break;
                    case "armored": armoured = rawEffects.getBoolean(("armored")); break;
                    case "ignoreArmor": ignoreArmour = rawEffects.getBoolean("ignoreArmor"); break;
                    case "piercing": piercing = rawEffects.getBoolean("piercing"); break;
                    case "stackable": stackable = rawEffects.getBoolean("stackable"); break;
                    case "refreshDuration": refreshDuration = rawEffects.getBoolean("refreshDuration"); break;
                    case "canCrit": canCrit = rawEffects.getBoolean("canCrit"); break;
                    case "aoe": aoe = rawEffects.getBoolean("aoe"); break;
                    case "aoe_canCrit": aoe_canCrit = rawEffects.getBoolean("aoe_canCrit"); break;
                    case "aoe_sureCrit": aoe_sureCrit = rawEffects.getBoolean("aoe_sureCrit"); break;
                    case "triggerPythonPassive": triggerPythonPassive = rawEffects.getBoolean("triggerPythonPassive"); break;

                    case "stat":
                        statBuff = true;
                        buffCounter++;
                        JSONObject rawStat = rawEffects.getJSONObject("stat");
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
                                        fp = new float[]{(float)rawEffects.getDouble("fp")};
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
                                        acc = new float[]{(float)rawEffects.getDouble("acc")};
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
                                        acc = new float[]{(float)rawEffects.getDouble("acc")};
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
                                        rof = new float[]{(float)rawEffects.getDouble("rof")};
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
                                        crit = new float[]{(float)rawEffects.getDouble("crit")};
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
                                        critDmg = new float[]{(float)rawEffects.getDouble("critdmg")};
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
                                        movespeed = new float[]{(float)rawEffects.getDouble("movespeed")};
                                    }
                                    break;
                                case "ap":
                                    /*JSONArray ap_ = rawStat.getJSONArray("ap");
                                    ap = new int[ap_.length()];
                                    for (int stat = 0; stat < ap_.length(); stat++) {
                                        ap[stat] = ap_.getInt(stat);
                                    }*/

                                    ap = new int[]{rawEffects.getInt("ap")};
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
                                        armour = new int[]{rawEffects.getInt("armor")};
                                    }
                                    break;
                            }
                                    //1,//FP
                                    //1,//Acc
                                    //1,//Eva
                                    //1,//Rof
                                    //1,//CritDmg
                                    //1,//Crit
                                    //0,//Rounds
                                    //1,//Armour
                                    //1,//AP
                                    //1,//Skill CD
                            allStats.add(names.getString(index));
                        }
                        break;
                    case "effect":
                        JSONObject rawEffect = rawEffects.getJSONObject("effect");
                        effect_2 = new Effect(rawEffect);
                        break;
                    case "after":
                        try{
                            JSONObject rawAfter = rawEffects.getJSONObject("after");
                            afterEffects = new Effect[]{new Effect(rawAfter)};
                        }catch (Exception e){
                            JSONArray rawAfter = rawEffects.getJSONArray("after");
                            afterEffects = new Effect[rawAfter.length()];
                            for(int index = 0; index < rawAfter.length(); index++){
                                afterEffects[index] = new Effect(rawAfter.getJSONObject(index));
                            }
                        }

                        break;
                    case "buttercream":
                        JSONArray rawButterCream = rawEffects.getJSONArray("buttercream");
                        butterCream = new int[rawButterCream.length()];
                        for (int index = 0; index < rawButterCream.length(); index++) {
                            butterCream[index] = rawButterCream.getInt(index);
                        }
                        break;
                    case "extraAttackChance":
                        JSONArray rawExtraAttackChance = rawEffects.getJSONArray("extraAttackChance");
                        extraAttackChance = new int[rawExtraAttackChance.length()];
                        for (int index = 0; index < rawExtraAttackChance.length(); index++) {
                            extraAttackChance[index] = rawExtraAttackChance.getInt(index);
                        }
                        break;
                    case "setTime":
                        JSONArray rawSetTime = rawEffects.getJSONArray("setTime");
                        setTime = new float[rawSetTime.length()];
                        for (int index = 0; index < rawSetTime.length(); index++) {
                            setTime[index] = rawSetTime.getInt(index);
                        }
                        break;
                    case "skillDamageBonus":
                        JSONArray rawSkillDamageBonus = rawEffects.getJSONArray("skillDamageBonus");
                        skillDamageBonus = new int[rawSkillDamageBonus.length()];
                        for (int index = 0; index < rawSkillDamageBonus.length(); index++) {
                            skillDamageBonus[index] = rawSkillDamageBonus.getInt(index);
                        }
                        break;
                    case "vulnerability":
                        try {
                            JSONArray rawVulnerability = rawEffects.getJSONArray("vulnerability");
                            vulnerability = new int[rawVulnerability.length()];
                            for (int index = 0; index < rawVulnerability.length(); index++) {
                                vulnerability[index] = rawVulnerability.getInt(index);
                            }
                        }
                        catch (Exception e){
                            vulnerability = new int[]{rawEffects.getInt("vulnerability")};
                        }
                        break;
                    case "hitCount":
                        JSONArray rawHitCount = rawEffects.getJSONArray("hitCount");
                        hitCount = new int[rawHitCount.length()];
                        for (int index = 0; index < rawHitCount.length(); index++) {
                            hitCount[index] = rawHitCount.getInt(index);
                        }
                        break;
                    case "fixedDamage":
                        JSONArray rawFixedDamage = rawEffects.getJSONArray("fixedDamage");
                        fixedDamage = new int[rawFixedDamage.length()];
                        for (int index = 0; index < rawFixedDamage.length(); index++) {
                            fixedDamage[index] = rawFixedDamage.getInt(index);
                        }
                        break;
                    case "rounds":
                        JSONArray rawRounds = rawEffects.getJSONArray("rounds");
                        rounds = new int[rawRounds.length()];
                        for (int index = 0; index < rawRounds.length(); index++) {
                            rounds[index] = rawRounds.getInt(index);
                        }
                        break;
                    case "multiplier":
                        try {
                            JSONArray rawMultiplier = rawEffects.getJSONArray("multiplier");
                            multiplier = new float[rawMultiplier.length()];
                            for (int index = 0; index < rawMultiplier.length(); index++) {
                                multiplier[index] = (float) rawMultiplier.getDouble(index);
                            }
                        }
                        catch (Exception e){
                            multiplier = new float[]{rawEffects.getInt("multiplier")};
                        }
                        break;
                    case "stacksToAdd":
                        try {
                            JSONArray rawStacksToAdd = rawEffects.getJSONArray("stacksToAdd");
                            stacksToAdd = new int[rawStacksToAdd.length()];
                            for (int index = 0; index < rawStacksToAdd.length(); index++) {
                                stacksToAdd[index] = rawStacksToAdd.getInt(index);
                            }
                        }
                        catch (Exception e){
                            stacksToAdd = new int[]{rawEffects.getInt("stacksToAdd")};
                        }
                        break;
                    case "stackChance":
                        JSONArray rawStackChance = rawEffects.getJSONArray("stackChance");
                        stackChance = new int[rawStackChance.length()];
                        for (int index = 0; index < rawStackChance.length(); index++) {
                            stackChance[index] = rawStackChance.getInt(index);
                        }
                        break;
                    case "duration":
                        try {
                            JSONArray rawDuration = rawEffects.getJSONArray("duration");
                            duration = new float[rawDuration.length()];
                            for (int index = 0; index < rawDuration.length(); index++) {
                                duration[index] = (float) rawDuration.getDouble(index);
                            }
                        }
                        catch (Exception e){
                            duration = new float[]{rawEffects.getInt("duration")};
                        }
                        break;
                    case "requirements":
                        JSONObject rawRequirements = rawEffects.getJSONObject("requirements");
                        requirements = new ArrayList<>();
                        if(rawRequirements.has("night")){
                            night = rawRequirements.getBoolean("night");
                            requirements.add("night");
                        }
                        if(rawRequirements.has("boss")){
                            boss = rawRequirements.getBoolean("boss");
                            requirements.add("boss");
                        }
                        if(rawRequirements.has("armored")){
                            armoured = rawRequirements.getBoolean("armored");
                            requirements.add("armored");
                        }
                }
            }
        }
        catch (JSONException e) {
                e.printStackTrace();
            }
    }
    Effect(Effect effect){
        this.type = effect.getType();
        this.target = effect.getTarget();
        this.name = effect.getName();
        this.trigger = effect.getTrigger();
        this.modifySkill = effect.getModifySkill();

        this.busyLinks = effect.getBusyLinks();
        this.attacksLeft = effect.getAttacksLeft();
        this.tick = effect.getTick();
        this.targets = effect.getTargets();
        this.fixedTime = effect.getFixedTime();
        this.maxStacks = effect.getMaxStacks();
        this.stacks = effect.getStacks();
        this.interval = effect.getInterval();
        this.victories = effect.getVictories();
        this.hits = effect.getHits();
        this.uses = effect.getUses();

        this.delay = effect.getDelay();
        this.radius = effect.getRadius();
        this.aoe_radius = effect.getAoe_radius();
        this.aoe_multiplier = effect.getAoe_multiplier();

        this.stun = effect.isStun();
        this.night = effect.isNight();
        this.boss = effect.isBoss();
        this.sureCrit = effect.isSureCrit();
        this.refreshDuration = effect.isRefreshDuration();
        this.armoured = effect.isArmoured();
        this.ignoreArmour = effect.isIgnoreArmour();
        this.piercing = effect.isPiercing();
        this.stackable = effect.isStackable();
        this.canCrit = effect.isCanCrit();
        this.aoe = effect.isAoe();
        this.aoe_canCrit = effect.isAoe_canCrit();
        this.aoe_sureCrit = effect.isAoe_sureCrit();
        this.triggerPythonPassive = effect.isTriggerPythonPassive();
        this.usable = effect.isUsable();

        this.requirements = effect.getRequirements();

        this.rounds = effect.getRounds();
        this.hitCount = effect.getHitCount();
        this.fixedDamage = effect.getFixedDamage();
        this.armour = effect.getArmour();
        this.vulnerability = effect.getVulnerability();
        this.stacksToAdd = effect.getStacksToAdd();
        this.stackChance = effect.getStackChance();
        this.skillDamageBonus = effect.getSkillDamageBonus();
        this.ap = effect.getAp();
        this.butterCream = effect.getButterCream();
        this.extraAttackChance = effect.getExtraAttackChance();

        this.fp = effect.getFp();
        this.acc = effect.getAcc();
        this.eva = effect.getEva();
        this.rof = effect.getRof();
        this.crit = effect.getCrit();
        this.critDmg = effect.getCritDmg();
        this.movespeed = effect.getMovespeed();
        this.duration = effect.getDuration();
        this.multiplier = effect.getMultiplier();
        this.setTime = effect.getSetTime();

        this.afterEffects = effect.getAfterEffects();
        this.effect_2 = effect.getEffect_2();

        this.level = effect.level;
    }

    public String getType(){
        return type;
    }
    public String getTarget(){
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
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
    public void setModifySkill(String modifySkill){
        this.modifySkill = modifySkill;
    }

    public int getBusyLinks() {
        return busyLinks;
    }
    public int getAttacksLeft() {
        return attacksLeft;
    }
    public void setAttacksLeft(int attacksLeft){
        this.attacksLeft = attacksLeft;
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
    public void setStacks(int stacks) {
        this.stacks = stacks;
    }
    public int getStacks() {
        return stacks;
    }
    public int getInterval() {
        return interval;
    }
    public int getVictories() {
        return victories;
    }
    public int getHits() {
        return hits;
    }
    public void setUses(int uses){
        this.uses = uses;
    }
    public int getUses() {
        return uses;
    }

    public float[] getDelay() {
        return delay;
    }
    public void setDelay(float[] delay){
        this.delay = delay;
    }
    public float getRadius() {
        return radius;
    }
    public float getAoe_radius() {
        return aoe_radius;
    }
    public float[] getAoe_multiplier() {
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

    public void setUsable(boolean usable){
        this.usable = usable;
    }
    public boolean isUsable() {
        return usable;
    }

    public List<String> getRequirements() {
        return requirements;
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
    public void setStacksToAdd(int[] stacksToAdd) {
        this.stacksToAdd = stacksToAdd;
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
    public int[] getExtraAttackChance(){
        return extraAttackChance;
    }

    public float[] getFp(){
        return fp;
    }
    public void setFp(float[] fp) {
        this.fp = fp;
    }
    public float[] getAcc(){
        return acc;
    }
    public void setAcc(float[] acc) {
        this.acc = acc;
    }
    public float[] getEva(){
        return eva;
    }
    public float[] getRof(){
        return rof;
    }
    public void setRof(float[] rof) {
        this.rof = rof;
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
    public void setDuration(float[] duration) {
        this.duration = duration;
    }
    public float[] getMultiplier(){
        return multiplier;
    }
    public void setMultiplier(float[] multiplier){
        this.multiplier = multiplier;
    }
    public void setSetTime(float[] setTime){
        this.setTime = setTime;
    }
    public float[] getSetTime() {
        return setTime;
    }

    public Effect[] getAfterEffects() {
        return afterEffects;
    }
    public Effect getEffect_2() {
        return effect_2;
    }

    public void setStackChance(int[] stackChance) {
        this.stackChance = stackChance;
    }

    public boolean isStatBuff(){
        return statBuff;
    }

    public List<String> getAllStatsNames(){
        return allStats;
    }

    public float[] getBuff(String buff){
        float[] temp;
        switch(buff){
            case "fp": return fp;
            case "acc": return acc;
            case "eva": return eva;
            case "rof": return rof;
            case "critDmg": return critDmg;
            case "crit": return crit;
            case "rounds":
                temp = new float[rounds.length];
                for(int i = 0; i < temp.length; i++){
                    temp[i] = (float) rounds[i];
                }
                return temp;
            case "armour":
                temp = new float[armour.length];
                for(int i = 0; i < temp.length; i++){
                    temp[i] = (float) armour[i];
                }
                return temp;
            //case "ap":
            default:
                temp = new float[ap.length];
                for(int i = 0; i < temp.length; i++){
                    temp[i] = (float) ap[i];
                }
                return temp;
        }
    }
}
