package com.qualimony.alcal;

import android.content.Context;
import android.widget.Button;

/**
 * Created by petre.popescu on 12/29/2015.
 */
public class DateButton extends Button {

    private int day;

    public DateButton(Context context, int day) {
        super(context);
        this.day = day;
        if(day <= 5)
            setBackgroundColor(0xff606060);
        else
            setBackgroundColor(0xff600000);
    }

}
