package com.shikikan.gflcompanionapp;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class SelectionFragment extends BottomSheetDialogFragment /*implements View.OnClickListener*/ {

    private SelectionListener selectionLister;
    ImageButton doll1, doll2, doll3, doll4, doll5;
    Doll[] dollData;
    Context context;
    int echelonPosition;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.bottom_sheet, container, false);

        //#TEST CODE#
        //This is used to hardcode in pre-selected T-Dolls to test out displaying T-Dolls once selected
        doll1 = v.findViewById(R.id.imageButton);
        doll2 = v.findViewById(R.id.imageButton2);
        doll3 = v.findViewById(R.id.imageButton3);
        doll4 = v.findViewById(R.id.imageButton5);
        doll5 = v.findViewById(R.id.imageButton9);


        //#TEST CODE#
        //This is used to hardcode in pre-selected T-Dolls to test out displaying T-Dolls once selected
        String px4 = dollData[225].getImage();
        String wa2k = dollData[45].getImage();
        String lee = dollData[47].getImage();
        String five_seven = dollData[136].getImage();
        String calico = dollData[91].getImage();


        //#TEST CODE#
        //This is used to hardcode in pre-selected T-Dolls to test out displaying T-Dolls once selected
        doll1.setImageResource(getResources().getIdentifier(px4, "drawable", context.getPackageName()));
        doll2.setImageResource(getResources().getIdentifier(wa2k, "drawable", context.getPackageName()));
        doll3.setImageResource(getResources().getIdentifier(lee, "drawable", context.getPackageName()));
        doll4.setImageResource(getResources().getIdentifier(five_seven, "drawable", context.getPackageName()));
        doll5.setImageResource(getResources().getIdentifier(calico, "drawable", context.getPackageName()));

        doll1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectionLister.onDollSelect(dollData[225].getID(), echelonPosition);
                dismiss();
            }
        });

        doll2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectionLister.onDollSelect(dollData[45].getID(), echelonPosition);
                dismiss();
            }
        });

        doll3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectionLister.onDollSelect(dollData[47].getID(), echelonPosition);
                dismiss();
            }
        });

        doll4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectionLister.onDollSelect(dollData[136].getID(), echelonPosition);
                dismiss();
            }
        });

        doll5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectionLister.onDollSelect(dollData[91].getID(), echelonPosition);
                dismiss();
            }
        });
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
    }

//    @Override
//    public void onClick(View view) {
//        switch(view.getID()){
//            case doll1.getID():
//                break;
//            case doll2.getID():
//
//        }
//        selectionLister.onDollSelect(dollData[136].getID(), echelonPosition);
//        dismiss();
//    }

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

        selectionLister = (SelectionListener) context;
    }
}
