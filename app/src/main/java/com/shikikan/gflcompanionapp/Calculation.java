package com.shikikan.gflcompanionapp;

import android.widget.ImageView;
import android.widget.TextView;

public class Calculation {
    Utils u;
    UI ui;

    public Calculation(Utils u){
        this.u = u;
        //this.ui = ui;
    }
    public void CalculateTileBuffs(Echelon e){
        ResetBuffs(e);
        int[] tiles;
        //Cycle through every T-Doll in the echelon.
        for(Doll doll : e.getAllDolls()){
            //If they have an ID, meaning they aren't a dummy T-Doll...
            if(doll.getID() != 0){
                //...get their tile formation.
                tiles = u.getDollTilesFormation(doll);
                //Cycle through the echelon again, this time to find the other T-Dolls.
                for(Doll od : e.getAllDolls()){
                    //Cycle through all the tiles obtained from the previous T-Doll.
                    for (int tile : tiles) {
                        //If one of the other T-Dolls in the echelon is on the currently iterated tile,...
                        if (od.getGridPosition() == tile)
                            //...they aren't a dummy T-Doll and they can be buffed by the tile...
                            if ((od.getType() == doll.getTilesBuffs()[0] || doll.getTilesBuffs()[0] == 0) && od.getID() != 0) {
                                //...highlight the tile.
                                //od.getGridImageView().setBackgroundColor(u.getHighlight());

                                int[] receiver = od.getReceivedTileBuffs(), buffer = doll.getTilesBuffs(), buffs = new int[7];
                                //Key - Buffer: 0 = Target, 1 = FP, 2 = ACC, 3 = EVA, 4 = ROF, 5 = CRIT, 6 = SKILLCD, 7 = ARMOUR
                                //Key - Buffer (HG): 0 = Target, 1 = FP-MIN, 2 = FP-MAX, 3 = ACC-MIN, 4 = ACC-MAX, 5 = EVA-MIN, 6 = EVA-MAX, 7 = ROF-MIN
                                //                   8 = ROF-MAX, 9 = CRIT-MIN, 10 = CRIT-MAX, 11 = SKILLCD-MIN, 12 = SKILLCD-MAX, 13 = ARMOUR-MIN, 14 = ARMOUR-MAX

                                //Key - Receiver: 0 = FP, 1 = ACC, 2 = EVA, 3 = ROF, 4 = CRIT, 5 = SKILLCD, 6 = ARMOUR
                                if(doll.getType() == 1){
                                    buffs[0] = (receiver[0] += Math.floor(buffer[1] + (((buffer[2] - buffer[1]) / 4f) * (u.Links(doll.getLevel()) - 1))));
                                    buffs[1] = (receiver[1] += Math.floor(buffer[3] + (((buffer[4] - buffer[3]) / 4f) * (u.Links(doll.getLevel()) - 1))));
                                    buffs[2] = (receiver[2] += Math.floor(buffer[5] + (((buffer[6] - buffer[5]) / 4f) * (u.Links(doll.getLevel()) - 1))));
                                    buffs[3] = (receiver[3] += Math.floor(buffer[7] + (((buffer[8] - buffer[7]) / 4f) * (u.Links(doll.getLevel()) - 1))));
                                    buffs[4] = (receiver[4] += Math.floor(buffer[9] + (((buffer[10] - buffer[9]) / 4f) * (u.Links(doll.getLevel()) - 1))));
                                    buffs[5] = (receiver[5] += Math.floor(buffer[11] + (((buffer[12] - buffer[11]) / 4f) * (u.Links(doll.getLevel()) - 1))));
                                    buffs[6] = (receiver[6] += Math.floor(buffer[13] + (((buffer[14] - buffer[13]) / 4f) * (u.Links(doll.getLevel()) - 1))));
                                }
                                else{
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

    private void ResetBuffs(Echelon e){
        for(int i = 0; i < e.getAllDolls().length; i++) e.getDoll(i).setReceivedTileBuffs(new int[7]);
    }
}
