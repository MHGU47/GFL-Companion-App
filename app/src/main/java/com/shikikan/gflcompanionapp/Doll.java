package com.shikikan.gflcompanionapp;

import android.widget.ImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class Doll {

    private int id, id_index, spritesheet_row, spritesheet_col, rarity, type, hp, fp, acc, eva, rof, crit,
            critdmg, ap, rounds, armour, framesPerAttack, growth_rating, construct_time, gridPosition,
            echelonPosition, level, skill_1Level, skill_2Level;
    
    private int simFp, simAcc, simEva, simRof, simCrit, simCritDmg, simAp, simRounds, simCurrentRounds,
            simArmour, totalShots, hits, misses, targets, busyLinks, skillDamage, numOfAttacks;
    
    private float affection, simCooldown;

    private JSONArray aliases;

    private String api_name, name, artist, voice, name_skill_1, icon_name_skill_1, tooltip_skill_1,
            tooltip_skill_2, tooltip_tiles, image, name_skill_2, icon_name_skill_2;

    private Boolean mod, en_craftable, en_released, skill_control, slug;

    private JSONObject rawSkill_1, rawSkill_2, tiles;

    private ImageView gridPosition_imageview;

    private int[] tilesFormation, tilesBuffs, receivedTileBuffs, equipmentBuffs;

    private List<Integer> shots;

    private Equipment[] equipment;

    private Timer normalAttackTimer, skillTimer;//TODO: Remove these, they're referenced in 'BS' now

    private Vector<Object> actionQueue, effectQueue;

    private Skill skill_1, skill_2;

    private List<Buff> buffs;

    //private List<Passive> passives;
    private Passive[] passives;

    private BattleStats bs;

    /**
     * Pass in the data in the form of a JSONObject so the data can be extracted properly.
     * To be used by Utils.LoadDollData(Context)
     * @param DollData JSONObject from the JSONArray of data from the 'dolls.json' file.
     */
    Doll(JSONObject DollData){
        try{
            id = (int) DollData.get("id");
            id_index = (int) DollData.get("id_index");
            aliases = (JSONArray) DollData.get("aliases");
            api_name = (String) DollData.get("api_name");
            spritesheet_row = (int) DollData.get("spritesheet_row");
            spritesheet_col = (int) DollData.get("spritesheet_col");
            name = (String) DollData.get("name");
            rarity = (int) DollData.get("rarity");
            level = 1;
            type = (int) DollData.get("type");
            mod = (boolean) DollData.get("mod");
            hp = (int) DollData.get("hp");
            fp = (int) DollData.get("fp");
            acc = (int) DollData.get("acc");
            eva = (int) DollData.get("eva");
            rof = (int) DollData.get("rof");
            crit = (int) DollData.get("crit");
            critdmg = (int) DollData.get("critdmg");
            ap = (int) DollData.get("ap");
            rounds = (int) DollData.get("rounds");
            armour = (int) DollData.get("armor");
            try{
                framesPerAttack = (int) DollData.get("frames_per_attack");
            }
            catch (Exception e){
                framesPerAttack = 0;
            }
            growth_rating = (int) DollData.get("growth_rating");
            construct_time = (int) DollData.get("construct_time");
            en_craftable = (boolean) DollData.get("en_craftable");
            en_released = (boolean) DollData.get("en_released");
            artist = (String) DollData.get("artist");
            voice = (String) DollData.get("voice");
            name_skill_1 = (String) DollData.get("name_skill1");
            icon_name_skill_1 = (String) DollData.get("icon_name_skill1");
            tooltip_skill_1 = (String) DollData.get("tooltip_skill1");
            rawSkill_1 = (JSONObject) DollData.get("skill");
            try {
                rawSkill_2 = (JSONObject) DollData.get("skill2");
                name_skill_2= DollData.getString("name_skill2");
                icon_name_skill_2 = DollData.getString("icon_name_skill2");
                tooltip_skill_2 = (String) DollData.get("tooltip_skill2");
            }
            catch (JSONException e){
                rawSkill_2 = null;
                name_skill_2= null;
                icon_name_skill_2 = null;
                tooltip_skill_2 = null;
            }
            skill_1Level = 1;
            skill_2Level = 1;
            try{
                skill_control = DollData.getBoolean("skill_control");
            }
            catch(Exception e){
                skill_control = null;
            }
            tooltip_tiles = (String) DollData.get("tooltip_tiles");
            tiles = (JSONObject) DollData.get("tiles");
            gridPosition = 0;
            gridPosition_imageview = null;
            echelonPosition = 0;
            receivedTileBuffs = new int[]{0, 0, 0, 0, 0, 0, 0};
            equipmentBuffs = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

            equipment = new Equipment[]{new Equipment(), new Equipment(), new Equipment()};

            affection = 0;
            image = "doll_" + DollData.get("id");
            
            //simAcc = 0;
            //simAp = 0;
            //simArmour = 0;
            //simCooldown = 0;
            //simCrit = 0;
            //simCritDmg = 0;
            //simEva = 0;
            //simFp = 0;
            //simRof = 0;
            //simRounds = 0;
            //simCurrentRounds = 0;
            //totalShots = 0;
            //misses = 0;
            //hits = 0;
            //shots = new ArrayList<>();
            //targets = 0;
            //busyLinks = 0;
            //skillDamage = 0;
            //numOfAttacks = 0;
            //normalAttackTimer = new Timer("normal attack",0);
            //skillTimer = new Timer("skill", 0);
            //actionQueue = new Vector<Object>();
            //effectQueue = new Vector<Object>();

            skill_1 = new Skill(name_skill_1, icon_name_skill_1, tooltip_skill_1, rawSkill_1);
            //TODO: Rewrite how these are passed in. Use an array to hold everything except 'rawSkill'
            if(name_skill_2 != null)
                skill_2 = new Skill(name_skill_2, icon_name_skill_2, tooltip_skill_2, rawSkill_2);
            buffs = new ArrayList<>();
            //passives = new ArrayList<>();
            try{
                JSONArray temp = DollData.getJSONArray("passives");
                passives = new Passive[temp.length()];
                for(int i = 0; i < temp.length(); i++){
                    passives[i] = new Passive(temp.getJSONObject(i));
                }
            }
            catch (Exception e){
                passives = null;
            }

        }
        catch (JSONException e){
            setNull();
        }
    }

    Doll(Doll newDoll){
        this.id = newDoll.getID();
        this.id_index = newDoll.getId_index();
        this.spritesheet_row = newDoll.getSpritesheet_row();
        this.spritesheet_col = newDoll.getSpritesheet_col();
        this.rarity = newDoll.getRarity();
        this.type = newDoll.getType();
        this.hp = newDoll.getHp();
        this.fp = newDoll.getFp();
        this.acc = newDoll.getAcc();
        this.eva = newDoll.getEva();
        this.rof = newDoll.getCrit();
        this.crit = newDoll.getCrit();
        this.critdmg = newDoll.getCritdmg();
        this.ap = newDoll.getAp();
        this.rounds = newDoll.getRounds();
        this.armour = newDoll.getArmour();
        this.framesPerAttack = newDoll.getFramesPerAttack();
        this.growth_rating = newDoll.getGrowth_rating();
        this.construct_time = newDoll.getConstruct_time();
        this.gridPosition = newDoll.getGridPosition();
        this.echelonPosition = newDoll.getEchelonPosition();
        this.level = newDoll.getLevel();
        this.skill_1Level = newDoll.getSkill_1Level();
        this.skill_2Level = newDoll.getSkill_2Level();
        this.affection = newDoll.getAffection();
        this.aliases = newDoll.getAliases();
        this.api_name = newDoll.getApi_name();
        this.name = newDoll.getName();
        this.artist = newDoll.getArtist();
        this.voice = newDoll.getVoice();
        this.name_skill_1 = newDoll.getName_skill_1();
        this.name_skill_2 = newDoll.getName_skill_2();
        this.icon_name_skill_1 = newDoll.getIcon_name_skill_1();
        this.icon_name_skill_2 = newDoll.getIcon_name_skill_2();
        this.tooltip_skill_1 = newDoll.getTooltip_skill_1();
        this.tooltip_skill_2 = newDoll.getTooltip_skill_2();
        this.tooltip_tiles = newDoll.getTooltip_tiles();
        this.image = newDoll.getImage();
        this.mod = newDoll.isMod();
        this.en_craftable = newDoll.isEn_craftable();
        this.en_released = newDoll.isEn_released();
        this.rawSkill_1 = newDoll.getRawSkill_1();
        this.rawSkill_2 = newDoll.getRawSkill_2();
        this.skill_1 = newDoll.getSkill_1();
        this.skill_2 = newDoll.getSkill_2();
        this.skill_control = newDoll.getSkill_control();
        this.tiles = newDoll.getRawTiles();
        this.gridPosition_imageview = newDoll.getGridImageView();
        this.tilesFormation = newDoll.getTilesFormation();
        this.tilesBuffs = newDoll.getTilesBuffs();
        this.receivedTileBuffs = newDoll.getReceivedTileBuffs();
        this.equipmentBuffs = newDoll.getEquipmentBuffs();
        this.equipment = newDoll.getAllEquipment();
        this.slug = newDoll.hasSlug();

        //this.simRounds = 0;
        //this.simCurrentRounds = 0;
        //this.simRof = 0;
        //this.simFp = 0;
        //this.simEva = 0;
        //this.simCritDmg = 0;
        //this.simCrit = 0;
        //this.simArmour = 0;
        //this.simAp = 0;
        //this.simAcc = 0;
        //this.simCooldown = 0;
        //this.totalShots = 0;
        //this.misses = 0;
        //this.hits = 0;
        //this.shots = new ArrayList<>();
        //this.targets = 0;
        //this.busyLinks = 0;
        //this.skillDamage = 0;
        //this.numOfAttacks = 0;
        //this.normalAttackTimer = new Timer("normal attack",0);
        //this.skillTimer = new Timer("skill", 0);
        //this.actionQueue = new Vector<Object>();
        //this.effectQueue = new Vector<Object>();
        this.buffs = getBuffs();
        //this.passives = getPassives();
        this.passives = newDoll.getPassives();
        this.bs = null;
    }

    Doll(){
        setNull();
    }

    private void setNull(){
        id = 0;
        id_index = 0;
        aliases = null;
        api_name = "";
        spritesheet_row = 0;
        spritesheet_col = 0;
        name = "";
        rarity = 0;
        level = 0;
        affection = 0;
        type = 0;
        mod = true;
        hp = 0;
        fp = 0;
        acc = 0;
        eva = 0;
        rof = 0;
        crit = 0;
        critdmg = 0;
        ap = 0;
        rounds = 0;
        armour = 0;
        framesPerAttack = 0;
        growth_rating = 0;
        construct_time = 0;
        en_craftable = true;
        en_released = true;
        artist = "";
        voice = "";
        name_skill_1 = "";
        icon_name_skill_1 = "";
        tooltip_skill_1 = "";
        rawSkill_1 = null;
        rawSkill_2 = null;
        skill_1Level = 0;
        skill_2Level = 0;
        name_skill_2 = "";
        icon_name_skill_2 = "";
        skill_control = null;
        tooltip_skill_2 = "";
        tooltip_tiles = "";
        tiles = null;
        gridPosition = 0;
        gridPosition_imageview = null;
        echelonPosition = 0;
        image = "placeholder";
        receivedTileBuffs = null;
        equipmentBuffs = null;
        equipment = null;

        //simRounds = 0;
        //simCurrentRounds = 0;
        //simRof = 0;
        //simFp = 0;
        //simEva = 0;
        //simCritDmg = 0;
        //simCrit = 0;
        //simArmour = 0;
        //simAp = 0;
        //simAcc = 0;
        //simCooldown = 0;
        //totalShots = 0;
        //misses = 0;
        //hits = 0;
        //shots = null;
        //targets = 0;
        //busyLinks = 0;
        //skillDamage = 0;
        //numOfAttacks = 0;
        //normalAttackTimer = null;
        //skillTimer = null;
        //actionQueue = null;
        skill_1 = null;
        skill_2 = null;
        buffs = null;
        //passives = null;
        passives = null;
        bs = null;
    }

    public int getID() {
        return id;
    }

    public int getId_index() {
        return id_index;
    }

    public int getSpritesheet_row() {
        return spritesheet_row;
    }

    public int getSpritesheet_col() {
        return spritesheet_col;
    }

    public int getRarity() {
        return rarity;
    }

    public void setLevel(int level) {
        this.level = level;
    }
    public int getLevel() {
        return level;
    }

    public void setSkill_1Level(int skillLevel) {
        this.skill_1Level = skillLevel;
    }
    public int getSkill_1Level() {
        return skill_2Level;
    }

    public void setSkill_2Level(int skillLevel) {
        this.skill_2Level = skillLevel;
    }
    public int getSkill_2Level() {
        return skill_2Level;
    }

    public void setAffection(String affection){
        switch(affection){
            case "Low":
                this.affection = -0.05f;
                break;
            case "Normal":
                this.affection = 0;
                break;
            case "Max":
                this.affection = 0.05f;
                break;
            default:
                this.affection = mod ? 0.15f : 0.10f;
                break;
        }
    }
    public float getAffection(){
        return affection;
    }

    public int getType() {
        return type;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }
    public int getHp() {
        return hp;
    }

    public void setFp(int fp) {
        this.fp = fp;
    }
    public int getFp() {
        return fp;
    }

    public void setAcc(int acc) {
        this.acc = acc;
    }
    public int getAcc() {
        return acc;
    }

    public void setEva(int eva) {
        this.eva = eva;
    }
    public int getEva() {
        return eva;
    }

    public void setRof(int rof) {
        this.rof = rof;
    }
    public int getRof() {
        return rof;
    }

    public void setCrit(int crit) {
        this.crit = crit;
    }
    public int getCrit() {
        return crit;
    }

    public void setCritdmg(int critdmg) {
        this.critdmg = critdmg;
    }
    public int getCritdmg() {
        return critdmg;
    }

    public void setAp(int ap) {
        this.ap = ap;
    }
    public int getAp() {
        return ap;
    }

    public void setRounds(int rounds) {
        this.rounds = rounds;
    }
    public int getRounds() {
        return rounds;
    }

    public void setArmour(int armour) {
        this.armour = armour;
    }
    public int getArmour() {
        return armour;
    }

    public void setFramesPerAttack(int framesPerAttack) {
        this.framesPerAttack = framesPerAttack;
    }
    public int getFramesPerAttack() {
        return framesPerAttack;
    }

    public int getGrowth_rating() {
        return growth_rating;
    }

    public int getConstruct_time() {
        return construct_time;
    }

    public JSONArray getAliases() {
        return aliases;
    }

    public String getApi_name() {
        return api_name;
    }

    public String getName() {
        return name;
    }

    public String getArtist() {
        return artist;
    }

    public String getVoice() {
        return voice;
    }

    public String getName_skill_1() {
        return name_skill_1;
    }

    public String getName_skill_2() {
        return name_skill_2;
    }

    public String getIcon_name_skill_1() {
        return icon_name_skill_1;
    }

    public String getIcon_name_skill_2() {
        return icon_name_skill_2;
    }

    public String getTooltip_skill_1() {
        return tooltip_skill_1;
    }

    public String getTooltip_skill_2() {
        return tooltip_skill_2;
    }

    public String getTooltip_tiles() {
        return tooltip_tiles;
    }

    public Boolean isMod() {
        return mod;
    }

    public Boolean isEn_craftable() {
        return en_craftable;
    }

    public Boolean isEn_released() {
        return en_released;
    }

    public JSONObject getRawSkill_1() {
        return rawSkill_1;
    }

    public JSONObject getRawSkill_2() {
        return rawSkill_2;
    }

    public Skill getSkill_1(){
        return skill_1;
    }

    public Skill getSkill_2(){
        return skill_2;
    }

    public Boolean getSkill_control() {
        return skill_control;
    }

    public JSONObject getRawTiles() {
        return tiles;
    }

    public String getImage() {
        return image;
    }

    //Grid/Positioning

    public void setGrid(int gridPosition, ImageView gridImageView) {
        this.gridPosition = gridPosition;
        gridPosition_imageview = gridImageView;
    }

    public void setGridPosition(int gridPosition) {
        this.gridPosition = gridPosition;
    }

    public int getGridPosition(){
        return gridPosition;
    }

    public ImageView getGridImageView() {
        return gridPosition_imageview;
    }

    public void setEchelonPosition(int echelonPosition) {
        this.echelonPosition = echelonPosition;
    }

    public int getEchelonPosition() {
        return echelonPosition;
    }

    //Equipment

    public void setEquipment(Equipment equip, int slot){
        equipment[slot - 1] = new Equipment(equip);
        slug = equip.getType() == 7;
    }

    public void removeEquipment(int slot){
        if(slot == 0){
            switch(level){
                case 1:
                case 10:
                    equipment = new Equipment[]{new Equipment(), new Equipment(), new Equipment()};
                    slug = false;
                    break;
                case 20:
                case 30:
                case 40:
                    equipment[1] = new Equipment();
                    equipment[2] = new Equipment();
                    slug = false;
                    break;
                case 50:
                case 60:
                case 70:
                    equipment[2] = new Equipment();
                    break;
                default:
                    break;
            }
        }
        else equipment[slot - 1] = new Equipment();
        slug = slug && slot != 2;
    }

    public Equipment[] getAllEquipment(){
        return equipment;
    }

    public Equipment getEquipment(int index){
        return equipment[index];
    }

    public void setEquipmentBuffs(int[] buffs){
        equipmentBuffs = buffs;
    }

    public int[] getEquipmentBuffs(){
        return equipmentBuffs;
    }

    public float getEquipmentBuff(String buff){
        switch(buff){
            case "fp": return (float)equipmentBuffs[0];
            case "acc": return (float)equipmentBuffs[1];
            case "eva": return (float)equipmentBuffs[2];
            case "movespeed": return (float)equipmentBuffs[3];
            case "rof": return (float)equipmentBuffs[4];
            case "critdmg": return (float)equipmentBuffs[5];
            case "crit": return (float)equipmentBuffs[6];
            case "ap": return (float)equipmentBuffs[7];
            case "armour": return (float)equipmentBuffs[8];
            case "nightview": return (float)equipmentBuffs[9];
            default: return (float)equipmentBuffs[10];
        }
    }

    public Boolean hasSlug() {
        return slug;
    }

    //Tiles

    public void setTiles(int[][] tiles){
        tilesFormation = tiles[0];
        tilesBuffs = tiles[1];
    }

    public int[] getTilesBuffs(){
        return tilesBuffs;
    }

    public int[]getTilesFormation(){
        return tilesFormation;
    }

    public void setReceivedTileBuffs(int[] buffs){
        receivedTileBuffs = buffs;
    }

    public int[] getReceivedTileBuffs(){
        return receivedTileBuffs;
    }

    public float getTileBuff(String buff){
        switch(buff){
            case "fp": return 1 + ((float)receivedTileBuffs[0] / 100);
            case "acc": return 1 + ((float)receivedTileBuffs[1] / 100);
            case "eva": return 1 + ((float)receivedTileBuffs[2] / 100);
            case "rof": return 1 + ((float)receivedTileBuffs[3] / 100);
            case "crit": return 1 + ((float)receivedTileBuffs[4] / 100);
            case "skillcd": return 1 + ((float)receivedTileBuffs[5] / 100);
            default: return 1 + ((float)receivedTileBuffs[6] / 100);
        }
    }
    
    //Simulation Setters/Getters

    public void setBattleStats(List<Integer> stats){
        bs = new BattleStats(this, stats);
    }

    /*public void setSimFp(int simFp) {
        this.simFp = simFp;
    }
    public int getSimFp() {
        return simFp;
    }

    public void setSimAcc(int simAcc) {
        this.simAcc = simAcc;
    }
    public int getSimAcc() {
        return simAcc;
    }

    public void setSimEva(int simEva) {
        this.simEva = simEva;
    }
    public int getSimEva() {
        return simEva;
    }

    public void setSimRof(int simRof) {
        this.simRof = simRof;
    }
    public int getSimRof() {
        return simRof;
    }

    public void setSimCrit(int simCrit) {
        this.simCrit = simCrit;
    }
    public int getSimCrit() {
        return simCrit;
    }

    public void setSimCritDmg(int simCritDmg) {
        this.simCritDmg = simCritDmg;
    }
    public int getSimCritDmg() {
        return simCritDmg;
    }

    public void setSimAp(int simAp) {
        this.simAp = simAp;
    }
    public int getSimAp() {
        return simAp;
    }

    public void setSimRounds(int simRounds) {
        this.simRounds = simRounds;
    }
    public int getSimRounds() {
        return simRounds;
    }

    public void setSimCurrentRounds(int simCurrentRounds) {
        this.simCurrentRounds = simCurrentRounds;
    }
    public int getSimCurrentRounds() {
        return simCurrentRounds;
    }

    public void setSimArmour(int simArmour) {
        this.simArmour = simArmour;
    }
    public int getSimArmour() {
        return simArmour;
    }*/

    public void setCooldown(float simCooldown) {
        this.simCooldown = 1 - (simCooldown / 100);
    }
    public float getCooldown() {
        return simCooldown;
    }

    /*public void setHits(int hits){
        this.hits = hits;
    }

    public void setMisses(int misses){
        this.misses = misses;
    }

    public void setTotalShots(int totalShots){
        this.totalShots = totalShots;
    }

    public List<Integer> getShots(){
        shots.add(hits);
        shots.add(misses);
        shots.add(totalShots);

        return shots;
    }*/

    public void setTargets(int targets) {
        this.targets = targets;
    }
    public int getTargets() {
        return targets;
    }

    /*public void setBusyLinks(int busyLinks) {
        this.busyLinks = busyLinks;
    }*/
    public int getBusyLinks() {
        return busyLinks;
    }

    /*public void setSkillDamage(int skillDamage) {
        this.skillDamage = skillDamage;
    }
    public int getSkillDamage() {
        return skillDamage;
    }

    public void setNumOfAttacks(int numOfAttacks) {
        this.numOfAttacks = numOfAttacks;
    }
    public int getNumOfAttacks() {
        return numOfAttacks;
    }

    public void setNormalAttackTimer(int timeLeft){
        normalAttackTimer.setTimeLeft(timeLeft);
    }
    public int getNormalAttackTimer(){
        return normalAttackTimer.getTimeLeft();
    }

    public void setSkillTimer(int timeLeft){
        skillTimer.setTimeLeft(timeLeft);
    }
    public int getSkillTimer(){
        return skillTimer.getTimeLeft();
    }

    public Timer[] getTimers(){
        return new Timer[] {normalAttackTimer, skillTimer};
    }

    public Timer getTimer(String type){
        for(Timer timer : getTimers()){
            if(timer.getType().contains(type)) return timer;
        }
        return new Timer("null", 0);
    }

    public boolean findTimer(String type){
        for(Timer timer : getTimers()){
            if(timer.getType().equals(type)) return true;
        }
        return false;
    }

    public void addToActionQueue(Object object){
        actionQueue.add(object);
    }

    public Vector<Object> getActionQueue(){
        return actionQueue;
    }

    public void addToEffectQueue(Object object){
        effectQueue.add(object);
    }

    public Vector<Object> getEffectQueue(){
        return effectQueue;
    }*/

    public void addBuff(Buff buff){
        buffs.add(buff);
    }

    public List<Buff> getBuffs(){
        return buffs;
    }

//    public void addPassive(Passive passive){
//        passives.add(passive);
//    }
//
//    public List<Passive> getPassives(){
//        return passives;
//    }

    public Passive[] getPassives() {
        return passives;
    }

    public BattleStats BS(){
        return bs;
    }


//    if ('passives' in doll) {
//        doll.battle.passives = JSON.parse(JSON.stringify(doll.passives));
//        $.each(doll.battle.passives, (index, passive) => {
//            if ('interval' in passive) {
//                passive.startTime = 1;
//            }
//            passive.level = 'skill2passive' in passive ? doll.skill2level : doll.skilllevel;
//            passive.effects = getUsableSkillEffects(passive.effects);
//            $.each(passive.effects, (j, effect) => {
//                effect.level = passive.level;
//            });
//        });
//    } else {
//        doll.battle.passives = [];
//    }
//    doll.battle.effect_queue = [];
//    doll.battle.action_queue = [];
//    doll.battle.timers = [];
}
