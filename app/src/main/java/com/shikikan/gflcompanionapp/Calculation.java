package com.shikikan.gflcompanionapp;

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

    public int getFP(Doll doll) {
        double fp = doll.getFp() + (Math.signum(doll.getAffection()) * Math.ceil(Math.abs(doll.getFp() * doll.getAffection())));
        //return (float)(fp + doll.getEquipmentBuff("fp")) * doll.getTileBuff("fp");
        return (int) Math.ceil((fp + doll.getEquipmentBuff("fp")) * doll.getTileBuff("fp"));
    }

    public int getAcc(Doll doll) {
        double acc = doll.getAcc() + (Math.signum(doll.getAffection()) * Math.ceil(Math.abs(doll.getAcc() * doll.getAffection())));
        //return (float)(acc + doll.getEquipmentBuff("acc")) * doll.getTileBuff("acc");
        return (int) Math.ceil((acc + doll.getEquipmentBuff("acc")) * doll.getTileBuff("acc"));
    }

    public int getEva(Doll doll) {
        double eva = doll.getEva() + (Math.signum(doll.getAffection()) * Math.ceil(Math.abs(doll.getEva() * doll.getAffection())));
        //return (float)(eva + doll.getEquipmentBuff("eva")) * doll.getTileBuff("eva");
        return (int) Math.ceil((eva + doll.getEquipmentBuff("eva")) * doll.getTileBuff("eva"));
    }

    public int getRof(Doll doll) {
        return (int) Math.ceil((doll.getRof() + doll.getEquipmentBuff("rof")) * doll.getTileBuff("rof"));
    }

    public int getCritDmg(Doll doll) {
        return (int) (100 + ((doll.getCritdmg() + doll.getEquipmentBuff("critdmg")) * doll.getTileBuff("critdmg")));
    }

    public int getCrit(Doll doll) {
        return (int) Math.ceil((doll.getCrit() + doll.getEquipmentBuff("crit")) * doll.getTileBuff("crit"));
    }

    public int getAP(Doll doll) {
        return (int) ((doll.getAp() + doll.getEquipmentBuff("ap")) * doll.getTileBuff("ap"));
    }

    public int getArmour(Doll doll) {
        return (int) ((doll.getArmour() + doll.getEquipmentBuff("armour")) * doll.getTileBuff("armour"));
    }

    public int getNightview(Doll doll) {
        return (int) ((doll.getEquipmentBuff("nightview")));
    }

    public int getRounds(Doll doll) {
        return (int) (doll.getRounds() + doll.getEquipmentBuff("rounds"));
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
                doll.setSimFp(getFP(doll));
                doll.setSimAcc(getAcc(doll));
                doll.setSimEva(getEva(doll));
                doll.setSimRof(getRof(doll));
                doll.setSimCritDmg(getCritDmg(doll));
                doll.setSimCrit(getCrit(doll));
                doll.setSimAp(getAP(doll));
                doll.setSimArmour(getArmour(doll));
                //NightView
                doll.setSimRounds(getRounds(doll));
                //doll.setTargets();
                doll.setCooldown(doll.getTileBuff("cd"));
                if (doll.getFramesPerAttack() != 0)
                    doll.setNormalAttackTimer(doll.getFramesPerAttack());
                else doll.setNormalAttackTimer((int) Math.floor(50 * (30f / doll.getRof())));

                //set up skill timer
                //set up skill 2 timer for mods
                //calculate skills
            }
        }
    }

    private void CalculateDPS(Echelon e) {

    }

    private void SimulateBattle(Echelon e) {
        //initialise graph variables here

        PrepBattleEchelon(e);
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
            //graphData.x.push(parseFloat((time / 30.0).toFixed(2)));

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
        }//TODO: Not finished at all - currently a placeholder

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
                for (Timer timer : doll.getTimers()) {
                    if (timer.getType().equals("normal attack")) {
                        boolean reloading = doll.findTimer("reloading");//Figure out where reload timers come into play
                        boolean stunned = doll.findTimer("stunned");
                        if (u.Links(doll.getLevel()) - doll.getBusyLinks() > 0 && !reloading && !stunned) {
                            timer.setTimeLeft(timer.getTimeLeft() - 1);
                        }
                    } else {
                        if (timer.getTimeLeft() > 0)
                            timer.setTimeLeft(timer.getTimeLeft() - 1);
                    }
                }

                //Skill timing
                for (Timer timer : doll.getTimers()) {
                    if (timer.getTimeLeft() == 0) {
                        boolean reloading = doll.findTimer("reloading");
                        boolean stunned = doll.findTimer("stunned");
                        if (timer.getType().equals("skill")) {
                            //If the T-Doll is unable to move...
                            if (stunned || (reloading && doll.getTimer("reload").getTimeLeft() != 0)) {
                                //...add 1 frame to the timer, effectively pausing it.
                                timer.setTimeLeft(timer.getTimeLeft() + 1);
                            } else {
                                //factor in skills
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
                            }//TODO: Factor in skills
                        } else if (timer.getType().equals("skill2")) {
                            //If the T-Doll is unable to move...
                            if (stunned || (reloading && doll.getTimer("reload").getTimeLeft() != 0)) {
                                //...add 1 frame to the timer, effectively pausing it.
                                timer.setTimeLeft(timer.getTimeLeft() + 1);
                            } else {
                                //factor in skills
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
                        } else {
                            //add to effect queue
                            doll.addToEffectQueue(timer);//TODO: Will two seperate queues be needed; one for effects and one for actions
                        }
                    }
                }

                //JS Code
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

                //tick and remove buffs
                for (Buff buff : doll.getBuffs()) {
                    buff.setTimeLeft(buff.getTimeLeft() - 1);
                }

                //TODO: This can't be done until skills are added
                //Removes buffs from the active buffs queue or something.
                /*doll.battle.buffs = doll.battle.buffs.filter(buff => {
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
                return true;*/













                //tick and remove passives

                for (Passive passive : doll.getPassives()) {
                    passive.setTimeLeft(passive.getTimeLeft() - 1);
                }

                List<Integer> remove = new ArrayList<>();
                //Goes through all the passives the T-Doll has...
                for (int index = 0; index < doll.getPassives().size(); index++) {
                    //...and if the passive has expired...
                    if (doll.getPassives().get(index).getTimeLeft() == 0) {
                        //...their index is added to the 'remove' list.
                        remove.add(index);
                    }
                }










                //tick and trigger time-based passives

                //The 'remove' list is iterated through...
                for (int index = 0; index < remove.size(); index++) {
                    //...and it's values are used to remove the expired passives.
                    doll.getPassives().remove((int) remove.get(index));
                }

                List<Passive> temp = new ArrayList<>();
                for (Passive passive : doll.getPassives()) {
                    if (passive.isInterval()) temp.add(passive);
                }

                for (Passive passive : temp) {
                    int interval;
                    if (passive.getAmountOfIntervals() > 1) {
                        interval = passive.getInterval(1);
                    } else {
                        interval = passive.getInterval(doll.getSkillLevel());
                    }

                    if ((currentFrame - passive.getStartTime() % Math.floor(interval * 30) == 0 && currentFrame != 1 && interval != -1)) {
                        //Activate the passive skill here
                        //triggerPassive('time', doll, enemy);
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
                if(doll.getID() == 0) return;

                for(int i = 0; i < doll.getEffectQueue().size(); i++){
                    Passive temp = (Passive)doll.getEffectQueue().get(i);//TODO: Look into reworking the queue/figuring
                                                                         //out how they work so that you can find the
                                                                         //correct type to assign them.
                    /*if (action.type == 'buff') {
                        activateBuff(doll, action, enemy);
                    }
                    else if (action.type == 'passive') {
                        addPassive(doll, action, enemy, currentFrame);
                    }
                    else if (action.type == 'removeBuff') {
                        removeBuff(doll, action, enemy);
                    }
                    else if (action.type == 'removePassive') {
                        removePassive(doll, action, enemy);
                    }
                    else if (action.type == 'modifySkill') {
                        modifySkill(doll, action, enemy, currentFrame);
                    }
                    else {
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
                    }*/
                }
            }

        }
    }
}
