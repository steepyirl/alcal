package com.qualimony.alcal;

import android.content.Context;
import android.view.View;
import android.widget.Button;

/**
 * Created by petre.popescu on 12/29/2015.
 */
public class CurrentDateButton extends Button {

    private DateResettable target;

    private class OnClickListener implements View.OnClickListener {
        public void onClick(View view) {
            target.resetDate();
        }
    }

    public CurrentDateButton(Context context, DateResettable target) {
        super(context);
        this.target = target;
        setOnClickListener(new OnClickListener());
    }
}
