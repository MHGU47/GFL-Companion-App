package com.shikikan.gflcompanionapp;

import android.content.Context;
import android.os.Bundle;
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
    private ImageButton[] imageButtons = new ImageButton[9];
    private String[] imageButtonIDs;
    private Doll[] dollIndex = new Doll[9];
    private List<Doll> HGs, SMGs, RFs, ARs, MGs, SGs;
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
        imageButtons[6] = v.findViewById(R.id.dollSelect_7);
        imageButtons[7] = v.findViewById(R.id.dollSelect_8);
        imageButtons[8] = v.findViewById(R.id.dollSelect_9);

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
        imageButtons[6].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectionListener.onDollSelect(dollIndex[6].getID(), echelonPosition);
                dismiss();
            }
        });
        imageButtons[7].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectionListener.onDollSelect(dollIndex[7].getID(), echelonPosition);
                dismiss();
            }
        });
        imageButtons[8].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectionListener.onDollSelect(dollIndex[8].getID(), echelonPosition);
                dismiss();
            }
        });

        v.findViewById(R.id.nav_next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoadPage();
            }
        });

        LoadPage();


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

        for (Doll doll : dollData) {
            if (doll.getType() == 1) HGs.add(doll);
        }//TODO 10/06/2020 Add in the code to allow the population of the lists that contain the
        //                 other T-Dolls
    }

    private void LoadPage(){
        for(int i = 0; i < 9; i++){
            imageButtons[i].setImageResource(getResources().getIdentifier(HGs.get(counter).getImage(), "drawable", context.getPackageName()));
            dollIndex[i] = HGs.get(counter);
            counter++;
            //TODO 10/06/2020 Find a way to allow 'edge scrolling'. Maybe do a check with a try/catch
            //                and then fill the rest of the index with null Doll objects. Then add
            //                code that prevents the user from selecting the null Doll objects.
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
            case R.id.dollSelect_7:
                selectionListener.onDollSelect(dollIndex[6].getID(), echelonPosition);
                dismiss();
            case R.id.dollSelect_8:
                selectionListener.onDollSelect(dollIndex[7].getID(), echelonPosition);
                dismiss();
            case R.id.dollSelect_9:
                selectionListener.onDollSelect(dollIndex[8].getID(), echelonPosition);
                dismiss();
            case R.id.nav_next:
                LoadPage();
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
