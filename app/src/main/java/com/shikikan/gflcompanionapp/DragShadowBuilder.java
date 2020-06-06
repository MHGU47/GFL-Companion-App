package com.shikikan.gflcompanionapp;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.core.content.res.ResourcesCompat;

public class DragShadowBuilder extends View.DragShadowBuilder
{
    private Drawable pic;

    public DragShadowBuilder(View view, Context context, Echelon e)
    {
        super(view);
        Resources res = context.getResources();
        pic = ResourcesCompat.getDrawable(res, R.drawable.ic_menu_camera, null);
        assert pic != null;
        pic.setAlpha(0);
        for(Doll doll : e.getAllDolls()){
            try{
                if(doll.getGridImageView() == view) {
                    pic = ResourcesCompat.getDrawable(res, res.getIdentifier(doll.getImage(), "drawable", context.getPackageName()), null);
                    break;
                }
            }
            catch (Exception ex) {
                break;
            }

        }

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

        int height = v.getHeight();
        int width = v.getWidth();

        pic.setBounds(0, 0, width, height);

        shadowSize.set(width, height);

        shadowTouchPoint.set(width/2, height/2);
    }
}
