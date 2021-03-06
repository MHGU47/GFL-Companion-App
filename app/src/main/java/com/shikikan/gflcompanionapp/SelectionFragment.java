package com.shikikan.gflcompanionapp;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class SelectionFragment extends BottomSheetDialogFragment implements View.OnClickListener {

    private SelectionListener selectionListener;
    //ImageButton doll1, doll2, doll3, doll4, doll5, doll6, doll7, doll8, doll9;
    //Button previous, next;
    private Doll[] dollData;
    private Equipment[] equipmentData;
    private Context context;
    private int echelonPosition, counter = 0;
    private ImageButton[] imageButtons = new ImageButton[9], typeSelectButtons = new ImageButton[6];
    //private String[] imageButtonIDs;
    private Doll[] dollIndex = new Doll[6];
    private Equipment[] equipmentIndex = new Equipment[9];
    private List<Doll> HGs, SMGs, RFs, ARs, MGs, SGs, SelectedType;
    private List<List<Doll>> AllTypes;
    private List<Equipment> Accessories, Magazine, Doll, SelectedEquipment;
    private List<List<Equipment>> AllEquipment;
    private boolean TDollSelect;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.bottom_sheet, container, false);
        imageButtons[0] = v.findViewById(R.id.select_1);
        imageButtons[1] = v.findViewById(R.id.select_2);
        imageButtons[2] = v.findViewById(R.id.select_3);
        imageButtons[3] = v.findViewById(R.id.select_4);
        imageButtons[4] = v.findViewById(R.id.select_5);
        imageButtons[5] = v.findViewById(R.id.select_6);
        imageButtons[6] = v.findViewById(R.id.select_7);
        imageButtons[7] = v.findViewById(R.id.select_8);
        imageButtons[8] = v.findViewById(R.id.select_9);

        ImageViewSetUp();

        typeSelectButtons[0] = v.findViewById(R.id.HGSelect);
        typeSelectButtons[1] = v.findViewById(R.id.SMGSelect);
        typeSelectButtons[2] = v.findViewById(R.id.RFSelect);
        typeSelectButtons[3] = v.findViewById(R.id.ARSelect);
        typeSelectButtons[4] = v.findViewById(R.id.MGSelect);
        typeSelectButtons[5] = v.findViewById(R.id.SGSelect);

        imageButtons[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TDollSelect) selectionListener.onDollSelect(dollIndex[0].getID(), echelonPosition);
                else selectionListener.onEquipmentSelect(equipmentIndex[0].getID(), echelonPosition);
                dismiss();
            }
        });
        imageButtons[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TDollSelect) selectionListener.onDollSelect(dollIndex[1].getID(), echelonPosition);
                else selectionListener.onEquipmentSelect(equipmentIndex[1].getID(), echelonPosition);
                dismiss();
            }
        });
        imageButtons[2].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TDollSelect) selectionListener.onDollSelect(dollIndex[2].getID(), echelonPosition);
                else selectionListener.onEquipmentSelect(equipmentIndex[2].getID(), echelonPosition);
                dismiss();
            }
        });
        imageButtons[3].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TDollSelect) selectionListener.onDollSelect(dollIndex[3].getID(), echelonPosition);
                else selectionListener.onEquipmentSelect(equipmentIndex[3].getID(), echelonPosition);
                dismiss();
            }
        });
        imageButtons[4].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TDollSelect) selectionListener.onDollSelect(dollIndex[4].getID(), echelonPosition);
                else selectionListener.onEquipmentSelect(equipmentIndex[4].getID(), echelonPosition);
                dismiss();
            }
        });
        imageButtons[5].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TDollSelect) selectionListener.onDollSelect(dollIndex[5].getID(), echelonPosition);
                else selectionListener.onEquipmentSelect(equipmentIndex[5].getID(), echelonPosition);
                dismiss();
            }
        });
        imageButtons[6].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectionListener.onEquipmentSelect(equipmentIndex[6].getID(), echelonPosition);
                dismiss();
            }
        });
        imageButtons[7].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectionListener.onEquipmentSelect(equipmentIndex[7].getID(), echelonPosition);
                dismiss();
            }
        });
        imageButtons[8].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectionListener.onEquipmentSelect(equipmentIndex[8].getID(), echelonPosition);
                dismiss();
            }
        });

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
    public void setUp_DollSelect(Doll[] dollData, Context context, int echelonPosition){
        this.dollData = dollData;
        this.context = context;
        this.echelonPosition = echelonPosition;
        TDollSelect = true;

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

    /**
     * This is used to setup the fragment so it is able to access the already existing Equipment[] array
     * and have the context in order to change the images of the buttons
     * @param equipmentData Array of 'Equipment' objects containing equipment info
     * @param context Context of the class using this function. It should be 'UI.java'
     * @param doll Selected T-Doll
     * @param slot Pass in the equipment slot used
     * @param u 'Utils' object
     */
    public void setUp_EquipmentSelect(Equipment[] equipmentData, Context context, Doll doll, int slot, Utils u){
        this.equipmentData = equipmentData;
        this.context = context;
        TDollSelect = false;
        echelonPosition = doll.getEchelonPosition();

        Accessories = new ArrayList<>();
        Magazine = new ArrayList<>();
        Doll = new ArrayList<>();

        AllEquipment = new ArrayList<>();
        SelectedEquipment = new ArrayList<>();

        for (Equipment equipment : equipmentData) {
            switch(equipment.getType()) {
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
                    if(u.ValidEquip(doll,equipment.getType())) Accessories.add(equipment);
                    break;
                case 5://AP Rounds
                case 6://HP Rounds
                case 7://Slug
                case 8://HV Ammo
                case 9://Buckshot
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
                    if(u.ValidEquip(doll,equipment.getType())) Magazine.add(equipment);
                    break;
                case 10://Exo
                case 11://Armour Plate
                case 12://High Evasion Exo
                case 14://Ammo Box
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
                    if(u.ValidEquip(doll,equipment.getType())) Doll.add(equipment);
                default: break;
            }
        }

        AllEquipment.add(Accessories);
        AllEquipment.add(Magazine);
        AllEquipment.add(Doll);

        switch(doll.getType()){
            case 2:
            case 6:
                if(slot == 0) slot = 2;
                else if(slot == 2) slot = 0;
            case 3:
            case 5:
                if(slot == 1) slot = 0;
                else if(slot == 0) slot = 1;
            case 4:
            default:
                break;
        }

        SelectedEquipment = AllEquipment.get(slot);
//
//        AllTypes.add(HGs);
//        AllTypes.add(SMGs);
//        AllTypes.add(RFs);
//        AllTypes.add(ARs);
//        AllTypes.add(MGs);
//        AllTypes.add(SGs);
//        SelectedType = AllTypes.get(0);
    }

    private void ImageViewSetUp(){
        if(TDollSelect){
            for(ImageButton btn : imageButtons){
                btn.setScaleType(ImageView.ScaleType.CENTER);
                ViewGroup.LayoutParams params = btn.getLayoutParams();
                params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                btn.setLayoutParams(params);
            }
        }
        else{
            for(ImageButton btn : imageButtons){
                btn.setScaleType(ImageView.ScaleType.CENTER_CROP);
                ViewGroup.LayoutParams params = btn.getLayoutParams();
                params.height = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100, getResources().getDisplayMetrics());
                btn.setLayoutParams(params);
            }
        }
    }

    private void LoadPage(boolean goBack, int type){

        //Used to determine whether the user needs to be taken back to the first page
        boolean reset = false;

        //Reset all 'ImageButtons' in the event that they aren't all needed. This stops the user from
        //being able to interact with them.
        for(ImageButton btn : imageButtons) btn.setVisibility(View.GONE);
        for(ImageButton btn : typeSelectButtons) btn.setVisibility(View.GONE);
        if(TDollSelect){
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
//                        switch(SelectedType.get(counter).getRarity()){
//                            case 2:
//                                imageButtons[i].setBackgroundColor(Color.argb(25, 10, 10, 10));
//                                break;
//                            case 3:
//                                imageButtons[i].setBackgroundColor(Color.argb(25, 0, 0, 10));
//                                break;
//                            case 4:
//                                imageButtons[i].setBackgroundColor(Color.argb(25, 0, 10, 0));
//                                break;
//                            default:
//                                imageButtons[i].setBackgroundColor(Color.argb(25, 255, 255, 0));
//                                break;
//                        }
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
        else{
            //If the 'type' passed in isn't '0', meaning that the user wishes to change the displayed T-Doll
            //type, reset the counter and change the list that 'SelectedType' uses.
            if(type != 0) {
                SelectedEquipment = AllEquipment.get(type - 1);
                counter = 0;
            }

//            //Go through each list in 'AllTypes'...
//            for(List<Equipment> equipmentType : AllEquipment){
//
//                //If the current list in 'dollType' is the same as 'SelectedType'...
//                if(equipmentType == SelectedEquipment){
//
//                    //Get the T-Doll type of the T-Doll within that list and use that to determine which
//                    //select button to hide.
//                    switch(equipmentType.get(0).getType()){
//                        case 1:
//                            typeSelectButtons[0].setVisibility(View.GONE);
//                            break;
//                        case 2:
//                            typeSelectButtons[1].setVisibility(View.GONE);
//                            break;
//                        case 3:
//                            typeSelectButtons[2].setVisibility(View.GONE);
//                            break;
//                        case 4:
//                            typeSelectButtons[3].setVisibility(View.GONE);
//                            break;
//                        case 5:
//                            typeSelectButtons[4].setVisibility(View.GONE);
//                            break;
//                        default:
//                            typeSelectButtons[5].setVisibility(View.GONE);
//                            break;
//                    }
//                }
//            }

            //Determines whether the user wants to go to a previous page or not
            if (goBack){

                //'counter' will be 9 when the user is on the first page as it will have already been
                //iterated during the displaying of the 'ImageViews'.
                if(counter == 9){

                    //If the user wants to go to the last page from the first page, set 'counter' to be
                    //the right amount to correctly display the T-Dolls the would appear on the last page.
                    //This is done by subtracting the remainder of the size of the list divided by 9 from
                    //the overall size and setting 'counter' to that value.
                    //counter = HGs.size() - (HGs.size() % 9);
                    counter = SelectedEquipment.size() - (SelectedEquipment.size() % 9);

                    //If the remainder is '0', meaning that 'counter' is the same as the list's size,
                    //subtract 9 form 'counter'.
                    if (counter == SelectedEquipment.size()) counter = SelectedEquipment.size() - 9;
                }

                //If the user is on the last page, subtract the remainder of the size of the list
                //divided by 9 from the overall size plus 9. The extra 9 is to push 'counter' further back
                //as the removing a pages worth of T-Dolls will effectively refresh the same page. Pushing
                //'counter' further back enables the previous' pages T-Dolls to appear.
                else if(counter == 0) counter = SelectedEquipment.size() - ((SelectedEquipment.size() % 9) + 9);

                    //If the user is on any other page besides the first or last, subtract 18 from 'counter'
                else counter -= 18;
            }

            for(int i = 0; i < 9; i++){

                //'Try/Catch' statement used to make sure that T-Dolls from the start and the end of the
                //list don't end up being displayed on the same page.
                try{

                    //If the last page hasn't been reached yet...
                    if(!reset){

                        //Display all the T-Dolls available, make the 'ImageButton' visible, add the
                        //T-Doll to the selection index and iterate counter.
                        imageButtons[i].setImageResource(getResources().getIdentifier(SelectedEquipment.get(counter).getImage(), "drawable", context.getPackageName()));
                        imageButtons[i].setVisibility(View.VISIBLE);
                        switch(SelectedEquipment.get(counter).getRarity()){
                            case 2:
                                imageButtons[i].setBackgroundColor(Color.argb(25, 10, 10, 10));
                                break;
                            case 3:
                                imageButtons[i].setBackgroundColor(Color.argb(25, 0, 0, 10));
                                break;
                            case 4:
                                imageButtons[i].setBackgroundColor(Color.argb(25, 0, 10, 0));
                                break;
                            default:
                                imageButtons[i].setBackgroundColor(Color.argb(50, 255, 255, 0));
                                break;
                        }
                        equipmentIndex[i] = SelectedEquipment.get(counter);
                        counter++;
                    }
                }
                catch (Exception e){
                    //When the end of the list is reached and the amount of T-Dolls left to display is
                    //less than 9, reset 'counter' and set 'reset' to true.
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

    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.select_1:
                selectionListener.onDollSelect(dollIndex[0].getID(), echelonPosition);
                dismiss();
            case R.id.select_2:
                selectionListener.onDollSelect(dollIndex[1].getID(), echelonPosition);
                dismiss();
            case R.id.select_3:
                selectionListener.onDollSelect(dollIndex[2].getID(), echelonPosition);
                dismiss();
            case R.id.select_4:
                selectionListener.onDollSelect(dollIndex[3].getID(), echelonPosition);
                dismiss();
            case R.id.select_5:
                selectionListener.onDollSelect(dollIndex[4].getID(), echelonPosition);
                dismiss();
            case R.id.select_6:
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

        void onEquipmentSelect(int equipID, int selectedTDoll);
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
