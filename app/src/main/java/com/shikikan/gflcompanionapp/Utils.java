package com.shikikan.gflcompanionapp;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.View;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Utils {
    private int highlight = Color.argb(25, 0, 255, 255);
    float[] FAIRY_RARITY_SCALARS = {0.4f, 0.5f, 0.6f, 0.8f, 1};
    private Doll[] Dolls;
    private Equipment[] Equipment;
    private Map <Integer,Integer> positions = new HashMap<>();
    private Map<Integer, int[]> SPEQ = new HashMap<Integer, int[]>();
    private int[] SPEQTDolls;

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

        SPEQ.put(3, new int[]{87});//M9 - Slot 1
        SPEQ.put(7, new int[]{57});//Stechkin - Slot 1
        SPEQ.put(10, new int[]{77});//PPK - Slot 1
        SPEQ.put(17, new int[]{81});//M3 - Slot 3
        SPEQ.put(18, new int[]{84});//Ingram - Slot 3
        SPEQ.put(26, new int[]{19});//MP5 - Slot 1
        SPEQ.put(35, new int[]{16});//Springfield - Slot 1
        SPEQ.put(38, new int[]{23});//Mosin-Nagant - Slot 3
        SPEQ.put(41, new int[]{37});//PTRD - Slot 3
        SPEQ.put(44, new int[]{34});//Kar98K - Slot 2
        SPEQ.put(48, new int[]{83});//Lee Enfield - Slot 2
        SPEQ.put(50, new int[]{80});//BM59 - Slot 2
        SPEQ.put(52, new int[]{24});//M16 - Slot 3
        SPEQ.put(55, new int[]{17});//ST AR-15 - Slot 2
        SPEQ.put(56, new int[]{20, 21, 22});//AK-47 - Slot 1
        SPEQ.put(60, new int[]{51});//G41 - Slot 3
        SPEQ.put(63, new int[]{63});//416 - Slot 1
        SPEQ.put(64, new int[]{20, 21, 22});//Type56-1 - Slot 1
        SPEQ.put(66, new int[]{45});//FAMAS - Slot 1
        SPEQ.put(69, new int[]{86});//TAR21 - Slot 3
        SPEQ.put(71, new int[]{78});//SIG510 - Slot 1
        SPEQ.put(72, new int[]{18});//M1918 - Slot 3
        SPEQ.put(79, new int[]{88});//RPD - Slot 3
        SPEQ.put(83, new int[]{36});//MG3
        SPEQ.put(96, new int[]{25});//UMP9 - Slot 1
        SPEQ.put(97, new int[]{25});//UMP40 - Slot 1
        SPEQ.put(98, new int[]{25});//UMP45 - Slot 1
        SPEQ.put(105, new int[]{82});//FG42 - Slot 2
        SPEQ.put(113, new int[]{26});//9A-91 - Slot 1
        SPEQ.put(120, new int[]{56});//MG4 - Slot 2
        SPEQ.put(124, new int[]{84});//Type95 - Slot 1
        SPEQ.put(125, new int[]{84});//Type97 - Slot 1
        SPEQ.put(133, new int[]{5});//6P62 - Slot 2
        SPEQ.put(153, new int[]{79});//KS23 - Slot 2
        SPEQ.put(167, new int[]{89});//RFB - Slot 1
        SPEQ.put(178, new int[]{5});//Contender - Slot 2
        SPEQ.put(180, new int[]{27});//Ameli - Slot 3
        SPEQ.put(208, new int[]{5});//C-MS - Slot 2
        SPEQ.put(221, new int[]{90});//Type 100 - Slot 3
        SPEQ.put(249, new int[]{43});//El CLEAR - Slot 1
        SPEQ.put(250, new int[]{44});//El FAIL - Slot 1
        SPEQ.put(251, new int[]{46});//SAA Mod 3 - Slot 1
        SPEQ.put(252, new int[]{30});//M1911 Mod 3 - Slot 2
        SPEQ.put(253, new int[]{40});//M1895 Mod 3 - Slot 1
        SPEQ.put(254, new int[]{52});//STEN Mod 3 - Slot 3
        SPEQ.put(255, new int[]{53});//M14 - Slot 2
        SPEQ.put(256, new int[]{38, 23});//Mosin-Nagant Mod 3 - Slot 2, Slot 3
        SPEQ.put(257, new int[]{41});//SV-98 Mod 3 - Slot 3
        SPEQ.put(258, new int[]{33});//FN-49 Mod 3 - Slot 2
        SPEQ.put(259, new int[]{28});//M4A1 Mod 3 - Slot 3
        SPEQ.put(260, new int[]{92, 24});//SOPMOD II Mod 3 - Slot 1
        SPEQ.put(261, new int[]{29, 17});//ST AR-15 - Slot 1, Slot 2
        SPEQ.put(262, new int[]{48});//G3 Mod 3 - Slot 1
        SPEQ.put(263, new int[]{54});//G36 Mod 3 - Slot 1
        SPEQ.put(264, new int[]{39, 18});//M1918 Mod 3 - Slot 2, Slot 3
        SPEQ.put(265, new int[]{55});//LWMMG Mod 3 - Slot 2
        SPEQ.put(266, new int[]{47});//Bren Mod 3 - Slot 2
        SPEQ.put(267, new int[]{41});//MP446 Mod 3 - Slot 1
        SPEQ.put(268, new int[]{31});//IDW Mod 3 - Slot 1
        SPEQ.put(269, new int[]{32});//Type64 Mod 3 - Slot 3
        SPEQ.put(270, new int[]{25, 49});//UMP45 Mod 3 - Slot 1, Slot 3
        SPEQ.put(289, new int[]{58});//AS Val Mod 3 - Slot 2
        SPEQ.put(290, new int[]{59});//StG44 Mod 2 - Slot 2
        SPEQ.put(291, new int[]{60});//Micro Uzi Mod 3 - Slot 3
        SPEQ.put(292, new int[]{69});//Dana - Slot 1
        SPEQ.put(293, new int[]{73});//Alma - Slot 3
        SPEQ.put(294, new int[]{70});//Stella - Slot 3
        SPEQ.put(295, new int[]{71});//Sei - Slot 3
        SPEQ.put(296, new int[]{61, 62, 63, 64, 65, 66, 67, 68});//Jill - All Slots
        SPEQ.put(297, new int[]{72});//Dorothy - Slot 2
        SPEQ.put(303, new int[]{35, 74});//416 Mod 3 - Slot 1, Slot 3
        SPEQ.put(304, new int[]{35, 75});//MP5 Mod 3 - Slot 1, Slot 3
        SPEQ.put(305, new int[]{25, 76});//UMP9 Mod 3 - Slot 1, Slot 3
        SPEQ.put(329, new int[]{91});//MAB3 Mod 3 - Slot 3
        SPEQ.put(330, new int[]{57, 92});//Stechkin Mod 3 - Slot 1, Slot 3







        SPEQTDolls = new int[] {
                3, //M9
                7, //Stechkin
                10, //PPK
                17, //M3
                18, //Ingram
                26, //MP5
                35, //Springfield
                38, //Mosin-Nagant
                41, //PTRD
                44, //Kar98k
                48, //Lee Enfield
                50, //BM59
                56, //AK-47
                60, //G41
                63, //416
                64, //Type56-1
                66, //FAMAS
                69, //TAR-21
                71, //SIG510
                72, //M1918
                79, //RPD
                83, //MG3
                96, //UMP9
                97, //UMP40
                98, //UMP45
                105, //FG42
                113, //9A-91
                120, //MG4
                124, //Type95
                125, //Type97
                133, //6P62
                153, //KS23
                167, //RFB
                178, //Contender
                180, //Ameli
                208, //C-MS
                221, //Type100
                249, //CLEAR
                250, //FAIL
                251, //SAA Mod 3
                252, //M1911 Mod 3
                253, //M1895 Mod 3
                254, //STEN Mod 3
                255, //M14 Mod 3
                256, //Mosin-Nagant Mod 3
                257, //SV-98 Mod 3
                258, //FN-49 Mod 3
                259, //M4A1 Mod 3
                262, //G3 Mod 3
                263, //G36 Mod 3
                264, //M1918
                265, //LWMMG Mod 3
                266, //Bren Mod 3
                267, //MP446 Mod 3
                268, //IDW Mod 3
                269, //Type64 Mod 3
                270, //UMP45 Mod 3
                289, //AS Val Mod 3
                290, //StG44 Mod 3
                291, //Micro Uzi Mod 3
                292, //Dana
                293, //Alma
                294, //Stella
                295, //Sei
                296, //Jill
                297, //Dorothy
                303, //416 Mod 3
                304, //MP5 Mod 3
                305, //UMP9 Mod 3
                329, //mab38mod
                330,//StechkinMod
        };
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

    public void levelChange(Echelon e){
        for(Doll doll : e.getAllDolls()){
            if(doll.getID() != 0){
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

                doll.removeEquipment(0);
            }
        }
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
            case R.id.removeDoll_1:
            case R.id.equipSlot_1:
            case R.id.removeEquipSlot_1: return 1;
            case R.id.doll_2:
            case R.id.pos_2:
            case R.id.removeDoll_2:
            case R.id.equipSlot_2:
            case R.id.removeEquipSlot_2: return 2;
            case R.id.doll_3:
            case R.id.pos_3:
            case R.id.removeDoll_3:
            case R.id.equipSlot_3:
            case R.id.removeEquipSlot_3: return 3;
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

    public int LevelToSpinnerPosition(int level, boolean TDollLevel) {
        if(!TDollLevel && level == 10) return 9;
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

    public int AffectionToSpinnerPosition(float affection){
        if(affection == -0.05f) return 0;
        else if(affection == 0) return 1;
        else if(affection == 0.05f) return 2;
        else return 3;
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

    public int EquipmentSlot(int type, int dollType){
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
                switch(dollType) {
                    case 2:
                    case 6: return 3;
                    case 3:
                    case 5: return 2;
                    default: return 1;
                }
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
                switch(dollType) {
                    case 3:
                    case 5: return 1;
                    default: return 2;
                }
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
                switch(dollType) {
                    case 2:
                    case 6: return 1;
                    default: return 3;
                }
            default: return 0;
        }
    }

    public boolean ValidEquip(Doll doll, int equipType){
        switch(doll.getType()){
            case 1: return (SPEQCheck(doll, equipType) || equipType == 4) || (equipType == 6) || (equipType == 10) || (equipType == 12) || (equipType == 13);
            case 2: return (SPEQCheck(doll, equipType) || equipType < 5) || (equipType == 6) || (equipType == 10) || (equipType == 12) || (equipType == 13);
            case 3: return (SPEQCheck(doll, equipType) || equipType < 4) || (equipType == 5) || (equipType == 15);
            case 4: return (SPEQCheck(doll, equipType) || equipType < 5) || (equipType == 8) || (equipType == 10) || (equipType == 12);
            case 5: return (SPEQCheck(doll, equipType) || equipType < 4) || (equipType == 5) || (equipType == 14);
            default: return (SPEQCheck(doll, equipType) || equipType < 4) || (equipType == 7) || (equipType == 9) || (equipType == 11);
        }
    }

    private boolean SPEQCheck(Doll doll, int equipType){
        if(SPEQ.containsKey(doll.getID())){
            List<Integer> speq = new ArrayList<>();
            for(int ID : Objects.requireNonNull(SPEQ.get(doll.getID()))) speq.add(ID);
            return speq.contains(equipType);
        }
        return false;
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

    public float getCapRof(Doll doll, float preBattleRof){
        preBattleRof = (float) Math.floor(preBattleRof);
        int cap;
        if(doll.getType() == 6) cap = 60;
        else if (doll.getType() == 5) return (int) Math.floor(Math.min(1000, Math.max(1, doll.getRof())));
        else cap = 120;

        float cap_ = Math.min(getEffectiveRof(cap), getEffectiveRof(preBattleRof));
        return (float) Math.floor(Math.min(cap_, Math.max(15, preBattleRof)));
    }

    public float getCapCrit(float preBattleCrit){
        return Math.min(100, Math.max(0, preBattleCrit));
    }

    public float getFrames(float originalRof){
        return (float) Math.floor(1500 / originalRof);
    }

    public float getEffectiveRof(float originalRof){
        float frames = getFrames(originalRof);
        return (float) Math.ceil(1500 / (frames + 0.9999));
    }

    public Effect[] getUsableSkillEffects(Effect[] effects){
        Effect[] temp = new Effect[effects.length];

        for(int i = 0; i < temp.length; i++){
            if(effects[i].getRequirements() == null){
                temp[i] = effects[i];
                continue;
            }
            boolean valid = true;
            for (String condition : effects[i].getRequirements()) {
                switch (condition) {
                    case "night":
                        valid = valid && (/*isNight == */effects[i].isNight());
                        break;
                    case "armored":
                        valid = valid && (/*enemyArmor > 0 == */effects[i].isArmoured());
                        break;
                    case "boss":
                        valid = valid && (/*isBoss == */effects[i].isBoss());
                        break;
                }
            }
            if(valid) temp[i] = effects[i];
        }
        return temp;
    }

    /***
     * Returns the number of enemy links hit by an effect
     * based on the effect's radius and the number of
     * enemies on field. Each enemy is assumed to have
     * 5 links unless isBoss is true.
     */
    public int getNumEnemyLinksHit(float radius, int enemyCount, boolean isBoss){
        int linksPerEnemy = isBoss ? 1 : 5;
        int maxEnemiesHit;

        if (radius <= 0)
            maxEnemiesHit = enemyCount;
        else if (radius <= 1)
            maxEnemiesHit = 1;
        else if (radius <= 1.5)
            maxEnemiesHit = 5;
        else if (radius <= 2)
            maxEnemiesHit = 9;
        else if (radius <= 2.5)
            maxEnemiesHit = 11;
        else if (radius <= 3)
            maxEnemiesHit = 15;
        else if (radius <= 3.5)
            maxEnemiesHit = 17;
        else if (radius <= 4)
            maxEnemiesHit = 21;
        else
            maxEnemiesHit = enemyCount;

        return Math.min(maxEnemiesHit, enemyCount) * linksPerEnemy;
    }
}
