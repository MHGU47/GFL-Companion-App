package com.shikikan.gflcompanionapp;

import android.content.Context;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class SelectionFragment extends BottomSheetDialogFragment implements View.OnClickListener {

    private SelectionListener selectionListener;
    ImageButton doll1, doll2, doll3, doll4, doll5, doll6, doll7, doll8, doll9;
    Button previous, next;
    private Doll[] dollData;
    private Context context;
    private int echelonPosition, counter = 0;
    private ImageButton[] imageButtons = new ImageButton[6], typeSelectButtons = new ImageButton[6];
    private String[] imageButtonIDs;
    private Doll[] dollIndex = new Doll[6];
    private List<Doll> HGs, SMGs, RFs, ARs, MGs, SGs, SelectedType;
    private List<List<Doll>> AllTypes;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.bottom_sheet, container, false);
        imageButtons[0] = v.findViewById(R.id.dollSelect_1);
        imageButtons[1] = v.findViewById(R.id.dollSelect_2);
        imageButtons[2] = v.findViewById(R.id.dollSelect_3);
        imageButtons[3] = v.findViewById(R.id.dollSelect_4);
        imageButtons[4] = v.findViewById(R.id.dollSelect_5);
        imageButtons[5] = v.findViewById(R.id.dollSelect_6);
//        imageButtons[6] = v.findViewById(R.id.dollSelect_7);
//        imageButtons[7] = v.findViewById(R.id.dollSelect_8);
//        imageButtons[8] = v.findViewById(R.id.dollSelect_9);

        typeSelectButtons[0] = v.findViewById(R.id.HGSelect);
        typeSelectButtons[1] = v.findViewById(R.id.SMGSelect);
        typeSelectButtons[2] = v.findViewById(R.id.RFSelect);
        typeSelectButtons[3] = v.findViewById(R.id.ARSelect);
        typeSelectButtons[4] = v.findViewById(R.id.MGSelect);
        typeSelectButtons[5] = v.findViewById(R.id.SGSelect);

        imageButtons[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectionListener.onDollSelect(dollIndex[0].getID(), echelonPosition);
                dismiss();
            }
        });
        imageButtons[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectionListener.onDollSelect(dollIndex[1].getID(), echelonPosition);
                dismiss();
            }
        });
        imageButtons[2].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectionListener.onDollSelect(dollIndex[2].getID(), echelonPosition);
                dismiss();
            }
        });
        imageButtons[3].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectionListener.onDollSelect(dollIndex[3].getID(), echelonPosition);
                dismiss();
            }
        });
        imageButtons[4].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectionListener.onDollSelect(dollIndex[4].getID(), echelonPosition);
                dismiss();
            }
        });
        imageButtons[5].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectionListener.onDollSelect(dollIndex[5].getID(), echelonPosition);
                dismiss();
            }
        });
//        imageButtons[6].setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                selectionListener.onDollSelect(dollIndex[6].getID(), echelonPosition);
//                dismiss();
//            }
//        });
//        imageButtons[7].setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                selectionListener.onDollSelect(dollIndex[7].getID(), echelonPosition);
//                dismiss();
//            }
//        });
//        imageButtons[8].setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                selectionListener.onDollSelect(dollIndex[8].getID(), echelonPosition);
//                dismiss();
//            }
//        });

        v.findViewById(R.id.nav_next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoadPage(false, 0);
            }
        });

        v.findViewById(R.id.nav_previous).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoadPage(true, 0);
            }
        });

        v.findViewById(R.id.HGSelect).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoadPage(false, 1);
            }
        });

        v.findViewById(R.id.SMGSelect).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoadPage(false, 2);
            }
        });

        v.findViewById(R.id.RFSelect).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoadPage(false, 3);
            }
        });

        v.findViewById(R.id.ARSelect).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoadPage(false, 4);
            }
        });

        v.findViewById(R.id.MGSelect).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoadPage(false, 5);
            }
        });

        v.findViewById(R.id.SGSelect).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoadPage(false, 6);
            }
        });

        LoadPage(false, 0);

        //TODO 11/06/2020 Add in code that switches the types of T-Dolls. This will most likely require
        //                either a new function that controls resetting the layout or altering
        //                'LoadPage(boolean)' again.

        //#TEST CODE#
        //This is used to hardcode in pre-selected T-Dolls to test out displaying T-Dolls once selected

//        for (Doll doll : dollData){
//            switch (doll.getType()){
//                case 1:
//                    HGs.add(doll);
//                    break;
//                case 2:
//                    SMGs.add(doll);
//                    break;
//                case 3:
//                    RFs.add(doll);
//                    break;
//                case 4:
//                    ARs.add(doll);
//                    break;
//                case 5:
//                    MGs.add(doll);
//                    break;
//                default:
//                    SGs.add(doll);
//                    break;
//            }
//        }


        //#TEST CODE#
        //This is used to hardcode in pre-selected T-Dolls to test out displaying T-Dolls once selected
        //String px4 = dollData[225].getImage();
        //String wa2k = dollData[45].getImage();
        //String lee = dollData[47].getImage();
        //String five_seven = dollData[136].getImage();
        //String calico = dollData[91].getImage();


        //#TEST CODE#
        //This is used to hardcode in pre-selected T-Dolls to test out displaying T-Dolls once selected
        //doll1.setImageResource(getResources().getIdentifier(dollIndex[0].getImage(), "drawable", context.getPackageName()));
        //doll2.setImageResource(getResources().getIdentifier(dollIndex[1].getImage(), "drawable", context.getPackageName()));
        //doll3.setImageResource(getResources().getIdentifier(dollIndex[2].getImage(), "drawable", context.getPackageName()));
        //doll4.setImageResource(getResources().getIdentifier(dollIndex[3].getImage(), "drawable", context.getPackageName()));
        //doll5.setImageResource(getResources().getIdentifier(dollIndex[4].getImage(), "drawable", context.getPackageName()));
        //doll6.setImageResource(getResources().getIdentifier(dollIndex[5].getImage(), "drawable", context.getPackageName()));
        //doll7.setImageResource(getResources().getIdentifier(dollIndex[6].getImage(), "drawable", context.getPackageName()));
        //doll8.setImageResource(getResources().getIdentifier(dollIndex[7].getImage(), "drawable", context.getPackageName()));
        //doll9.setImageResource(getResources().getIdentifier(dollIndex[8].getImage(), "drawable", context.getPackageName()));

//        doll1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                selectionListener.onDollSelect(dollIndex[0].getID(), echelonPosition);
//                dismiss();
//            }
//        });
//
//        doll2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                selectionListener.onDollSelect(dollIndex[1].getID(), echelonPosition);
//                dismiss();
//            }
//        });
//
//        doll3.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                selectionListener.onDollSelect(dollIndex[2].getID(), echelonPosition);
//                dismiss();
//            }
//        });
//
//        doll4.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                selectionListener.onDollSelect(dollIndex[3].getID(), echelonPosition);
//                dismiss();
//            }
//        });
//
//        doll5.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                selectionListener.onDollSelect(dollIndex[4].getID(), echelonPosition);
//                dismiss();
//            }
//        });
//
//        doll6.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                selectionListener.onDollSelect(dollIndex[5].getID(), echelonPosition);
//                dismiss();
//            }
//        });
//
//        doll7.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                selectionListener.onDollSelect(dollIndex[6].getID(), echelonPosition);
//                dismiss();
//            }
//        });
//
//        doll8.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                selectionListener.onDollSelect(dollIndex[7].getID(), echelonPosition);
//                dismiss();
//            }
//        });
//
//        doll9.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                selectionListener.onDollSelect(dollIndex[8].getID(), echelonPosition);
//                dismiss();
//            }
//        });
        return v;
    }

    /**
     * This is used to setup the fragment so it is able to access the already existing Doll[] array
     * and have the context in order to change the images of the buttons
     * @param dollData Array of 'Doll' objects containing T-Doll info
     * @param context Context of the class using this function. It should be 'UI.java'
     * @param echelonPosition Selected echelon position determined by which select button was used
     */
    public void setUp(Doll[] dollData, Context context, int echelonPosition){
        this.dollData = dollData;
        this.context = context;
        this.echelonPosition = echelonPosition;

        HGs = new ArrayList<>();
        SMGs = new ArrayList<>();
        RFs = new ArrayList<>();
        ARs = new ArrayList<>();
        MGs = new ArrayList<>();
        SGs = new ArrayList<>();
        AllTypes = new ArrayList<>();
        SelectedType = new ArrayList<>();

        for (Doll doll : dollData) {
            switch(doll.getType()) {
                case 1:
                    HGs.add(doll);
                    break;
                case 2:
                    SMGs.add(doll);
                    break;
                case 3:
                    RFs.add(doll);
                    break;
                case 4:
                    ARs.add(doll);
                    break;
                case 5:
                    MGs.add(doll);
                    break;
                default:
                    SGs.add(doll);
                    break;
            }
            //SGs.add(doll);
        }

        AllTypes.add(HGs);
        AllTypes.add(SMGs);
        AllTypes.add(RFs);
        AllTypes.add(ARs);
        AllTypes.add(MGs);
        AllTypes.add(SGs);
        SelectedType = AllTypes.get(0);
    }

    private void LoadPage(boolean goBack, int type){

        //Used to determine whether the user needs to be taken back to the first page
        boolean reset = false;

        //Reset all 'ImageButtons' in the event that they aren't all needed. This stops the user from
        //being able to interact with them.
        for(ImageButton btn : imageButtons) btn.setVisibility(View.GONE);
        for(ImageButton btn : typeSelectButtons) btn.setVisibility(View.VISIBLE);

        //If the 'type' passed in isn't '0', meaning that the user wishes to change the displayed T-Doll
        //type, reset the counter and change the list that 'SelectedType' uses.
        if(type != 0) {
            SelectedType = AllTypes.get(type - 1);
            counter = 0;
        }

        //Go through each list in 'AllTypes'...
        for(List<Doll> dollType : AllTypes){

            //If the current list in 'dollType' is the same as 'SelectedType'...
            if(dollType == SelectedType){

                //Get the T-Doll type of the T-Doll within that list and use that to determine which
                //select button to hide.
                switch(dollType.get(0).getType()){
                    case 1:
                        typeSelectButtons[0].setVisibility(View.GONE);
                        break;
                    case 2:
                        typeSelectButtons[1].setVisibility(View.GONE);
                        break;
                    case 3:
                        typeSelectButtons[2].setVisibility(View.GONE);
                        break;
                    case 4:
                        typeSelectButtons[3].setVisibility(View.GONE);
                        break;
                    case 5:
                        typeSelectButtons[4].setVisibility(View.GONE);
                        break;
                    default:
                        typeSelectButtons[5].setVisibility(View.GONE);
                        break;
                }
            }
        }

        //Determines whether the user wants to go to a previous page or not
        if (goBack){

            //'counter' will be 6 when the user is on the first page as it will have already been
            //iterated during the displaying of the 'ImageViews'.
            if(counter == 6){

                //If the user wants to go to the last page from the first page, set 'counter' to be
                //the right amount to correctly display the T-Dolls the would appear on the last page.
                //This is done by subtracting the remainder of the size of the list divided by 6 from
                //the overall size and setting 'counter' to that value.
                //counter = HGs.size() - (HGs.size() % 6);
                counter = SelectedType.size() - (SelectedType.size() % 6);

                //If the remainder is '0', meaning that 'counter' is the same as the list's size,
                //subtract 6 form 'counter'.
                if (counter == SelectedType.size()) counter = SelectedType.size() - 6;
            }

            //If the user is on the last page, subtract the remainder of the size of the list
            //divided by 6 from the overall size plus 6. The extra 6 is to push 'counter' further back
            //as the removing a pages worth of T-Dolls will effectively refresh the same page. Pushing
            //'counter' further back enables the previous' pages T-Dolls to appear.
            else if(counter == 0) counter = SelectedType.size() - ((SelectedType.size() % 6) + 6);

            //If the user is on any other page besides the first or last, subtract 18 from 'counter'
            else counter -= 12;
        }

        for(int i = 0; i < 6; i++){

            //'Try/Catch' statement used to make sure that T-Dolls from the start and the end of the
            //list don't end up being displayed on the same page.
            try{

                //If the last page hasn't been reached yet...
                if(!reset){

                    //Display all the T-Dolls available, make the 'ImageButton' visible, add the
                    //T-Doll to the selection index and iterate counter.
                    imageButtons[i].setImageResource(getResources().getIdentifier(SelectedType.get(counter).getImage(), "drawable", context.getPackageName()));
                    imageButtons[i].setVisibility(View.VISIBLE);
                    dollIndex[i] = SelectedType.get(counter);
                    counter++;
                }
            }
            catch (Exception e){
                //When the end of the list is reached and the amount of T-Dolls left to display is
                //less than 6, reset 'counter' and set 'reset' to true.
                counter = 0;
                reset = true;

                //If the exception happens when no buttons have been set, start from the beginning
                //of the list. This is to prevent blank pages from appearing.
                if(i == 0) {
                    reset = false;
                    i = -1;
                }
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.dollSelect_1:
                selectionListener.onDollSelect(dollIndex[0].getID(), echelonPosition);
                dismiss();
            case R.id.dollSelect_2:
                selectionListener.onDollSelect(dollIndex[1].getID(), echelonPosition);
                dismiss();
            case R.id.dollSelect_3:
                selectionListener.onDollSelect(dollIndex[2].getID(), echelonPosition);
                dismiss();
            case R.id.dollSelect_4:
                selectionListener.onDollSelect(dollIndex[3].getID(), echelonPosition);
                dismiss();
            case R.id.dollSelect_5:
                selectionListener.onDollSelect(dollIndex[4].getID(), echelonPosition);
                dismiss();
            case R.id.dollSelect_6:
                selectionListener.onDollSelect(dollIndex[5].getID(), echelonPosition);
                dismiss();
//            case R.id.dollSelect_7:
//                selectionListener.onDollSelect(dollIndex[6].getID(), echelonPosition);
//                dismiss();
//            case R.id.dollSelect_8:
//                selectionListener.onDollSelect(dollIndex[7].getID(), echelonPosition);
//                dismiss();
//            case R.id.dollSelect_9:
//                selectionListener.onDollSelect(dollIndex[8].getID(), echelonPosition);
//                dismiss();
            case R.id.nav_next:
                LoadPage(false, 0);
                break;
        }
    }

    /**
     * This interface is used to communicate with the activity that holds the SelectionFragment. This
     * sets it up so that functions can be called in here, when their main body is in UI.java
     */
    public interface SelectionListener {
        /**
         * Used to pass the ID of the T-Doll and the chosen echelon position to the UI
         * @param dollID ID of the currently selected T-Doll
         * @param echelonPosition Echelon position corresponding to the select button used
         */
        void onDollSelect(int dollID, int echelonPosition);
    }

    /**
     * This overrides the default onAttach() function and makes it so that the selectionListener refers
     * to MainActivity.java when this fragment is created.
     * @param context Should be MainActivity.java
     */
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        selectionListener = (SelectionListener) context;
    }
}
