package com.qualimony.alcal;

import android.content.Context;
import android.widget.Button;

/**
 * Created by petre.popescu on 12/29/2015.
 */
public class DateButton extends Button {

    private int week;
    private int day;

    public DateButton(Context context, int week, int day) {
        super(context);
        this.week = week;
        this.day = day;
    }

    public int getWeek() {
        return week;
    }

    public int getDay() {
        return day;
    }

}
