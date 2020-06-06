package com.shikikan.gflcompanionapp;

import android.content.Context;
import android.util.Log;
import android.view.View;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class Utils {
    float[] FAIRY_RARITY_SCALARS = {0.4f, 0.5f, 0.6f, 0.8f, 1};
    private Doll[] Doll;
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
            case R.id.pos_1: return 1;
            case R.id.doll_2:
            case R.id.pos_2: return 2;
            case R.id.doll_3:
            case R.id.pos_3: return 3;
            case R.id.doll_4:
            case R.id.pos_4: return 4;
            case R.id.doll_5:
            case R.id.pos_5: return 5;
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

    public void LoadDollData(Context context) {
        JSONArray DollData;
        try{
            DollData = new JSONArray(LoadJSON(context));
            Doll = new Doll[DollData.length()];
            for(int i = 0; i < DollData.length(); i++) Doll[i] = new Doll(DollData.getJSONObject(i));
        }
        catch (JSONException e){
            Log.d("tag","error",e);
        }
    }

    private String LoadJSON(Context context) {
        String json;
        try {
            InputStream is = context.getResources().openRawResource(R.raw.dolls);
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
        return Doll[dollID-1];
    }

    public Doll[] getAllDolls() {
        return Doll;
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
        int[] tilePositions, buffs = new int[15];
        String temp;
        String[] temp2;
        JSONObject effects;
        JSONObject tiles = doll.getRawTiles();
        try{
            dollPosition = (int) tiles.get("self");
            temp = (String) tiles.get("target");
            temp2 = temp.split(",");
            //tilePositions = new int[temp2.length];

            //test
            tilePositions = new int[temp2.length + 1];
            tilePositions[0] = dollPosition;

            //test
            for(int i = 0; i < temp2.length; i++) tilePositions[i + 1] = Integer.parseInt(temp2[i]);
            //TODO: Check these 'test' lines of code



            effects = (JSONObject) tiles.get("effect");

            buffs[0] = (int) tiles.get("target_type");
            if(doll.getType() == 1){
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
                buffs[1] = (int) effects.get("fp");
                buffs[2] = (int)  effects.get("fp");

                buffs[3] = (int)  effects.get("acc");
                buffs[4] = (int)  effects.get("acc");

                buffs[5] = (int)  effects.get("eva");
                buffs[6] = (int)  effects.get("eva");

                buffs[7] = (int)  effects.get("rof");
                buffs[8] = (int)  effects.get("rof");

                buffs[9] = (int)  effects.get("crit");
                buffs[10] = (int)  effects.get("crit");

                buffs[11] = (int)  effects.get("skillcd");
                buffs[12] = (int)  effects.get("skillcd");

                buffs[13] = (int)  effects.get("armor");
                buffs[14] = (int)  effects.get("armor");

                //TODO 04/06/2020: Make this array smaller at some point
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

    public void checkTiles(){

    }
}
