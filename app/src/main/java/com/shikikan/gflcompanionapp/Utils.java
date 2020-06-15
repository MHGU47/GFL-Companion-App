package com.shikikan.gflcompanionapp;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.Spinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class Utils {
    private int highlight = Color.argb(25, 0, 255, 255);
    float[] FAIRY_RARITY_SCALARS = {0.4f, 0.5f, 0.6f, 0.8f, 1};
    private Doll[] Dolls;
    private Equipment[] Equipment;
    private Map <Integer,Integer> positions = new HashMap<>();

    public Utils(){
        positions.put(32, 1);
        positions.put(33, 2);
        positions.put(34, 3);
        positions.put(22, 4);
        positions.put(23, 5);
        positions.put(24, 6);
        positions.put(12, 7);
        positions.put(13, 8);
        positions.put(14, 9);

        positions.put(1, 32);
        positions.put(2, 33);
        positions.put(3, 34);
        positions.put(4, 22);
        positions.put(5, 23);
        positions.put(6, 24);
        positions.put(7, 12);
        positions.put(8, 13);
        positions.put(9, 14);
    }

    private float[] TDollGrowthFactor(int level, String factor, boolean basic){
        if(level < 100){
            if(basic){
                switch(factor){
                    case "hp": return new float[] {55, 0.555f};
                    case "armour": return new float[] {2, 0.161f};
                    case "eva":
                    case "acc": return new float[] {5};
                    case "fp": return new float[] {16};
                    default: return new float[] {45};
                }
            }
            else{
                switch(factor){
                    case "eva":
                    case "acc": return new float[] {0.303f, 0};
                    case "fp": return new float[] {0.242f, 0};
                    default: return new float[] {0.181f, 0};
                }
            }
        }
        else {
            if(basic){
                switch(factor){
                    case "hp": return new float[] {96.283f, 0.138f};
                    case "armour": return new float[] {13.979f, 0.04f};
                    case "eva":
                    case "acc": return new float[] {5};
                    case "fp": return new float[] {16};
                    default: return new float[] {45};
                }
            }
            else{
                switch(factor){
                    case "eva":
                    case "acc": return new float[] {0.075f, 22.572f};
                    case "fp": return new float[] {0.06f, 18.018f};
                    default: return new float[] {0.022f, 15.741f};
                }
            }
        }
    }

    private float Scalars(int dollType, String scalar){
        switch(dollType){
            case 1:
                switch (scalar){
                    case "hp":
                    case "fp": return 0.6f;
                    case "rof": return 0.8f;
                    case "acc": return 1.2f;
                    case "eva": return 1.8f;
                    default: return 0;
                }
            case 2:
                switch (scalar){
                    case "hp":
                    case "eva": return 1.6f;
                    case "fp": return 0.6f;
                    case "rof": return 1.2f;
                    case "acc": return 0.3f;
                    default: return 0;
                }
            case 3:
                switch (scalar){
                    case "hp":
                    case "eva": return 0.8f;
                    case "fp": return 2.4f;
                    case "rof": return 0.5f;
                    case "acc": return 1.6f;
                    default: return 0;
                }
            case 4:
                if(!scalar.contains("armour")) return 1;
                else return 0;
            case 5:
                switch (scalar){
                    case "hp": return 1.5f;
                    case "fp": return 1.8f;
                    case "rof": return 1.6f;
                    case "acc":
                    case "eva": return 0.6f;
                    default: return 0;
                }
            default:
                switch (scalar){
                    case "hp": return 2;
                    case "fp": return 0.7f;
                    case "rof": return 0.4f;
                    case "acc":
                    case "eva": return 0.3f;
                    default: return 1;
                }
        }
    }

    public void levelchange(Doll doll){
        doll.setHp((int) (Math.ceil((TDollGrowthFactor(doll.getLevel(), "hp", true)[0] + ((doll.getLevel() - 1) * TDollGrowthFactor(doll.getLevel(), "hp", true)[1])) * Scalars(doll.getType(), "hp") * getDoll(doll.getID()).getHp() / 100)) * Links(doll.getLevel()));

        doll.setFp((int) Math.ceil(TDollGrowthFactor(doll.getLevel(), "fp",true)[0] * Scalars(doll.getType(),"fp") * getDoll(doll.getID()).getFp() / 100));
        doll.setFp((int) (doll.getFp() + (Math.ceil((TDollGrowthFactor(doll.getLevel(), "fp", false)[1] + ((doll.getLevel() - 1) * TDollGrowthFactor(doll.getLevel(), "fp", false)[0])) * Scalars(doll.getType(), "fp") * getDoll(doll.getID()).getFp() * doll.getGrowth_rating() / 100 / 100))));

        doll.setAcc((int) Math.ceil(TDollGrowthFactor(doll.getLevel(), "acc",true)[0] * Scalars(doll.getType(),"acc") * getDoll(doll.getID()).getAcc() / 100));
        doll.setAcc((int) (doll.getAcc() + (Math.ceil((TDollGrowthFactor(doll.getLevel(), "acc", false)[1] + ((doll.getLevel() - 1) * TDollGrowthFactor(doll.getLevel(), "acc", false)[0])) * Scalars(doll.getType(), "acc") * getDoll(doll.getID()).getAcc() * doll.getGrowth_rating() / 100 / 100))));

        doll.setEva((int) Math.ceil(TDollGrowthFactor(doll.getLevel(), "eva",true)[0] * Scalars(doll.getType(),"eva") * getDoll(doll.getID()).getEva() / 100));
        doll.setEva((int) (doll.getEva() + (Math.ceil((TDollGrowthFactor(doll.getLevel(), "eva", false)[1] + ((doll.getLevel() - 1) * TDollGrowthFactor(doll.getLevel(), "eva", false)[0])) * Scalars(doll.getType(), "eva") * getDoll(doll.getID()).getEva() * doll.getGrowth_rating() / 100 / 100))));

        doll.setRof((int) Math.ceil(TDollGrowthFactor(doll.getLevel(), "rof",true)[0] * Scalars(doll.getType(),"rof") * getDoll(doll.getID()).getRof() / 100));
        doll.setRof((int) (doll.getRof() + (Math.ceil((TDollGrowthFactor(doll.getLevel(), "rof", false)[1] + ((doll.getLevel() - 1) * TDollGrowthFactor(doll.getLevel(), "rof", false)[0])) * Scalars(doll.getType(), "rof") * getDoll(doll.getID()).getRof() * doll.getGrowth_rating() / 100 / 100))));

        doll.setArmour((int) Math.ceil((TDollGrowthFactor(doll.getLevel(), "armour", true)[0] + ((doll.getLevel() - 1) * TDollGrowthFactor(doll.getLevel(), "armour", true)[1])) * Scalars(doll.getType(), "armour") * getDoll(doll.getID()).getArmour() / 100));
    }

    public int Links(int level){
        switch(level){
            case 1: return 1;
            case 10:
            case 20: return 2;
            case 30:
            case 40:
            case 50:
            case 60: return 3;
            case 70:
            case 80: return 4;
            default: return 5;
        }
    }

    public int FairyGrowthFactor_Basic(String factor){
        switch (factor){
            case "fp": return 7;
            case "acc": return 25;
            case "eva": return 20;
            case "armour": return 5;
            default: return 10;
        }
    }

    public float FairyGrowthFactor_Grow(String factor){
        switch (factor){
            case "fp": return 0.076f;
            case "acc": return 0.252f;
            case "eva": return 0.202f;
            case "armour": return 0.05f;
            default: return 0.101f;
        }
    }

    public int IDtoInt(View view) {
        switch (view.getId()){
            case R.id.doll_1:
            case R.id.pos_1:
            case R.id.removeDoll_1: return 1;
            case R.id.doll_2:
            case R.id.pos_2:
            case R.id.removeDoll_2: return 2;
            case R.id.doll_3:
            case R.id.pos_3:
            case R.id.removeDoll_3: return 3;
            case R.id.doll_4:
            case R.id.pos_4:
            case R.id.removeDoll_4: return 4;
            case R.id.doll_5:
            case R.id.pos_5:
            case R.id.removeDoll_5: return 5;
            case R.id.pos_6: return 6;
            case R.id.pos_7: return 7;
            case R.id.pos_8: return 8;
            default: return 9;
        }
    }

    public int GridPositionToViewID(int position) {
        switch (position){
            case 1: return R.id.pos_1;
            case 2: return R.id.pos_2;
            case 3: return R.id.pos_3;
            case 4: return R.id.pos_4;
            case 5: return R.id.pos_5;
            case 6: return R.id.pos_6;
            case 7: return R.id.pos_7;
            case 8: return R.id.pos_8;
            default: return R.id.pos_9;
        }
    }

    public int LevelToSpinnerPosition(int level, boolean Skill) {
        if(Skill && level == 10) return 9;
        switch (level) {
            case 1:
                return 0;
            case 2:
            case 10:
                return 1;
            case 3:
            case 20:
                return 2;
            case 4:
            case 30:
                return 3;
            case 5:
            case 40:
                return 4;
            case 6:
            case 50:
                return 5;
            case 7:
            case 60:
                return 6;
            case 8:
            case 70:
                return 7;
            case 9:
            case 80:
                return 8;
            case 90:
                return 9;
            case 100:
                return 10;
            default:
                return 11;
        }
    }

    public void LoadDollData(Context context) {
        JSONArray DollData;
        try{
            DollData = new JSONArray(LoadJSON(context, "T-Doll"));
            Dolls = new Doll[DollData.length()];
            for(int i = 0; i < DollData.length(); i++) Dolls[i] = new Doll(DollData.getJSONObject(i));
        }
        catch (JSONException e){
            Log.d("tag","error",e);
        }
    }

    public void LoadEquipmentData(Context context) {
        JSONArray EquipmentData;
        try{
            EquipmentData = new JSONArray(LoadJSON(context, "Equipment"));
            Equipment = new Equipment[EquipmentData.length()];
            for(int i = 0; i < EquipmentData.length(); i++) Equipment[i] = new Equipment(EquipmentData.getJSONObject(i));
        }
        catch (JSONException e){
            Log.d("tag","error",e);
        }
    }

    private String LoadJSON(Context context, String JSONFile) {
        String json;
        InputStream is;

        switch(JSONFile){
            case "T-Doll":
                is = context.getResources().openRawResource(R.raw.dolls);
                break;
            //case "Equipment":
                // = context.getResources().openRawResource(R.raw.equips);
                //break;
            default:
                is = context.getResources().openRawResource(R.raw.equips);
                break;
        }

        try {

            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, StandardCharsets.UTF_8);
        }
        catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    public Doll getDoll(int dollID){
        return Dolls[dollID-1];
    }

    public Doll[] getAllDolls() {
        return Dolls;
    }

    public Equipment getEquipment(int equipmentID){
        return Equipment[equipmentID-1];
    }

    public Equipment[] getAllEquipment() {
        return Equipment;
    }

    public int EquipmentSlot(int type){
        switch(type) {
            case 1://Crit Scope
            case 2://Holo Sight
            case 3://Red Dot Sight
            case 4://Night Vision
            case 13://Suppressor
            case 20://AK-47/Type 56-1 EX Scope
            case 21://AK-47/Type 56-1 EX Scope
            case 22://AK-47/Type 56-1 EX Scope
            case 26://9A-91 EX Scope
            case 29://ST AR-15 Mod Equip
            case 32://Type 64 Mod Equip
            case 33://FN49 Mod Equip
            case 34://Kar98K EX Scope
            case 35://HK416 EX Scope
            case 38://Mosin-Nagant Mod Scope
            case 39://M1918 Mod Scope
            case 40://M1895 Mod Silencer
            case 41://MP-446 Mod Equip
            case 43://Clear EX Equip
            case 44://Fail EX Equip
            case 45://FAMAS EX Scope
            case 46://SAA Mod Equip
            case 47://Bren Mod Equip
            case 48://G3 Mod Equip
            case 49://UMP45 Mod Equip
            case 50://M4 SOPMOD II Mod Equip
            case 52://Sten MKII Mod Equip
            case 53://M14 Mod Equip
            case 54://G36 Mod Scope
            case 55://LWMMG Mod Scope
            case 56://MG4 EX Scope
            case 57://Stechkin EX Equip
            case 60://Micro Uzi Mod Scope
            case 61://Jill EX Equip
            case 64://Jill EX Equip
            case 65://Jill EX Equip
            case 75://MP5 Mod Scope
            case 76://UMP9 Mod Scope
            case 77://PPK EX Suppressor
            case 78://SIG-510 EX Equip
            case 80://BM29 EX Equip
            case 81://M3 EX Suppressor
            case 82://FG42 EX Scope
            case 83://Lee Enfield EX Scope
            case 84://Type 95/97 EX Scope
            case 85://Ingram EX Equip
            case 87://M9 EX Equip
            case 89://RFB EX Equip
            case 90://Type 100 EX Equip
            case 91://Beretta Model 38 Mod Scope
                return 1;
            case 5://AP Rounds
            case 6://HP Rounds
            case 7://Slug
            case 8://HV Ammo
            case 9://Buckshot
            case 14://Ammo Box
            case 16://Springfield EX AP
            case 17://ST AR-15 EX Rounds
            case 30://M1911 Mod Equip
            case 36://MG3 EX Ammo Box
            case 58://AS Val Mod Rounds
            case 59://StG44 Mod Rounds
            case 62://Jill EX Equip
            case 63://Jill EX Equip
            case 66://Jill EX Equip
            case 72://Dorothy EX Equip
            case 79://KS-23 EX Rounds
                return 2;
            case 10://Exo
            case 11://Armour Plate
            case 12://High Evasion Exo
            case 15://Cape
            case 18://M1918 EX Chip
            case 19://MP5 EX Exo
            case 23://Mosin-Nagant EX Chip
            case 24://M16A1 EX Armour
            case 25://UMP EX Exo
            case 27://Ameli EX Chip
            case 28://M4A1 Mod Equip
            case 31://IDW Mod Equip
            case 37://PTRD EX Cape
            case 42://SV-98 Mod Cape
            case 51://G41 EX Equip
            case 67://Jill EX Equip
            case 68://Jill EX Equip
            case 69://Dana EX Equip
            case 70://Stella EX Equip
            case 71://Sei EX Equip
            case 73://Dana EX Equip
            case 74://HK416 Mod Equip
            case 86://Tar-21 EX Equip
            case 88://RPD EX Equip
            case 92://Stechkin Mod Equip
                return 3;
            default:

                return 0;
        }
    }

//    public int[][] getDollTilesFormation(Doll doll){
//        int dollPosition = 0;
//        int[] tilePositions, tilePositions2, buffs = new int[15];
//        String temp;
//        String[] temp2;
//        JSONObject effects;
//        JSONObject tiles = doll.getRawTiles();
//        try{
//            dollPosition = (int) tiles.get("self");
//            temp = (String) tiles.get("target");
//            temp2 = temp.split(",");
//            tilePositions = new int[temp2.length];
//
//            //test
//            tilePositions2 = new int[temp2.length + 1];
//            tilePositions2[0] = dollPosition;
//
//            for(int i = 0; i < temp2.length; i++) tilePositions[i] = Integer.parseInt(temp2[i]);
//
//            //test
//            for(int i = 0; i < temp2.length; i++) tilePositions2[i + 1] = Integer.parseInt(temp2[i]);
//
//
//
//            effects = (JSONObject) tiles.get("effect");
//
//            buffs[0] = (int) tiles.get("target_type");
//            if(doll.getType() == 1){
//                buffs[1] = (int) ((JSONArray) effects.get("fp")).get(0);
//                buffs[2] = (int) ((JSONArray) effects.get("fp")).get(1);
//
//                buffs[3] = (int) ((JSONArray) effects.get("acc")).get(0);
//                buffs[4] = (int) ((JSONArray) effects.get("acc")).get(1);
//
//                buffs[5] = (int) ((JSONArray) effects.get("eva")).get(0);
//                buffs[6] = (int) ((JSONArray) effects.get("eva")).get(1);
//
//                buffs[7] = (int) ((JSONArray) effects.get("rof")).get(0);
//                buffs[8] = (int) ((JSONArray) effects.get("rof")).get(1);
//
//                buffs[9] = (int) ((JSONArray) effects.get("crit")).get(0);
//                buffs[10] = (int) ((JSONArray) effects.get("crit")).get(1);
//
//                buffs[11] = (int) ((JSONArray) effects.get("skillcd")).get(0);
//                buffs[12] = (int) ((JSONArray) effects.get("skillcd")).get(1);
//
//                buffs[13] = (int) ((JSONArray) effects.get("armor")).get(0);
//                buffs[14] = (int) ((JSONArray) effects.get("armor")).get(1);
//            }
//            else{
//                buffs[1] = (int) effects.get("fp");
//                buffs[2] = (int)  effects.get("fp");
//
//                buffs[3] = (int)  effects.get("acc");
//                buffs[4] = (int)  effects.get("acc");
//
//                buffs[5] = (int)  effects.get("eva");
//                buffs[6] = (int)  effects.get("eva");
//
//                buffs[7] = (int)  effects.get("rof");
//                buffs[8] = (int)  effects.get("rof");
//
//                buffs[9] = (int)  effects.get("crit");
//                buffs[10] = (int)  effects.get("crit");
//
//                buffs[11] = (int)  effects.get("skillcd");
//                buffs[12] = (int)  effects.get("skillcd");
//
//                buffs[13] = (int)  effects.get("armor");
//                buffs[14] = (int)  effects.get("armor");
//
//                //TODO 04/06/2020: Make this array smaller at some point
//            }
//
//        }
//        catch (JSONException e){
//            Log.d("tag","error",e);
//            tilePositions = new int[0];
//            tilePositions2 = new int[0];
//            buffs = new int[0];
//        }
//
//        //int[] t = new int[tilePositions.length + 1];
//        //t[0] = dollPosition;
//        //System.arraycopy(t, 1, tilePositions, 0, tilePositions.length);
//
//        //return new int[][]{setUpDollTilesFormation(dollPosition, tilePositions), buffs};
//        return tilePositions2;
//    }

    public int[][] setUpDollTilesFormation(Doll doll){
        int dollPosition;
        int[] tilePositions, buffs;
        String temp;
        String[] temp2;
        JSONObject effects;
        JSONObject tiles = doll.getRawTiles();
        try{
            dollPosition = (int) tiles.get("self");
            temp = (String) tiles.get("target");
            temp2 = temp.split(",");

            tilePositions = new int[temp2.length + 1];
            tilePositions[0] = dollPosition;

            //test
            for(int i = 0; i < temp2.length; i++) tilePositions[i + 1] = Integer.parseInt(temp2[i]);
            //TODO: Check these 'test' lines of code

            effects = (JSONObject) tiles.get("effect");

            if(doll.getType() == 1){
                buffs = new int[15];
                buffs[0] = (int) tiles.get("target_type");
                buffs[1] = (int) ((JSONArray) effects.get("fp")).get(0);
                buffs[2] = (int) ((JSONArray) effects.get("fp")).get(1);

                buffs[3] = (int) ((JSONArray) effects.get("acc")).get(0);
                buffs[4] = (int) ((JSONArray) effects.get("acc")).get(1);

                buffs[5] = (int) ((JSONArray) effects.get("eva")).get(0);
                buffs[6] = (int) ((JSONArray) effects.get("eva")).get(1);

                buffs[7] = (int) ((JSONArray) effects.get("rof")).get(0);
                buffs[8] = (int) ((JSONArray) effects.get("rof")).get(1);

                buffs[9] = (int) ((JSONArray) effects.get("crit")).get(0);
                buffs[10] = (int) ((JSONArray) effects.get("crit")).get(1);

                buffs[11] = (int) ((JSONArray) effects.get("skillcd")).get(0);
                buffs[12] = (int) ((JSONArray) effects.get("skillcd")).get(1);

                buffs[13] = (int) ((JSONArray) effects.get("armor")).get(0);
                buffs[14] = (int) ((JSONArray) effects.get("armor")).get(1);
            }
            else{
                buffs = new int[8];
                buffs[0] = (int) tiles.get("target_type");
                buffs[1] = (int) effects.get("fp");

                buffs[2] = (int)  effects.get("acc");
                buffs[3] = (int)  effects.get("eva");

                buffs[4] = (int)  effects.get("rof");
                buffs[5] = (int)  effects.get("crit");

                buffs[6] = (int)  effects.get("skillcd");
                buffs[7] = (int)  effects.get("armor");
            }
        }
        catch (JSONException e){
            Log.d("tag","error",e);
            tilePositions = new int[0];
            buffs = new int[0];
        }

        return new int [][]{tilePositions, buffs};
    }

    public int[] getDollTilesFormation(Doll doll){
        //tileFormation array format is as follows: 0 = T-Doll position, 1, 2... = the position of the
        //tiles relative to the T-Doll.
        int[] tileFormation = new int[doll.getTilesFormation().length - 1];

        //Find the difference by subtracting the current T-Doll position from the original tile
        //formation position saved with their tile buff formation.
        int shift = positions.get(doll.getGridPosition()) - doll.getTilesFormation()[0];

        //This converts the the tile positions from being the values in the JSON file into numpad format
        //tile positions relative to the T-Doll's current position.
        for(int i = 0; i < doll.getTilesFormation().length - 1; i++) {
            try{//Adding the buff tile positions to the T-Doll position found, both from the JSON file,
                //along with the difference between the JSON T-Doll position and the current position.
                int position = positions.get(shift + doll.getTilesFormation()[i + 1] + doll.getTilesFormation()[0]);
                tileFormation[i] = position;

                //If the converted position of the tile ends up being off the grid, set that tile's
                //position to 0. If this value is used to highlight a tile, the code in UI.java will
                //catch it and not use this item in the array.
                if(position > 9 || position < 1) tileFormation[i] = 0;
            }
            catch(Exception e){
                tileFormation[i] = 0;
            }

        }

        return tileFormation;
    }

    public int getHighlight(){
        return highlight;
    }

    public void checkTiles(){
//        int[] tiles;
//        for(Doll doll : e.getAllDolls()){
//            if(doll.getID() != 0){
//                tiles = u.getDollTilesFormation(doll);
//                //Doll[] temp = e.getAllDolls();
//                for(Doll od : e.getAllDolls()){
//                    for (int tile : tiles) {
//                        if (od.getGridPosition() == tile)
//                            findViewById(u.GridPositionToViewID(od.getGridPosition())).setBackgroundColor(Color.BLACK);
//                    }
//                }
//            }
//        }
    }
}
