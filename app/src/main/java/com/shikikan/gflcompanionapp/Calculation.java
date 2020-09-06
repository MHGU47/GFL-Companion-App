package com.shikikan.gflcompanionapp;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Calculation {
    private Utils u;
    private Echelon echelon;
    private Enemy enemy;

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
                    doll.BS().getSkill_1Effects()[0].setFp(new float[]{4});
                break;
            case 189://K2
                doll.BS().getSkill_1().modeName = "fever";
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
                doll.BS().getSkill_1().marks = 0;
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
                    for(int i = 0; i < size; i++) temp[i] = doll.BS().getSkill_1().getEffects()[0];
                    doll.BS().setSkill_1Effects(temp);
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

                    doll.BS().addBuff(new Buff(effect));

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

                    doll.BS().getSkill_1Effects()[0] = effect;


                    //doll.battle.buffs.push(buff);
                    //doll.battle.skill.effects[0] = activeBuff;
                }
                catch (JSONException ex) {
                    ex.printStackTrace();
                }
                break;
            case 224://M82A1
                doll.BS().getSkill_1().skillUseCount = 0;
                break;
            case 227://JS9
                //Gain self buffs based on the number of enemies remaining. With only 1 group of
                //enemies left, increase self damage by 50% for 5 seconds (with 3 stacks). For each
                //additional enemy, remove 1 damage buff and add 1 evasion buff, increasing self
                //evasion by 35% for 5 seconds. Evasion buff can stack up to 6 times
                int fpstacks = enemyCount > 3 ? 0 : 4 - enemyCount;
                int evastacks = enemyCount == 1 ? 0 : Math.min(6, enemyCount - 1);
                doll.BS().getSkill_1Effects()[0].setStacks(evastacks);
                doll.BS().getSkill_1Effects()[1].setStacks(fpstacks);
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
                    doll.BS().addBuff(new Buff(effect));
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
                    doll.BS().getSkill_1Effects()[0].setFp(new float[]{0});
                    doll.BS().getSkill_1Effects()[0].setRof(new float[]{30, 36, 41, 47, 52, 58, 63, 69, 74, 80});
                    doll.BS().getSkill_1Effects()[0].setAcc(new float[]{30, 36, 41, 47, 52, 58, 63, 69, 74, 80});
                }
                else {
                    doll.BS().getSkill_1Effects()[0].setFp(new float[]{40, 46, 51, 57, 62, 68, 73, 79, 84, 90});
                    doll.BS().getSkill_1Effects()[0].setRof(new float[]{0});
                    doll.BS().getSkill_1Effects()[0].setAcc(new float[]{0});
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
                        for(int i = 0; i < size; i++)
                            doll.BS().addPassive(new Passive(passive));// TODO: Needs to be in passives
                    }
                }
                catch (JSONException ex) {
                    ex.printStackTrace();
                }
                break;
            case 249://Clear-TODO: Check how dollID is used in the JS code base
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
                    doll.BS().getSkill_1Effects()[i + 1].getAfterEffects()[0].setTarget("doll");
                    doll.BS().getSkill_1Effects()[i + 1].getAfterEffects()[0].dollID = e.getDoll(targetDolls[i]).getID();
                    if (hasSPEQ) {
                        doll.BS().getSkill_1Effects()[i + 1].getAfterEffects()[0].setFp(new float[]{20, 22, 24, 27, 29, 31, 33, 36, 38, 40});
                        doll.BS().getSkill_1Effects()[i + 1].getAfterEffects()[0].setAcc(new float[]{20, 22, 24, 27, 29, 31, 33, 36, 38, 40});
                    }
                }
                break;
            case 253://M1895 Mod 3 Nagant Revolver
                //doll.battle.skill2.icd = (doll.battle.timers.find(t => t.type == 'normalAttack').timeLeft + 3) / 30;
                break;
            case 256://Mosin-Nagant Mod 3
                //Passive: Every enemy unit killed by Mosin increases her damage by 20% for 3 seconds.
                //Killing an enemy unit with her skill 1 increases her rate of fire by 30% for 5 seconds
                if (doll.BS().getSkill_1Effects()[0].getAfterEffects()[0].getTarget().equals("self")) {
                    int[] rof = {15, 17, 18, 20, 22, 23, 25, 27, 28, 30};
                    doll.BS().getSkill_1Effects()[0].getAfterEffects()[0].setRof(new float[]{rof[doll.getSkill_2Level() - 1]});
                }
                break;
            case 259://M4A1 Mod 3
                doll.BS().getSkill_1Effects()[2].setMultiplier(new float[] {doll.BS().getSkill_1Effects()[2].getMultiplier()[doll.getSkill_2Level() - 1]});
                break;
            case 260://SOPMODII Mod 3
                doll.BS().getSkill_1Effects()[0].getAfterEffects()[1].setMultiplier(new float[]{doll.BS().getSkill_1Effects()[0].getAfterEffects()[1].getMultiplier()[doll.getSkill_2Level() - 1]});
                doll.BS().getSkill_1Effects()[0].getAfterEffects()[2].setMultiplier(new float[]{doll.BS().getSkill_1Effects()[0].getAfterEffects()[2].getMultiplier()[doll.getSkill_2Level() - 1]});
                doll.BS().getSkill_1Effects()[0].getAfterEffects()[3].setMultiplier(new float[]{doll.BS().getSkill_1Effects()[0].getAfterEffects()[3].getMultiplier()[doll.getSkill_2Level() - 1]});
                break;
            case 262://G3 Mod 3
                if(doll.BS().getSkill_1().isBuffedNade()){
                    float[] temp = new float[doll.BS().getSkill_1Effects()[0].getMultiplier().length];
                    for(int i = 0; i < doll.BS().getSkill_1Effects()[0].getMultiplier().length; i++){
                        temp[i] = doll.BS().getSkill_1Effects()[0].getMultiplier()[i] + doll.BS().getSkill_2Effects()[0].getMultiplier()[doll.getSkill_2Level() - 1];
                    }
                    doll.BS().getSkill_1Effects()[0].setMultiplier(temp);
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
                        doll.BS().setSkill_2Effects(temp);
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

                    doll.BS().addBuff(new Buff(new Effect(effectsObject)));
                    //effects[0] = new Effect(effectsObject);

                    effectsObject.put("type", "buff");
                    effectsObject.put("target", "self");
                    effectsObject.put("name", "normalAttackBuff");
                    effectsObject.put("level", doll.getSkill_2Level());
                    effectsObject.put("multiplier", new float[]{1.25f, 1.27f, 1.28f, 1.3f, 1.32f, 1.33f, 1.35f, 1.37f, 1.38f, 1.4f});
                    effectsObject.put("stacksLeft", 3);
                    effectsObject.put("duration", -1);

                    doll.BS().addBuff(new Buff(new Effect(effectsObject)));
                    //effects[1] = new Effect(effectsObject);

//                    Buff[] buffs = new Buff[2];
//                    for(int i = 0; i < 2; i++) buffs[i] = new Buff(effects[i]);
//
//                    doll.BS().addBuff(buffs);
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

                    doll.BS().addBuff(new Buff(new Effect(effectsObject)));
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

                    doll.BS().addBuff(new Buff(new Effect(effectsObject)));
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
                    doll.BS().getSkill_1().numberOfShots = 0;
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

                    doll.BS().addBuff(new Buff(new Effect(effectsObject)));
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
                float[] multiplier1 = doll.BS().getSkill_1Effects()[0].getMultiplier();
                float[] multiplier2 = doll.BS().getSkill_1Effects()[1].getMultiplier();
                float[] multiplier3 = doll.BS().getSkill_1Effects()[2].getMultiplier();
                doll.BS().getSkill_1Effects()[0].setMultiplier(new float[]{multiplier1[doll.getSkill_1Level() - 1] * grenadeDamageBonus[doll.getSkill_1Level() - 1]});
                doll.BS().getSkill_1Effects()[1].setMultiplier(new float[]{multiplier2[doll.getSkill_1Level() - 1] * grenadeDamageBonus[doll.getSkill_1Level() - 1]});
                doll.BS().getSkill_1Effects()[2].setMultiplier(new float[]{multiplier3[doll.getSkill_1Level() - 1] * grenadeDamageBonus[doll.getSkill_1Level() - 1]});
                break;
            case 291://Micro Uzi Mod 3
                doll.BS().getSkill_2Effects()[0].getAfterEffects()[0].setDuration(new float[]{doll.BS().getSkill_2Effects()[0].getDuration()[doll.getSkill_1Level() - 1]});
                break;
            case 292://Dana
                doll.BS().setTargets(1);
                try{
                    effectsObject.put("type", "buff");
                    effectsObject.put("name", "normalAttackBuff");
                    effectsObject.put("level", doll.getSkill_1Level());
                    effectsObject.put("duration", -1);
                    effectsObject.put("multiplier", new float[]{1.2f, 1.27f, 1.33f, 1.4f, 1.47f, 1.53f, 1.6f, 1.67f, 1.73f, 1.8f});

                    doll.BS().addBuff(new Buff(new Effect(effectsObject)));
                }
                catch (JSONException ex) {
                    ex.printStackTrace();
                }

//                doll.battle.targets = 1;
//                let normalAttack = {
//                        type: 'buff',
//                        name: 'normalAttackBuff',
//                        duration: -1,
//                        multiplier: [1.2, 1.27, 1.33, 1.4, 1.47, 1.53, 1.6, 1.67, 1.73, 1.8],
//                level: doll.skilllevel
//                };
//                doll.battle.buffs.push($.extend(true, {}, normalAttack));
                break;
            case 296://Jill
                doll.BS().getTimer("normalAttack").setTimeLeft(-1);

                float cooldownBonus = doll.BS().getPreBattleStat("fp") > 30 ? 30 : doll.BS().getPreBattleStat("fp");

                try{
                    effectsObject.put("type", "buff");
                    effectsObject.put("target", "self");
                    effectsObject.put("level", doll.getSkill_1Level());
                    effectsObject.put("duration", -1);

                    effectsObject.put("stat", stats);

                    stats.put("skillcd", cooldownBonus * -1);

                    doll.BS().addBuff(new Buff(new Effect(effectsObject)));
                }
                catch (JSONException ex) {
                    ex.printStackTrace();
                }
//                doll.battle.timers.find(timer => timer.type == 'normalAttack').timeLeft = -1;
//
//                let cooldownBonus = doll.base.fp > 30 ? 30 : doll.base.fp;
//                let skillcdBuff = {
//                        type: 'buff',
//                        target: 'self',
//                        level: doll.skilllevel,
//                        stat: {
//                    skillcd: cooldownBonus * -1
//                },
//                duration: -1
//};
//                doll.battle.buffs.push(skillcdBuff);

                /*if (doll.equip1 == 106 && doll.equip2 == 105 && doll.equip3 == 109) {
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
                }*/
                break;//TODO: Not finished
            case 297://Dorothy
                if (doll.getGridPosition() == 22 || doll.getGridPosition() == 23 || doll.getGridPosition() == 24) {
                    doll.BS().getSkill_1Effects()[0].setDelay(new float[]{0});
                    doll.BS().getSkill_1Effects()[0].getAfterEffects()[0].setDuration(doll.BS().getSkill_1Effects()[1].getDelay());
                    doll.BS().getSkill_1Effects()[0].getAfterEffects()[1].setDuration(doll.BS().getSkill_1Effects()[1].getDelay());
                } else {
                    doll.BS().getSkill_1Effects()[1].setDelay(new float[]{0});
                    doll.BS().getSkill_1Effects()[1].getAfterEffects()[0].setDuration(doll.BS().getSkill_1Effects()[0].getDelay());
                    doll.BS().getSkill_1Effects()[1].getAfterEffects()[1].setDuration(doll.BS().getSkill_1Effects()[0].getDelay());
                }
                break;
            case 304://MP5 Mod 3
                //int numStacks = enemyCount > 3 ? 3 : enemyCount;
                doll.BS().getSkill_1Effects()[0].getAfterEffects()[0].setStacks(enemyCount > 3 ? 3 : enemyCount);
                doll.BS().getSkill_1Effects()[0].getAfterEffects()[0].level = doll.getSkill_2Level();
                break;
            case 307://SSG3000
                doll.BS().getSkill_1().numberOfShots = 0;
                break;
            case 309://ACR
//                let singleBuff = {
//                        type:"buff",
//                        target:"self",
//                        stat:{
//                    fp:[5,6,6,7,7,8,8,9,9,10]
//                },
//                name:"acrSingleDebuffBuff",
//                        stackable: true,
//                        stacks: 0,
//                        max_stacks: 1,
//                        level: doll.skilllevel,
//                        duration:-1
//};
//                let multiBuff = {
//                        type:"buff",
//                        target:"self",
//                        stat:{
//                    fp:[3,3,3,4,4,4,4,5,5,5]
//                },
//                stackable: true,
//                        stacks: 0,
//                        max_stacks: 8,
//                        name:"acrMultipleDebuffBuff",
//                        level: doll.skilllevel,
//                        duration:-1
//};
//                doll.battle.buffs.push(singleBuff);
//                doll.battle.buffs.push(multiBuff);
                break;//TODO: Not finished
            case 310://M1895CB
                doll.BS().reserveAmmo = 30;
                doll.BS().reserveAmmoMode = false;
                break;
            case 320://MAT49
                doll.BS().getSkill_1Effects()[0].setStacks(enemyCount == 1 ? 0 : Math.min(6, enemyCount - 1));
                doll.BS().getSkill_1Effects()[1].setStacks(enemyCount > 3 ? 0 : 4 - enemyCount);
                break;
            case 323://SL8
//                let dollTypesOnTiles = [];
//                let sl8Tiles = [-20,-10,-9,-8];
//                $.each(sl8Tiles, (index, tile) => {
//                    let d = echelon.find(d => d.pos == doll.pos + tile);
//                    if (d != undefined && d.id != -1) {
//                        dollTypesOnTiles.push(d.type);
//                    }
//                });
//                let uniqueTypesOnTiles = [...new Set(dollTypesOnTiles)];
//                let fpstacks = uniqueTypesOnTiles.length > 3 ? 3 : uniqueTypesOnTiles.length;
//                doll.battle.skill.effects[1].stacks = fpstacks;
                break;//TODO: Not finished
            case 328://Webley
                for (int i = 0; i < 5; i++) {
                    if (e.getDoll(i).getID() == 0) {
                        continue;
                    }
                    if (e.getDoll(i).getGridPosition() != doll.getGridPosition()) {
                        doll.BS().getSkill_1Effects()[0].setTarget("none");
                    } else {
                        doll.BS().getSkill_1Effects()[1].setTarget("none");
                        doll.BS().getSkill_1Effects()[2].setModifySkill("none");
                    }
                    break;
                }
            case 332://AK15
                doll.BS().getSkill_1Effects()[1].setStacks(enemyCount);
                doll.BS().getSkill_1Effects()[1].setStacksToAdd(new int[]{enemyCount});
                break;
        }
    }

    private void CalculateDPS(Echelon e) {

    }

    private void SimulateBattle(Echelon e) {
        //initialise graph variables here

        for(Doll doll : e.getAllDolls()){
              doll.BS().setBattleStats();
        };
        //initialise enemies
        //initialise fairies

        float simulationLength = 30 * battleLength;
        float totaldamage8s = 0;
        float totaldamage12s = 0;
        float totaldamage20s = 0;

        //apply fairy talent effect to dolls
        //apply fortress node effect


        //walk time
        //graphData.x.push(0); This is where information for the x axis will be added
        float time = 0;
        for (time = 1; time < walkTime * 30/*frames that the game runs at standard*/; time++) {
            /*graphData.x.push(parseFloat((time / 30.0).toFixed(2)));*/

            //fairy timer altering
            /*if (fairy.id != -1) {
                graphData.y[5].data.push(graphData.y[5].data[time - 1]);
                $.each(fairy.battle.timers, (index, timer) => {
                    if (timer.timeLeft > 1) {
                        timer.timeLeft--;
                    }
                });
            }*/

            //Setting buffs and passives

            for (int i = 0; i < e.getAllDolls().length; i++) {
                //graphData.y[i].data.push(graphData.y[i].data[time - 1]);
                Doll doll = e.getDoll(i);
                if (doll.getID() == 0) continue;

                for(Timer timer : doll.BS().getTimers()){
                    if (timer.getTimeLeft() > 1) { // && timer.type != 'normalAttack' ??? need to check this assumption
                        timer.setTimeLeft(timer.getTimeLeft() - 1);
                    }
                }

                //tick and remove buffs
                for(Buff buff : doll.BS().getBuffs()){
                    if(buff.getTimeLeft() > 0){
                        buff.setTimeLeft(buff.getTimeLeft() - 1);
                    }
                }
                int[] removal = new int[doll.BS().getBuffs().size()];
                int index = 0;
                for(Buff buff : doll.BS().getBuffs()){
                    //if(buff.getTimeLeft() > 0){//If the buff still has time before it activates(?)...　(redundant)
                    if(buff.getTimeLeft() == 0){//If the buff doesn't have time before it activates(?)...
                                                //バフは始まる前時間が持っていなくないなら
                        if(buff.getEffect() != null){//...check to see if it has an effect. (redundant. Buffs should have effects)
                                                    //効果あることをチェックする
                            if(buff.getEffect().getAfterEffects() != null){//If the effect has an after effect...
                                if(buff.getEffect().getAfterEffects().length > 1){
                                    for(Effect afterEffect : buff.getEffect().getAfterEffects()){//...loop through all the effects
                                        if(afterEffect.level == 0) afterEffect.level = buff.getEffect().level;//...assign them with levels...
                                        doll.BS().addToEffectQueue(afterEffect);//...and add them to the queue.
                                        //doll.BS().addToEffectQueue_Ver2(afterEffect);
                                        doll.BS().addToQueueNames("afterEffect");
                                    }
                                }
                            }
                        }
                        removal[index] = doll.BS().getBuffs().indexOf(buff);//If the buff has activated, add it's index to the removal array
                                                                       //バフは始まっていたならバフのIndexが消すArrayにあげる(?)
                    }
                    index++;

                }
                for (int value : removal) doll.BS().getBuffs().remove(value);

                //tick and remove passives
                /*I'm not sure how this links in with the rest of the code as 'passives' don't really
                have anything referring to 'timeleft' in the original JS code.*/
                /*for(Passive passive : doll.getPassives()){
                    if(passive.getTimeLeft() > 0){
                        passive.setTimeLeft(passive.getTimeLeft() - 1);
                    }
                }
                $.each(doll.battle.passives, (index, passive) => {
                    if ('timeLeft' in passive) {
                        passive.timeLeft--;
                    }
                });
                doll.battle.passives = doll.battle.passives.filter(passive => {
                if ('timeLeft' in passive) {
                    if (passive.timeLeft == 0) {
                        return false;
                    }
                }
                return true;
      });*/

                //tick and trigger time-based passives
                for(Passive passive : doll.BS().getPassives()){
                    float interval = passive.getInterval().length > 1 ? passive.getInterval()[passive.level - 1] : passive.getInterval()[0];
                    if((time - passive.startTime) % Math.floor(interval * 30) == 0 && time != 1 && interval != -1){
                        triggerPassive("time", doll, enemy, new int[]{});
                    }
                }

                calculateSkillBonus(i);
                calculateBattleStats(i);
            }

            /*for (let i = 0; i < 5; i++) {
                graphData.y[i].data.push(graphData.y[i].data[time - 1]);
                let doll = echelon[i];
                if (doll.id == -1) continue;

                $.each(doll.battle.timers, (index, timer) => {
                    if (timer.timeLeft > 1) { // && timer.type != 'normalAttack' ??? need to check this assumption
                        timer.timeLeft--;
                    }
                });

                //tick and remove buffs
                $.each(doll.battle.buffs, (index, buff) => {
                    if ('timeLeft' in buff) {
                        buff.timeLeft--;
                    }
                });
                doll.battle.buffs = doll.battle.buffs.filter(buff => {
                if ('timeLeft' in buff) {
                    if (buff.timeLeft == 0) {
                        if ('after' in buff) {
                            if ($.isArray(buff.after)) {
                                $.each(buff.after, (index, effect) => {
                                    if (!('level' in effect))
                                    effect.level = buff.level;
                                    doll.battle.effect_queue.push($.extend(true, {}, effect));
                                });
                            } else {
                                if (!('level' in buff.after))
                                buff.after.level = buff.level;
                                doll.battle.effect_queue.push($.extend(true, {}, buff.after));
                            }
                        }
                        return false;
                    }
                }
                return true;
      });

                //tick and remove passives
                $.each(doll.battle.passives, (index, passive) => {
                    if ('timeLeft' in passive) {
                        passive.timeLeft--;
                    }
                });
                doll.battle.passives = doll.battle.passives.filter(passive => {
                if ('timeLeft' in passive) {
                    if (passive.timeLeft == 0) {
                        return false;
                    }
                }
                return true;
      });

                //tick and trigger time-based passives
                $.each(doll.battle.passives.filter(passive => 'interval' in passive), (index, passiveskill) => {
                    let interval = $.isArray(passiveskill.interval) ? passiveskill.interval[passiveskill.level - 1] : passiveskill.interval;
                    if ((time - passiveskill.startTime) % Math.floor(interval * 30) == 0 && time != 1 && interval != -1) {
                        triggerPassive('time', doll, enemy);
                    }
                });

                calculateSkillBonus(i);
                calculateBattleStats(i);
            }*/
        }//TODO: Double check this JS code to make sure everything has been converted as needed

        //Battle stage
        //graphData.x.push((time / 30.0).toFixed(2)); Add information to the graph x axis
        float currentFrame = time;
        for (currentFrame = time; currentFrame < simulationLength; currentFrame++) {
            //graphData.x.push(parseFloat((currentFrame / 30.0).toFixed(2))); Add information to the graph x axis

            //tick timers, queue actions
            for (int i = 0; i < e.getAllDolls().length; i++) {
                Doll doll = e.getDoll(i);
                if (doll.getID() == 0) return;

                //graphData.y[i].data.push(graphData.y[i].data[currentFrame - 1]); Add information to the graph y axis

                //Normal attack timing
                for (Timer timer : doll.BS().getTimers()) {
                    if (timer.getType().equals("normal attack")) {
                        boolean reloading = doll.BS().getTimer("reloading") != null;//Figure out where reload timers come into play
                        boolean stunned = doll.BS().getTimer("stunned") != null;
                        if (u.Links(doll.getLevel()) - doll.getBusyLinks() > 0 && !reloading && !stunned)
                            timer.setTimeLeft(timer.getTimeLeft() - 1);
                    }
                    else
                        if (timer.getTimeLeft() > 0)
                            timer.setTimeLeft(timer.getTimeLeft() - 1);
                }

                //Skill timing
                //Skill Timers/Queues
                for (Timer timer : doll.BS().getTimers()) {
                    if (timer.getTimeLeft() == 0) {
                        boolean reloading = doll.BS().getTimer("reloading") != null;
                        boolean stunned = doll.BS().getTimer("stunned") != null;
                        if (timer.getType().equals("skill")) {
                            //If the T-Doll is unable to move...
                            if (stunned || (reloading && doll.BS().getTimer("reload").getTimeLeft() != 0)) {
                                //...add 1 frame to the timer, effectively pausing it.
                                timer.setTimeLeft(timer.getTimeLeft() + 1);
                            }
                            else {
                                //factor in skills
                                for(Effect effect : doll.BS().getSkill_1().getEffects()){
                                    if(effect.level != 0) effect.level = doll.getSkill_1Level();
                                    if(effect.getType().equals("loadRounds")){
                                        List<Doll> targets = getBuffTargets(doll, new Effect[]{effect}, enemy);//TODO Figure out where 'targets' comes into play
                                        for(Doll d : targets){
                                            d.BS().currentRounds += effect.getRounds().length > 1 ? effect.getRounds()[effect.level - 1] : effect.getRounds()[0];
                                        }
                                    }
                                    else {
                                        doll.BS().addToEffectQueue(effect);
                                        //doll.BS().addToEffectQueue_Ver2(effect);
                                        doll.BS().addToQueueNames("effect");
                                    }
                                }
                                timer.setTimeLeft(Math.round(doll.BS().getSkill_1().getCD()[doll.getSkill_1Level() - 1] * 30 * doll.BS().CD));
                                //timer.timeLeft = Math.round(doll.battle.skill.cd[doll.skilllevel - 1] * 30 * doll.battle.skillcd);
                                /*$.each(doll.battle.skill.effects, (index, effect) => {
                                    if (!('level' in effect)) {
                                        effect.level = doll.skilllevel;
                                    }
                                    if (effect.type == 'loadRounds') {
                                        let targets = getBuffTargets(doll, effect, enemy);
                                        $.each(targets, (index, target) => {
                                            target.battle.currentRounds += $.isArray(effect.rounds) ? effect.rounds[effect.level - 1] : effect.rounds;
                                        });
                                    } else {
                                        doll.battle.effect_queue.push($.extend({}, effect));
                                    }
                                });
                                timer.timeLeft = Math.round(doll.battle.skill.cd[doll.skilllevel - 1] * 30 * doll.battle.skillcd);*/
                            }//TODO: Double check with JS code
                        }
                        else if (timer.getType().equals("skill2")) {
                            //If the T-Doll is unable to move...
                            if (stunned || (reloading && doll.BS().getTimer("reload").getTimeLeft() != 0)) {
                                //...add 1 frame to the timer, effectively pausing it.
                                timer.setTimeLeft(timer.getTimeLeft() + 1);
                            } else {
                                //factor in skills
                                for(Effect effect : doll.BS().getSkill_2().getEffects()){
                                    if(effect.level != 0) effect.level = doll.getSkill_2Level();
                                    if(effect.getType().equals("loadRounds")){
                                        List<Doll> targets = getBuffTargets(doll, new Effect[]{effect}, enemy);//TODO Figure out where 'targets' comes into play
                                        for(Doll d : targets){
                                            d.BS().currentRounds += effect.getRounds().length > 1 ? effect.getRounds()[effect.level - 1] : effect.getRounds()[0];
                                        }
                                    }
                                    else {
                                        doll.BS().addToEffectQueue(effect);
                                        //doll.BS().addToEffectQueue_Ver2(effect);
                                        doll.BS().addToQueueNames("effect");
                                    }
                                }
                                timer.setTimeLeft(Math.round(doll.BS().getSkill_2().getCD()[doll.getSkill_2Level() - 1] * 30 * doll.BS().CD));
                                //timer.timeLeft = Math.round(doll.battle.skill.cd[doll.skilllevel - 1] * 30 * doll.battle.skillcd);
                                /*$.each(doll.battle.skill.effects, (index, effect) => {
                                    if (!('level' in effect)) {
                                        effect.level = doll.skilllevel;
                                    }
                                    if (effect.type == 'loadRounds') {
                                        let targets = getBuffTargets(doll, effect, enemy);
                                        $.each(targets, (index, target) => {
                                            target.battle.currentRounds += $.isArray(effect.rounds) ? effect.rounds[effect.level - 1] : effect.rounds;
                                        });
                                    } else {
                                        doll.battle.effect_queue.push($.extend({}, effect));
                                    }
                                });
                                timer.timeLeft = Math.round(doll.battle.skill.cd[doll.skilllevel - 1] * 30 * doll.battle.skillcd);*/
                            }
                        }
                        else {
                            //add to effect queue
                            //doll.BS().addToEffectQueue_Ver2(timer);// TODO Find a way to add timers to the effect queue
                            doll.BS().addToEffectQueue(timer);
                            doll.BS().addToQueueNames("timer");
                        }
                    }
                }

                //Possible duplicate?
                /*for(Timer timer : doll.BS().getTimers()){
                    if(timer.getTimeLeft() == 0){
                        boolean reloading = doll.BS().getTimer("reload") == null;
                        boolean stunned = doll.BS().getTimer("stun") == null;
                        if(timer.getType().equals("skill")){
                            if(stunned || (reloading && doll.BS().getTimer("reload").getTimeLeft() != 0)){
                                timer.setTimeLeft(timer.getTimeLeft() + 1);
                            }
                            else{
                                for(Effect effect : doll.getSkill_1().getEffects()){
                                    if(effect.level != 0){
                                        effect.level = doll.getSkill_1Level();
                                    }
                                    if(effect.getType().equals("loadRounds")){
                                        List<Doll> targets = getBuffTargets(doll, effect, enemy);//TODO Figure out where 'targets' comes into play
                                        for(Doll d : targets){
                                            d.BS().currentRounds += effect.getRounds().length > 1 ? effect.getRounds()[effect.level - 1] : effect.getRounds()[0];
                                        }
                                    }
                                    else{
                                        doll.BS().addToEffectQueue(effect);
                                    }
                                }
                                timer.setTimeLeft(Math.round(doll.BS().getSkill_1().getCD()[doll.getSkill_1Level() - 1] * 30 * doll.BS().CD));
                            }
                        }
                        else if(timer.getType().equals("skill2")){
                            if(stunned || (reloading && doll.BS().getTimer("reload").getTimeLeft() != 0)){
                                timer.setTimeLeft(timer.getTimeLeft() + 1);
                            }
                            else{
                                for(Effect effect : doll.getSkill_2().getEffects()){
                                    if(effect.level != 0){
                                        effect.level = doll.getSkill_2Level();
                                    }
                                    if(effect.getType().equals("loadRounds")){
                                        List<Doll> targets = getBuffTargets(doll, effect, enemy);//TODO Figure out where 'targets' comes into play
                                        for(Doll d : targets){
                                            d.BS().currentRounds += effect.getRounds().length > 1 ? effect.getRounds()[effect.level - 1] : effect.getRounds()[0];
                                        }
                                    }
                                    else{
                                        doll.BS().addToEffectQueue(effect);
                                    }
                                }
                                timer.setTimeLeft(Math.round(doll.BS().getSkill_2().getCD()[doll.getSkill_2Level() - 1] * 30 * doll.BS().CD));
                            }
                        }
                        else{
                            //doll.BS().addToEffectQueue(timer);// TODO Find a way to add timers to the effect queue
                        }
                    }
                }*/
                /*$.each(doll.battle.timers, (index, timer) => {
                    if (timer.timeLeft == 0) {
                        let reloading = doll.battle.timers.find(timer => timer.type == 'reload') === undefined ? false : true;
                        let stunned = doll.battle.buffs.find(b => 'stun' in b) === undefined ? false : true;
                        if (timer.type == 'skill') {
                            if (stunned || (reloading && doll.battle.timers.find(timer => timer.type == 'reload').timeLeft != 0)) {
                                timer.timeLeft++;
                            } else {
                                $.each(doll.battle.skill.effects, (index, effect) => {
                                    if (!('level' in effect)) {
                                        effect.level = doll.skilllevel;
                                    }
                                    if (effect.type == 'loadRounds') {
                                        let targets = getBuffTargets(doll, effect, enemy);
                                        $.each(targets, (index, target) => {
                                            target.battle.currentRounds += $.isArray(effect.rounds) ? effect.rounds[effect.level - 1] : effect.rounds;
                                        });
                                    } else {
                                        doll.battle.effect_queue.push($.extend({}, effect));
                                    }
                                });
                                timer.timeLeft = Math.round(doll.battle.skill.cd[doll.skilllevel - 1] * 30 * doll.battle.skillcd);
                            }
                        } else if (timer.type == 'skill2') {
                            if (stunned || (reloading && doll.battle.timers.find(timer => timer.type == 'reload').timeLeft != 0)) {
                                timer.timeLeft++;
                            } else {
                                $.each(doll.battle.skill2.effects, (index, effect) => {
                                    if (!('level' in effect)) {
                                        effect.level = doll.skill2level;
                                    }
                                    if (effect.type == 'loadRounds') {
                                        let targets = getBuffTargets(doll, effect, enemy);
                                        $.each(targets, (index, target) => {
                                            target.battle.currentRounds += $.isArray(effect.rounds) ? effect.rounds[effect.level - 1] : effect.rounds;
                                        });
                                    } else {
                                        doll.battle.effect_queue.push($.extend({}, effect));
                                    }
                                });
                                timer.timeLeft = Math.round(doll.battle.skill2.cd[doll.skill2level - 1] * 30 * doll.battle.skillcd);
                            }
                        } else {
                            doll.battle.effect_queue.push($.extend({}, timer));
                        }
                    }
                });*/


                //remove expired timers - Why? Do I need this
                //doll.battle.timers = doll.battle.timers.filter(timer => timer.timeLeft != 0);
                int timersize = doll.BS().getTimers().length;
                for(int ii = 0; ii < timersize; ii++){
                    if (doll.BS().getTimers()[ii].getTimeLeft() == 0){
                        //TODO: Remove Timers here
                    }
                }

                //tick and remove buffs
                for (Buff buff : doll.BS().getBuffs()) {
                    buff.setTimeLeft(buff.getTimeLeft() - 1);
                }

                //Removes buffs from the active buffs queue or something.
                int[] removal = new int[doll.BS().getBuffs().size()];
                int index = 0;
                for(Buff buff : doll.BS().getBuffs()){
                    //if(buff.getTimeLeft() > 0){//If the buff still has time before it activates(?)...　(redundant)
                    if(buff.getTimeLeft() == 0){//If the buff doesn't have time before it activates(?)...
                        //バフは始まる前時間が持っていなくないなら
                        if(buff.getEffect() != null){//...check to see if it has an effect. (redundant. Buffs should have effects)
                            //効果あることをチェックする
                            if(buff.getEffect().getAfterEffects() != null){//If the effect has an after effect...
                                for(Effect afterEffect : buff.getEffect().getAfterEffects()){//...loop through all the effects
                                    if(afterEffect.level == 0) afterEffect.level = buff.getEffect().level;//...assign them with levels...
                                    //doll.BS().addToEffectQueue(afterEffect);//...and add them to the queue.
                                    doll.BS().addToEffectQueue(buff);
                                    //doll.BS().addToEffectQueue_Ver2(afterEffect);
                                    doll.BS().addToQueueNames("buff");
                                }
                            }
                        }
                        removal[index] = doll.BS().getBuffs().indexOf(buff);//If the buff has activated, add it's index to the removal array
                        //バフは始まっていたならバフのIndexが消すArrayにあげる(?)
                    }
                    index++;
                    //}

                }
                for (int value : removal) doll.BS().getBuffs().remove(value);

                //tick and remove passives
                //TODO Check how passives interact with the rest of the code
                for (Passive passive : doll.getPassives()) passive.timeleft--;

                removal = new int[doll.BS().getPassives().size()];
                index = 0;
                for(Passive passive : doll.BS().getPassives()){
                    if(passive.timeleft == 0) index++;
                }
                for (int value : removal) doll.BS().getPassives().remove(value);

                //List<Integer> remove = new ArrayList<>();
                //Goes through all the passives the T-Doll has...
                /*for (int index = 0; index < doll.getPassives().size(); index++) {
                    //...and if the passive has expired...
                    if (doll.getPassives().get(index).getTimeLeft() == 0) {
                        //...their index is added to the 'remove' list.
                        remove.add(index);
                    }
                }*/

                //tick and trigger time-based passives

                //The 'remove' list is iterated through...
                /*for (int index = 0; index < remove.size(); index++) {
                    //...and it's values are used to remove the expired passives.
                    //doll.getPassives().remove((int) remove.get(index));
                }*/

                for(Passive passive : doll.BS().getPassives()){
                    for(Effect effect : passive.getEffects()){
                        float interval = effect.getInterval() > 1 ? passive.getInterval()[passive.level - 1] : passive.getInterval()[0];
                        if((currentFrame - passive.startTime) % Math.floor(interval * 30) == 0 && currentFrame != 1 && interval != -1){
                            triggerPassive("timer", doll, enemy, new int[]{});
                        }
                    }
                }

            }

            //tick fairy skill timer
            /*if (fairy.id != -1) {
                graphData.y[5].data.push(graphData.y[5].data[currentFrame - 1]);
                $.each(fairy.battle.timers, (index, timer) => timer.timeLeft--);
                $.each(fairy.battle.timers, (index, timer) => {
                    if (timer.timeLeft == 0) {
                        if (timer.type == 'skill') {
                            $.each(fairy.battle.skill.effects, (i, effect) => {
                                effect.level = fairy.skilllevel;
                                fairy.battle.effect_queue.push(effect);
                            });
                            timer.timeLeft = Math.round(fairy.battle.skill.cd * 30);
                        }
                    }
                });
                fairy.battle.timers = fairy.battle.timers.filter(timer => timer.timeLeft != 0);
            }*/

            //tick/remove enemy buffs
            /*$.each(enemy.battle.buffs, (index, buff) => {
                if ('timeLeft' in buff) {
                    buff.timeLeft--;
                }
            });
            enemy.battle.buffs = enemy.battle.buffs.filter(buff => {
            if ('timeLeft' in buff) {
                if (buff.timeLeft == 0) {
                    return false;
                }
            }
            return true;
    });*/

            //apply buffs, handle effects that aren't actions
            for(Doll doll : e.getAllDolls()){
                if(doll.getID() == 0) continue;

                int size = doll.BS().getEffectQueue_Ver2().size();
                for(int i = 0; i < size; i++){
                    //Queue temp = doll.BS().getEffectQueue_Ver2().get(i);//TODO: Look into reworking the queue
                    //TODO: Maybe consider making the list a string array that can store a unique name
                    //or something to the object that needs to be called.
                    //Object t = doll.BS().getEffectQueue().get(i);

                    switch(doll.BS().getQueueNames().get(i)){
                        case "timer":
                            Timer timer = doll.BS().getEffectQueue_Timer().get(0);
                            switch(timer.getType()) {
                                case "buff":
                                    //activateBuff(doll, timer, enemy);
                                    doll.BS().getEffectQueue_Timer().remove(0);
                                    break;
                                case "passive":
                                    //addPassive(doll, timer, enemy);
                                    doll.BS().getEffectQueue_Timer().remove(0);
                                    break;
                                case "removeBuff":
                                    //removeBuff(doll, timer, enemy);
                                    doll.BS().getEffectQueue_Timer().remove(0);
                                    break;
                                case "removePassive":
                                    //removePassive(doll, timer, enemy);
                                    doll.BS().getEffectQueue_Timer().remove(0);
                                    break;
                                case "modifySkill":
                                    //modifySkill(doll, timer, enemy, currentFrame);
                                    doll.BS().getEffectQueue_Timer().remove(0);
                                    break;
                                default:
                                    /*if ('delay' in action) {
                                    action.timeLeft = $.isArray(action.delay) ? Math.round(action.delay[action.level - 1] * 30) : Math.round(action.delay * 30) + 1;
                                    }
                                    if ('busylinks' in action) {
                                        doll.battle.busylinks += Math.min(action.busylinks, doll.links);
                                    }
                                    if ('duration' in action) {
                                        action.timeLeft = $.isArray(action.duration) ? Math.round(action.duration[action.level - 1] * 30) : Math.round(action.duration * 30);
                                    }
                                    doll.battle.action_queue.push(action);*/
                                    //Most likely isn't needed for timers
                                    break;
                            }
                            break;//TODO: Figure out how timers would be used in the queue. Possibly only skills should be in the action queue, as timers only need to be added in as names only?
                        case "afterEffect"://TODO: Maybe consider adding buffs as a whole to the queue instead of just their effects
                        case "effect":
                            Effect effect = doll.BS().getEffectQueue().get(0);
                            switch(effect.getType()){
                                //case "buff": activateBuff(doll, effect, enemy);
                                //case "passive": addPassive(doll, effect, enemy);
                                //case "removeBuff": removeBuff(doll, effect, enemy);
                                //case "removePassive": removePassive(doll, effect, enemy);
                                case "modifySkill":
                                    modifySkill(doll, effect, enemy, currentFrame);
                                    doll.BS().getEffectQueue().remove(0);
                                    break;
                                default:
                                    //(effect.getDelay() != null)
                                    //if ('delay' in action) {
                                    //action.timeLeft = $.isArray(action.delay) ? Math.round(action.delay[action.level - 1] * 30) : Math.round(action.delay * 30) + 1;
                                    //}
                                    //if ('busylinks' in action) {
                                    //    doll.battle.busylinks += Math.min(action.busylinks, doll.links);
                                    //}
                                    //if ('duration' in action) {
                                    //    action.timeLeft = $.isArray(action.duration) ? Math.round(action.duration[action.level - 1] * 30) : Math.round(action.duration * 30);
                                    //}

                                    //doll.BS().addToActionNames(effect.getType());
                                    //doll.BS().addToActionQueue(effect);
                                    //doll.BS().getEffectQueue_Buff().remove(0);
                                    //TODO Is this needed here?
                                    break;
                            }
                        default:
                            Buff buff = doll.BS().getEffectQueue_Buff().get(0);
                            switch(buff.getEffect().getType()){
                                case "buff":
                                    activateBuff(doll, buff, enemy);
                                    doll.BS().getEffectQueue_Buff().remove(0);
                                    break;
                                //case "passive": addPassive(doll, effect, enemy);
                                case "removeBuff":
                                    removeBuff(doll, buff, enemy);
                                    doll.BS().getEffectQueue_Buff().remove(0);
                                    break;
                                //case "removePassive": removePassive(doll, effect, enemy);
                                //case "modifySkill": modifySkill(doll, effect, enemy);
                                default:
                                    if(buff.getEffect().getDelay() != null)
                                        buff.setTimeLeft(buff.getEffect().getDelay().length > 1 ? Math.round(buff.getEffect().getDelay()[buff.getEffect().level - 1] * 30) : Math.round((buff.getEffect().getDelay()[0] * 30)) + 1);
                                    if(buff.getEffect().getBusyLinks() != 0)
                                        doll.BS().setBusyLinks(doll.BS().getBusyLinks() + Math.min(buff.getEffect().getBusyLinks(), u.Links(doll.getLevel())));
                                    if (buff.getEffect().getDuration() != null)
                                        buff.setTimeLeft(buff.getEffect().getDuration().length > 1 ? Math.round(buff.getEffect().getDuration()[buff.getEffect().level - 1] * 30) : Math.round(buff.getEffect().getDuration()[0] * 30));
                                    //doll.BS().action_queue.push(action);

                                    //TODO: set up the action queue
                                    //TODO: YOU ARE HERE
                                    doll.BS().addToActionNames(buff.getEffect().getType());
                                    doll.BS().addToActionQueue(buff);
                                    doll.BS().getEffectQueue_Buff().remove(0);
                                break;
                            }
                    }
                }
            }

            //apply buffs, handle effects that aren't actions
            /*for (let i = 0; i < 5; i++) {
                let doll = echelon[i];
                if (doll.id == -1) continue;

                let len = doll.battle.effect_queue.length;
                for (let j = 0; j < len; j++) {
                    let action = doll.battle.effect_queue.shift();

                    if (action.type == 'buff') {
                        activateBuff(doll, action, enemy);
                    } else if (action.type == 'passive') {
                        addPassive(doll, action, enemy, currentFrame);
                    } else if (action.type == 'removeBuff') {
                        removeBuff(doll, action, enemy);
                    } else if (action.type == 'removePassive') {
                        removePassive(doll, action, enemy);
                    } else if (action.type == 'modifySkill') {
                        modifySkill(doll, action, enemy, currentFrame);
                    } else {
                        if ('delay' in action) {
                            action.timeLeft = $.isArray(action.delay) ? Math.round(action.delay[action.level - 1] * 30) : Math.round(action.delay * 30) + 1;
                        }
                        if ('busylinks' in action) {
                            doll.battle.busylinks += Math.min(action.busylinks, doll.links);
                        }
                        if ('duration' in action) {
                            action.timeLeft = $.isArray(action.duration) ? Math.round(action.duration[action.level - 1] * 30) : Math.round(action.duration * 30);
                        }
                        doll.battle.action_queue.push(action);
                    }
                }
            }*/

            //fairy queue handling
            /*if (fairy.id != -1) {
                let len = fairy.battle.effect_queue.length;
                for (let j = 0; j < len; j++) {
                    let action = fairy.battle.effect_queue.shift();

                    if (action.type == 'buff') {
                        activateBuff(fairy, action, enemy);
                    } else {
                        fairy.battle.action_queue.push(action);
                    }
                }
            }*/

            //recalculate stats to include all buffs
            for(int i = 0; i < e.getAllDolls().length; i++){
                if(e.getDoll(i).getID() != 0){
                    calculateSkillBonus(i);
                    calculateBattleStats(i);
                }
            }
            //calculateEnemyStats(enemy);

            //perform actions
            for(Doll doll : e.getAllDolls()){
                if(doll.getID() == 0) continue;

                float dmg = 0;
                int queueLength = doll.BS().getActionQueueNames().size();

                for(int i = 0; i < queueLength; i++){
                    switch(doll.BS().getActionQueueNames().get(i)){
                        case "normalAttack":
                        case "reload"://Timers
                            Buff attackBuff = null;
                            for(Buff b : doll.BS().getBuffs()){
                                if(b.getEffect().getName().equals("normalAttackBuffs")){
                                    attackBuff = b.getEffect().getName().equals("normalAttackBuffs") ? b : null;
                                    break;
                                }
                            }
                            int shotMultiplier = doll.getID() == 294 ? 2 : 1;

                            if(attackBuff != null){
                                /*let canCrit = 'canCrit' in attackBuff ? attackBuff.canCrit : true;
                                let sureCrit = 'sureCrit' in attackBuff ? attackBuff.sureCrit : false;
                                let sureHit = 'sureHit' in attackBuff ? attackBuff.sureHit : false;
                                let piercing = 'piercing' in attackBuff ? attackBuff.piercing : false;*/

                                boolean canCrit = !attackBuff.getEffect().isCanCrit() || attackBuff.getEffect().isCanCrit();
                                boolean sureCrit = attackBuff.getEffect().isSureCrit() && attackBuff.getEffect().isSureCrit();

                                float multiplier = 1;
                                if(attackBuff.getEffect().getMultiplier() != null){
                                    multiplier = attackBuff.getEffect().getMultiplier().length > 1 ? attackBuff.getEffect().getMultiplier()[attackBuff.getEffect().level - 1] : attackBuff.getEffect().getMultiplier()[0];
                                }
                                dmg = Math.max(shotMultiplier, doll.BS().fp * multiplier + shotMultiplier/* * Math.min(2, doll.BS().ap - enemy.BS().armour)*/);//TODO Finish this

                                if(!sureCrit){
                                    dmg *= (doll.BS().acc/* / (doll.BS().acc + enemy.BS().eva)*/);//TODO Finish this
                                }

                                if(canCrit){
                                    float critDmg = doll.BS().critDmg;
                                    //if(attackBuff.getEffect().extraCritDamage)//TODO Stella excluisve, part of her passive. Make a function for handling this?
                                    /*if ('extraCritDamage' in attackBuff) {
                                        critdmg += $.isArray(attackBuff.extraCritDamage) ? attackBuff.extraCritDamage[attackBuff.level - 1] : attackBuff.extraCritDamage;
                                    }*/

                                    dmg *= sureCrit ? (1 + (critDmg / 100)) : (1 + critDmg / 100) * (doll.BS().crit / 100) + (1 - doll.BS().crit / 100);
                                }
                                //dmg *= enemy.BS().vulnerability;//TODO Finish enemy class
                                dmg *= u.Links(doll.getLevel()) - doll.BS().getBusyLinks();

                                int hitCount = 1;
                                if(attackBuff.getEffect().getHitCount() != null){
                                    hitCount = attackBuff.getEffect().getHitCount().length > 1 ? attackBuff.getEffect().getHitCount()[attackBuff.getEffect().level - 1] : attackBuff.getEffect().getHitCount()[0];
                                }

                                dmg *= hitCount;

                                doll.BS().setHits((int) (doll.BS().getHits() + Math.floor((u.Links(doll.getLevel()) - doll.BS().getBusyLinks()) * hitCount * shotMultiplier * (doll.BS().acc/* / (doll.BS().acc + enemy.BS().eva)*/))));
                                doll.BS().setTotalShots((int) (doll.BS().getTotalShots() + Math.floor((u.Links(doll.getLevel()) - doll.BS().getBusyLinks()) * hitCount * shotMultiplier)));

                                if(doll.getType() == 6){
                                    if(attackBuff.getEffect().getTarget() != null && !doll.hasSlug()){
                                        dmg *= 1 /*Math.min(attackBuff.getEffect().getTargets(), enemy.count)*/;//TODO Finish enemys
                                    }
                                    else{
                                        dmg *= 1 /*Math.min(doll.BS().getTargets(), enemy.count)*/;//TODO Finish enemys
                                    }
                                }

                                if(attackBuff.getEffect().isAoe()){
                                    float dameji = Math.max(1, doll.BS().fp + Math.min(2, doll.BS().ap - enemy.BS().armour));
                                    if(attackBuff.getEffect().getAoe_multiplier() != null)
                                        dameji *= attackBuff.getEffect().getAoe_multiplier().length > 1 ? attackBuff.getEffect().getAoe_multiplier()[attackBuff.getEffect().level - 1] : attackBuff.getEffect().getAoe_multiplier()[0];

                                    boolean aoe_sureCrit;
                                    boolean aoe_canCrit;
                                    //boolean aoe_sureCrit;

                                    /*let aoe_sureHit = 'aoe_sureHit' in attackBuff ? attackBuff.aoe_sureHit : true;
                                    let aoe_canCrit = 'aoe_canCrit' in attackBuff ? attackBuff.aoe_canCrit : false;
                                    let aoe_sureCrit = 'aoe_sureCrit' in attackBuff ? attackBuff.aoe_sureCrit : false;*/

                                    /*if (!aoe_sureHit) {
                                        damage *= (doll.battle.acc / (doll.battle.acc + enemy.battle.eva));
                                    }

                                    if (aoe_canCrit) {
                                        if (aoe_sureCrit) {
                                            damage *= 1 + (doll.battle.critdmg / 100);
                                        } else {
                                            damage *= 1 + (doll.battle.critdmg * (doll.battle.crit / 100) / 100);
                                        }
                                    }*/

                                    dameji *= u.getNumEnemyLinksHit(attackBuff.getEffect().getAoe_radius(), enemy.count, isBoss);

                                    dmg += dameji;
                                }

                                if(piercing){
                                    float pierceDamage = dmg * (enemy.count - 1) / 2;
                                    dmg += pierceDamage;
                                }
                            }
                            else{
                                dmg = Math.max(shotMultiplier, doll.BS().fp + shotMultiplier * Math.min(2, doll.BS().ap - enemy.BS().armour));
                                dmg *= (doll.BS().acc / (doll.BS().acc + enemy.BS().eva));
                                dmg *= 1 + (doll.BS().critDmg * (doll.BS().crit / 100) / 100);
                                dmg *= enemy.BS().vulnerability;
                                dmg *= u.Links(doll.getLevel()) - doll.BS().getBusyLinks();
                                if(doll.getType() == 6){
                                    dmg = dmg * Math.min(doll.BS().getTargets(), enemy.count);
                                }
                                doll.BS().setHits(doll.BS().getHits() + Math.floor((u.Links(doll.getLevel()) - doll.BS().getBusyLinks()) * shotMultiplier * (doll.BS().acc / (doll.BS().acc + enemy.BS().eva))));
                                doll.BS().setTotalShots((int) (doll.BS().getTotalShots() + Math.floor((u.Links(doll.getLevel()) - doll.BS().getBusyLinks() * shotMultiplier))));
                            }

                            //handle pkp
                            float extradmg = 0;
                            Passive afterAttack = null;
                            for(Passive passive : doll.BS().getPassives()){
                                if(passive.getTrigger().equals("afterAttack")) {
                                    afterAttack = passive;
                                    break;
                                }
                            }
                            if (afterAttack != null) {
                                Effect extraAttack = afterAttack.getEffects()[0];
                                //let canCrit = 'canCrit' in extraAttack ? extraAttack.canCrit : true;
                                //let sureCrit = 'sureCrit' in extraAttack ? extraAttack.sureCrit : false;
                                //let sureHit = 'sureHit' in extraAttack ? extraAttack.sureHit : false;
                                //TODO Finish these

                                float extraAttackFP = doll.BS().fp;
                                if (extraAttack.getMultiplier() != null) {
                                    extraAttackFP *= extraAttack.getMultiplier().length > 1 ? extraAttack.getMultiplier()[extraAttack.level - 1] : extraAttack.getMultiplier()[0];
                                }
                                extradmg = Math.max(1, extraAttackFP + Math.min(2, doll.BS().ap - enemy.BS().armuor));

                                extradmg *= !sureHit ? (doll.BS().acc / (doll.BS().acc + enemy.BS().eva)) : 1;
                                if (canCrit) {
                                    extradmg *= sureCrit ? (1 + (doll.BS().critDmg / 100)) : 1 + (doll.BS().critDmg * (doll.BS().crit / 100) / 100);
                                }
                                float extraShots = 1;
                                int extraTotalShots = 1;
                                if (extraAttack.getHitCount() != null) {
                                    extradmg *= extraAttack.getHitCount().length > 1 ? extraAttack.getHitCount()[extraAttack.level - 1] : extraAttack.getHitCount()[0];
                                    extraShots *= extraAttack.getHitCount().length > 1 ? extraAttack.getHitCount()[extraAttack.level - 1] : extraAttack.getHitCount()[0];
                                    extraTotalShots *= extraAttack.getHitCount().length > 1 ? extraAttack.getHitCount()[extraAttack.level - 1] : extraAttack.getHitCount()[0];
                                }

                                extraShots *= sureHit? u.Links(doll.getLevel()) - doll.BS().getBusyLinks() :  Math.floor ((u.Links(doll.getLevel()) - doll.BS().getBusyLinks()) * (doll.BS().acc / (doll.BS().acc + enemy.BS().eva)));
                                extraTotalShots = u.Links(doll.getLevel()) - doll.BS().getBusyLinks();

                                if (extraAttack.getExtraAttackChance() != null) {
                                    extraShots *= (double) extraAttack.getExtraAttackChance()[extraAttack.level - 1] / 100;
                                }
                                doll.BS().setHits((int) (doll.BS().getHits() + Math.floor(extraShots)));
                                doll.BS().setTotalShots(doll.BS().getTotalShots() + extraTotalShots);

                                extradmg *= enemy.BS().vulnerability;
                                extradmg *= u.Links(doll.getLevel()) - doll.BS().getBusyLinks();
                                extradmg *= extraAttack.getExtraAttackChance() != null ? extraAttack.getExtraAttackChance()[extraAttack.level - 1] / 100f : 1;
                            }

                            dmg += extradmg;

                            if(doll.BS().rounds > 0){
                                doll.BS().rounds--;
                                if(doll.BS().currentRounds == 1){
                                    triggerPassive("lastShot", doll, enemy, new int[]{0});
                                }
                                if(doll.BS().currentRounds == 0){
                                    triggerPassive("outOfAmmo", doll, enemy, new int[]{0});
                                }

                                Buff ARmodeBuff = null;
                                for(Buff b : doll.BS().getBuffs()){
                                    if(b.getEffect().getName().equals("ARmode")){
                                        ARmodeBuff = b;
                                        break;
                                    }
                                }

                                if(doll.BS().currentRounds == 0 && ARmodeBuff == null){
                                    Timer reloadTimer = new Timer("reload", 0);

                                    if(doll.getType() == 5){ //MGs reload
                                        reloadTimer.setTimeLeft((int) Math.floor(30 * (4 + 200 / doll.BS().rof)));//TODO Figure out whether reload timers should have ints or floats
                                    }
                                    else if(doll.getType() == 6){ //SGs reload
                                        reloadTimer.setTimeLeft((int) Math.floor((30 * (1.4 * 0.5 * doll.BS().rounds))));
                                    }
                                    else{//Falcon reload formula
                                        reloadTimer.setTimeLeft((int) Math.floor(30 * (120 / (doll.BS().rof + 10))) + 30);
                                    }

                                    Buff reloadBuff = null;
                                    for(Buff b : doll.BS().getBuffs()){
                                        if(b.getEffect().getName().equals("reloadBuff")){
                                            reloadBuff = b;
                                            break;
                                        }
                                    }

                                    if(reloadBuff != null){
                                        if(reloadBuff.getEffect().getFixedTime() != 0){
                                            reloadTimer.setTimeLeft(reloadTimer.getTimeLeft() * reloadBuff.getEffect().getMultiplier().length > 1 ? Math.pow((reloadBuff.getEffect().getMultiplier()[reloadBuff.getEffect().level - 1] / 100 + 1), reloadBuff.getEffect().getStacks()) : Math.pow(reloadBuff.getEffect().getMultiplier()[0] / 100 + 1, reloadBuff.getEffect().getStacks());
                                        }
                                        else{
                                            reloadTimer.setTimeLeft((int) (reloadTimer.getTimeLeft() * reloadBuff.getEffect().getMultiplier().length > 1 ? ((reloadBuff.getEffect().getMultiplier()[reloadBuff.getEffect().level - 1]) / 100) + 1 : (reloadBuff.getEffect().getMultiplier()[0] / 100) + 1));
                                        }

                                        if(reloadBuff.getEffect().getSetTime() != null){
                                            reloadTimer.setTimeLeft((int) (reloadBuff.getEffect().getSetTime().length > 1 ? Math.floor(reloadBuff.getEffect().getSetTime()[reloadBuff.getEffect().level - 1] * 30) : Math.floor(30 * reloadBuff.getEffect().getSetTime()[0])));
                                        }

                                        if(reloadBuff.getEffect().getUses() != 0){
                                            reloadBuff.getEffect().setUses(reloadBuff.getEffect().getUses() - 1);
                                            if(reloadBuff.getEffect().getUses() == 0){
                                                reloadBuff.setTimeLeft(1);
                                            }
                                        }

                                        reloadTimer.setTimeLeft((int) Math.floor(reloadTimer.getTimeLeft()));
                                    }
                                    if(reloadTimer.getTimeLeft() != 0){
                                        doll.BS().addTimer(reloadTimer);
                                        triggerPassive("reloadStart", doll, enemy, new int[]{0});
                                    }
                                    else{
                                        doll.BS().currentRounds += doll.BS().rounds;
                                    }
                                }
                            }

                            Timer normalAttackTimer = new Timer("normalAttack", 0);

                            normalAttackTimer.setTimeLeft(doll.getFramesPerAttack() != 0 ? doll.getFramesPerAttack() : (int) Math.floor(50 * 30 / doll.BS().rof));

                            Buff ARMode = null;
                            for(Buff b : doll.BS().getBuffs()){
                                if(ARMode.getEffect().getName().equals("ARmode")) {
                                    ARMode = b;
                                    break;
                                }
                            }

                            Buff Sweep = null;
                            for(Buff b : doll.BS().getBuffs()){
                                if(Sweep.getEffect().getName().equals("sweep")) {
                                    Sweep = b;
                                    break;
                                }
                            }

                            if(ARMode != null) normalAttackTimer.setTimeLeft((int) Math.floor(50 * 30 / doll.BS().rof));
                            if(Sweep != null) normalAttackTimer.setTimeLeft(10);

                            doll.BS().addTimer(normalAttackTimer);//TODO: Check to see if this conflicts with any other timer
                            doll.BS().numOfAttacks++;

                            List<Buff> limitedAttackBuffs = new ArrayList<>();
                            int[] removal = new int[doll.BS().getBuffs().size()];
                            int index = 0;
                            for(Buff buff : doll.BS().getBuffs()){
                                if(buff.getEffect().getTrigger() != null){
                                    if(buff.getEffect().getTrigger().equals("attacksLeft"))
                                        limitedAttackBuffs.add(buff);
                                }
                            }

                            for(Buff buff : limitedAttackBuffs){
                                buff.getEffect().setAttacksLeft(buff.getEffect().getAttacksLeft() - 1);
                            }

                            for(Buff buff : doll.BS().getBuffs()){
                                if(buff.getEffect().getTrigger() != null){
                                    if(buff.getEffect().getTrigger().equals("attacksLeft")){
                                        if(buff.getEffect().getAttacksLeft() == 0)
                                            removal[index] = doll.BS().getBuffs().indexOf(buff);
                                    }
                                }
                                index++;
                            }

                            for (int value : removal) doll.BS().getBuffs().remove(value);


                            List<Passive> limitedAttackPassives = new ArrayList<>();
                            removal = new int[doll.BS().getPassives().size()];
                            index = 0;
                            for(Passive passive : doll.BS().getPassives()){
                                for(Effect effect : passive.getEffects()){
                                    if(effect.getTrigger() != null){
                                        if(effect.getTrigger().equals("attacksLeft"))
                                            limitedAttackPassives.add(passive);
                                    }
                                }
                            }

                            for(Passive passive : limitedAttackPassives){
                                for(Effect effect : passive.getEffects())
                                    try{
                                        effect.setAttacksLeft(effect.getAttacksLeft() - 1);
                                    }
                                    catch (Exception ex){
                                            ex.printStackTrace();
                                    }
                            }

                            for(Passive passive : doll.BS().getPassives()){
                                for(Effect effect : passive.getEffects()){
                                    if(effect.getTrigger() != null){
                                        if(effect.getTrigger().equals("attacksLeft")){
                                            if(effect.getAttacksLeft() == 0)
                                                removal[index] = doll.BS().getPassives().indexOf(passive);
                                        }
                                    }
                                }
                                index++;
                            }

                            for (int value : removal) doll.BS().getPassives().remove(value);


                            triggerPassive("normalAttack", doll, enemy, new int[]{0});

                            for(Passive passive : doll.BS().getPassives()){
                                if(passive.getTrigger().equals("everyXhits")){
                                    //int hits = passive.getHits().length > 1 ? passive.getHits()[passive.level - 1] : passive.getHits()[0];
                                    // TODO This is treated as an array in the original JS
                                    int hits = passive.getHits();
                                    if(doll.BS().numOfAttacks % hits == 0 && hits != -1)
                                        triggerPassive("everyXhits", doll, enemy, new int[]{0});
                                }
                            }

                            if (currentFrame <= 30 * 8 + 1) {
                                totaldamage8s += dmg;
                            }
                            if (currentFrame <= 30 * 12 + 1) {
                                totaldamage12s += dmg;
                            }
                            if (currentFrame <= 30 * 20 + 1) {
                                totaldamage20s += dmg;
                            }
                            //graphData.y[i].data[currentFrame] += Math.round(dmg);
                            break;
                        case "grenade":
                        case "grenadedot":
                        case "smoke":
                        case "chargedshot":
                        case "burstimpact":

                    }
                }
            }

            for (let i = 0; i < 5; i++) {
                let doll = echelon[i];
                if (doll.id == -1) continue;

                let dmg = 0;

                let len = doll.battle.action_queue.length;
                for (let j = 0; j < len; j++) {
                    let action = doll.battle.action_queue.shift();

                    if (action.type == 'normalAttack') {
                        let attackBuff = doll.battle.buffs.find(buff => buff.name == 'normalAttackBuff');
                        // Handle Stella
                        let shotmultiplier = doll.id == 294 ? 2 : 1;

                        if (attackBuff !== undefined) {
                            let canCrit = 'canCrit' in attackBuff ? attackBuff.canCrit : true;
                            let sureCrit = 'sureCrit' in attackBuff ? attackBuff.sureCrit : false;
                            let sureHit = 'sureHit' in attackBuff ? attackBuff.sureHit : false;
                            let piercing = 'piercing' in attackBuff ? attackBuff.piercing : false;

                            let multiplier = 1;
                            if ('multiplier' in attackBuff) {
                                multiplier = $.isArray(attackBuff.multiplier) ? attackBuff.multiplier[attackBuff.level - 1] : attackBuff.multiplier;
                            }
                            dmg = Math.max(shotmultiplier, doll.battle.fp * multiplier + shotmultiplier * Math.min(2, doll.battle.ap - enemy.battle.armor));

                            if (!sureHit) {
                                dmg *= (doll.battle.acc / (doll.battle.acc + enemy.battle.eva));
                            }
                            if (canCrit) {
                                let critdmg = doll.battle.critdmg;
                                if ('extraCritDamage' in attackBuff) {
                                    critdmg += $.isArray(attackBuff.extraCritDamage) ? attackBuff.extraCritDamage[attackBuff.level - 1] : attackBuff.extraCritDamage;
                                }
                                dmg *= sureCrit ? (1 + (critdmg / 100)) : (1 + critdmg / 100) * (doll.battle.crit / 100) + (1 - doll.battle.crit / 100);
                            }
                            dmg *= enemy.battle.vulnerability;
                            dmg *= doll.links - doll.battle.busylinks;

                            let hitCount = 1;
                            if ('hitCount' in attackBuff) {
                                hitCount = $.isArray(attackBuff.hitCount) ? attackBuff.hitCount[attackBuff.level - 1] : attackBuff.hitCount;
                            }
                            dmg *= hitCount;

                            doll.shots.hits += Math.floor ((doll.links - doll.battle.busylinks) * hitCount * shotmultiplier * (doll.battle.acc / (doll.battle.acc + enemy.battle.eva)));
                            doll.shots.total += Math.floor((doll.links - doll.battle.busylinks) * hitCount * shotmultiplier);

                            if (doll.type == 6) { //sg
                                if (('targets' in attackBuff) && (!doll.hasSlug)) {
                                    dmg = dmg * Math.min(attackBuff.targets, enemy.count);
                                } else {
                                    dmg = dmg * Math.min(doll.battle.targets, enemy.count);
                                }
                            }

                            if ('aoe' in attackBuff) {
                                let damage = Math.max(1, doll.battle.fp + Math.min(2, doll.battle.ap - enemy.battle.armor));
                                // damage *= (doll.battle.acc / (doll.battle.acc + enemy.battle.eva));
                                if ('aoe_multiplier' in attackBuff) {
                                    damage *= $.isArray(attackBuff.aoe_multiplier) ? attackBuff.aoe_multiplier[attackBuff.level - 1] : attackBuff.aoe_multiplier;
                                }

                                let aoe_sureHit = 'aoe_sureHit' in attackBuff ? attackBuff.aoe_sureHit : true;
                                let aoe_canCrit = 'aoe_canCrit' in attackBuff ? attackBuff.aoe_canCrit : false;
                                let aoe_sureCrit = 'aoe_sureCrit' in attackBuff ? attackBuff.aoe_sureCrit : false;

                                if (!aoe_sureHit) {
                                    damage *= (doll.battle.acc / (doll.battle.acc + enemy.battle.eva));
                                }

                                if (aoe_canCrit) {
                                    if (aoe_sureCrit) {
                                        damage *= 1 + (doll.battle.critdmg / 100);
                                    } else {
                                        damage *= 1 + (doll.battle.critdmg * (doll.battle.crit / 100) / 100);
                                    }
                                }


                                damage *= damageUtils.getNumEnemyLinksHit(attackBuff.aoe_radius, enemy.count, isBoss);

                                dmg += damage;
                            }

                            if (piercing) {
                                //temporary solution: hit half of the enemies on the field, not including the target
                                //need to decide on how to determine number of enemies hit by a piercing attack
                                let piercedamage = dmg * (enemy.count - 1) / 2;
                                dmg += piercedamage;
                            }

                            if ('modifySkill' in attackBuff) {
                                modifySkill(doll, attackBuff, enemy, currentFrame);
                            }
                        } else {

                            dmg = Math.max(shotmultiplier, doll.battle.fp + shotmultiplier * Math.min(2, doll.battle.ap - enemy.battle.armor));
                            dmg *= (doll.battle.acc / (doll.battle.acc + enemy.battle.eva));
                            dmg *= 1 + (doll.battle.critdmg * (doll.battle.crit / 100) / 100);
                            dmg *= enemy.battle.vulnerability;
                            dmg *= doll.links - doll.battle.busylinks;
                            if (doll.type == 6) { //sg
                                dmg = dmg * Math.min(doll.battle.targets, enemy.count);
                            }
                            doll.shots.hits += Math.floor ((doll.links - doll.battle.busylinks) * shotmultiplier * (doll.battle.acc / (doll.battle.acc + enemy.battle.eva)));
                            doll.shots.total += Math.floor((doll.links - doll.battle.busylinks) * shotmultiplier);
                        }

                        //handle pkp
                        let extradmg = 0;
                        let afterAttack = doll.battle.passives.find(passive => passive.trigger == 'afterAttack');
                        if (afterAttack !== undefined) {
                            let extraAttack = afterAttack.effects[0];
                            let canCrit = 'canCrit' in extraAttack ? extraAttack.canCrit : true;
                            let sureCrit = 'sureCrit' in extraAttack ? extraAttack.sureCrit : false;
                            let sureHit = 'sureHit' in extraAttack ? extraAttack.sureHit : false;

                            let extraAttackFP = doll.battle.fp;
                            if ('multiplier' in extraAttack) {
                                extraAttackFP *= $.isArray(extraAttack.multiplier) ? extraAttack.multiplier[extraAttack.level - 1] : extraAttack.multiplier;
                            }
                            extradmg = Math.max(1, extraAttackFP + Math.min(2, doll.battle.ap - enemy.battle.armor));

            /* pretty sure the multiplier for all skills are applied to fp directly
            if ('multiplier' in extraAttack) {
              extradmg *= $.isArray(extraAttack.multiplier) ? extraAttack.multiplier[extraAttack.level - 1] : extraAttack.multiplier;
            }
            */

                            extradmg *= !sureHit ? (doll.battle.acc / (doll.battle.acc + enemy.battle.eva)) : 1;
                            if (canCrit) {
                                extradmg *= sureCrit ? (1 + (doll.battle.critdmg / 100)) : 1 + (doll.battle.critdmg * (doll.battle.crit / 100) / 100);
                            }

                            let extrashots = 1;
                            let extratotalshots = 1;
                            if ('hitCount' in extraAttack) {
                                extradmg *= $.isArray(extraAttack.hitCount) ? extraAttack.hitCount[extraAttack.level - 1] : extraAttack.hitCount;
                                extrashots *= $.isArray(extraAttack.hitCount) ? extraAttack.hitCount[extraAttack.level - 1] : extraAttack.hitCount;
                                extratotalshots *= $.isArray(extraAttack.hitCount) ? extraAttack.hitCount[extraAttack.level - 1] : extraAttack.hitCount;
                            }

                            extrashots *= sureHit? doll.links - doll.battle.busylinks :  Math.floor ((doll.links - doll.battle.busylinks) * (doll.battle.acc / (doll.battle.acc + enemy.battle.eva)));
                            extratotalshots = doll.links - doll.battle.busylinks;

                            if ('extraAttackChance' in extraAttack) {
                                extrashots *= extraAttack.extraAttackChance[extraAttack.level - 1] / 100;
                            }
                            doll.shots.hits += Math.floor(extrashots);
                            doll.shots.total += extratotalshots;

                            extradmg *= enemy.battle.vulnerability;
                            extradmg *= doll.links - doll.battle.busylinks;
                            extradmg *= 'extraAttackChance' in extraAttack ? extraAttack.extraAttackChance[extraAttack.level - 1] / 100 : 1;
                        }
                        dmg += extradmg;

                        if (doll.battle.rounds > 0) {
                            doll.battle.currentRounds--;

                            if (doll.battle.currentRounds == 1) {
                                triggerPassive('lastShot', doll, enemy);
                            }
                            if (doll.battle.currentRounds == 0) {
                                triggerPassive('outOfAmmo', doll, enemy);
                            }

                            if (doll.battle.currentRounds == 0 && doll.battle.buffs.find(buff => buff.name == 'ARmode') == undefined) {
                                let reloadTimer = {
                                        type: 'reload',
                                        timeLeft: 0
              };
                                if (doll.type == 5) {
                                    //mg reload formula
                                    reloadTimer.timeLeft = Math.floor(30 * (4 + 200 / doll.battle.rof));
                                } else if (doll.type == 6) {
                                    //sg reload formula
                                    reloadTimer.timeLeft = Math.floor(30 * (1.4 + 0.5 * doll.battle.rounds));
                                } else {
                                    //falcon reload formula
                                    reloadTimer.timeLeft = Math.floor(30 * (120 / (doll.battle.rof + 10))) + 30;
                                }

                                let reloadBuff = doll.battle.buffs.find(buff => buff.name == 'reloadBuff');
                                if (reloadBuff !== undefined) {
                                    if ('fixedTime' in reloadBuff) {
                                        reloadTimer.timeLeft += $.isArray(reloadBuff.fixedTime) ? Math.floor(reloadBuff.fixedTime[reloadBuff.level - 1] * 30) : Math.floor(30 * reloadBuff.fixedTime);
                                    }
                                    if ('multiplier' in reloadBuff) {
                                        if ('stackable' in reloadBuff) {
                                            reloadTimer.timeLeft *= $.isArray(reloadBuff.multiplier) ? Math.pow((reloadBuff.multiplier[reloadBuff.level - 1] / 100 + 1), reloadBuff.stacks) : Math.pow(reloadBuff.multiplier / 100 + 1, reloadBuff.stacks);
                                        } else {
                                            reloadTimer.timeLeft *= $.isArray(reloadBuff.multiplier) ? ((reloadBuff.multiplier[reloadBuff.level - 1]) / 100) + 1 : (reloadBuff.multiplier / 100) + 1;
                                        }
                                    }

                                    if ('setTime' in reloadBuff) {
                                        reloadTimer.timeLeft = $.isArray(reloadBuff.setTime) ? Math.floor(reloadBuff.setTime[reloadBuff.level - 1] * 30) : Math.floor(30 * reloadBuff.setTime);
                                    }

                                    if ('uses' in reloadBuff) {
                                        reloadBuff.uses--;
                                        if (reloadBuff.uses == 0) {
                                            reloadBuff.timeLeft = 1;
                                        }
                                    }

                                    reloadTimer.timeLeft = Math.floor(reloadTimer.timeLeft);
                                }
                                if (reloadTimer.timeLeft != 0) {
                                    doll.battle.timers.push(reloadTimer);
                                    triggerPassive('reloadStart', doll, enemy);
                                } else {
                                    doll.battle.currentRounds += doll.battle.rounds;
                                }
                            }
                        }

                        // TODO: add check for reloadtimer. if exists, do not add normalattacktimer <- why???
                        let normalAttackTimer = {
                                type: 'normalAttack',
                                timeLeft: 0
          };
                        normalAttackTimer.timeLeft = 'frames_per_attack' in doll.battle ? doll.battle.frames_per_attack : Math.floor(50 * 30 / doll.battle.rof);
                        if (doll.battle.buffs.find(buff => buff.name == 'ARmode') !== undefined) {
                            normalAttackTimer.timeLeft = Math.floor(50 * 30 / doll.battle.rof);
                        }
                        if (doll.battle.buffs.find(buff => buff.name == 'sweep') !== undefined) {
                            normalAttackTimer.timeLeft = 10;
                        }
                        doll.battle.timers.push(normalAttackTimer);

                        doll.battle.numAttacks++;


                        let limitedAttackBuffs = doll.battle.buffs.filter(buff => 'attacksLeft' in buff);
                        $.each(limitedAttackBuffs, (index, buff) => buff.attacksLeft--);
                        doll.battle.buffs = doll.battle.buffs.filter(buff => {
                        if ('attacksLeft' in buff) {
                            if (buff.attacksLeft == 0) {
                                return false;
                            }
                        }
                        return true;
          });

                        let limitedAttackPassives = doll.battle.passives.filter(passive => 'attacksLeft' in passive);
                        $.each(limitedAttackPassives, (index, passive) => passive.attacksLeft--);
                        doll.battle.passives = doll.battle.passives.filter(passive => {
                        if ('attacksLeft' in passive) {
                            if (passive.attacksLeft == 0) {
                                return false;
                            }
                        }
                        return true;
          });

                        triggerPassive('normalAttack', doll, enemy);

                        $.each(doll.battle.passives.filter(p => p.trigger == 'everyXhits'), (index, passive) => {
                            let hits = $.isArray(passive.hits) ? passive.hits[passive.level - 1] : passive.hits;
                            if (doll.battle.numAttacks % hits == 0 && hits != -1) {
                                triggerPassive('everyXhits', doll, enemy);
                            }
                        });


                        if (currentFrame <= 30 * 8 + 1) {
                            totaldamage8s += dmg;
                        }
                        if (currentFrame <= 30 * 12 + 1) {
                            totaldamage12s += dmg;
                        }
                        if (currentFrame <= 30 * 20 + 1) {
                            totaldamage20s += dmg;
                        }
                        graphData.y[i].data[currentFrame] += Math.round(dmg);
                    }

                    if (action.type == 'reload') {
                        doll.battle.currentRounds += doll.battle.rounds;

                        //apparently the counter for terminating barrage MGs resets when they reload
                        doll.battle.numAttacks = 1;

                        triggerPassive('reload', doll, enemy);
                        //add normalAttackTimer here
                    }

                    if (action.type == 'grenade') {
                        if ('delay' in action) {
                            if (action.timeLeft != 0) {
                                action.timeLeft--;
                                doll.battle.action_queue.push(action);
                                continue;
                            }
                        }

                        let sureHit = 'sureHit' in action ? action.sureHit : true;
                        let canCrit = 'canCrit' in action ? action.canCrit : false;
                        let ignoreArmor = 'ignoreArmor' in action ? action.ignoreArmor : true;

                        if (!('multiplier' in action)) {
                            dmg = doll.battle.fp;
                        } else {
                            dmg = $.isArray(action.multiplier) ? doll.battle.fp * action.multiplier[action.level - 1] : doll.battle.fp * action.multiplier;
                        }
                        if (!ignoreArmor) {
                            dmg = Math.max(1, dmg + Math.min(2, doll.battle.ap - enemy.battle.armor));
                        }
                        if (!sureHit) {
                            dmg *= (doll.battle.acc / (doll.battle.acc + enemy.battle.eva));
                        }
                        if (canCrit) {
                            dmg *= damageUtils.getExpectedCritDamageMultiplier(doll, action);
                        }

                        dmg *= enemy.battle.vulnerability;

                        dmg *= damageUtils.getNumEnemyLinksHit(action.radius, enemy.count, isBoss);

                        if (enemy.count >= 3) {
                            triggerPassive('hit3ormore', doll, enemy);
                        } else {
                            triggerPassive('hitlessthanthree', doll, enemy);
                        }

                        // maybe a custom skill control for choosing how many nades overlap
                        // would be better
                        if (doll.id == 229) {
                            //k11
                            if (isBoss) {
                                // Overlapping nades deal 6x instead of 4x, 50% boost
                                let additionalDmg = dmg * (doll.links - 1) * 0.5;
                                dmg = dmg * doll.links + additionalDmg;
                            } else {
                                dmg *= doll.links;
                            }
                        }

                        doll.battle.busylinks -= Math.min(action.busylinks, doll.links);

                        if ('after' in action) {
                            if ($.isArray(action.after)) {
                                $.each(action.after, (index, effect) => {
                                    effect.level = action.level;
                                    doll.battle.effect_queue.push(effect);
                                });
                            } else {
                                action.after.level = action.level;
                                doll.battle.effect_queue.push(action.after);
                            }
                        }

                        doll.battle.skilldamage += Math.round(dmg);

                        if (currentFrame <= 30 * 8 + 1) {
                            totaldamage8s += dmg;
                        }
                        if (currentFrame <= 30 * 12 + 1) {
                            totaldamage12s += dmg;
                        }
                        if (currentFrame <= 30 * 20 + 1) {
                            totaldamage20s += dmg;
                        }
                        graphData.y[i].data[currentFrame] += Math.round(dmg);
                    }

                    if (action.type == 'grenadedot') {
                        action.timeLeft--;

                        if (action.timeLeft % action.tick == 0) {
                            dmg = $.isArray(action.multiplier) ? doll.battle.fp * action.multiplier[action.level - 1] : doll.battle.fp * action.multiplier;
                            //grenades ignore Armor
                            //grenades cant miss
                            //grenades cant crit
                            dmg *= enemy.battle.vulnerability;

                            if ('fixedDamage' in action) {
                                dmg = $.isArray(action.fixedDamage) ? action.fixedDamage[action.level - 1] : action.fixedDamage;
                            }

                            dmg *= damageUtils.getNumEnemyLinksHit(action.radius, enemy.count, isBoss);
                        }

                        if (action.timeLeft != 0) {
                            doll.battle.action_queue.push(action);
                        }

                        doll.battle.skilldamage += Math.round(dmg);

                        if (currentFrame <= 30 * 8 + 1) {
                            totaldamage8s += dmg;
                        }
                        if (currentFrame <= 30 * 12 + 1) {
                            totaldamage12s += dmg;
                        }
                        if (currentFrame <= 30 * 20 + 1) {
                            totaldamage20s += dmg;
                        }
                        graphData.y[i].data[currentFrame] += Math.round(dmg);
                    }

                    if (action.type == 'smoke' || action.type == 'stun') {
                        if ('delay' in action) {
                            if (action.timeLeft != 0) {
                                action.timeLeft--;
                                doll.battle.action_queue.push(action);
                                continue;
                            }
                        }

                        if ('after' in action) {
                            if ($.isArray(action.after)) {
                                $.each(action.after, (index, effect) => {
                                    if (!('level' in effect))
                                    effect.level = action.level;
                                    doll.battle.effect_queue.push(effect);
                                });
                            } else {
                                if (!('level' in action.after))
                                action.after.level = action.level;
                                doll.battle.effect_queue.push(action.after);
                            }
                        }

                        doll.battle.busylinks -= Math.min(action.busylinks, doll.links);
                    }

                    if (action.type == 'chargedshot') {
                        if (action.timeLeft != 0) {
                            action.timeLeft--;
                            doll.battle.action_queue.push(action);
                            continue;
                        }

                        //unless specified, charged shots cannot miss and cannot crit and ignore armor
                        let sureHit = 'sureHit' in action ? action.sureHit : true;
                        let canCrit = 'canCrit' in action ? action.canCrit : false;
                        let ignoreArmor = 'ignoreArmor' in action ? action.ignoreArmor : true;

                        dmg = $.isArray(action.multiplier) ? doll.battle.fp * action.multiplier[action.level - 1] : doll.battle.fp * action.multiplier;
                        if (!('multiplier' in action)) {
                            dmg = doll.battle.fp;
                        }
                        if (!ignoreArmor) {
                            dmg = Math.max(1, dmg + Math.min(2, doll.battle.ap - enemy.battle.armor));
                        }

                        if (!sureHit) {
                            dmg *= (doll.battle.acc / (doll.battle.acc + enemy.battle.eva));
                        }
                        if (canCrit) {
                            dmg *= damageUtils.getExpectedCritDamageMultiplier(doll, action);
                        }
                        dmg *= enemy.battle.vulnerability;
                        dmg *= doll.battle.busylinks;

                        doll.shots.hits += sureHit? doll.battle.busylinks : Math.floor (doll.battle.busylinks * (doll.battle.acc / (doll.battle.acc + enemy.battle.eva)));
                        doll.shots.total += doll.battle.busylinks;

                        if ('piercing' in action) {
                            dmg *= enemy.count + 1;
                        }
                        if ('skillDamageBonus' in action) {
                            let skillbonus = $.isArray(action.skillDamageBonus) ? 1 + (action.skillDamageBonus[action.level - 1] / 100) : 1 + (action.skillDamageBonus / 100);
                            if ('victories' in action) {
                                skillbonus = Math.pow(skillbonus, action.victories);
                            }
                            dmg *= skillbonus;
                        }

                        doll.battle.busylinks -= Math.min(action.busylinks, doll.links);

                        if ('after' in action) {
                            action.after.level = action.level;
                            if (action.after.type == 'buff') {
                                activateBuff(doll, action.after, enemy);
                            } else {
                                doll.battle.effect_queue.push(action.after);
                            }
                        }

                        if ('modifySkill' in action) {
                            modifySkill(doll, action, enemy, currentFrame);
                        }

                        doll.battle.skilldamage += Math.round(dmg);

                        if (currentFrame <= 30 * 8 + 1) {
                            totaldamage8s += dmg;
                        }
                        if (currentFrame <= 30 * 12 + 1) {
                            totaldamage12s += dmg;
                        }
                        if (currentFrame <= 30 * 20 + 1) {
                            totaldamage20s += dmg;
                        }
                        graphData.y[i].data[currentFrame] += Math.round(dmg);
                    }

                    if (action.type == 'burstimpact') {
                        if ('delay' in action) {
                            if (action.timeLeft != 0) {
                                action.timeLeft--;
                                doll.battle.action_queue.push(action);
                                continue;
                            }
                        }

                        let sureHit = 'sureHit' in action ? action.sureHit : true;
                        let canCrit = 'canCrit' in action ? action.canCrit : true;

                        dmg = $.isArray(action.multiplier) ? doll.battle.fp * action.multiplier[action.level - 1] : doll.battle.fp * action.multiplier;
                        dmg = Math.max(1, dmg + Math.min(2, doll.battle.ap - enemy.battle.armor));
                        if (!sureHit) {
                            dmg *= (doll.battle.acc / (doll.battle.acc + enemy.battle.eva));
                        }
                        if (canCrit) {
                            dmg *= damageUtils.getExpectedCritDamageMultiplier(doll, action);
                        }
                        if ('fixedDamage' in action) {
                            dmg = $.isArray(action.fixedDamage) ? action.fixedDamage[action.level - 1] : action.fixedDamage;
                        }
                        dmg *= enemy.battle.vulnerability;

                        if ('busylinks' in action) {
                            dmg *= doll.battle.busylinks;
                            doll.shots.hits += sureHit? doll.battle.busylinks : doll.battle.busylinks * (doll.battle.acc / (doll.battle.acc + enemy.battle.eva));
                            doll.shots.total += doll.battle.busylinks;
                        } else {
                            dmg *= doll.links;
                            doll.shots.hits += sureHit? doll.links : doll.links * (doll.battle.acc / (doll.battle.acc + enemy.battle.eva));
                            doll.shots.total += doll.links;
                        }

                        if (!('targets' in action)) {
                            dmg = dmg * Math.min(doll.battle.targets, enemy.count);
                        } else {
                            dmg = dmg * Math.min(action.targets, enemy.count);
                        }

                        if ('busylinks' in action) {
                            doll.battle.busylinks -= Math.min(action.busylinks, doll.links);
                        }

                        if (currentFrame <= 30 * 8 + 1) {
                            totaldamage8s += dmg;
                        }
                        if (currentFrame <= 30 * 12 + 1) {
                            totaldamage12s += dmg;
                        }
                        if (currentFrame <= 30 * 20 + 1) {
                            totaldamage20s += dmg;
                        }
                        graphData.y[i].data[currentFrame] += Math.round(dmg);
                    }
                }
            }
        }

    }

    /*private void handleTimersForBuffsEffects(Timer timer, Doll doll){
        if (timer.getType().equals("buff")) {
            activateBuff(doll, timer, enemy);
        }
        else if (timer.getType().equals("passive")) {
            addPassive(doll, timer, enemy, currentFrame);
        }
        else if (timer.type == 'removeBuff') {
            removeBuff(doll, timer, enemy);
        }
        else if (timer.type == 'removePassive') {
            removePassive(doll, timer, enemy);
        }
        else if (action.type == 'modifySkill') {
            modifySkill(doll, timer, enemy, currentFrame);
        }
        else {
            if ('delay' in timer) {
                action.timeLeft = $.isArray(action.delay) ? Math.round(action.delay[action.level - 1] * 30) : Math.round(action.delay * 30) + 1;
            }
            if ('busylinks' in action) {
                doll.battle.busylinks += Math.min(action.busylinks, doll.links);
            }
            if ('duration' in action) {
                action.timeLeft = $.isArray(action.duration) ? Math.round(action.duration[action.level - 1] * 30) : Math.round(action.duration * 30);
            }
            doll.battle.action_queue.push(action);
        }
    }*/

    private void triggerPassive(String trigger, Doll doll, Enemy enemy, int[] triggerChance){
        if(doll.BS().getPassives() != null){
            for(Passive passive : doll.BS().getPassives()){
                if(passive.getTrigger().equals(trigger)){
                    for(Effect effect : passive.getEffects()){
                        if(effect.getType().equals("buff")){
                            Buff buff = new Buff(effect);
                            if(triggerChance != null) buff.getEffect().setStackChance(triggerChance);
                            activateBuff(doll, buff, enemy);
                        }
                        else if(effect.getType().equals("loadRounds")) {
                            List<Doll> targets = getBuffTargets(doll, new Effect[]{effect}, enemy);//TODO Figure out where 'targets' comes into play
                        }
                        else {
                            //doll.BS().addToEffectQueue_Ver2(effect);
                            //doll.BS().addToEffectQueue(effect);
                            doll.BS().addToEffectQueue(passive);
                            doll.BS().addToQueueNames("passive");
                        }
                    }
                }
            }
        }
    }

    private List<Doll> getBuffTargets(Doll doll, Effect[] buffEffects, Enemy enemy) {
        List<Doll> targetsDolls = new ArrayList<>();
        List<Enemy> targetsEnemies = new ArrayList<>();
        for(Effect buffEffect : buffEffects){
            if (buffEffect.getTarget().equals("all")) {
                for (int i = 0; i < 5; i++) {
                    if (echelon.getDoll(i).getID() != 0) {
                        targetsDolls.add(echelon.getDoll(i));
                    }
                }
            }

            if (buffEffect.getTarget().equals("leader")) {
                for (int i = 0; i < 5; i++) {
                    if (echelon.getDoll(i).getID() != 0) {
                        targetsDolls.add(echelon.getDoll(i));
                        break;
                    }
                }
            }

            if (buffEffect.getTarget().equals("enemy")) {
                targetsEnemies.add(enemy);
            }

            if (buffEffect.getTarget().equals("self")) {
                targetsDolls.add(doll);
            }

            if (buffEffect.getTarget().equals("tiles")) {
                int[] tiles = new int[]{doll.getTilesFormation().length};
                for(int i = 0; i < tiles.length; i++){
                    for(int tile : tiles){
                        if(tile == echelon.getDoll(i).getGridPosition() && echelon.getDoll(i).getID() != 0)
                            targetsDolls.add(echelon.getDoll(i));
                    }
                }
            }

            if (buffEffect.getTarget().equals("selfandtiles")) {
                int[] tiles = new int[]{doll.getTilesFormation().length};
                for(int i = 0; i < tiles.length; i++){
                    for(int tile : tiles){
                        if(tile == echelon.getDoll(i).getGridPosition() && echelon.getDoll(i).getID() != 0)
                            targetsDolls.add(echelon.getDoll(i));
                    }
                }
                targetsDolls.add(doll);
            }

            if (buffEffect.getTarget().equals("tilesHGSMG")) {
                int[] tiles = new int[]{doll.getTilesFormation().length};
                for(int i = 0; i < tiles.length; i++){
                    for(int tile : tiles){
                        if(tile == echelon.getDoll(i).getGridPosition() && echelon.getDoll(i).getID() != 0)
                            if(echelon.getDoll(i).getType() == 1 || echelon.getDoll(i).getType() == 2)
                                targetsDolls.add(echelon.getDoll(i));
                    }
                }
            }

            if (buffEffect.getTarget().equals("tilesARRF")) {
                int[] tiles = new int[]{doll.getTilesFormation().length};
                for(int i = 0; i < tiles.length; i++){
                    for(int tile : tiles){
                        if(tile == echelon.getDoll(i).getGridPosition() && echelon.getDoll(i).getID() != 0)
                            if(echelon.getDoll(i).getType() == 3 || echelon.getDoll(i).getType() == 4)
                                targetsDolls.add(echelon.getDoll(i));
                    }
                }
            }

            if (buffEffect.getTarget().equals("tilesSGMG")) {
                int[] tiles = new int[]{doll.getTilesFormation().length};
                for(int i = 0; i < tiles.length; i++){
                    for(int tile : tiles){
                        if(tile == echelon.getDoll(i).getGridPosition() && echelon.getDoll(i).getID() != 0)
                            if(echelon.getDoll(i).getType() == 5 || echelon.getDoll(i).getType() == 6)
                                targetsDolls.add(echelon.getDoll(i));
                    }
                }
            }

            if (buffEffect.getTarget().equals("column")) {
//            let col = [-20, -10, 10, 20];
//            $.each(col, (index, distance) => {
//                let target = echelon.find(d => d.pos == doll.pos + distance && d.id != -1);
//                if (target !== undefined) {
//                    targets.push(target);
//                }
//            });

                int[] col_ = new int[]{-3, -6, 3, 6};
                for(Doll doll_ : echelon.getAllDolls()){
                    for(int tile : col_){
                        if(doll_.getGridPosition() + tile == doll.getGridPosition() && doll_.getID() != 0)
                            targetsDolls.add(doll_);
                    }
                }
            }

            if (buffEffect.getTarget().equals("front")) {
//            let dollInFront = echelon.find(d => d.pos == doll.pos + 1);
//            if (dollInFront !== undefined && dollInFront.id != -1) {
//                targets.push(dollInFront);
//            }
                int pos = doll.getGridPosition();
                int front = (pos != 3) && (pos != 6) && (pos != 9) ? 1 : 0;

                for(Doll d : echelon.getAllDolls()){
                    if((d.getGridPosition() == doll.getGridPosition() + front) && d.getID() != 0)
                        targetsDolls.add(d);
                }
            }

            if (buffEffect.getTarget().equals("behind")) {
//            let dollBehind = echelon.find(d => d.pos == doll.pos - 1);
//            if (dollBehind !== undefined && dollBehind.id != -1) {
//                targets.push(dollBehind);
//            }
                int pos = doll.getGridPosition();
                int behind = (pos != 1) && (pos != 4) && (pos != 7) ? 1 : 0;

                for(Doll d : echelon.getAllDolls()){
                    if((d.getGridPosition() == doll.getGridPosition() - behind) && d.getID() != 0)
                        targetsDolls.add(d);
                }
            }

            if (buffEffect.getTarget().equals("frontrow")) {
                for(Doll d : echelon.getAllDolls()){
                    int pos = d.getGridPosition();
                    if((pos == 9 || pos == 6 || pos == 3) && d.getID() != 0) targetsDolls.add(d);
                }
            }

            if (buffEffect.getTarget().equals("middlerow")) {
                for(Doll d : echelon.getAllDolls()){
                    int pos = d.getGridPosition();
                    if((pos == 8 || pos == 5 || pos == 2) && d.getID() != 0) targetsDolls.add(d);
                }
            }

            if (buffEffect.getTarget().equals("backrow")) {
                for(Doll d : echelon.getAllDolls()){
                    int pos = d.getGridPosition();
                    if((pos == 7 || pos == 4 || pos == 1) && d.getID() != 0) targetsDolls.add(d);
                }
            }

            if (buffEffect.getTarget().equals("frontline")) {
//            let frontColumnDolls = echelon.filter(d => d.id != -1 && (d.pos == 14 || d.pos == 24 || d.pos == 34));
//            let middleColumnDolls = echelon.filter(d => d.id != -1 && (d.pos == 13 || d.pos == 23 || d.pos == 33));
//            let backColumnDolls = echelon.filter(d => d.id != -1 && (d.pos == 12 || d.pos == 22 || d.pos == 32));
//
//            if (frontColumnDolls.length > 0) {
//                targets.push(...frontColumnDolls);
//            } else if (middleColumnDolls.length > 0) {
//                targets.push(...middleColumnDolls);
//            } else if (backColumnDolls.length > 0) {
//                targets.push(...backColumnDolls);
//            }
            }

            if (buffEffect.getTarget().equals("rearline")) {
//            let frontColumnDolls = echelon.filter(d => d.id != -1 && (d.pos == 14 || d.pos == 24 || d.pos == 34));
//            let middleColumnDolls = echelon.filter(d => d.id != -1 && (d.pos == 13 || d.pos == 23 || d.pos == 33));
//            let backColumnDolls = echelon.filter(d => d.id != -1 && (d.pos == 12 || d.pos == 22 || d.pos == 32));
//
//            if (frontColumnDolls.length > 0) {
//                targets.push(...middleColumnDolls);
//                targets.push(...backColumnDolls);
//            } else if (middleColumnDolls.length > 0) {
//                targets.push(...backColumnDolls);
//            }
            }

            if (buffEffect.getTarget().equals("doll")) {
                for(Doll d : echelon.getAllDolls()) {
                    if (d.getID() == buffEffect.dollID){
                        targetsDolls.add(d);
                        break;
                    }
                }
            }

            if (buffEffect.getTarget().equals("hg")) {
                for(Doll d : echelon.getAllDolls()){
                    if(d.getType() == 1 && d.getID() != 0)
                        targetsDolls.add(d);
                }
            }

            if (buffEffect.getTarget().equals("smg")) {
                for(Doll d : echelon.getAllDolls()){
                    if(d.getType() == 2 && d.getID() != 0)
                        targetsDolls.add(d);
                }
            }

            if (buffEffect.getTarget().equals("rf")) {
                for(Doll d : echelon.getAllDolls()){
                    if(d.getType() == 3 && d.getID() != 0)
                        targetsDolls.add(d);
                }
            }

            if (buffEffect.getTarget().equals("ar")) {
                for(Doll d : echelon.getAllDolls()){
                    if(d.getType() == 4 && d.getID() != 0)
                        targetsDolls.add(d);
                }
            }

            if (buffEffect.getTarget().equals("mg")) {
                for(Doll d : echelon.getAllDolls()){
                    if(d.getType() == 5 && d.getID() != 0)
                        targetsDolls.add(d);
                }
            }

            if (buffEffect.getTarget().equals("sg")) {
                for(Doll d : echelon.getAllDolls()){
                    if(d.getType() == 6 && d.getID() != 0)
                        targetsDolls.add(d);
                }
            }
        }


        return targetsDolls;
    }//TODO: Needs to return Enemy objects as well

    private void activateBuff(Doll doll, Buff buff, Enemy enemy){
        List<Doll> targets = getBuffTargets(doll, new Effect[]{buff.getEffect()}, enemy);

        Effect effect = buff.getEffect();

        buff.setTimeLeft((float) (effect.getDuration().length > 1 ? Math.floor(effect.getDuration()[effect.level - 1] * 30) : Math.floor(effect.getDuration()[0] * 30)));
        for(Doll d : targets){
            if(effect.isStackable()){
                for(Buff b : d.BS().getBuffs()){
                    if(b.getEffect().getName().equals(effect.getName())) addStack(d, b.getEffect(), enemy);
                }
                d.addBuff(buff);
            }
            else d.addBuff(buff);

            if(effect.isStatBuff() && !doll.getName().equals("Python") && !effect.isTriggerPythonPassive()){
                int[] triggerChance = effect.getStackChance();
                if(effect.getFp() != null){
                    triggerPassive("receivefp", d, enemy, triggerChance);
                }
                if(effect.getRof() != null){
                    triggerPassive("receiverof", d, enemy, triggerChance);
                }
                if(effect.getEva() != null){
                    triggerPassive("receiveeva", d, enemy, triggerChance);
                }
                if(effect.getAcc() != null){
                    triggerPassive("receiveacc", d, enemy, triggerChance);
                }
                if(effect.getCrit() != null){
                    triggerPassive("receivecrit", d, enemy, triggerChance);
                }
            }
        }
    }

    private void removeBuff(Doll doll, Buff buff, Enemy enemy){
        List<Doll> targets = getBuffTargets(doll, new Effect[]{buff.getEffect()}, enemy);

        for(Doll target : targets){
            int[] removal = new int[target.BS().getBuffs().size()];
            int index = 0;
            for(Buff b : target.BS().getBuffs()){
                if(b.getEffect().getName() != null){
                    if(b.getEffect().getName().equals(buff.getEffect().getName()))
                        removal[index] = target.BS().getBuffs().indexOf(b);
                }
                index++;
            }
            for (int value : removal) doll.BS().getBuffs().remove(value);
        }
    }

    private void addPassive(Doll doll, Passive passive, Enemy enemy, int currentTime){
        Passive p = new Passive(passive);

        if(p.level == 0) p.level = doll.getLevel();
        for(Effect effect : p.getEffects()) effect.level = p.level;

        if(p.getDuration() != null) p.timeleft = (int) (p.getDuration().length > 1 ? Math.floor(p.getDuration()[p.level - 1] * 30) : Math.floor(p.getDuration()[0] * 30));//TODO Consider making timeleft a float
        if(p.getInterval() != null) p.startTime = currentTime;

        if(p.getTarget() != null){
            List<Doll> targets = getBuffTargets(doll, p.getEffects(), enemy);
            for(Doll d : targets) d.BS().addPassive(p);
        }
        else{
            doll.BS().addPassive(p);
        }
    }

    private void removePassive(Doll doll, Passive passive, Enemy enemy){
        List<Doll> targets = getBuffTargets(doll, passive.getEffects(), enemy);

        for(Doll target : targets){
            int[] removal = new int[target.BS().getPassives().size()];
            int index = 0;
            for(Passive p : target.BS().getPassives()){
                if(p.getName().equals(passive.getName())) removal[index] = target.BS().getPassives().indexOf(p);
                index++;
            }
            for (int value : removal) doll.BS().getPassives().remove(value);
        }
    }

    private void modifySkill(Doll doll, Effect effect, Enemy enemy, float currentTime){
        if (doll.getID() == 199) {
            if (effect.getModifySkill().equals("addMark")) {
                doll.BS().getSkill_1().marks++;
                Buff activeBuff = null;

                for(Buff b : doll.getBuffs()){
                    if(b.getEffect().getName().equals("normalAttackBuff")){
                        activeBuff = b;
                        break;
                    }
                }

                if (activeBuff != null) activeBuff.getEffect().setAttacksLeft(activeBuff.getEffect().getAttacksLeft() + 1);
            }
            if (effect.getModifySkill().equals("removeMark")) {
                doll.BS().getSkill_1().marks--;
            }
            if (effect.getModifySkill().equals("accumulate")) {
                for(Effect effect_: doll.BS().getEffectQueue()){
                    if(effect_.getType().equals("normalAttackBuff"))
                        effect_.setAttacksLeft(doll.BS().getSkill_1().marks);
                }
            }//TODO: Double check this
        }//Ballista
    }

    /*function modifySkill(doll, effect, enemy, currentTime) {
                if (doll.id == 224) {
            //m82a1
            if (effect.modifySkill == 'usedSkill') {
                doll.battle.skillUseCount++;

                if (doll.battle.skillUseCount == 2) {
                    doll.battle.skill.effects[0].multiplier = doll.battle.skill.effects[0].multiplier.map(mult => mult * 2);
                }

                if (doll.battle.skillUseCount >= 3) {
                    doll.battle.skill.effects = [];
                }
            }
        }

        if (doll.id == 261) {
            //star mod3
            if (effect.modifySkill == 'checkAvenger') {
                let buffExists = enemy.battle.buffs.find(b => b.name == 'avenger');
                if (buffExists) {
                    let buff = doll.battle.buffs.find(b => b.name == 'normalAttackBuff');
                    buff.multiplier = [1.1, 1.12, 1.12, 1.14, 1.14, 1.16, 1.16, 1.18, 1.18, 1.2];
                }
            }
        }

        if (doll.id == 260) {
            //sop mod3
            if (effect.modifySkill == 'checkAvenger') {
                let buffExists = enemy.battle.buffs.find(b => b.name == 'avenger');
                if (buffExists) {
                    let skill2bonus = [0.15, 0.16, 0.17, 0.18, 0.19, 0.21, 0.22, 0.23, 0.24, 0.25];
                    $.each(doll.battle.effect_queue, (index, effect) => {
                        if (effect.type == 'grenade') {
                            effect.multiplier += skill2bonus[doll.skill2level - 1];
                        }
                    });
                }
            }
        }

        if (doll.id == 257) {
            //sv98 mod3
            if (effect.modifySkill == 'checkBuff') {
                let buff = doll.battle.buffs.find(b => b.name == 'sv98mod');
                if (buff !== undefined) {
                    let damagebonus = [10, 11, 12, 13, 14, 14, 15, 16, 17, 18];
                    let chargedshot = doll.battle.action_queue.find(action => action.name == 'sv98modshot');
                    if (chargedshot !== undefined) {
                        chargedshot.skillDamageBonus = damagebonus[doll.skill2level - 1];
                    }
                }
            }
            if (effect.modifySkill == 'resetTimer') {
                let passive = doll.battle.passives.find(p => p.name == 'sv98modpassive');
                passive.startTime = currentTime;
            }
        }

        if (doll.id == 189) {
            //k2
            if (effect.modifySkill == 'changeHeatStats') {
                let heatbuff = doll.battle.buffs.find(b => b.name == 'heat');
                heatbuff.stat.fp = [-3.2, -3.1, -3, -2.8, -2.7, -2.6, -2.4, -2.3, -2.2, -2];
                heatbuff.stat.acc = [-3.2, -3.1, -3, -2.8, -2.7, -2.6, -2.4, -2.3, -2.2, -2];
            }
            if (effect.modifySkill == 'changeHeatStatsDown') {
                let heatbuff = doll.battle.buffs.find(b => b.name == 'heat');
                heatbuff.stat.fp = 0;
                heatbuff.stat.acc = 0;
            }
            if (effect.modifySkill == 'singleEnemyAttackStack') {
                let singleTargetBuff = doll.battle.buffs.find(b => 'attacksOnSingle' in b);
                if (singleTargetBuff !== undefined) {
                    singleTargetBuff.attacksOnSingle++;
                    if (singleTargetBuff.attacksOnSingle > 10) {
                        singleTargetBuff.attacksOnSingle = 10;
                    }
                    singleTargetBuff.multiplier = 0.05 * singleTargetBuff.attacksOnSingle + 1;
                }
            }
            if (effect.modifySkill == 'switchMode') {
                if (doll.skill.mode == 'fever') {
                    //switch to note
                    let normalpassive = doll.battle.passives.find(p => p.trigger == 'normalAttack');
                    normalpassive.effects[0].stacksToAdd = -1;
                    doll.battle.buffs = doll.battle.buffs.filter(b => b.name != 'normalAttackBuff');
                    let sittingbuff = {
                            type: 'buff',
                            target: 'self',
                            stat: {
                        eva: [-60, -58, -56, -54, -52, -50, -48, -46, -44, -40]
                    },
                    level: doll.skilllevel,
                            duration: -1,
                            name: 'sitting'
        };
                    let singleTargetBuff = {
                            type: 'buff',
                            target: 'self',
                            name: 'normalAttackBuff',
                            attacksOnSingle: 0,
                            multiplier: 1,
                            duration: -1,
                            level: doll.skilllevel
        };
                    doll.battle.buffs.push(sittingbuff);
                    doll.battle.buffs.push(singleTargetBuff);
                    doll.skill.mode = 'note';
                } else {
                    //switch to fever
                    let normalpassive = doll.battle.passives.find(p => p.trigger == 'normalAttack');
                    normalpassive.effects[0].stacksToAdd = 1;
                    doll.battle.buffs = doll.battle.buffs.filter(b => b.name != 'sitting');
                    doll.battle.buffs = doll.battle.buffs.filter(b => !('attacksOnSingle' in b));
                    let feverbuff = {
                            type: 'buff',
                            target: 'self',
                            name: 'normalAttackBuff',
                            hitCount: 3,
                            multiplier: [0.4, 0.412, 0.424, 0.436, 0.448, 0.46, 0.472, 0.484, 0.496, 0.52],
                    duration: -1,
                            level: doll.skilllevel
        };
                    doll.battle.buffs.push(feverbuff);
                    doll.skill.mode = 'fever';
                }
            }
        }

        if (doll.id == 278) {
            //m200
            if (effect.modifySkill == 'resetShotCount') {
                doll.battle.skill.numShots = 0;
            }
            if (effect.modifySkill == 'addChargedShot') {
                doll.battle.skill.numShots++;
                let hasSniperMode = doll.battle.buffs.find(b => b.name == 'm200') !== undefined ? true : false;
                if (hasSniperMode) {
                    let chargedshot = {
                            type: 'chargedshot',
                            delay: [2.5, 2.4, 2.3, 2.2, 2.1, 1.9, 1.8, 1.7, 1.6, 1.5],
                    busylinks: 5,
                            canCrit: true,
                            ignoreArmor: false,
                            multiplier: [1.5, 1.56, 1.61, 1.67, 1.72, 1.78, 1.83, 1.89, 1.94, 2],
                    modifySkill: 'addChargedShot',
                            level: doll.skilllevel
        };

                    if (doll.battle.skill.numShots < 7) {
                        if ('delay' in chargedshot) {
                            chargedshot.timeLeft = $.isArray(chargedshot.delay) ? Math.round(chargedshot.delay[chargedshot.level - 1] * 30) : Math.round(chargedshot.delay * 30) + 1;
                            if (doll.pre_battle.skillcd > 0) {
                                chargedshot.timeLeft = Math.floor((1 - doll.pre_battle.skillcd / 100) * chargedshot.timeLeft);
                            }
                        }
                        if ('busylinks' in chargedshot) {
                            doll.battle.busylinks += Math.min(chargedshot.busylinks, doll.links);
                        }
                        doll.battle.action_queue.push(chargedshot);
                    }
                }
            }
        }

        if (doll.id == 279) {
            //Falcon
            if (effect.modifySkill == 'changePassiveTimer') {
                if (doll.battle.passives[0].interval == 6) {
                    doll.battle.passives[0].interval = 10;
                    doll.battle.passives[0].startTime = currentTime;
                }
            }

            if (effect.modifySkill == 'useSpecialAmmo') {
                let ammoBuff = doll.battle.buffs.find(b => b.name == 'falcon');
                if (ammoBuff !== undefined) {
                    if (ammoBuff.stacks > 0) {
                        ammoBuff.stacks--;
                        let chargedshot = {
                                type: 'chargedshot',
                                delay: [2, 2, 2, 2, 2, 2, 2, 2, 2, 2],
                        busylinks: 5,
                                canCrit: true,
                                multiplier: [1.5, 1.61, 1.72, 1.83, 1.94, 2.06, 2.17, 2.28, 2.39, 2.5],
                        level: doll.skilllevel
          };
                        if ('delay' in chargedshot) {
                            chargedshot.timeLeft = $.isArray(chargedshot.delay) ? Math.round(chargedshot.delay[chargedshot.level - 1] * 30) : Math.round(chargedshot.delay * 30) + 1;
                        }
                        if ('busylinks' in chargedshot) {
                            doll.battle.busylinks += Math.min(chargedshot.busylinks, doll.links);
                        }
                        doll.battle.action_queue.push(chargedshot);
                    }
                }
            }
        }

        //chauchat
        if (doll.id == 282) {
            if (effect.modifySkill == 'consumeStack') {
                let buff = doll.battle.buffs.find(b => b.name == 'chauchat');
                if (buff == undefined) {
                    return;
                }

                let reloadBuff = {
                        type: 'buff',
                        target: 'self',
                        name: 'reloadBuff',
                        duration: -1,
                        uses: 1,
                        stackable: true,
                        max_stacks: 2,
                        stacks: 1,
                        multiplier: [-10, -11, -12, -13, -14, -16, -17, -18, -19, -20],
                level: doll.skilllevel
      };

                if (buff.stacks > 0) { //add a check for existing reloadbuff if stacks can't be wasted. needs ingame testing
                    buff.stacks--;
                    activateBuff(doll, $.extend({}, reloadBuff), enemy);
                }
            }
        }

        //dana
        if (doll.id == 292) {
            if (effect.modifySkill == 'buffSkillDamage') {
                doll.battle.effect_queue[0].multiplier[doll.battle.effect_queue[0].level - 1] *= (1 + doll.battle.armor / 100);
            }
        }

        //jill
        if (doll.id == 296) {
            if (effect.modifySkill == 'danafavorite') {
                //its a shield not armor zzzzz
                // let dana = echelon.find(d => d.id == 292);
                // if(dana !== undefined) {
                //   let armorBuff = {
                //     type:"buff",
                //     target:"self",
                //     stat:{
                //       armor:50
                //     },
                //     level:doll.skilllevel,
                //     duration:[5,5.3,5.7,6,6.3,6.7,7,7.3,7.7,8]
                //   };
                //   activateBuff(dana, armorBuff, null);
                // }
            }

            if (effect.modifySkill == 'almafavorite') {
                let alma = echelon.find(d => d.id == 293);
                if (alma !== undefined) {
                    alma.battle.skill.effects[0].duration = alma.battle.skill.effects[0].duration.map(time => time + 1);
                }
            }

            if (effect.modifySkill == 'stellafavorite') {
                let stella = echelon.find(d => d.id == 294);
                if (stella !== undefined) {
                    stella.battle.passives.find(p => p.name == 'stella').stacksRequired = 10;
                }
            }
        }

        //ads
        if (doll.id == 301) {
            if (effect.modifySkill == 'increaseStackChance') {
                let corrosionBuff = doll.battle.buffs.find(b => b.name == 'corrosion');
                if (corrosionBuff !== undefined) {
                    corrosionBuff.stackChance = 100;
                }
            }

            if (effect.modifySkill == 'decreaseStackChance') {
                let corrosionBuff = doll.battle.buffs.find(b => b.name == 'corrosion');
                if (corrosionBuff !== undefined) {
                    corrosionBuff.stackChance = [20, 22, 24, 26, 30, 32, 34, 36, 38, 40];
                }
            }
        }

        //ssg3000
        if (doll.id == 307) {
            if (effect.modifySkill == 'resetShotCount') {
                doll.battle.skill.numShots = 0;
            }
            if (effect.modifySkill == 'addChargedShot') {
                doll.battle.skill.numShots++;
                let hasSniperMode = doll.battle.buffs.find(b => b.name == 'ssg3000') !== undefined ? true : false;
                if (hasSniperMode) {
                    let chargedshot = {
                            type: 'chargedshot',
                            delay: [2.5, 2.4, 2.3, 2.2, 2.1, 1.9, 1.8, 1.7, 1.6, 1.5],
                    busylinks: 5,
                            canCrit: true,
                            ignoreArmor: false,
                            sureHit: true,
                            multiplier: [1.2, 1.27, 1.33, 1.4, 1.47, 1.53, 1.6, 1.67, 1.73, 1.8],
                    modifySkill: 'addChargedShot',
                            level: doll.skilllevel
        };

                    if (doll.battle.skill.numShots < 7) {
                        if ('delay' in chargedshot) {
                            chargedshot.timeLeft = $.isArray(chargedshot.delay) ? Math.round(chargedshot.delay[chargedshot.level - 1] * 30) : Math.round(chargedshot.delay * 30) + 1;
                            if (doll.pre_battle.skillcd > 0) {
                                chargedshot.timeLeft = Math.floor((1 - doll.pre_battle.skillcd / 100) * chargedshot.timeLeft);
                            }
                        }
                        if ('busylinks' in chargedshot) {
                            doll.battle.busylinks += Math.min(chargedshot.busylinks, doll.links);
                        }
                        doll.battle.action_queue.push(chargedshot);
                    }
                }
            }
        }

        //acr
        if (doll.id == 309) {
            if (effect.modifySkill == 'checkEnemyDebuffs') {
                let debuffCount = 0;

                let fpdebuff = false, accdebuff = false, evadebuff = false, rofdebuff = false;
                let armordebuff = false, movespeeddebuff = false, burning = false, stunned = false;
                $.each(enemy.battle.buffs, (index, enemyBuff) => {
                    if ('stat' in enemyBuff) {
                        if ('fp' in enemyBuff.stat) {
                            let buffAmount = $.isArray(enemyBuff.stat.fp) ? enemyBuff.stat.fp[enemyBuff.level - 1] : enemyBuff.stat.fp;
                            if (buffAmount < 0)
                                fpdebuff = true;
                        }
                        if ('acc' in enemyBuff.stat) {
                            let buffAmount = $.isArray(enemyBuff.stat.acc) ? enemyBuff.stat.acc[enemyBuff.level - 1] : enemyBuff.stat.acc;
                            if (buffAmount < 0)
                                accdebuff = true;
                        }
                        if ('eva' in enemyBuff.stat) {
                            let buffAmount = $.isArray(enemyBuff.stat.eva) ? enemyBuff.stat.eva[enemyBuff.level - 1] : enemyBuff.stat.eva;
                            if (buffAmount < 0)
                                evadebuff = true;
                        }
                        if ('rof' in enemyBuff.stat) {
                            let buffAmount = $.isArray(enemyBuff.stat.rof) ? enemyBuff.stat.rof[enemyBuff.level - 1] : enemyBuff.stat.rof;
                            if (buffAmount < 0)
                                rofdebuff = true;
                        }
                        if ('armor' in enemyBuff.stat) {
                            let buffAmount = $.isArray(enemyBuff.stat.armor) ? enemyBuff.stat.armor[enemyBuff.level - 1] : enemyBuff.stat.armor;
                            if (buffAmount < 0)
                                armordebuff = true;
                        }
                        if ('movespeed' in enemyBuff.stat) {
                            let buffAmount = $.isArray(enemyBuff.stat.movespeed) ? enemyBuff.stat.movespeed[enemyBuff.level - 1] : enemyBuff.stat.movespeed;
                            if (buffAmount < 0)
                                movespeeddebuff = true;
                        }
                    }

                    if ('stun' in enemyBuff && enemyBuff.stun)
                    stunned = true;
                });

                $.each(echelon, (index, d) => {
                    if (d.id != -1) {
                        if (d.battle.action_queue.find(action => action.type == 'grenadedot')) {
                            burning = true;
                        }
                    }
                });

                if (fpdebuff)
                    debuffCount++;
                if (accdebuff)
                    debuffCount++;
                if (evadebuff)
                    debuffCount++;
                if (rofdebuff)
                    debuffCount++;
                if (armordebuff)
                    debuffCount++;
                if (movespeeddebuff)
                    debuffCount++;
                if (burning)
                    debuffCount++;
                if (stunned)
                    debuffCount++;

                if (debuffCount > 0) {
                    doll.battle.buffs.find(b => b.name == 'acrSingleDebuffBuff').stacks = 1;
                    doll.battle.buffs.find(b => b.name == 'acrMultipleDebuffBuff').stacks = debuffCount - 1;
                } else {
                    doll.battle.buffs.find(b => b.name == 'acrSingleDebuffBuff').stacks = 0;
                    doll.battle.buffs.find(b => b.name == 'acrMultipleDebuffBuff').stacks = 0;
                }
            }
        }

        //m1895cb
        if (doll.id == 310) {
            if (effect.modifySkill == 'gainReserveAmmo') {
                if (doll.battle.reserveAmmoMode) {
                    doll.battle.buffs.find(b => b.name == 'normalAttackBuff').attacksLeft++;
                    doll.battle.currentRounds++;
                } else {
                    doll.battle.reserveAmmo = doll.battle.reserveAmmo > 30 ? 30 : doll.battle.reserveAmmo++;
                }
            }

            if (effect.modifySkill == 'useReserveAmmo') {
                if (doll.battle.reserveAmmoMode) {
                    let fpBuff = {
                            type:"buff",
                            target:"self",
                            stat:{
                        fp:[10,11,12,13,14,16,17,18,19,20]
                    },
                    level: doll.skilllevel,
                            name:"m1895cb_fp",
                            stackable:true,
                            stacks:1,
                            max_stacks:1,
                            duration:5
        };
                    let accBuff = {
                            type:"buff",
                            target:"self",
                            stat:{
                        acc:[-25,-24,-23,-22,-21,-19,-18,-17,-16,-15]
                    },
                    level: doll.skilllevel,
                            name:"m1895cb_acc",
                            stackable:true,
                            stacks:1,
                            max_stacks:10,
                            duration:5
        };

                    activateBuff(doll, fpBuff, enemy);
                    activateBuff(doll, accBuff, enemy);

                    if (doll.battle.buffs.find(b => b.name == 'normalAttackBuff').attacksLeft == 1) {
                        let lastShotPassive = {
                                type:"passive",
                                name:"m1895cb_lastShot",
                                trigger:"lastShot",
                                effects:[
                        {
                            type:"modifySkill",
                                    modifySkill:"useReserveAmmo"
                        }
            ]
          };
                        let delayBuff = {
                                type:"stun",
                                busylinks:0,
                                delay:0.5,
                                after:[
                        lastShotPassive
            ],
                        timeLeft: doll.frames_per_attack + 2
          };
                        doll.battle.action_queue.push(delayBuff);
                        doll.battle.reserveAmmoMode = false;
                    }
                } else {
                    if (doll.battle.passives.find(p => p.name == 'm1895cb_lastShot')) {
                        let removePassiveEffect = {
                                type:"removePassive",
                                target:"self",
                                name:"m1895cb_lastShot"
          };
                        removePassive(doll, removePassiveEffect, enemy);

                        let normalAttackBuff = {
                                type:"buff",
                                target:"self",
                                name:"normalAttackBuff",
                                modifySkill:"useReserveAmmo",
                                attacksLeft:doll.battle.reserveAmmo
          };
                        doll.battle.buffs.push(normalAttackBuff);
                    } else {
                        doll.battle.currentRounds += doll.battle.reserveAmmo;
                        doll.battle.reserveAmmo = 0;

                        let fpBuff = {
                                type:"buff",
                                target:"self",
                                stat:{
                            fp:[10,11,12,13,14,16,17,18,19,20]
                        },
                        level: doll.skilllevel,
                                name:"m1895cb_fp",
                                stackable:true,
                                stacks:1,
                                max_stacks:1,
                                duration:5
          };
                        let accBuff = {
                                type:"buff",
                                target:"self",
                                stat:{
                            acc:[-25,-24,-23,-22,-21,-19,-18,-17,-16,-15]
                        },
                        level: doll.skilllevel,
                                name:"m1895cb_acc",
                                stackable:true,
                                stacks:1,
                                max_stacks:10,
                                duration:5
          };

                        activateBuff(doll, fpBuff, enemy);
                        activateBuff(doll, accBuff, enemy);

                        doll.battle.reserveAmmoMode = true;
                    }
                }
            }
        }

        //desert Eagle
        if (doll.id == 312) {
            if (effect.modifySkill == 'stackMultiplier') {
                let attackbuff = doll.battle.buffs.find(b => b.name == 'normalAttackBuff');
                if (attackbuff) {
                    if (attackbuff.multiplier == 1.6)
                        attackbuff.multiplier = 2.56;
                    else if (attackbuff.multiplier == 2.56)
                        attackbuff.multiplier = 4.096;
                }
            }
        }

        //kacpdw
        if (doll.id == 326) {
            if (effect.modifySkill == 'clearDebuffs') {
                $.each(doll.battle.buffs, (i, buff) => {
                    if ('stat' in buff) {
                        $.each(buff.stat, (stat, amount) => {
                            if ($.isArray(amount)) {
                                amount[buff.level - 1] = amount[buff.level - 1] < 0 ? 0 : amount[buff.level - 1];
                            } else {
                                amount = amount < 0 ? 0 : amount;
                            }
                        });
                    }
                });
            }
        }

        //rpk16
        if (doll.id == 327) {
            if (effect.modifySkill == 'switchtoMGmode') {
                let reloadTimer = {
                        type: 'reload',
                        timeLeft: 30
      };
                doll.battle.timers.push(reloadTimer);
                doll.battle.currentRounds = 0;
            }
        }

        //webley
        if (doll.id == 328) {
            if (effect.modifySkill == 'reduceLeaderCD') {
                let leader = undefined;
                for (let i = 0; i < 5; i++) {
                    if (echelon[i].id == -1) {
                        continue;
                    } else {
                        leader = echelon[i];
                        break;
                    }
                }
                let leaderSkillTimer = leader.battle.timers.find(t => t.type == 'skill');
                if (leaderSkillTimer !== undefined) {
                    let cdReduction = [15,16,17,18,19,21,22,23,24,25];
                    let cdr = 1 - (cdReduction[doll.skilllevel - 1] / 100);
                    leaderSkillTimer.timeLeft = Math.ceil(leaderSkillTimer.timeLeft * cdr);
                }
            }
        }

        //sig556
        if (doll.id == 331) {
            if (effect.modifySkill == 'toggleSkill') {
                let isActive = doll.battle.buffs.find(b => b.name == 'sweep') !== undefined;

                if (isActive) {
                    doll.battle.buffs = doll.battle.buffs.filter(b => b.name != 'sweep');
                    doll.battle.passives[0].effects[0].stacksToAdd = -1;
                } else {
                    let skillBuff = {
                            type:"buff",
                            target:"self",
                            name:"sweep",
                            stat:{
                        fp:[25,28,31,33,36,39,42,44,47,50]
                    },
                    duration:-1,
                            level: doll.skilllevel
        };
                    doll.battle.buffs.push(skillBuff);
                    doll.battle.passives[0].effects[0].stacksToAdd = 1;
                }
            }
        }

        //no doll id check to have this work on any doll
        if (effect.modifySkill == 'bleedingjane') {
            for (let i = 0; i < 5; i++) {
                if (echelon[i].pos == doll.pos) {
                    calculateSkillBonus(i);
                    let extraCrit = doll.pre_battle.crit * doll.battle.skillbonus.crit - 100;
                    if (extraCrit > 0) {
                        let bleedingjaneBuff = doll.battle.buffs.find(b => b.name == 'bleedingjane');
                        bleedingjaneBuff.stat.critdmg = 0.6 * extraCrit;
                    }
                }
            }
        }
    }*/

    private void addStack(Doll target, Effect effect, Enemy enemy){
        Buff buff = new Buff(0);
        for(Buff b : target.BS().getBuffs()){
            if(b.getEffect().getName().equals(effect.getName())) buff = b;
        }

        if(buff.getEffect().getStacksToAdd() != null){
            buff.getEffect().setStacks(effect.getStacksToAdd().length > 1 ? effect.getStacksToAdd()[effect.level - 1] : effect.getStacksToAdd()[0]);
        }
        else{
            buff.getEffect().setStacks(buff.getEffect().getStacks() + 1);
        }

        if(buff.getEffect().getMaxStacks() != 0){
            buff.getEffect().setStacks(Math.min(buff.getEffect().getMaxStacks(), buff.getEffect().getStacks()));
        }

        buff.getEffect().setStacks(Math.max(0, buff.getEffect().getStacks()));//Why was this here in the original JS?

        if(!buff.getEffect().isRefreshDuration())
            buff.setTimeLeft((float) (buff.getEffect().getDuration().length > 1 ? Math.floor(buff.getEffect().getDuration()[buff.getEffect().level - 1] * 30) : Math.floor(buff.getEffect().getDuration()[0] * 30)));

        for(Passive passive : target.BS().getPassives()){
            if(passive.getTrigger().equals("hasStacks")){
                Buff b = null;
                for(Buff buff1 : target.BS().getBuffs()){
                    if(buff1.getEffect().getName().equals(passive.getName())){
                        b = buff1;
                        break;
                    }
                }
                if(b != null){
                    int stacksNeeded = passive.getStacksRequired().length > 1 ? passive.getStacksRequired()[passive.level - 1] : passive.getStacksRequired()[0];
                    if(b.getEffect().getStackChance() != null){
                        float expectedStacks = b.getEffect().getStackChance().length > 1 ? b.getEffect().getStacks() * b.getEffect().getStackChance()[b.getEffect().level - 1] / 100f : b.getEffect().getStacks() * b.getEffect().getStackChance()[0] / 100f;
                        if(expectedStacks >= stacksNeeded){
                            triggerPassive("hasStacks", target, enemy, new int[]{0});
                        }
                    }
                    else if(b.getEffect().getStacks() >= stacksNeeded){
                        triggerPassive("hasStacks", target, enemy, new int[]{0});
                    }
                }
            }
        }

        //notHasStacks passives
        for(Passive passive : target.BS().getPassives()){
            if(passive.getTrigger().equals("notHasStacks")){
                Buff b = null;
                for(Buff buff1 : target.BS().getBuffs()){
                    if(buff1.getEffect().getName().equals(passive.getName())){
                        b = buff1;
                        break;
                    }
                }
                if(b != null){
                    int stacksNeeded = passive.getStacksRequired().length > 1 ? passive.getStacksRequired()[passive.level - 1] : passive.getStacksRequired()[0];
                    if(b.getEffect().getStackChance() != null){
                        float expectedStacks = b.getEffect().getStackChance().length > 1 ? b.getEffect().getStacks() * b.getEffect().getStackChance()[b.getEffect().level - 1] / 100f : b.getEffect().getStacks() * b.getEffect().getStackChance()[0] / 100f;
                        if(expectedStacks <= stacksNeeded){
                            triggerPassive("notHasStacks", target, enemy, new int[]{0});
                        }
                    }
                    else if(b.getEffect().getStacks() <= stacksNeeded){
                        triggerPassive("notHasStacks", target, enemy, new int[]{0});
                    }
                }
            }
        }
    }

    private void calculateSkillBonus(int dollIndex){
        Doll doll = echelon.getDoll(dollIndex);

        for(Buff buff : doll.BS().getBuffs()){
            if(buff.isStatBuff()){//Find out if the buff is a stat buff
                for(int i = 0; i < buff.getEffect().getAllStatsNames().size(); i++){//Loop through every stat that is buffed
                    float bonus = 1;
                    List<String> stats = buff.getEffect().getAllStatsNames();//Store the names of the stats that are used for later use
                    Effect effect = buff.getEffect();
                    if(buff.isStackable()){//If the buff is stackable...
                        if(buff.getEffect().getRounds() != null){//...and the buff affects rounds (MGs only), apply the buff accordingly
                            bonus = effect.getRounds().length > 1 ? effect.getRounds()[effect.level - 1] * effect.getStacks() : effect.getRounds()[0] * effect.getStacks();
                            doll.BS().setSkillBonus("rounds", doll.BS().getSkillBonus("rounds") + bonus);
                        }
                        else {//If the buff doesn't affect rounds...
                            if(effect.getStackChance() != null){//...but has a stack chance...
                                for(int ii = 0; ii < effect.getStacks(); ii++){//...loop through all the stacks and apply the buff accordingly.
                                    bonus = effect.getStackChance().length > 1 ? effect.getStackChance()[effect.level - 1] / 100f : effect.getStackChance()[0] / 100f;
                                    bonus *= effect.getBuff(stats.get(i)).length > 1 ? effect.getBuff(stats.get(i))[effect.level = 1] / 100 : effect.getBuff(stats.get(i))[0] / 100;
                                    bonus += 1;
                                    doll.BS().setSkillBonus(stats.get(i), doll.BS().getSkillBonus(stats.get(i)) * bonus);
                                }
                            }
                            else{
                                for(int ii = 0; ii < effect.getStacks(); ii++){//...loop through all the stacks and apply the buff accordingly.
                                    bonus = effect.getBuff(stats.get(i)).length > 1 ? effect.getBuff(stats.get(i))[effect.level - 1] / 100f : effect.getBuff(stats.get(i))[0] / 100f;
                                    bonus += 1;
                                    doll.BS().setSkillBonus(stats.get(i), doll.BS().getSkillBonus(stats.get(i)) * bonus);
                                }
                            }
                        }
                    }
                    else{
                        if(stats.get(i).equals("rounds")){
                            doll.BS().setSkillBonus("rounds", effect.getBuff("rounds").length > 1 ? effect.getBuff("rounds")[effect.level - 1] : effect.getBuff("rounds")[0]);
                        }
                        else{
                            if(effect.getStackChance() != null){
                                bonus = effect.getStackChance().length > 1 ? effect.getStackChance()[effect.level - 1] / 100f : effect.getStackChance()[0] / 100f;
                                bonus *= effect.getBuff(stats.get(i)).length > 1 ? effect.getBuff(stats.get(i))[effect.level - 1] / 100 : effect.getBuff(stats.get(i))[0] / 100;
                                doll.BS().setSkillBonus(stats.get(i), doll.BS().getSkillBonus(stats.get(i)) * (bonus + 1));
                            }
                            else{
                                doll.BS().setSkillBonus(stats.get(i), effect.getBuff(stats.get(i)).length > 1 ? (1 + (effect.getBuff(stats.get(i))[effect.level - 1] / 100)) : (1 + (effect.getBuff(stats.get(i))[0] / 100)));
                            }
                        }
                    }
                }
            }
        }
    }

    private void calculateBattleStats(int dollIndex){
        Doll doll = echelon.getDoll(dollIndex);

        doll.BS().fp = doll.BS().getPreBattleStat("fp") * doll.BS().getSkillBonus("fp");
        doll.BS().acc = (float) Math.floor(doll.BS().getPreBattleStat("acc") * doll.BS().getSkillBonus("acc"));
        doll.BS().eva = (float) Math.floor(doll.BS().getPreBattleStat("eva") * doll.BS().getSkillBonus("eva"));
        doll.BS().rof = (float) Math.floor(doll.BS().getPreBattleStat("rof") * doll.BS().getSkillBonus("rof"));
        doll.BS().rof_uncapped = doll.BS().rof;
        doll.BS().crit = doll.BS().getPreBattleStat("crit") * doll.BS().getSkillBonus("crit");
        doll.BS().crit_uncapped = doll.BS().crit;
        doll.BS().critDmg = (float) Math.floor(doll.BS().getPreBattleStat("critDmg") * doll.BS().getSkillBonus("critDmg"));
        doll.BS().armour = (float) Math.floor(doll.BS().getPreBattleStat("armour") * doll.BS().getSkillBonus("armour"));
        doll.BS().rounds = (float) Math.floor(doll.BS().getPreBattleStat("rounds") * doll.BS().getSkillBonus("rounds"));
        doll.BS().ap = (float) Math.floor(doll.BS().getPreBattleStat("ap") * doll.BS().getSkillBonus("ap"));
        doll.BS().CD = doll.BS().getPreBattleStat("cd") * doll.BS().getSkillBonus("cd");

        //cap stats - TODO: Shorten this by merging it with the above code
        doll.BS().fp = Math.max(0, doll.BS().fp);
        doll.BS().acc = Math.max(1, doll.BS().acc);
        doll.BS().eva = Math.max(0, doll.BS().eva);
        doll.BS().rof = u.getCapRof(doll, doll.BS().rof);
        doll.BS().crit = u.getCapCrit(doll.BS().crit);
        doll.BS().critDmg = Math.max(0, doll.BS().critDmg);
        doll.BS().ap = Math.max(0, doll.BS().ap);
        doll.BS().armour = Math.max(0, doll.BS().armour);

        //Track max stats
        //FP
        //Acc
        //Eva
        //Capped Rof
        //Uncapped Rof
        //CritDmg
        //Capped Crit
        //Uncapped Crit
        //Rounds
        //Armour
        //AP
        doll.BS().maxStats[0] = Math.max(doll.BS().maxStats[0], doll.BS().fp);
        doll.BS().maxStats[1] = Math.max(doll.BS().maxStats[1], doll.BS().acc);
        doll.BS().maxStats[2] = Math.max(doll.BS().maxStats[2], doll.BS().eva);
        doll.BS().maxStats[3] = Math.max(doll.BS().maxStats[3], doll.BS().rof);
        doll.BS().maxStats[4] = Math.max(doll.BS().maxStats[4], doll.BS().rof_uncapped);
        doll.BS().maxStats[5] = Math.max(doll.BS().maxStats[5], doll.BS().critDmg);
        doll.BS().maxStats[6] = Math.max(doll.BS().maxStats[6], doll.BS().crit);
        doll.BS().maxStats[7] = Math.max(doll.BS().maxStats[7], doll.BS().crit_uncapped);
        doll.BS().maxStats[8] = Math.max(doll.BS().maxStats[8], doll.BS().rounds);
        doll.BS().maxStats[9] = Math.max(doll.BS().maxStats[9], doll.BS().armour);
        doll.BS().maxStats[10] = Math.max(doll.BS().maxStats[10], doll.BS().ap);

        doll.BS().minStats[0] = Math.min(doll.BS().minStats[0], doll.BS().fp);
        doll.BS().minStats[1] = Math.min(doll.BS().minStats[1], doll.BS().acc);
        doll.BS().minStats[2] = Math.min(doll.BS().minStats[2], doll.BS().eva);
        doll.BS().minStats[3] = Math.min(doll.BS().minStats[3], doll.BS().rof);
        doll.BS().minStats[4] = Math.min(doll.BS().minStats[4], doll.BS().rof_uncapped);
        doll.BS().minStats[5] = Math.min(doll.BS().minStats[5], doll.BS().critDmg);
        doll.BS().minStats[6] = Math.min(doll.BS().minStats[6], doll.BS().crit);
        doll.BS().minStats[7] = Math.min(doll.BS().minStats[7], doll.BS().crit_uncapped);
        doll.BS().minStats[8] = Math.min(doll.BS().minStats[8], doll.BS().rounds);
        doll.BS().minStats[9] = Math.min(doll.BS().minStats[9], doll.BS().armour);
        doll.BS().minStats[10] = Math.min(doll.BS().minStats[10], doll.BS().ap);


    }

}
