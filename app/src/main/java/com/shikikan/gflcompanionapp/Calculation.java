package com.shikikan.gflcompanionapp;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Calculation {
    private Utils u;
    private Echelon echelon;

    private int battleLength = 20, walkTime = 0;//Will be set via the user determining how long the
    //echelon will walk for.

    public Calculation(Utils u) {
        this.u = u;
    }

    private void CalculateTileBuffs(Echelon e) {
        int[] tiles;
        //Cycle through every T-Doll in the echelon.
        for (Doll doll : e.getAllDolls()) {
            //If they have an ID, meaning they aren't a dummy T-Doll...
            if (doll.getID() != 0) {
                //...get their tile formation.
                tiles = u.getDollTilesFormation(doll);
                //Cycle through the echelon again, this time to find the other T-Dolls.
                for (Doll od : e.getAllDolls()) {
                    //Cycle through all the tiles obtained from the previous T-Doll.
                    for (int tile : tiles) {
                        //If one of the other T-Dolls in the echelon is on the currently iterated tile,...
                        if (od.getGridPosition() == tile)
                            //...they aren't a dummy T-Doll and they can be buffed by the tile...
                            if ((od.getType() == doll.getTilesBuffs()[0] || doll.getTilesBuffs()[0] == 0) && od.getID() != 0) {
                                //...highlight the tile.
                                int[] receiver = od.getReceivedTileBuffs(), buffer = doll.getTilesBuffs(), buffs = new int[7];
                                //Key - Buffer: 0 = Target, 1 = FP, 2 = ACC, 3 = EVA, 4 = ROF, 5 = CRIT, 6 = SKILLCD, 7 = ARMOUR
                                //Key - Buffer (HG): 0 = Target, 1 = FP-MIN, 2 = FP-MAX, 3 = ACC-MIN, 4 = ACC-MAX, 5 = EVA-MIN, 6 = EVA-MAX, 7 = ROF-MIN
                                //                   8 = ROF-MAX, 9 = CRIT-MIN, 10 = CRIT-MAX, 11 = SKILLCD-MIN, 12 = SKILLCD-MAX, 13 = ARMOUR-MIN, 14 = ARMOUR-MAX

                                //Key - Receiver: 0 = FP, 1 = ACC, 2 = EVA, 3 = ROF, 4 = CRIT, 5 = SKILLCD, 6 = ARMOUR
                                if (doll.getType() == 1) {
                                    buffs[0] = (receiver[0] += Math.floor(buffer[1] + (((buffer[2] - buffer[1]) / 4f) * (u.Links(doll.getLevel()) - 1))));
                                    buffs[1] = (receiver[1] += Math.floor(buffer[3] + (((buffer[4] - buffer[3]) / 4f) * (u.Links(doll.getLevel()) - 1))));
                                    buffs[2] = (receiver[2] += Math.floor(buffer[5] + (((buffer[6] - buffer[5]) / 4f) * (u.Links(doll.getLevel()) - 1))));
                                    buffs[3] = (receiver[3] += Math.floor(buffer[7] + (((buffer[8] - buffer[7]) / 4f) * (u.Links(doll.getLevel()) - 1))));
                                    buffs[4] = (receiver[4] += Math.floor(buffer[9] + (((buffer[10] - buffer[9]) / 4f) * (u.Links(doll.getLevel()) - 1))));
                                    buffs[5] = (receiver[5] += Math.floor(buffer[11] + (((buffer[12] - buffer[11]) / 4f) * (u.Links(doll.getLevel()) - 1))));
                                    buffs[6] = (receiver[6] += Math.floor(buffer[13] + (((buffer[14] - buffer[13]) / 4f) * (u.Links(doll.getLevel()) - 1))));
                                } else {
                                    buffs[0] = receiver[0] += buffer[1];
                                    buffs[1] = receiver[1] += buffer[2];
                                    buffs[2] = receiver[2] += buffer[3];
                                    buffs[3] = receiver[3] += buffer[4];
                                    buffs[4] = receiver[4] += buffer[5];
                                    buffs[5] = receiver[5] += buffer[6];
                                    buffs[6] = receiver[6] += buffer[7];
                                }

                                if (buffs[5] > 30) buffs[5] = 30;

                                od.setReceivedTileBuffs(buffs);
                            }
                    }
                }
            }
        }
    }

    private void CalculateEquipmentBuffs(Echelon e) {
        int[] buffs;
        //Equipment[] equips;
        for (Doll doll : e.getAllDolls()) {
            if (doll.getID() != 0) {
                buffs = doll.getEquipmentBuffs();
                //equips = doll.getAllEquipment();
                for (Equipment equipment : doll.getAllEquipment()) {
                    buffs[0] += Math.floor((equipment.getLevelBonus("fp") / 10000f * equipment.getLevel() + 1) * equipment.getFp());
                    buffs[1] += Math.floor((equipment.getLevelBonus("acc") / 10000f * equipment.getLevel() + 1) * equipment.getAcc());
                    buffs[2] += Math.floor((equipment.getLevelBonus("eva") / 10000f * equipment.getLevel() + 1) * equipment.getEva());
                    buffs[4] += Math.floor((equipment.getLevelBonus("rof") / 10000f * equipment.getLevel() + 1) * equipment.getRof());
                    buffs[5] += Math.floor((equipment.getLevelBonus("critdmg") / 10000f * equipment.getLevel() + 1) * equipment.getCritdmg());
                    buffs[6] += Math.floor((equipment.getLevelBonus("crit") / 10000f * equipment.getLevel() + 1) * equipment.getCrit());
                    buffs[7] += Math.floor((equipment.getLevelBonus("ap") / 10000f * equipment.getLevel() + 1) * equipment.getAp());
                    buffs[8] += Math.floor((equipment.getLevelBonus("armour") / 10000f * equipment.getLevel() + 1) * equipment.getArmour());
                    buffs[9] += Math.floor((equipment.getLevelBonus("nightview") / 10000f * equipment.getLevel() + 1) * equipment.getNightview());
                    buffs[10] += Math.floor((equipment.getLevelBonus("rounds") / 10000f * equipment.getLevel() + 1) * equipment.getRounds());
                }
                doll.setEquipmentBuffs(buffs);
            }
        }
    }

    public void CalculateStats(Echelon e) {
        //echelon = new Echelon(e);
        u.levelChange(e);
        ResetBuffs(e);
        CalculateTileBuffs(e);
        CalculateEquipmentBuffs(e);
        PrepBattleEchelon(e);
    }

    private int GetFP(Doll doll) {
        double fp = doll.getFp() + (Math.signum(doll.getAffection()) * Math.ceil(Math.abs(doll.getFp() * doll.getAffection())));
        //return (float)(fp + doll.getEquipmentBuff("fp")) * doll.getTileBuff("fp");
        return (int) Math.ceil((fp + doll.getEquipmentBuff("fp")) * doll.getTileBuff("fp"));
    }

    private int GetAcc(Doll doll) {
        double acc = doll.getAcc() + (Math.signum(doll.getAffection()) * Math.ceil(Math.abs(doll.getAcc() * doll.getAffection())));
        //return (float)(acc + doll.getEquipmentBuff("acc")) * doll.getTileBuff("acc");
        return (int) Math.ceil((acc + doll.getEquipmentBuff("acc")) * doll.getTileBuff("acc"));
    }

    private int GetEva(Doll doll) {
        double eva = doll.getEva() + (Math.signum(doll.getAffection()) * Math.ceil(Math.abs(doll.getEva() * doll.getAffection())));
        //return (float)(eva + doll.getEquipmentBuff("eva")) * doll.getTileBuff("eva");
        return (int) Math.ceil((eva + doll.getEquipmentBuff("eva")) * doll.getTileBuff("eva"));
    }

    private int GetRof(Doll doll) {
        return (int) Math.ceil((doll.getRof() + doll.getEquipmentBuff("rof")) * doll.getTileBuff("rof"));
    }

    private int GetCritDmg(Doll doll) {
        return (int) (100 + ((doll.getCritdmg() + doll.getEquipmentBuff("critdmg")) * doll.getTileBuff("critdmg")));
    }

    private int GetCrit(Doll doll) {
        return (int) Math.ceil((doll.getCrit() + doll.getEquipmentBuff("crit")) * doll.getTileBuff("crit"));
    }

    private int GetAP(Doll doll) {
        return (int) ((doll.getAp() + doll.getEquipmentBuff("ap")) * doll.getTileBuff("ap"));
    }

    private int GetArmour(Doll doll) {
        return (int) ((doll.getArmour() + doll.getEquipmentBuff("armour")) * doll.getTileBuff("armour"));
    }

    public int GetNightview(Doll doll) {
        return (int) ((doll.getEquipmentBuff("nightview")));
    }

    public int GetRounds(Doll doll) {
        return (int) (doll.getRounds() + doll.getEquipmentBuff("rounds"));
    }

    public int[] getStats(Doll doll){
        int[] temp = new int[11];

        temp[0] = doll.getHp();
        temp[1] = GetFP(doll);
        temp[2] = GetAcc(doll);
        temp[3] = GetEva(doll);
        temp[4] = GetRof(doll);
        temp[5] = GetCritDmg(doll);
        temp[6] = GetCrit(doll);
        temp[7] = GetRounds(doll);
        temp[8] = GetArmour(doll);
        temp[9] = GetAP(doll);
        temp[10] = GetNightview(doll);

        return temp;
    }

    private void ResetBuffs(Echelon e) {
        //for(int i = 0; i < e.getAllDolls().length; i++) e.getDoll(i).setReceivedTileBuffs(new int[7]);
        for (Doll doll : e.getAllDolls()) {
            doll.setEquipmentBuffs(new int[11]);
            doll.setReceivedTileBuffs(new int[7]);
        }
    }


    private void PrepBattleEchelon(Echelon e) {
        for (Doll doll : e.getAllDolls()) {
            if (doll.getID() != 0) {
                //doll.setSimFp(getFP(e.getDoll(doll.getEchelonPosition() - 1)));
                //doll.setSimAcc(getAcc(e.getDoll(doll.getEchelonPosition() - 1)));
                //doll.setSimEva(getEva(e.getDoll(doll.getEchelonPosition() - 1)));
                //doll.setSimRof(getRof(e.getDoll(doll.getEchelonPosition() - 1)));
                //doll.setSimCritDmg(getCritDmg(e.getDoll(doll.getEchelonPosition() - 1)));
                //doll.setSimCrit(getCrit(e.getDoll(doll.getEchelonPosition() - 1)));
                //doll.setSimAp(getAP(e.getDoll(doll.getEchelonPosition() - 1)));
                //doll.setSimArmour(getArmour(e.getDoll(doll.getEchelonPosition() - 1)));
                //NightView
                //doll.setSimRounds(getRounds(e.getDoll(doll.getEchelonPosition() - 1)));
                //doll.setSimFp(getFP(doll));
                //doll.setSimAcc(getAcc(doll));
                //doll.setSimEva(getEva(doll));
                //doll.setSimRof(getRof(doll));
                //doll.setSimCritDmg(getCritDmg(doll));
                //doll.setSimCrit(getCrit(doll));
                //doll.setSimAp(getAP(doll));
                //doll.setSimArmour(getArmour(doll));
                //NightView
                //doll.setSimRounds(getRounds(doll));
                //doll.setTargets();


                List<Integer> stats = new ArrayList<>();

                //'getStats(Doll)' applies equipment and tile bonuses
                for(int stat : getStats(doll)) stats.add(stat);
                doll.setBattleStats(stats);

                //set up skill timer
                //set up skill 2 timer for mods
                //calculate skills

                //apply fairy bonus multiplicatively, ceiling
                /*doll.pre_battle.fp = Math.ceil(doll.pre_battle.fp * (1 + fairy.aura.fp / 100));
                doll.pre_battle.acc = Math.ceil(doll.pre_battle.acc * (1 + fairy.aura.acc / 100));
                doll.pre_battle.eva = Math.ceil(doll.pre_battle.eva * (1 + fairy.aura.eva / 100));
                doll.pre_battle.armor = Math.ceil(doll.pre_battle.armor * (1 + fairy.aura.armor / 100));
                doll.pre_battle.critdmg = Math.ceil((doll.pre_battle.critdmg + 100) * (1 + fairy.aura.critdmg / 100)) - 100;*/
                preBattleSkillChanges(doll, e);
            }
        }
    }

    /*private void setUsableSkillEffects(Effect[] effects){
        for (Effect effect : effects) {
            if (effect.getRequirements() == null) {
                effect.setUsable(true);
                continue;
            }
            boolean valid = true;
            for (String condition : effect.getRequirements()) {
                switch (condition) {
                    case "night":
                        valid = valid && (isNight == effect.isNight());
                        break;
                    case "armored":
                        valid = valid && (enemyArmor > 0 == effect.isArmoured());
                        break;
                    case "boss":
                        valid = valid && (isBoss == effect.isBoss());
                        break;
                }
            }
            effect.setUsable(valid);
        }
    }*/
    /*function getUsableSkillEffects(effects) {
        let validEffects = [];

        for (let i = 0; i < effects.length; i++) {
            if (!('requirements' in effects[i])) {
                validEffects.push($.extend({}, effects[i]));
                continue;
            }
            let valid = true;
            $.each(effects[i].requirements, (condition, value) => {
                if (condition == 'night') {
                    valid = valid && (isNight == value);
                }
                if (condition == 'armored') {
                    valid = valid && (enemyArmor > 0 == value);
                }
                if (condition == 'boss') {
                    valid = valid && (isBoss == value);
                }
            });
            if (valid) {
                validEffects.push($.extend({}, effects[i]));
            }
        }

        return validEffects;
    }*/

    //This sets up the passive part of the T-Dolls skill. These aren't necessarily the passives found
    //within the JSON file.
    private void preBattleSkillChanges(Doll doll, Echelon e){
        int size = 0, enemyCount = 12;//TODO: This needs to be set by the user via the UI;
        JSONObject passive = new JSONObject();
        JSONArray effectsArray = new JSONArray();
        JSONObject effectsObject = new JSONObject();
        JSONObject stats = new JSONObject();
        switch(doll.getID()){
            case 7://Stechkin
            case 330://Stechkin Mod 3
                if (doll.getEquipment(0).getID() == 99)
                    doll.getBS().getSkill_1Effects()[0].setFp(new float[]{4});
                break;
            case 189://K2
                doll.getBS().getSkill_1().modeName = "fever";
//                let feverbuff = {
//                        type: 'buff',
//                        target: 'self',
//                        name: 'normalAttackBuff',
//                        hitCount: 3,
//                        multiplier: [0.4, 0.412, 0.424, 0.436, 0.448, 0.46, 0.472, 0.484, 0.496, 0.52],
//                duration: -1,
//                        level: doll.skilllevel
//};
//                let heatbuff = {
//                        type: 'buff',
//                        target: 'self',
//                        duration: -1,
//                        stackable: true,
//                        stacks: 1,
//                        max_stacks: 20,
//                        stacksToAdd: 1,
//                        name: 'heat',
//                        stat: {
//                    fp: 0,
//                            acc: 0
//                },
//                level: doll.skilllevel
//};
//                let normalAttackPassive = {
//                        type: 'passive',
//                        trigger: 'normalAttack',
//                        level: doll.skilllevel,
//                        effects: [
//                {
//                    type: 'buff',
//                            target: 'self',
//                        duration: -1,
//                        stackable: true,
//                        stacks: 1,
//                        max_stacks: 20,
//                        stacksToAdd: 1,
//                        name: 'heat',
//                        stat: {
//                    fp: 0,
//                            acc: 0
//                },
//                    level: doll.skilllevel
//                },
//                {
//                    type: 'modifySkill',
//                            modifySkill: 'singleEnemyAttackStack'
//                }
//  ]
//};
//                let hasStacksPassive = {
//                        type: 'passive',
//                        trigger: 'hasStacks',
//                        stacksRequired: 16,
//                        name: 'heat',
//                        level: doll.skilllevel,
//                        effects: [
//                {
//                    type: 'modifySkill',
//                            modifySkill: 'changeHeatStats'
//                }
//  ]
//};
//                let notHasStacksPassive = {
//                        type: 'passive',
//                        trigger: 'notHasStacks',
//                        stacksRequired: 15,
//                        name: 'heat',
//                        level: doll.skilllevel,
//                        effects: [
//                {
//                    type: 'modifySkill',
//                            modifySkill: 'changeHeatStatsDown'
//                }
//  ]
//};
//                doll.battle.buffs.push(feverbuff);
//                doll.battle.buffs.push(heatbuff);
//                doll.battle.passives.push(normalAttackPassive);
//                doll.battle.passives.push(hasStacksPassive);
//                doll.battle.passives.push(notHasStacksPassive);
                break;//TODO: Not finished
            case 199://Ballista
                doll.getBS().getSkill_1().marks = 0;
                break;
            case 192://Carcano M1891 - Strawberry Cano
                //Passive Info: 40% chance per attack to increase crit rate and rate of fire by 4%
                //for 2 seconds for dolls standing in the same column, stacking up to 3 times
                for (int i = 0; i < 5; i++) {
                    if (e.getDoll(i).getID() == 0) continue;

                    if (e.getDoll(i).getID() != 192 && e.getDoll(i).getType() == 3) size++;
                }
                if(size > 0){
                    Effect[] temp = new Effect[size];
                    for(int i = 0; i < size; i++) temp[i] = doll.getBS().getSkill_1().getEffects()[0];
                    doll.getBS().setSkill_1Effects(temp);
                }
                break;
            case 193://Carcano M91/38 - Grape Cano
                try{
                    Effect effect;

                    effectsObject.put("type", "buff");
                    effectsObject.put("target", "self");
                    effectsObject.put("name", "grape");
                    effectsObject.put("duration", -1);
                    effectsObject.put("stackable", true);
                    effectsObject.put("stacks", 0);
                    effectsObject.put("max_stacks", 99);
                    effectsObject.put("stacksToAdd", new int[]{1, 1, 1, 1, 1, 1, 2, 2, 2, 2});
                    effectsObject.put("stackChance", new int[]{30, 35, 40, 45, 50, 55, 55, 60, 65, 70});
                    effectsObject.put("level", doll.getSkill_1Level());

                    effect = new Effect(effectsObject);

                    doll.getBS().setBuffs(new Buff[]{new Buff(effect)});

                    effectsObject.put("type", "buff");
                    effectsObject.put("target", "self");
                    effectsObject.put("name", "grape");
                    effectsObject.put("level", doll.getSkill_1Level());
                    effectsObject.put("duration", -1);
                    effectsObject.put("stackable", true);
                    effectsObject.put("max_stacks", 99);
                    effectsObject.put("stackChance", new int[]{30, 35, 40, 45, 50, 55, 55, 60, 65, 70});

                    int[] tempstackstoadd = new int[]{12, 13, 14, 14, 15, 16, 16, 17, 17, 18};
                    int[] tempstackchance = new int[]{30, 35, 40, 45, 50, 55, 55, 60, 65, 70};

                    int tempadd = (int) Math.ceil(100f / tempstackchance[doll.getSkill_1Level() - 1] * tempstackstoadd[doll.getSkill_1Level() - 1]); //to make up for the stackChance when hasStacks passive is calculated

                    effectsObject.put("stacksToAdd", tempadd);
                    effectsObject.put("stacks", tempadd);

                    //activeBuff.stacksToAdd = Math.ceil(100 / activeBuff.stackChance[activeBuff.level - 1] * activeBuff.stacksToAdd[activeBuff.level - 1]); //to make up for the stackChance when hasStacks passive is calculated
                    //activeBuff.stacks = activeBuff.stacksToAdd; //in case there is a point in the battle where the skill goes off right after the hasStacks passive is triggered and removes all stacks

                    effect = new Effect(effectsObject);

                    doll.getBS().getSkill_1Effects()[0] = effect;


                    //doll.battle.buffs.push(buff);
                    //doll.battle.skill.effects[0] = activeBuff;
                }
                catch (JSONException ex) {
                    ex.printStackTrace();
                }
                break;
            case 224://M82A1
                doll.getBS().getSkill_1().skillUseCount = 0;
                break;
            case 227://JS9
                //Gain self buffs based on the number of enemies remaining. With only 1 group of
                //enemies left, increase self damage by 50% for 5 seconds (with 3 stacks). For each
                //additional enemy, remove 1 damage buff and add 1 evasion buff, increasing self
                //evasion by 35% for 5 seconds. Evasion buff can stack up to 6 times
                int fpstacks = enemyCount > 3 ? 0 : 4 - enemyCount;
                int evastacks = enemyCount == 1 ? 0 : Math.min(6, enemyCount - 1);
                doll.getBS().getSkill_1Effects()[0].setStacks(evastacks);
                doll.getBS().getSkill_1Effects()[1].setStacks(fpstacks);
                break;
            case 231://Type 88
                try{
                    effectsObject.put("type", "buff");
                    effectsObject.put("target", "self");
                    effectsObject.put("level", doll.getSkill_1Level());
                    effectsObject.put("stat", stats);

                    stats.put("acc", new float[]{-40, -38, -36, -33, -31, -29, -27, -24, -22, -20});

                    effectsObject.put("duration", 6);
                    effectsObject.put("timeLeft", 180);

                    Effect effect = new Effect(effectsObject);
                    doll.getBS().setBuffs(new Buff[]{new Buff(effect)});
                }
                catch (JSONException ex) {
                    ex.printStackTrace();
                }
                break;
            case 235://64 Shiki
                //Gain different effects based on the number of enemies when the skill is activated.
                //If more than 5 enemies exist, increase self rate of fire and accuracy by 80% for
                //3 seconds, otherwise increase self damage by 90% for 3 seconds. After 3 seconds,
                //if more than 2 enemies are left, grant a 25HP shield to allies on her tiles,
                //otherwise increase damage for self and allies on tiles by 55% for 5 seconds
                //TODO: Implement tile buff if it is not done elsewhere
                if (enemyCount > 5) {
                    doll.getBS().getSkill_1Effects()[0].setFp(new float[]{0});
                    doll.getBS().getSkill_1Effects()[0].setRof(new float[]{30, 36, 41, 47, 52, 58, 63, 69, 74, 80});
                    doll.getBS().getSkill_1Effects()[0].setAcc(new float[]{30, 36, 41, 47, 52, 58, 63, 69, 74, 80});
                }
                else {
                    doll.getBS().getSkill_1Effects()[0].setFp(new float[]{40, 46, 51, 57, 62, 68, 73, 79, 84, 90});
                    doll.getBS().getSkill_1Effects()[0].setRof(new float[]{0});
                    doll.getBS().getSkill_1Effects()[0].setAcc(new float[]{0});
                }
                break;
            case 239://Jericho
                //Passive Info: When allies on her tiles reload, increase their damage and accuracy
                //by 5%, stacking up to 3 times for 15 seconds
                try{
                    passive.put("type", "passive");
                    passive.put("trigger", "reload");
                    passive.put("effects", effectsArray);

                    effectsArray.put(effectsObject);

                    effectsObject.put("type", "buff");
                    effectsObject.put("target", "self");
                    effectsObject.put("name", "jericho");
                    effectsObject.put("stat", stats);

                    stats.put("fp", 5);
                    stats.put("acc", 5);

                    effectsObject.put("duration", 15);
                    effectsObject.put("stackable", true);
                    effectsObject.put("stacks", 1);
                    effectsObject.put("max_stacks", 3);

                    int[] tiles = new int[4];
                    System.arraycopy(doll.getTilesFormation(), 1, tiles, 0, 4);

                    for(int i = 0; i < e.getAllDolls().length; i++){
                        if(e.getDoll(i).getID() != 0){
                            for(int pos : tiles){
                                if(e.getDoll(i).getGridPosition() == pos){
                                    if(e.getDoll(i).getType() == 5 || e.getDoll(i).getType() == 6){// have to specify mg/sg so it doesn't work on falcon
                                        size++;
                                    }
                                }
                            }
                        }
                    }
                    if(size > 0){
                        Passive[] temp = new Passive[size];
                        for(int i = 0; i < size; i++) //temp[i] = doll.getBS().getSkill_1().getEffects()[0];
                            temp[i] = new Passive(passive);// TODO: Needs to be in passives
                        doll.getBS().setPassives(temp);
                    }
                }
                catch (JSONException ex) {
                    ex.printStackTrace();
                }
                break;
            case 249://Clear
                //Stops shooting and plays her guitar 5 times. Each time she plays, a teammate other
                //than herself receives a damage and accuracy buff of 30% (40% if she has Glory Light
                //equipped) for 3 seconds
                int[] targetDolls;
                for(int i = 0; i < e.getAllDolls().length; i++) {
                    if(!(e.getDoll(i).getID() == 0 || e.getDoll(i).getID() == doll.getID()))
                        size++;
                }
                if(size == 0) break;

                targetDolls = new int[size];
                for(int i = 0; i < size;){
                    if(!(e.getDoll(i).getID() == 0 || e.getDoll(i).getID() == doll.getID())) {
                        targetDolls[i] = e.getDoll(i).getID();
                        i++;
                    }
                }
                boolean hasSPEQ = doll.getEquipment(0).getID() == 85;
                //hand out all 5 buffs in sequence
                /*for (int i = 0; i < 5; i += targetDolls.length) {
                    for (int j = 0; j < Math.min(targetDolls.length, 5 - i); j++) {
                        doll.getBS().getSkill_1Effects()[i + j + 1].getAfterEffects()[0].setTarget("doll");
                        doll.getBS().getSkill_1Effects()[i + j + 1].getAfterEffects()[0].dollID = e.getDoll(targetDolls[j]).getID();
                        if (hasSPEQ) {
                            doll.getBS().getSkill_1Effects()[i + j + 1].getAfterEffects()[0].setFp(new float[]{20, 22, 24, 27, 29, 31, 33, 36, 38, 40});
                            doll.getBS().getSkill_1Effects()[i + j + 1].getAfterEffects()[0].setAcc(new float[]{20, 22, 24, 27, 29, 31, 33, 36, 38, 40});
                        }
                    }
                }*/
                for (int i = 0; i < size; i++) {
                    doll.getBS().getSkill_1Effects()[i + 1].getAfterEffects()[0].setTarget("doll");
                    doll.getBS().getSkill_1Effects()[i + 1].getAfterEffects()[0].dollID = e.getDoll(targetDolls[i]).getID();
                    if (hasSPEQ) {
                        doll.getBS().getSkill_1Effects()[i + 1].getAfterEffects()[0].setFp(new float[]{20, 22, 24, 27, 29, 31, 33, 36, 38, 40});
                        doll.getBS().getSkill_1Effects()[i + 1].getAfterEffects()[0].setAcc(new float[]{20, 22, 24, 27, 29, 31, 33, 36, 38, 40});
                    }
                }
            case 253://M1895 Mod 3 Nagant Revolver
                //doll.battle.skill2.icd = (doll.battle.timers.find(t => t.type == 'normalAttack').timeLeft + 3) / 30;
                break;
            case 256://Mosin-Nagant Mod 3
                //Passive: Every enemy unit killed by Mosin increases her damage by 20% for 3 seconds.
                //Killing an enemy unit with her skill 1 increases her rate of fire by 30% for 5 seconds
                if (doll.getBS().getSkill_1Effects()[0].getAfterEffects()[0].getTarget().equals("self")) {
                    int[] rof = {15, 17, 18, 20, 22, 23, 25, 27, 28, 30};
                    doll.getBS().getSkill_1Effects()[0].getAfterEffects()[0].setRof(new float[]{rof[doll.getSkill_2Level() - 1]});
                }
                break;
            case 259://M4A1 Mod 3
                doll.getBS().getSkill_1Effects()[2].setMultiplier(new float[] {doll.getBS().getSkill_1Effects()[2].getMultiplier()[doll.getSkill_2Level() - 1]});
                break;
            case 260://SOPMODII Mod 3
                doll.getBS().getSkill_1Effects()[0].getAfterEffects()[1].setMultiplier(new float[]{doll.getBS().getSkill_1Effects()[0].getAfterEffects()[1].getMultiplier()[doll.getSkill_2Level() - 1]});
                doll.getBS().getSkill_1Effects()[0].getAfterEffects()[2].setMultiplier(new float[]{doll.getBS().getSkill_1Effects()[0].getAfterEffects()[2].getMultiplier()[doll.getSkill_2Level() - 1]});
                doll.getBS().getSkill_1Effects()[0].getAfterEffects()[3].setMultiplier(new float[]{doll.getBS().getSkill_1Effects()[0].getAfterEffects()[3].getMultiplier()[doll.getSkill_2Level() - 1]});
                break;
            case 262://G3 Mod 3
                if(doll.getBS().getSkill_1().isBuffedNade()){
                    float[] temp = new float[doll.getBS().getSkill_1Effects()[0].getMultiplier().length];
                    for(int i = 0; i < doll.getBS().getSkill_1Effects()[0].getMultiplier().length; i++){
                        temp[i] = doll.getBS().getSkill_1Effects()[0].getMultiplier()[i] + doll.getBS().getSkill_2Effects()[0].getMultiplier()[doll.getSkill_2Level() - 1];
                    }
                    doll.getBS().getSkill_1Effects()[0].setMultiplier(temp);
                }
                break;
            case 263://G36 Mod 3
                int buffCount = 0;

                // Check right tile
                for(Doll doll_ : e.getAllDolls())
                    if(doll_.getGridPosition() == doll.getGridPosition() + 1) buffCount++;

                // Check bottom right tile
                for(Doll doll_ : e.getAllDolls()) {
                    if(doll.getGridPosition() != 9 && doll.getGridPosition() != 6 && doll.getGridPosition() != 3)
                        if (doll_.getGridPosition() == doll.getGridPosition() - 2) buffCount++;
                }

                try {
                    effectsObject.put("type", "buff");
                    effectsObject.put("target", "self");
                    effectsObject.put("name", "g36mod");
                    effectsObject.put("stat", stats);

                    stats.put("rof", new int[]{5, 6, 6, 7, 7, 8, 8, 9, 9, 10});

                    effectsObject.put("duration", new float[]{3, 3.2f, 3.4f, 3.7f, 3.9f, 4.1f, 4.3f, 4.6f, 4.8f, 5});
                    effectsObject.put("stackable", true);
                    effectsObject.put("stacks", 1);
                    effectsObject.put("max_stacks", 3);

                    /*int[] tiles = new int[2];
                    System.arraycopy(doll.getTilesFormation(), 1, tiles, 0, 2);

                    for(int i = 0; i < e.getAllDolls().length; i++){
                        if(e.getDoll(i).getID() != 0){
                            for(int pos : tiles){
                                if(e.getDoll(i).getGridPosition() == pos){
                                    if(e.getDoll(i).getType() == 5 || e.getDoll(i).getType() == 6){// have to specify mg/sg so it doesn't work on falcon
                                        size++;
                                    }
                                }
                            }
                        }
                    }*/
                    Effect[] temp = new Effect[buffCount];
                    if (buffCount > 0) {
                        for (int i = 0; i < buffCount; i++) temp[i] = new Effect(effectsObject);
                        doll.getBS().setSkill_2Effects(temp);
                    }
                }
                catch (JSONException ex) {
                    ex.printStackTrace();
                }
                break;
            case 264://M1918 Mod 3
                //Passive: Reload time is reduced to 5 seconds. After reloading, the first 3 bullets
                //with deal an additional 40% damage (affects the first volley)
                try{
                    Effect[] effects = new Effect[2];

                    effectsObject.put("type", "buff");
                    effectsObject.put("target", "self");
                    effectsObject.put("name", "reloadBuff");
                    effectsObject.put("level", doll.getSkill_2Level());
                    effectsObject.put("setTime", new float[]{5.5f, 5.4f, 5.4f, 5.3f, 5.3f, 5.2f, 5.2f, 5.1f, 5.1f, 5});
                    effectsObject.put("duration", -1);

                    effects[0] = new Effect(effectsObject);

                    effectsObject.put("type", "buff");
                    effectsObject.put("target", "self");
                    effectsObject.put("name", "normalAttackBuff");
                    effectsObject.put("level", doll.getSkill_2Level());
                    effectsObject.put("multiplier", new float[]{1.25f, 1.27f, 1.28f, 1.3f, 1.32f, 1.33f, 1.35f, 1.37f, 1.38f, 1.4f});
                    effectsObject.put("stacksLeft", 3);
                    effectsObject.put("duration", -1);

                    effects[1] = new Effect(effectsObject);

                    Buff[] buffs = new Buff[2];
                    for(int i = 0; i < 2; i++) buffs[i] = new Buff(effects[i]);

                    doll.getBS().setBuffs(buffs);
                }
                catch (JSONException ex) {
                    ex.printStackTrace();
                }
                break;
            case 268://IDW Mod 3
                //Passive: Start battle with 3 bars of battery, with each bar increasing self rate
                //of fire by 10% and damage by 20%. Every 2.6 seconds, lose 1 bar. Bars recharge to
                //3 when skill1 activates.
                try{
                    effectsObject.put("type", "buff");
                    effectsObject.put("target", "self");
                    effectsObject.put("name", "idwmod");
                    effectsObject.put("level", doll.getSkill_2Level());
                    effectsObject.put("stat", stats);

                    stats.put("fp", new int[]{6, 6, 7, 7, 8, 8, 9, 9, 10, 10});
                    stats.put("rof", new int[]{12, 13, 14, 15, 16, 16, 17, 18, 19, 20});

                    effectsObject.put("duration", -1);
                    effectsObject.put("stackable", true);
                    effectsObject.put("stacks", 3);
                    effectsObject.put("stacksToAdd", 3);
                    effectsObject.put("max_stacks", 3);

                    Buff[] buffs = new Buff[]{new Buff(new Effect(effectsObject))};

                    doll.getBS().setBuffs(buffs);
                }
                catch (JSONException ex) {
                    ex.printStackTrace();
                }
                break;
            case 273://Falcon
                try{
                    effectsObject.put("type", "buff");
                    effectsObject.put("target", "self");
                    effectsObject.put("name", "normalAttackBuff");
                    effectsObject.put("level", doll.getSkill_2Level());
                    effectsObject.put("duration", -1);
                    effectsObject.put("multiplier", 1.5f);

                    Buff[] buffs = new Buff[]{new Buff(new Effect(effectsObject))};

                    doll.getBS().setBuffs(buffs);
                }
                catch (JSONException ex) {
                    ex.printStackTrace();
                }
                break;
            case 278://M200
                try{
                    effectsObject.put("type", "buff");
                    effectsObject.put("target", "self");
                    effectsObject.put("name", "normalAttackBuff");
                    effectsObject.put("duration", -1);
                    effectsObject.put("level", doll.getSkill_1Level());
                    effectsObject.put("multiplier", 1.05f);

                    Effect effect = new Effect(effectsObject);

                    //if(enemyArmour == 0) doll.getBS().setBuffs(new Buff[]{new Buff(effect)});
                    //TODO: Needs enemy armour to be a thing. This requires enemies to be set up
                    doll.getBS().getSkill_1().numberOfShots = 0;
                }
                catch (JSONException ex) {
                    ex.printStackTrace();
                }
                break;
            case 282://Chauchat
                try{
                    effectsObject.put("type", "buff");
                    effectsObject.put("target", "self");
                    effectsObject.put("name", "chauchat");
                    effectsObject.put("duration", -1);
                    effectsObject.put("stackable", true);
                    effectsObject.put("stacks", 1);
                    effectsObject.put("max_stacks", 4);

                    Buff[] buffs = new Buff[]{new Buff(new Effect(effectsObject))};

                    doll.getBS().setBuffs(buffs);
                }
                catch (JSONException ex) {
                    ex.printStackTrace();
                }
                break;
            case 283://MG36
                /*int[] targetSquares = doll.tiles.target.split(',');
                targetSquares = targetSquares.map(targetSquare => parseInt(targetSquare));
                targetSquares = targetSquares.map(targetSquare => targetSquare + doll.pos);
                let validSquares = [12, 13, 14, 22, 23, 24, 32, 33, 34];
                $.each(targetSquares, (index, targetSquare) => {
                    if ($.inArray(targetSquare, validSquares) != -1) {
                        let dollOnTile = echelon.find(d => d.pos == targetSquare);
                        if (dollOnTile !== undefined && dollOnTile.id != -1) {
                            if (dollOnTile.type == 4) {
                                doll.battle.skill.effects[1].stacks++;
                            }
                            if (dollOnTile.type == 2) {
                                doll.battle.skill.effects[2].stacks++;
                            }
                            if (dollOnTile.type == 6) {
                                doll.battle.skill.effects[3].rounds++;
                            }
                        }
                    }
                });*/
                break;//TODO: Not finished
            case 290://STG44 Mod 3
                float[] grenadeDamageBonus = new float[]{1.10f, 1.11f, 1.12f, 1.13f, 1.14f, 1.16f, 1.17f, 1.18f, 1.19f, 1.20f};
                float[] multiplier1 = doll.getBS().getSkill_1Effects()[0].getMultiplier();
                float[] multiplier2 = doll.getBS().getSkill_1Effects()[1].getMultiplier();
                float[] multiplier3 = doll.getBS().getSkill_1Effects()[2].getMultiplier();
                doll.getBS().getSkill_1Effects()[0].setMultiplier(new float[]{multiplier1[doll.getSkill_1Level() - 1] * grenadeDamageBonus[doll.getSkill_1Level() - 1]});
                doll.getBS().getSkill_1Effects()[1].setMultiplier(new float[]{multiplier2[doll.getSkill_1Level() - 1] * grenadeDamageBonus[doll.getSkill_1Level() - 1]});
                doll.getBS().getSkill_1Effects()[2].setMultiplier(new float[]{multiplier3[doll.getSkill_1Level() - 1] * grenadeDamageBonus[doll.getSkill_1Level() - 1]});
                break;
            case 291://Micro Uzi Mod 3
                doll.getBS().getSkill_2Effects()[0].getAfterEffects()[0].setDuration(new float[]{doll.getBS().getSkill_2Effects()[0].getDuration()[doll.getSkill_1Level() - 1]});
                break;
            case 304://MP5 Mod 3
                //int numStacks = enemyCount > 3 ? 3 : enemyCount;
                doll.getBS().getSkill_1Effects()[0].getAfterEffects()[0].setStacks(enemyCount > 3 ? 3 : enemyCount);
                doll.getBS().getSkill_1Effects()[0].getAfterEffects()[0].level = doll.getSkill_2Level();
                break;
            case 307://SSG3000
                doll.getBS().getSkill_1().numberOfShots = 0;
                break;
            case 310://M1895CB
                doll.getBS().reserveAmmo = 30;
                doll.getBS().reserveAmmoMode = false;
                break;
            case 332://AK15
                doll.getBS().getSkill_1Effects()[1].setStacks(enemyCount);
                doll.getBS().getSkill_1Effects()[1].setStacksToAdd(new int[]{enemyCount});
                break;
        }
    /*function preBattleSkillChanges(doll) {











        //dana
        if (doll.id == 292) {
            doll.battle.targets = 1;
            let normalAttack = {
                    type: 'buff',
                    name: 'normalAttackBuff',
                    duration: -1,
                    multiplier: [1.2, 1.27, 1.33, 1.4, 1.47, 1.53, 1.6, 1.67, 1.73, 1.8],
            level: doll.skilllevel
    };
            doll.battle.buffs.push($.extend(true, {}, normalAttack));
        }

        //dorothy
        if (doll.id == 297) {
            if (doll.pos == 22 || doll.pos == 23 || doll.pos == 24) {
                doll.battle.skill.effects[0].delay = 0;
                doll.battle.skill.effects[0].after[0].duration = doll.battle.skill.effects[1].delay;
                doll.battle.skill.effects[0].after[1].duration = doll.battle.skill.effects[1].delay;
            } else {
                doll.battle.skill.effects[1].delay = 0;
                doll.battle.skill.effects[1].after[0].duration = doll.battle.skill.effects[0].delay;
                doll.battle.skill.effects[1].after[1].duration = doll.battle.skill.effects[0].delay;
            }
        }

        //jill
        if (doll.id == 296) {
            doll.battle.timers.find(timer => timer.type == 'normalAttack').timeLeft = -1;

            let cooldownBonus = doll.base.fp > 30 ? 30 : doll.base.fp;
            let skillcdBuff = {
                    type: 'buff',
                    target: 'self',
                    level: doll.skilllevel,
                    stat: {
                skillcd: cooldownBonus * -1
            },
            duration: -1
    };
            doll.battle.buffs.push(skillcdBuff);

            if (doll.equip1 == 106 && doll.equip2 == 105 && doll.equip3 == 109) {
                doll.battle.skill.effects.find(e => e.name == 'bigbeer').delay = 3;
            } else if (doll.equip1 == 103 && doll.equip2 == 104 && doll.equip3 == 110) {
                doll.battle.skill.effects.find(e => e.name == 'brandtini').delay = 3;
            } else if (doll.equip1 == 103 && doll.equip2 == 105 && doll.equip3 == 109) {
                doll.battle.skill.effects.find(e => e.name == 'pianowoman').delay = 3;
                let dorothy = echelon.find(d => d.id == 297);
                if (dorothy !== undefined) {
                    dorothy.battle.skill.effects[0].after[1].stat.eva = dorothy.battle.skill.effects[0].after[1].stat.eva.map(x => x / 2);
                    dorothy.battle.skill.effects[1].after[1].stat.acc = dorothy.battle.skill.effects[0].after[1].stat.acc.map(x => x / 2);
                }
            } else if (doll.equip1 == 103 && doll.equip2 == 104 && doll.equip3 == 109) {
                doll.battle.skill.effects.find(e => e.name == 'moonblast').delay = 3;
            } else if (doll.equip1 == 106 && doll.equip2 == 105 && doll.equip3 == 110) {
                doll.battle.skill.effects.find(e => e.name == 'bleedingjane').delay = 3;
            } else if (doll.equip1 == 107 && doll.equip2 == 108 && doll.equip3 == 109) {
                doll.battle.skill.effects.find(e => e.name == 'fringeweaver').delay = 3;
            } else {
                doll.battle.skill.effects.find(e => e.name == 'sugarrush').delay = 3;
            }
        }





        //acr
        if (doll.id == 309) {
            let singleBuff = {
                    type:"buff",
                    target:"self",
                    stat:{
                fp:[5,6,6,7,7,8,8,9,9,10]
            },
            name:"acrSingleDebuffBuff",
                    stackable: true,
                    stacks: 0,
                    max_stacks: 1,
                    level: doll.skilllevel,
                    duration:-1
    };
            let multiBuff = {
                    type:"buff",
                    target:"self",
                    stat:{
                fp:[3,3,3,4,4,4,4,5,5,5]
            },
            stackable: true,
                    stacks: 0,
                    max_stacks: 8,
                    name:"acrMultipleDebuffBuff",
                    level: doll.skilllevel,
                    duration:-1
    };
            doll.battle.buffs.push(singleBuff);
            doll.battle.buffs.push(multiBuff);
        }



        if (doll.id == 320) {
            //mat49
            let fpstacks = enemyCount > 3 ? 0 : 4 - enemyCount;
            let evastacks = enemyCount == 1 ? 0 : Math.min(6, enemyCount - 1);
            doll.battle.skill.effects[0].stacks = evastacks;
            doll.battle.skill.effects[1].stacks = fpstacks;
        }

        if (doll.id == 323) {
            //sl8
            let dollTypesOnTiles = [];
            let sl8Tiles = [-20,-10,-9,-8];
            $.each(sl8Tiles, (index, tile) => {
                let d = echelon.find(d => d.pos == doll.pos + tile);
                if (d != undefined && d.id != -1) {
                    dollTypesOnTiles.push(d.type);
                }
            });
            let uniqueTypesOnTiles = [...new Set(dollTypesOnTiles)];
            let fpstacks = uniqueTypesOnTiles.length > 3 ? 3 : uniqueTypesOnTiles.length;
            doll.battle.skill.effects[1].stacks = fpstacks;
        }

        //webley
        if (doll.id == 328) {
            for (let i = 0; i < 5; i++) {
                if (echelon[i].id == -1) {
                    continue;
                }
                if (echelon[i].pos != doll.pos) {
                    doll.battle.skill.effects[0].target = 'none';
                } else {
                    doll.battle.skill.effects[1].target = 'none';
                    doll.battle.skill.effects[2].modifySkill = 'none';
                }
                break;
            }
        }



        */
    }

    private void CalculateDPS(Echelon e) {

    }

//    private void SimulateBattle(Echelon e) {
//        //initialise graph variables here
//
//        for(Doll doll : e.getAllDolls(){
//              doll.getBS().setBattleStats();
//        };
//        //initialise enemies
//        //initialise fairies
//
//        float simulationLength = 30 * battleLength;
//        float totaldamage8s = 0;
//        float totaldamage12s = 0;
//        float totaldamage20s = 0;
//
//        //apply fairy talent effect to dolls
//        //apply fortress node effect
//
//
//        //walk time
//        //graphData.x.push(0); This is where information for the x axis will be added
//        float time = 0;
//        for (time = 1; time < walkTime * 30/*frames that the game runs at standard*/; time++) {
//            //graphData.x.push(parseFloat((time / 30.0).toFixed(2)));
//
//            //fairy timer altering
//            /*if (fairy.id != -1) {
//                graphData.y[5].data.push(graphData.y[5].data[time - 1]);
//                $.each(fairy.battle.timers, (index, timer) => {
//                    if (timer.timeLeft > 1) {
//                        timer.timeLeft--;
//                    }
//                });
//            }*/
//
//            //Setting buffs and passives
//
//            for (int i = 0; i < e.getAllDolls().length; i++) {
//                //graphData.y[i].data.push(graphData.y[i].data[time - 1]);
//                Doll doll = e.getDoll(i);
//                if (doll.getID() == 0) continue;
//
//                for(Timer timer : doll.getTimers()){
//                    if (timer.getTimeLeft() > 1) { // && timer.type != 'normalAttack' ??? need to check this assumption
//                        timer.setTimeLeft(timer.getTimeLeft() - 1);
//                    }
//                }
//
//                //tick and remove buffs
//                for(Buff buff : doll.getBuffs()){
//                    if(buff.getTimeLeft() > 0){
//                        buff.setTimeLeft(buff.getTimeLeft() - 1);
//                    }
//                }
//                int[] removal = new int[doll.getBuffs().size()];
//                int index = 0;
//                for(Buff buff : doll.getBuffs()){
//                    if(buff.getTimeLeft() > 0){
//                        //if(buff.getTimeLeft() == 0){
//
//                        //}
//
//
//
//                        /*if ('after' in buff) {
//                            if ($.isArray(buff.after)) {
//                                $.each(buff.after, (index, effect) => {
//                                    if (!('level' in effect))
//                                    effect.level = buff.level;
//                                    doll.battle.effect_queue.push($.extend(true, {}, effect));
//                                });
//                            } else {
//                                if (!('level' in buff.after))
//                                buff.after.level = buff.level;
//                                doll.battle.effect_queue.push($.extend(true, {}, buff.after));
//                            }
//                        }
//                        return false;*/
//                        removal[index] = doll.getBuffs().indexOf(buff);
//                    }
//                    index++;
//                }
//                for (int value : removal) doll.getBuffs().remove(value);
//
//                //tick and remove passives
//
//                for(Passive passive : doll.getPassives()){
//                    if(passive)
//                }
//                $.each(doll.battle.passives, (index, passive) => {
//                    if ('timeLeft' in passive) {
//                        passive.timeLeft--;
//                    }
//                });
//                doll.battle.passives = doll.battle.passives.filter(passive => {
//                if ('timeLeft' in passive) {
//                    if (passive.timeLeft == 0) {
//                        return false;
//                    }
//                }
//                return true;
//      });
//
//                //tick and trigger time-based passives
//                $.each(doll.battle.passives.filter(passive => 'interval' in passive), (index, passiveskill) => {
//                    let interval = $.isArray(passiveskill.interval) ? passiveskill.interval[passiveskill.level - 1] : passiveskill.interval;
//                    if ((time - passiveskill.startTime) % Math.floor(interval * 30) == 0 && time != 1 && interval != -1) {
//                        triggerPassive('time', doll, enemy);
//                    }
//                });
//
//                calculateSkillBonus(i);
//                calculateBattleStats(i);
//            }
//
//            /*for (let i = 0; i < 5; i++) {
//                graphData.y[i].data.push(graphData.y[i].data[time - 1]);
//                let doll = echelon[i];
//                if (doll.id == -1) continue;
//
//                $.each(doll.battle.timers, (index, timer) => {
//                    if (timer.timeLeft > 1) { // && timer.type != 'normalAttack' ??? need to check this assumption
//                        timer.timeLeft--;
//                    }
//                });
//
//                //tick and remove buffs
//                $.each(doll.battle.buffs, (index, buff) => {
//                    if ('timeLeft' in buff) {
//                        buff.timeLeft--;
//                    }
//                });
//                doll.battle.buffs = doll.battle.buffs.filter(buff => {
//                if ('timeLeft' in buff) {
//                    if (buff.timeLeft == 0) {
//                        if ('after' in buff) {
//                            if ($.isArray(buff.after)) {
//                                $.each(buff.after, (index, effect) => {
//                                    if (!('level' in effect))
//                                    effect.level = buff.level;
//                                    doll.battle.effect_queue.push($.extend(true, {}, effect));
//                                });
//                            } else {
//                                if (!('level' in buff.after))
//                                buff.after.level = buff.level;
//                                doll.battle.effect_queue.push($.extend(true, {}, buff.after));
//                            }
//                        }
//                        return false;
//                    }
//                }
//                return true;
//      });
//
//                //tick and remove passives
//                $.each(doll.battle.passives, (index, passive) => {
//                    if ('timeLeft' in passive) {
//                        passive.timeLeft--;
//                    }
//                });
//                doll.battle.passives = doll.battle.passives.filter(passive => {
//                if ('timeLeft' in passive) {
//                    if (passive.timeLeft == 0) {
//                        return false;
//                    }
//                }
//                return true;
//      });
//
//                //tick and trigger time-based passives
//                $.each(doll.battle.passives.filter(passive => 'interval' in passive), (index, passiveskill) => {
//                    let interval = $.isArray(passiveskill.interval) ? passiveskill.interval[passiveskill.level - 1] : passiveskill.interval;
//                    if ((time - passiveskill.startTime) % Math.floor(interval * 30) == 0 && time != 1 && interval != -1) {
//                        triggerPassive('time', doll, enemy);
//                    }
//                });
//
//                calculateSkillBonus(i);
//                calculateBattleStats(i);
//            }*/
//        }//TODO: Not finished at all - currently a placeholder
//
//        //Battle stage
//        //graphData.x.push((time / 30.0).toFixed(2)); Add information to the graph x axis
//        float currentFrame = time;
//        for (currentFrame = time; currentFrame < simulationLength; currentFrame++) {
//            //graphData.x.push(parseFloat((currentFrame / 30.0).toFixed(2))); Add information to the graph x axis
//
//            //tick timers, queue actions
//            for (int i = 0; i < e.getAllDolls().length; i++) {
//                Doll doll = e.getDoll(i);
//                if (doll.getID() == 0) return;
//
//                //graphData.y[i].data.push(graphData.y[i].data[currentFrame - 1]); Add information to the graph y axis
//
//                //Normal attack timing
//                for (Timer timer : doll.getTimers()) {
//                    if (timer.getType().equals("normal attack")) {
//                        boolean reloading = doll.findTimer("reloading");//Figure out where reload timers come into play
//                        boolean stunned = doll.findTimer("stunned");
//                        if (u.Links(doll.getLevel()) - doll.getBusyLinks() > 0 && !reloading && !stunned) {
//                            timer.setTimeLeft(timer.getTimeLeft() - 1);
//                        }
//                    }
//                    else {
//                        if (timer.getTimeLeft() > 0)
//                            timer.setTimeLeft(timer.getTimeLeft() - 1);
//                    }
//                }
//
//                //Skill timing
//                for (Timer timer : doll.getTimers()) {
//                    if (timer.getTimeLeft() == 0) {
//                        boolean reloading = doll.findTimer("reloading");
//                        boolean stunned = doll.findTimer("stunned");
//                        if (timer.getType().equals("skill")) {
//                            //If the T-Doll is unable to move...
//                            if (stunned || (reloading && doll.getTimer("reload").getTimeLeft() != 0)) {
//                                //...add 1 frame to the timer, effectively pausing it.
//                                timer.setTimeLeft(timer.getTimeLeft() + 1);
//                            }
//                            else {
//                                //factor in skills
//                                /*$.each(doll.battle.skill.effects, (index, effect) => {
//                                    if (!('level' in effect)) {
//                                        effect.level = doll.skilllevel;
//                                    }
//                                    if (effect.type == 'loadRounds') {
//                                        let targets = getBuffTargets(doll, effect, enemy);
//                                        $.each(targets, (index, target) => {
//                                            target.battle.currentRounds += $.isArray(effect.rounds) ? effect.rounds[effect.level - 1] : effect.rounds;
//                                        });
//                                    } else {
//                                        doll.battle.effect_queue.push($.extend({}, effect));
//                                    }
//                                });
//                                timer.timeLeft = Math.round(doll.battle.skill.cd[doll.skilllevel - 1] * 30 * doll.battle.skillcd);*/
//                            }//TODO: Factor in skills
//                        }
//                        else if (timer.getType().equals("skill2")) {
//                            //If the T-Doll is unable to move...
//                            if (stunned || (reloading && doll.getTimer("reload").getTimeLeft() != 0)) {
//                                //...add 1 frame to the timer, effectively pausing it.
//                                timer.setTimeLeft(timer.getTimeLeft() + 1);
//                            } else {
//                                //factor in skills
//                                /*$.each(doll.battle.skill.effects, (index, effect) => {
//                                    if (!('level' in effect)) {
//                                        effect.level = doll.skilllevel;
//                                    }
//                                    if (effect.type == 'loadRounds') {
//                                        let targets = getBuffTargets(doll, effect, enemy);
//                                        $.each(targets, (index, target) => {
//                                            target.battle.currentRounds += $.isArray(effect.rounds) ? effect.rounds[effect.level - 1] : effect.rounds;
//                                        });
//                                    } else {
//                                        doll.battle.effect_queue.push($.extend({}, effect));
//                                    }
//                                });
//                                timer.timeLeft = Math.round(doll.battle.skill.cd[doll.skilllevel - 1] * 30 * doll.battle.skillcd);*/
//                            }
//                        }
//                        else {
//                            //add to effect queue
//                            doll.addToEffectQueue(timer);
//                        }
//                    }
//                }
//
//                //JS Code
//                /*$.each(doll.battle.timers, (index, timer) => {
//                    if (timer.timeLeft == 0) {
//                        let reloading = doll.battle.timers.find(timer => timer.type == 'reload') === undefined ? false : true;
//                        let stunned = doll.battle.buffs.find(b => 'stun' in b) === undefined ? false : true;
//                        if (timer.type == 'skill') {
//                            if (stunned || (reloading && doll.battle.timers.find(timer => timer.type == 'reload').timeLeft != 0)) {
//                                timer.timeLeft++;
//                            } else {
//                                $.each(doll.battle.skill.effects, (index, effect) => {
//                                    if (!('level' in effect)) {
//                                        effect.level = doll.skilllevel;
//                                    }
//                                    if (effect.type == 'loadRounds') {
//                                        let targets = getBuffTargets(doll, effect, enemy);
//                                        $.each(targets, (index, target) => {
//                                            target.battle.currentRounds += $.isArray(effect.rounds) ? effect.rounds[effect.level - 1] : effect.rounds;
//                                        });
//                                    } else {
//                                        doll.battle.effect_queue.push($.extend({}, effect));
//                                    }
//                                });
//                                timer.timeLeft = Math.round(doll.battle.skill.cd[doll.skilllevel - 1] * 30 * doll.battle.skillcd);
//                            }
//                        } else if (timer.type == 'skill2') {
//                            if (stunned || (reloading && doll.battle.timers.find(timer => timer.type == 'reload').timeLeft != 0)) {
//                                timer.timeLeft++;
//                            } else {
//                                $.each(doll.battle.skill2.effects, (index, effect) => {
//                                    if (!('level' in effect)) {
//                                        effect.level = doll.skill2level;
//                                    }
//                                    if (effect.type == 'loadRounds') {
//                                        let targets = getBuffTargets(doll, effect, enemy);
//                                        $.each(targets, (index, target) => {
//                                            target.battle.currentRounds += $.isArray(effect.rounds) ? effect.rounds[effect.level - 1] : effect.rounds;
//                                        });
//                                    } else {
//                                        doll.battle.effect_queue.push($.extend({}, effect));
//                                    }
//                                });
//                                timer.timeLeft = Math.round(doll.battle.skill2.cd[doll.skill2level - 1] * 30 * doll.battle.skillcd);
//                            }
//                        } else {
//                            doll.battle.effect_queue.push($.extend({}, timer));
//                        }
//                    }
//                });*/
//
//
//                //remove expired timers - Why? Do I need this
//                //doll.battle.timers = doll.battle.timers.filter(timer => timer.timeLeft != 0);
//
//                //tick and remove buffs
//                for (Buff buff : doll.getBuffs()) {
//                    buff.setTimeLeft(buff.getTimeLeft() - 1);
//                }
//
//                //TODO: This can't be done until skills are added
//                //Removes buffs from the active buffs queue or something.
//                /*doll.battle.buffs = doll.battle.buffs.filter(buff => {
//                if ('timeLeft' in buff) {
//                    if (buff.timeLeft == 0) {
//                        if ('after' in buff) {
//                            if ($.isArray(buff.after)) {
//                                $.each(buff.after, (index, effect) => {
//                                    if (!('level' in effect))
//                                    effect.level = buff.level;
//                                    doll.battle.effect_queue.push($.extend(true, {}, effect));
//                                });
//                            } else {
//                                if (!('level' in buff.after))
//                                buff.after.level = buff.level;
//                                doll.battle.effect_queue.push($.extend(true, {}, buff.after));
//                            }
//                        }
//                        return false;
//                    }
//                }
//                return true;*/
//
//
//
//
//
//
//
//
//
//
//
//
//
//                //tick and remove passives
//
//                for (Passive passive : doll.getPassives()) {
//                    //passive.setTimeLeft(passive.getTimeLeft() - 1);
//                }
//
//                List<Integer> remove = new ArrayList<>();
//                //Goes through all the passives the T-Doll has...
//                /*for (int index = 0; index < doll.getPassives().size(); index++) {
//                    //...and if the passive has expired...
//                    if (doll.getPassives().get(index).getTimeLeft() == 0) {
//                        //...their index is added to the 'remove' list.
//                        remove.add(index);
//                    }
//                }*/
//
//
//
//
//
//
//
//
//
//
//                //tick and trigger time-based passives
//
//                //The 'remove' list is iterated through...
//                for (int index = 0; index < remove.size(); index++) {
//                    //...and it's values are used to remove the expired passives.
//                    //doll.getPassives().remove((int) remove.get(index));
//                }
//
//                List<Passive> temp = new ArrayList<>();
//                for (Passive passive : doll.getPassives()) {
//                    //if (passive.isInterval()) temp.add(passive);
//                }
//
//                /*for (Passive passive : temp) {
//                    int interval;
//                    if (passive.getAmountOfIntervals() > 1) {
//                        interval = passive.getInterval(1);
//                    } else {
//                        interval = passive.getInterval(doll.getSkillLevel());
//                    }
//
//                    if ((currentFrame - passive.getStartTime() % Math.floor(interval * 30) == 0 && currentFrame != 1 && interval != -1)) {
//                        //Activate the passive skill here
//                        //triggerPassive('time', doll, enemy);
//                    }
//                }*/
//
//            }
//
//            //tick fairy skill timer
//            /*if (fairy.id != -1) {
//                graphData.y[5].data.push(graphData.y[5].data[currentFrame - 1]);
//                $.each(fairy.battle.timers, (index, timer) => timer.timeLeft--);
//                $.each(fairy.battle.timers, (index, timer) => {
//                    if (timer.timeLeft == 0) {
//                        if (timer.type == 'skill') {
//                            $.each(fairy.battle.skill.effects, (i, effect) => {
//                                effect.level = fairy.skilllevel;
//                                fairy.battle.effect_queue.push(effect);
//                            });
//                            timer.timeLeft = Math.round(fairy.battle.skill.cd * 30);
//                        }
//                    }
//                });
//                fairy.battle.timers = fairy.battle.timers.filter(timer => timer.timeLeft != 0);
//            }*/
//
//            //tick/remove enemy buffs
//            /*$.each(enemy.battle.buffs, (index, buff) => {
//                if ('timeLeft' in buff) {
//                    buff.timeLeft--;
//                }
//            });
//            enemy.battle.buffs = enemy.battle.buffs.filter(buff => {
//            if ('timeLeft' in buff) {
//                if (buff.timeLeft == 0) {
//                    return false;
//                }
//            }
//            return true;
//    });*/
//
//            //apply buffs, handle effects that aren't actions
//            for(Doll doll : e.getAllDolls()){
//                if(doll.getID() == 0) return;
//
//                for(int i = 0; i < doll.getEffectQueue().size(); i++){
//                    Passive temp = (Passive)doll.getEffectQueue().get(i);//TODO: Look into reworking the queue/figuring
//                                                                         //out how they work so that you can find the
//                                                                         //correct type to assign them.
//                    /*if (action.type == 'buff') {
//                        activateBuff(doll, action, enemy);
//                    }
//                    else if (action.type == 'passive') {
//                        addPassive(doll, action, enemy, currentFrame);
//                    }
//                    else if (action.type == 'removeBuff') {
//                        removeBuff(doll, action, enemy);
//                    }
//                    else if (action.type == 'removePassive') {
//                        removePassive(doll, action, enemy);
//                    }
//                    else if (action.type == 'modifySkill') {
//                        modifySkill(doll, action, enemy, currentFrame);
//                    }
//                    else {
//                        if ('delay' in action) {
//                            action.timeLeft = $.isArray(action.delay) ? Math.round(action.delay[action.level - 1] * 30) : Math.round(action.delay * 30) + 1;
//                        }
//                        if ('busylinks' in action) {
//                            doll.battle.busylinks += Math.min(action.busylinks, doll.links);
//                        }
//                        if ('duration' in action) {
//                            action.timeLeft = $.isArray(action.duration) ? Math.round(action.duration[action.level - 1] * 30) : Math.round(action.duration * 30);
//                        }
//                        doll.battle.action_queue.push(action);
//                    }*/
//                }
//            }
//
//        }
//    }
}
