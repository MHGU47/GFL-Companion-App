package com.shikikan.gflcompanionapp;

import android.content.Context;
import android.widget.ImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Doll {

    private int id, id_index, spritesheet_row, spritesheet_col, rarity, type, hp, fp, acc, eva, rof, crit,
            critdmg, ap, rounds, armour, growth_rating, construct_time, gridPosition, echelonPosition;

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

    public int getType() {
        return type;
    }

    public int getHp() {
        return hp;
    }

    public int getFp() {
        return fp;
    }

    public int getAcc() {
        return acc;
    }

    public int getEva() {
        return eva;
    }

    public int getRof() {
        return rof;
    }

    public int getCrit() {
        return crit;
    }

    public int getCritdmg() {
        return critdmg;
    }

    public int getAp() {
        return ap;
    }

    public int getRounds() {
        return rounds;
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
