package com.shikikan.gflcompanionapp;

import android.content.Context;
import android.widget.ImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Doll {

    private int id, id_index, spritesheet_row, spritesheet_col, rarity, type, hp, fp, acc, eva, rof, crit,
            critdmg, ap, rounds, armour, growth_rating, construct_time, gridPosition, echelonPosition,
            level, skillLevel;

    private JSONArray aliases;

    private String api_name, name, artist, voice, name_skill_1, icon_name_skill_1, tooltip_skill_1,
            tooltip_skill_2, tooltip_tiles, image;

    private Boolean mod, en_craftable, en_released;

    private JSONObject skill, tiles;

    private ImageView gridPosition_imageview;

    private int[]tilesFormation, tilesBuffs;

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
            growth_rating = (int) DollData.get("growth_rating");
            construct_time = (int) DollData.get("construct_time");
            en_craftable = (boolean) DollData.get("en_craftable");
            en_released = (boolean) DollData.get("en_released");
            artist = (String) DollData.get("artist");
            voice = (String) DollData.get("voice");
            name_skill_1 = (String) DollData.get("name_skill1");
            icon_name_skill_1 = (String) DollData.get("icon_name_skill1");
            tooltip_skill_1 = (String) DollData.get("tooltip_skill1");
            skill = (JSONObject) DollData.get("skill");
            tooltip_skill_2 = (String) DollData.get("tooltip_skill2");
            tooltip_tiles = (String) DollData.get("tooltip_tiles");
            tiles = (JSONObject) DollData.get("tiles");
            gridPosition = 0;
            gridPosition_imageview = null;
            echelonPosition = 0;


            image = "doll_" + DollData.get("id");


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
        this.growth_rating = newDoll.getGrowth_rating();
        this.construct_time = newDoll.getConstruct_time();
        this.gridPosition = newDoll.getGridPosition();
        this.echelonPosition = newDoll.getEchelonPosition();
        this.level = newDoll.getLevel();
        this.skillLevel = newDoll.getSkillLevel();
        this.aliases = newDoll.getAliases();
        this.api_name = newDoll.getApi_name();
        this.name = newDoll.getName();
        this.artist = newDoll.getArtist();
        this.voice = newDoll.getVoice();
        this.name_skill_1 = newDoll.getName_skill_1();
        this.icon_name_skill_1 = newDoll.getIcon_name_skill_1();
        this.tooltip_skill_1 = newDoll.getTooltip_skill_1();
        this.tooltip_skill_2 = newDoll.getTooltip_skill_2();
        this.tooltip_tiles = newDoll.getTooltip_tiles();
        this.image = newDoll.getImage();
        this.mod = newDoll.getMod();
        this.en_craftable = newDoll.getEn_craftable();
        this.en_released = newDoll.getEn_released();
        this.skill = newDoll.getSkill();
        this.tiles = newDoll.getRawTiles();
        this.gridPosition_imageview = newDoll.getGridImageView();
        this.tilesFormation = newDoll.getTilesFormation();
        this.tilesBuffs = getTilesBuffs();
    }

    public Doll(){
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
        growth_rating = 0;
        construct_time = 0;
        en_craftable = true;
        en_released = true;
        artist = "";
        voice = "";
        name_skill_1 = "";
        icon_name_skill_1 = "";
        tooltip_skill_1 = "";
        skill = null;
        tooltip_skill_2 = "";
        tooltip_tiles = "";
        tiles = null;
        gridPosition = 0;
        gridPosition_imageview = null;
        echelonPosition = 0;
        image = "adddoll";
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

    public void setSkillLevel(int skillLevel) {
        this.skillLevel = skillLevel;
    }
    public int getSkillLevel() {
        return skillLevel;
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

    public String getIcon_name_skill_1() {
        return icon_name_skill_1;
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

    public Boolean getMod() {
        return mod;
    }

    public Boolean getEn_craftable() {
        return en_craftable;
    }

    public Boolean getEn_released() {
        return en_released;
    }

    public JSONObject getSkill() {
        return skill;
    }

    public JSONObject getRawTiles() {
        return tiles;
    }

    public String getImage() {
        return image;
    }

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
    
//    public void setGridImageView(ImageView imageview) {
//
//    }

    public ImageView getGridImageView() {
        return gridPosition_imageview;
    }

    public void setEchelonPosition(int echelonPosition) {
        this.echelonPosition = echelonPosition;
    }

    public int getEchelonPosition() {
        return echelonPosition;
    }

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
}
