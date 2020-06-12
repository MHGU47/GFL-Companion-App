package com.shikikan.gflcompanionapp;

import org.json.JSONObject;

public class Equipment {

    private int id, type, rarity, fp, acc, eva, movespeed, rof, critdmg, crit, ap, armour, nightview, rounds,
        construct_time;
    private JSONObject rawLevelBonus;
    private int[] levelBonus;
    private boolean en_craftable, en_released;
    private String name, tooltip, image;

    Equipment(JSONObject equipData){
        try{
            id = (int) equipData.get("id");
            type = (int) equipData.get("type");
            rarity = (int) equipData.get("rarity");
            fp = (int) equipData.get("fp");
            acc = (int) equipData.get("acc");
            eva = (int) equipData.get("eva");
            movespeed = (int) equipData.get("movespeed");
            rof = (int) equipData.get("rof");
            critdmg = (int) equipData.get("critdmg");
            crit = (int) equipData.get("crit");
            ap = (int) equipData.get("ap");
            armour = (int) equipData.get("armor");
            nightview = (int) equipData.get("nightview");
            rounds = (int) equipData.get("rounds");
            construct_time = (int) equipData.get("construct_time");

            rawLevelBonus = (JSONObject) equipData.get("level_bonus");
            levelBonus = null;

            en_craftable = (boolean) equipData.get("en_craftable");
            en_released = (boolean) equipData.get("en_released");

            name = (String) equipData.get("name");
            tooltip = (String) equipData.get("tooltip");
            image = "equip_" + equipData.get("id");
        }
        catch (Exception e){
            setNull();
        }

    }

    Equipment(Equipment equipment){
        this.id = equipment.getID();
        this.type = equipment.getType();
        this.rarity = equipment.getRarity();
        this.fp = equipment.getFp();
        this.acc = equipment.getAcc();
        this.eva = equipment.getEva();
        this.movespeed = equipment.getMovespeed();
        this.rof = equipment.getRof();
        this.critdmg = equipment.getCritdmg();
        this.crit = equipment.getCrit();
        this.ap = equipment.getAp();
        this.armour = equipment.getArmour();
        this.nightview = equipment.getNightview();
        this.rounds = equipment.getRounds();
        this.construct_time = equipment.getConstruct_time();
        this.rawLevelBonus = equipment.getRawLevelBonus();
        this.levelBonus = equipment.getLevelBonus();
        this.en_craftable = equipment.getEn_Craftable();
        this.en_released = equipment.getEn_Released();
        this.name = equipment.getName();
        this.tooltip = equipment.getTooltip();
    }

    Equipment(){
        setNull();
    }

    private void setNull(){
        id = 0;
        type = 0;
        rarity = 0;
        fp = 0;
        acc = 0;
        eva = 0;
        movespeed = 0;
        rof = 0;
        critdmg = 0;
        crit = 0;
        ap = 0;
        armour = 0;
        nightview = 0;
        rounds = 0;
        construct_time = 0;

        rawLevelBonus = null;
        levelBonus = null;

        en_craftable = false;
        en_released = false;

        name = "";
        tooltip = "";
    }

    public void setID(int id){
        this.id = id;
    }
    public int getID(){
        return id;
    }

    public void setType(int type){
        this.type = type;
    }
    public int getType(){
        return type;
    }

    public void setRarity(int rarity){
        this.rarity = rarity;
    }
    public int getRarity(){
        return rarity;
    }

    public void setFp(int fp){
        this.fp = fp;
    }
    public int getFp(){
        return fp;
    }

    public void setAcc(int acc){
        this.acc = acc;
    }
    public int getAcc(){
        return acc;
    }

    public void setEva(int eva){
        this.eva = eva;
    }
    public int getEva(){
        return eva;
    }

    public void setMovespeed(int movespeed){
        this.movespeed = movespeed;
    }
    public int getMovespeed(){
        return movespeed;
    }

    public void setRof(int rof){
        this.rof = rof;
    }
    public int getRof(){
        return rof;
    }

    public void setCritdmg(int critdmg){
        this.critdmg = critdmg;
    }
    public int getCritdmg(){
        return critdmg;
    }

    public void setCrit(int crit){
        this.crit = crit;
    }
    public int getCrit(){
        return crit;
    }

    public void setAp(int ap){
        this.ap = ap;
    }
    public int getAp(){
        return ap;
    }

    public void setArmour(int armour){
        this.armour = armour;
    }
    public int getArmour(){
        return armour;
    }

    public void setNightview(int nightview){
        this.nightview = nightview;
    }
    public int getNightview(){
        return nightview;
    }

    public void setRounds(int rounds){
        this.rounds = rounds;
    }
    public int getRounds(){
        return rounds;
    }

    public void setConstruct_time(int construct_time){
        this.construct_time = construct_time;
    }
    public int getConstruct_time(){
        return construct_time;
    }

    public void setRawLevelBonus(JSONObject rawLevelBonus){
        this.rawLevelBonus = rawLevelBonus;
    }
    public JSONObject getRawLevelBonus(){
        return rawLevelBonus;
    }

    public void setLevelBonus(int[] levelBonus){
        this.levelBonus = levelBonus;
    }
    public int[] getLevelBonus(){
        return levelBonus;
    }

    public void setEn_craftable(boolean en_craftable){
        this.en_craftable = en_craftable;
    }
    public boolean getEn_Craftable(){
        return en_craftable;
    }

    public void setEn_released(boolean en_released){
        this.en_released = en_released;
    }
    public boolean getEn_Released(){
        return en_released;
    }

    public void setName(String name){
        this.name = name;
    }
    public String getName(){
        return name;
    }

    public void setTooltip(String type){
        this.tooltip = tooltip;
    }
    public String getTooltip(){
        return tooltip;
    }

    public String getImage() {
        return image;
    }
}
