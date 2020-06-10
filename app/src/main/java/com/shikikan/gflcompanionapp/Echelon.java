package com.shikikan.gflcompanionapp;

import android.widget.ImageView;

public class Echelon {
    private Doll[] Dolls = new Doll[5];
    private ImageView[] imageViews;
    private Utils u;

    public Echelon(ImageView[] imageViews, Utils u) {
        this.imageViews = imageViews;
        this.u = u;
        Dolls[0] = new Doll();
        Dolls[1] = new Doll();
        Dolls[2] = new Doll();
        Dolls[3] = new Doll();
        Dolls[4] = new Doll();

        for(int i = 0; i < Dolls.length; i++){
            //Grid position is passed through as 'i + 1' to ensure that when T-Dolls swap to an empty
            //space occupied by a null T-Doll, they will have a proper grid position after the swap
            Dolls[i].setGrid(i + 1, imageViews[i]);
        }
    }

    /**
     * Used to add the T-Doll to the echelon.
     * @param doll Pass in the selected T-Doll
     * @param echelonPosition Pass in the T-Dolls current position in the echelon. This will be used
     *                        to determine the starting position of the T-Doll on the grid.
     */
    public void addDoll(Doll doll, int echelonPosition) {
        boolean swap = false;
        int oldPosition = 0;
        for(Doll doll_ : Dolls){
            if(doll_.getID() == doll.getID()){
                swap = true;
                oldPosition = doll_.getEchelonPosition();
                break;
            }
        }

        if(swap){
            Doll temp = Dolls[echelonPosition - 1];
            for(Doll existingDoll : Dolls) if(doll.getID() == existingDoll.getID()) doll = existingDoll;

            doll.setEchelonPosition(echelonPosition);
            doll.setTiles(u.setUpDollTilesFormation(doll));
            Dolls[doll.getEchelonPosition() - 1] = doll;

            Dolls[oldPosition - 1] = temp;
            temp.setEchelonPosition(oldPosition);
            swapGridPosition(temp, Dolls[doll.getEchelonPosition() - 1]);
        }
        else{
            Doll newDoll = new Doll(doll);
            checkGrid(newDoll, echelonPosition);
            newDoll.setEchelonPosition(echelonPosition);

            newDoll.setTiles(u.setUpDollTilesFormation(newDoll));
            Dolls[newDoll.getEchelonPosition() - 1] = newDoll;
        }
    }

    public void removeDoll(int echelonPosition) {
        Dolls[echelonPosition - 1] = new Doll();
        Dolls[echelonPosition - 1].setGrid(echelonPosition, imageViews[echelonPosition]);
        checkGrid(Dolls[echelonPosition - 1], echelonPosition);
    }

    public Doll getDoll(int index) {
        return Dolls[index];
    }
    
    public Doll[] getAllDolls(){
        return Dolls;
    }

    /**
     * Used to make sure that the space a T-Doll is assigned to upon joining the echelon isn't occupied
     * already.
     * @param newDoll T-Doll being added to the echelon
     * @param echelonPosition The default position assigned to them via the button that was used to
     *                        select them.
     */
    private void checkGrid(Doll newDoll, int echelonPosition) {
        int check = 0;
        for (int i = 1; i < 10; i++) {
                for (Doll doll : Dolls) if(i != doll.getGridPosition()) check++;

                if (check == 5) {
                    echelonPosition = i;
                    break;
                }
                else check = 0;
            }
        newDoll.setGrid(echelonPosition, imageViews[echelonPosition-1]);
    }

    /**
     * Used to swap the positions of the T-Dolls on the grid and to reflect those changes in the code
     * as well.
     * @param idleDoll The T-Doll occupying the space being moved to
     * @param movingDoll The T-Doll being being moved
     */
    public void swapGridPosition(Doll idleDoll, Doll movingDoll) {
        int temp = idleDoll.getGridPosition();
        ImageView temp2 = idleDoll.getGridImageView();
        idleDoll.setGrid(movingDoll.getGridPosition(), movingDoll.getGridImageView());
        movingDoll.setGrid(temp, temp2);
    }

//    public void updateEchelon(){
//        for (Doll doll : Dolls) {
//            if (doll.getId() == 0) doll.setGridPosition(Dolls[0].getGridPosition());
//        }
//    }

}
