package com.shikikan.gflcompanionapp;

import android.content.ClipData;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.Display;
import android.view.DragEvent;
import android.view.HapticFeedbackConstants;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

public class UI extends AppCompatActivity implements SelectionFragment.SelectionListener, View.OnClickListener, AdapterView.OnItemSelectedListener {

    private ImageButton[] selectButtons;
    private String[] AllImageViewIDs = {"pos_1", "pos_2", "pos_3",
            "pos_4", "pos_5", "pos_6", "pos_7", "pos_8", "pos_9"};
    private TextView[] Stats;
    private Spinner TDollLevelSelect, SkillLevelSelect;
    private Utils u = new Utils();
    private Echelon e;
    private Calculation c;
    private int selectedDoll = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        setUp();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        Log.d("tag", "config changed");
        super.onConfigurationChanged(newConfig);

        int orientation = newConfig.orientation;
        if (orientation == Configuration.ORIENTATION_PORTRAIT)
            //Log.d("tag", "Portrait");
            setImageViews(true);
        else if (orientation == Configuration.ORIENTATION_LANDSCAPE)
            //Log.d("tag", "Landscape");
            setImageViews(false);
        else
            Log.w("tag", "other: " + orientation);
    }

    /**
     * Set Up method. Pretty self explanatory really
     */
    private void setUp() {
        u.LoadEquipmentData(this);
        u.LoadDollData(this);
        c = new Calculation(u);

        setImageViews(true);

//        Stats = new TextView[11];
//        Stats[0] = findViewById(R.id.name_text);
//        Stats[1] = findViewById(R.id.hp_text);
//        Stats[2] = findViewById(R.id.fp_text);
//        Stats[3] = findViewById(R.id.acc_text);
//        Stats[4] = findViewById(R.id.eva_text);
//        Stats[5] = findViewById(R.id.rof_text);
//        Stats[6] = findViewById(R.id.crit_text);
//        Stats[7] = findViewById(R.id.critDmg_text);
//        Stats[8] = findViewById(R.id.rounds_text);
//        Stats[9] = findViewById(R.id.armour_text);
//        Stats[10] = findViewById(R.id.ap_text);

        Stats = new TextView[]{findViewById(R.id.name_text), findViewById(R.id.hp_text),
                               findViewById(R.id.fp_text), findViewById(R.id.acc_text),
                               findViewById(R.id.eva_text), findViewById(R.id.rof_text),
                               findViewById(R.id.crit_text), findViewById(R.id.critDmg_text),
                               findViewById(R.id.rounds_text), findViewById(R.id.armour_text),
                               findViewById(R.id.ap_text)};

        TDollLevelSelect = findViewById(R.id.level_select);
        SkillLevelSelect = findViewById(R.id.skill_level_select);

        ArrayAdapter TDollLevelAdapter = ArrayAdapter.createFromResource(this,R.array.tdoll_level,
                android.R.layout.simple_spinner_dropdown_item);
        TDollLevelSelect.setAdapter(TDollLevelAdapter);
        TDollLevelSelect.setOnItemSelectedListener(this);

        ArrayAdapter SkillLevelAdapter = ArrayAdapter.createFromResource(this,R.array.skill_level,
                android.R.layout.simple_spinner_dropdown_item);
        SkillLevelSelect.setAdapter(SkillLevelAdapter);
        SkillLevelSelect.setOnItemSelectedListener(this);

        selectButtons = new ImageButton[]{findViewById(R.id.doll_1), findViewById(R.id.doll_2),
                findViewById(R.id.doll_3), findViewById(R.id.doll_4), findViewById(R.id.doll_5)};
    }

    /**
     * Set the ImageViews for the grid properly. Echelon is also set up here as it needs the TextViews.
     * The parameter probably won't be needed in the future due to the project probably being fixed
     * to portrait orientation only.
     * @param portrait Pass in whether the phone is in portrait or not in the form of a boolean
     */
    private void setImageViews(boolean portrait) {
        ImageView[] imageViews = new ImageView[9];
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        RelativeLayout grid = findViewById(R.id.grid);

        int width = size.x;
        grid.getLayoutParams().height = width;

        for(int i = 0; i < 9; i++) {
            imageViews[i] = findViewById(getResources().getIdentifier(AllImageViewIDs[i], "id", getPackageName()));
            if(portrait){
                imageViews[i].getLayoutParams().height = width/3;
                imageViews[i].getLayoutParams().width = width/3;
            }
            else{
                imageViews[i].getLayoutParams().height = (width/3)/2;
                imageViews[i].getLayoutParams().width = (width/3)/2;
            }
            imageViews[i].setOnLongClickListener(listenClick);
            imageViews[i].setOnDragListener(listenDrag);
        }

        e = new Echelon(imageViews, u);
    }

    @Override
    public void onDollSelect(int dollID, int echelonPosition) {
        updateEchelon(dollID, echelonPosition);
    }

    @Override
    public void onEquipmentSelect(int equipID, int selectedDoll) {
        e.getDoll(selectedDoll - 1).setEquipment(u.getEquipment(equipID), u.EquipmentSlot(u.getEquipment(equipID).getID()));
    }

    @Override
    public void onClick(View v) {
        SelectionFragment sf;
        switch (v.getId()){
            case R.id.button:
                //Update();
                test();
                break;
            case R.id.doll_1:
            case R.id.doll_2:
            case R.id.doll_3:
            case R.id.doll_4:
            case R.id.doll_5:
                sf = new SelectionFragment();
                sf.setUp_DollSelect(u.getAllDolls(), UI.this, u.IDtoInt(v));
                sf.show(getSupportFragmentManager(), "test");
                break;
            case R.id.removeDoll_1:
            case R.id.removeDoll_2:
            case R.id.removeDoll_3:
            case R.id.removeDoll_4:
            case R.id.removeDoll_5:
                e.removeDoll(u.IDtoInt(v));
            case R.id.pos_1:
            case R.id.pos_2:
            case R.id.pos_3:
            case R.id.pos_4:
            case R.id.pos_5:
            case R.id.pos_6:
            case R.id.pos_7:
            case R.id.pos_8:
            case R.id.pos_9:
                displayStats(v);
                displayDollGrid(v);
                break;
            case R.id.equipSlot_1:
            case R.id.equipSlot_2:
            case R.id.equipSlot_3:
                sf = new SelectionFragment();
                sf.setUp_EquipmentSelect(u.getAllEquipment(), UI.this, selectedDoll);
                sf.show(getSupportFragmentManager(), "test");
                break;
        }
    }

    private void test() {
        int[] tiles;
        //Cycle through every T-Doll in the echelon.
//        for(Doll doll : e.getAllDolls()){
//            //If they have an ID, meaning they aren't a dummy T-Doll...
//            if(doll.getID() != 0){
//                //...get their tile formation.
//                tiles = u.getDollTilesFormation(doll);
//                //Cycle through the echelon again, this time to find the other T-Dolls.
//                for(Doll od : e.getAllDolls()){
//                    //Cycle through all the tiles obtained from the previous T-Doll.
//                    for (int tile : tiles) {
//                        //If one of the other T-Dolls in the echelon is on the currently iterated tile,...
//                        if (od.getGridPosition() == tile)
//                            //...they aren't a dummy T-Doll and they can be buffed by the tile...
//                            if ((od.getType() == doll.getTilesBuffs()[0] || doll.getTilesBuffs()[0] == 0) && od.getID() != 0)
//                                //...highlight the tile.
//                                findViewById(u.GridPositionToViewID(od.getGridPosition())).setBackgroundColor(u.getHighlight());
//                    }
//                }
//            }
//        }

        //u.levelchange(e.getDoll(selectedDoll - 1));
        //c.CalculateTileBuffs(e);

    }

    public void displayStats(View gridImageView) {
        selectedDoll = 0;
        c.CalculateTileBuffs(e);
        for (Doll doll : e.getAllDolls()) {
            if (doll.getGridImageView() == gridImageView && doll.getID() != 0) {
                u.levelchange(doll);
                Stats[0].setText(doll.getName());
                Stats[1].setText(String.valueOf(doll.getHp()));
                Stats[2].setText(String.valueOf((int)Math.ceil(doll.getFp() * doll.getTileBuff("fp"))));
                Stats[3].setText(String.valueOf((int)Math.ceil(doll.getAcc() * doll.getTileBuff("acc"))));
                Stats[4].setText(String.valueOf((int)Math.ceil(doll.getEva() * doll.getTileBuff("eva"))));
                Stats[5].setText(String.valueOf((int)Math.ceil(doll.getRof() * doll.getTileBuff("rof"))));
                Stats[6].setText(String.valueOf((int)Math.ceil(doll.getCrit() * doll.getTileBuff("crit"))));
                Stats[7].setText(String.valueOf(doll.getCritdmg()));
                Stats[8].setText(String.valueOf(doll.getRounds()));
                Stats[9].setText(String.valueOf((int)Math.ceil(doll.getArmour() * doll.getTileBuff("armour"))));
                Stats[10].setText(String.valueOf(doll.getAp()));
                selectedDoll = doll.getEchelonPosition();
                TDollLevelSelect.setSelection(u.LevelToSpinnerPosition(doll.getLevel(), false));
                SkillLevelSelect.setSelection(u.LevelToSpinnerPosition(doll.getSkillLevel(), true));
                break;
            }
        }
    }

    /**
     * Updates the echelon by adding in the selected T-Doll to the echelon.
     * @param dollID The ID of the doll. This will be used to find the specific T-Doll within the
     *               'Doll' array found in 'Utils.java' by subtracting '1' from it
     * @param echelonPosition The echelon position corresponding to the chosen select button
     */
    private void updateEchelon(int dollID, int echelonPosition){
        //Added the selected T-Doll to the echelon
        e.addDoll(u.getDoll(dollID), echelonPosition);

        //Refresh the grid and select buttons
        updateUI();
    }

    private void updateUI() {
        for (String ImageViewID : AllImageViewIDs) {
            ImageView t = findViewById(getResources().getIdentifier(ImageViewID, "id", getPackageName()));
            t.setImageResource(0);
            t.setBackgroundColor(Color.TRANSPARENT);
        }

        for (int i = 0; i < e.getAllDolls().length; i++) {
            e.getDoll(i).getGridImageView().setImageResource(getResources().getIdentifier(e.getDoll(i).getImage(), "drawable", getPackageName()));
        }

        for (int i = 0; i < e.getAllDolls().length; i++){
            if(e.getDoll(i).getID() != 0){
                selectButtons[i].setImageResource(getResources().getIdentifier(e.getDoll(i).getImage(), "drawable", getPackageName()));
            }
        }
    }

    private void displayDollGrid(View gridImageView) {
        updateUI();
        for (Doll doll : e.getAllDolls()) {
            if (doll.getGridImageView() == gridImageView && doll.getID() != 0) {
                int[] tiles = u.getDollTilesFormation(doll);
                for (int ID : tiles)
                    if (ID != 0)
                        findViewById(u.GridPositionToViewID(ID)).setBackgroundColor(u.getHighlight());
                break;
            }
        }
    }

    /**
     * Used to update the information displayed on the UI. Is called whenever a change is made, such
     * as a formation change or swapping equipment.
     */
    /*private void Update(){
        Stats[0].setText(u.getDoll(iii).getName());
        Stats[1].setText(u.getDoll(iii).getName());
        Stats[2].setText(u.getDoll(iii).getName());
        Stats[3].setText(String.valueOf(u.getDoll(iii).getHp()));
        Stats[4].setText(String.valueOf(u.getDoll(iii).getFp()));
        Stats[5].setText(String.valueOf(u.getDoll(iii).getAcc()));
        Stats[6].setText(String.valueOf(u.getDoll(iii).getEva()));
        Stats[7].setText(String.valueOf(u.getDoll(iii).getRof()));
        Stats[8].setText(String.valueOf(u.getDoll(iii).getCrit()));
        Stats[9].setText(String.valueOf(u.getDoll(iii).getCritdmg()));
        Stats[10].setText(String.valueOf(u.getDoll(iii).getRounds()));
        Stats[11].setText(String.valueOf(u.getDoll(iii).getArmour()));
        Stats[12].setText(String.valueOf(u.getDoll(iii).getAp()));
        iii++;
    }*/

    private View.OnLongClickListener listenClick = new View.OnLongClickListener()
    {
        @Override
        public boolean onLongClick(View v) {
            ClipData data = ClipData.newPlainText("", "");
            DragShadowBuilder dragShadow = new DragShadowBuilder(v, UI.this, e);

            v.setHapticFeedbackEnabled(true);
            v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY, HapticFeedbackConstants.FLAG_IGNORE_GLOBAL_SETTING);
            v.startDrag(data, dragShadow, v, 0);

            return false;
        }
    };

    private View.OnDragListener listenDrag = new View.OnDragListener() {

        @Override
        public boolean onDrag(View v, DragEvent event)
        {
            int dragEvent = event.getAction();
            ImageView temp = (ImageView) event.getLocalState();
            ImageView dragged = findViewById(temp.getId());//Old position

            switch (dragEvent)
            {
                case DragEvent.ACTION_DRAG_ENTERED:
                    v.setAlpha(0.5f);
                    dragged.setVisibility(View.VISIBLE);
                    break;

                case DragEvent.ACTION_DRAG_LOCATION:
                    v.setBackgroundColor(u.getHighlight());
                    v.setAlpha(0.5f);
                    break;

                case DragEvent.ACTION_DRAG_EXITED:
                    v.setBackgroundColor(Color.TRANSPARENT);
                    v.setAlpha(1);
                    break;

                case DragEvent.ACTION_DROP:
                    ImageView target = (ImageView) v;//New position
                    Doll idleDoll = null, movingDoll = null;

                    for(int i = 0; i < e.getAllDolls().length; i++) {
                        if(e.getDoll(i).getGridImageView() == target) {
                            idleDoll = e.getDoll(i);
                            break;
                        }
                    }

                    if(idleDoll == null) {
                        idleDoll = new Doll();
                        idleDoll.setGrid(u.IDtoInt(target), target);
                    }

                    for(int i = 0; i < e.getAllDolls().length; i++) {
                        if(e.getDoll(i).getGridImageView() == dragged) {
                            movingDoll = e.getDoll(i);
                            break;
                        }
                    }

                    if(movingDoll == null) {
                        movingDoll = new Doll();
                        movingDoll.setGrid(u.IDtoInt(dragged), dragged);
                    }

                    e.swapGridPosition(idleDoll, movingDoll);

                    dragged.setVisibility(View.VISIBLE);
                    v.setBackgroundColor(Color.TRANSPARENT);
                    v.setAlpha(1);
                    updateUI();
                    break;
            }

            return true;
        }
    };

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if(selectedDoll != 0){
            switch (parent.getId()){
                case R.id.level_select:
                    String t = ((Spinner)findViewById(R.id.level_select)).getSelectedItem().toString();
                    e.getDoll(selectedDoll - 1).setLevel(Integer.parseInt(t));
                    displayStats(e.getDoll(selectedDoll - 1).getGridImageView());
                    break;
                case R.id.skill_level_select:
                    e.getDoll(selectedDoll - 1).setSkillLevel(Integer.parseInt(((Spinner)findViewById(R.id.skill_level_select)).getSelectedItem().toString()));
                    displayStats(e.getDoll(selectedDoll - 1).getGridImageView());
                    break;
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    /*private class DragShadow extends View.DragShadowBuilder
    {
        //ColorDrawable greyBox;
        Drawable pic;

        public DragShadow(View view)
        {
            super(view);
            //greyBox = new ColorDrawable(Color.LTGRAY);
            Resources res = getActivity().getResources();
            pic = ResourcesCompat.getDrawable(res, R.drawable.ic_menu_camera, null);
        }

        @Override
        public void onDrawShadow(Canvas canvas)
        {
            pic.draw(canvas);
        }

        @Override
        public void onProvideShadowMetrics(Point shadowSize,
                                           Point shadowTouchPoint)
        {
            View v = getView();

            int height = (int) v.getHeight();
            int width = (int) v.getWidth();

            pic.setBounds(0, 0, width, height);

            shadowSize.set(width, height);

            shadowTouchPoint.set((int)width/2, (int)height/2);
        }
    }*/
}
