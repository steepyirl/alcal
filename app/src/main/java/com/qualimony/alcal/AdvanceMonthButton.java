package com.qualimony.alcal;

import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.widget.Button;

/**
 * Created by petre.popescu on 12/29/2015.
 */
public class AdvanceMonthButton extends Button {

    private int direction;
    private Typeface face;
    private MonthAdvanceable advanceTarget;

    private class OnClickListener implements View.OnClickListener {
        public void onClick(View view) {
            advanceTarget.advanceMonth(direction);
        }
    }

    public AdvanceMonthButton(Context context, Typeface face, MonthAdvanceable advanceTarget, int advanceDirection) {
        super(context);
        this.face = face;
        this.advanceTarget = advanceTarget;
        direction = advanceDirection;
        setTypeface(face);
        if(direction > 0)
            setText(">");
        else if(direction < 0)
            setText("<");
        else
            setText("*");
        setOnClickListener(new OnClickListener());
    }
}
