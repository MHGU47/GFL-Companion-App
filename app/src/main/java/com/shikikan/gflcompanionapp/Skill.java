package com.shikikan.gflcompanionapp;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Skill {

    String skillName_1, skillName_2, iconName_1, iconName_2, tooltipSkill_1, tooltipSkill_2, type,
            target;

    int initialCD;

    float[] CD, stat_fp, duration;

    Skill(String skillName_1, String iconName_1, String tooltipSkill_1, String tooltipSkill_2, JSONObject rawSkillInfo){
        try{
            this.skillName_1 = skillName_1;
            this.tooltipSkill_1 = tooltipSkill_1;
            this.iconName_1 = iconName_1;

            //this.skillName_2 = skillName_1;
            this.tooltipSkill_2 = tooltipSkill_2;
            //this.iconName_2 = iconName_2;

            initialCD = rawSkillInfo.getInt("icd");
            JSONArray rawCD = rawSkillInfo.getJSONArray("cd");
            CD = new float[rawCD.length()];
            for(int i = 0; i < rawCD.length(); i++){
                CD[i] = (float) rawCD.getDouble(i);
            }

            JSONObject rawEffects = rawSkillInfo.getJSONObject("effects");
            type = rawEffects.getString("type");
            target = rawEffects.getString("target");

            JSONObject rawStat = rawEffects.getJSONObject("stat");//Holds all the arrays for stat multipliers
            JSONArray names = rawStat.names();//Holds the names to refer to the arrays within 'rawStat'
            if(names != null){
                for(int i = 0; i < rawStat.length(); i++){
                    JSONArray temp = rawStat.getJSONArray(names.getString(i));
                    switch(names.getString(i)){
                        case "fp":
                            for(int ii = 0; ii<  temp.length(); ii++) stat_fp[ii] = (float) temp.getDouble(ii);
                            break;
                    }
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
