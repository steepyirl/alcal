package com.qualimony.alcal;

import android.content.Context;
import android.widget.TextView;

import com.qualimony.ka.KaCalendar;
import com.qualimony.ka.KaDateFormat;

import java.util.Calendar;

/**
 * Created by petre.popescu on 12/29/2015.
 */
public class SelectedMonth extends TextView implements MonthAdvanceable, DateResettable {

    private KaCalendar time;
    private KaDateFormat format = new KaDateFormat("G.y.M");

    public SelectedMonth(Context context) {
        super(context);
    }

    public void setTime(KaCalendar time) {
        this.time = time;
        KaCalendar cal = new KaCalendar();
        cal.setTimeInMillis(this.time.getTimeInMillis());
        reRender();
    }

    private void reRender() {
        setText(format.format(time.getTimeInMillis()));
    }

    @Override
    public void advanceMonth(int increment) {
        time.add(KaCalendar.KA_MONTH, increment);
        reRender();
    }

    @Override
    public void resetDate() {
        Calendar cal = Calendar.getInstance();
        time.setTimeInMillis(cal.getTimeInMillis());
        reRender();
    }
}
