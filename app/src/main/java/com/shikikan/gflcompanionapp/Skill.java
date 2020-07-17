package com.shikikan.gflcompanionapp;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Skill {

    private String skillName, skillName_2, iconName, iconName_2, tooltipSkill, tooltipSkill_2, type,
            target, trigger;
    String modeName;

    /*private String type_2, target_2;

    private int initialCD, busyLinks, attacksLeft, stackChance, tick;

    private int initialCD_2, busyLinks_2, attacksLeft_2, stackChance_2;

    private int[] rounds;*/

    private float[] CD, stat_fp, stat_acc, stat_eva, stat_rof, stat_crit, stat_movespeed, duration, multiplier;

    /*private float[] CD_2, stat_fp_2, stat_acc_2, stat_eva_2, stat_rof_2, stat_crit_2, stat_movespeed_2, duration_2, multiplier_2;

    private float delay, radius;

    private float delay_2;

    private boolean stun, night, boss, requiremnts, sureCrit, refreshDuration;

    private boolean stun_2, night_2, boss_2, requiremnts_2, refreshDuration_2;


    //After-Effect Variables

    private String afterEffectType, afterEffectTarget;

    private float afterEffectRadius;

    private float[] afterEffectMultiplier, afterEffectDuration;

    private int afterEffectDelay, afterEffectBusyLinks;

    private int[] afterEffectRounds;

    //After after-effect variables

    private String afterAfterEffectType, afterAfterEffectTarget;

    private float afterAfterEffectRadius;

    private float[] afterAfterEffectMultiplier, afterAfterEffectDuration;

    private int afterAfterEffectDelay, afterAfterEffectBusyLinks;

    private int[] afterAfterEffectRounds;*/

    private int initialCD;
    int marks, skillUseCount, numberOfShots;

    private boolean buffedNade;

    private Effect[] effects;

    Skill(String skillName, String iconName, String tooltipSkill, JSONObject rawSkillInfo){
        try{
            this.skillName = skillName;
            this.tooltipSkill = tooltipSkill;
            this.iconName = iconName;

            //this.skillName_2 = skillName_1;
            //this.tooltipSkill_2 = tooltipSkill_2;
            //this.iconName_2 = iconName_2;

            JSONArray names = rawSkillInfo.names();
            if(names != null){
                for(int i = 0; i < names.length(); i++){
                    switch (names.getString(i)){
                        case "buffednade": buffedNade = rawSkillInfo.getBoolean("buffednade"); break;
                        case "icd": initialCD = rawSkillInfo.getInt("icd"); break;
                        case "cd":
                            JSONArray rawCD = rawSkillInfo.getJSONArray("cd");
                            CD = new float[rawCD.length()];
                            for(int ii = 0; ii < rawCD.length(); ii++) CD[ii] = (float) rawCD.getDouble(ii);
                            break;
                        case "effects"://'effects' is a JSONArray inside the 'rawSkillInfo' JSONObject
                            JSONArray tt = rawSkillInfo.getJSONArray(names.getString(i));//Place 'effects' into an array
                            effects = new Effect[tt.length()];//Create a new array to hold the 'SkillEffects' objects
                            for(int in = 0; in < tt.length(); in++)
                                effects[in] = new Effect(tt.getJSONObject(in));//Pass JSONObjects into 'SkillEffects.java'
                            break;
                    }
                }
            }
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }
    Skill(Skill skill){
        this.skillName = skill.getSkillName();
        this.iconName = skill.getIconName();
        this.tooltipSkill = skill.getTooltipSkill();
        //this.tooltipSkill_2 = skill.getTooltipSkill_2();
        this.CD = skill.getCD();
        this.initialCD = skill.getInitialCD();
        this.buffedNade = skill.isBuffedNade();
        this.effects = skill.getEffects();
    }

    public String getSkillName() {
        return skillName;
    }
    public String getIconName() {
        return iconName;
    }
    public String getTooltipSkill() {
        return tooltipSkill;
    }
    /*public String getTooltipSkill_2() {
        return tooltipSkill_2;
    }*/

    public float[] getCD() {
        return CD;
    }
    public int getInitialCD() {
        return initialCD;
    }
    public boolean isBuffedNade() {
        return buffedNade;
    }
    public Effect[] getEffects(){
        return effects;
    }

    /*public void setMarks(int marks){
        this.marks = marks;
    }
    public int getMarks() {
        return marks;
    }*/
}
