package com.qualimony.alcal;

import android.content.Context;
import android.graphics.Typeface;
import android.view.Gravity;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.qualimony.ka.KaCalendar;

/**
 * Created by petre.popescu on 1/1/2016.
 */
public class MonthView {
    private TextView[] dayLabels = new TextView[9];
    private DateButton[][] dateButtons = new DateButton[4][9];

    public MonthView(Context context, Typeface face) {
        //Initialize the labels
        for(int i = 0; i < 9; i++) {
            dayLabels[i] = new TextView(context);
            dayLabels[i].setTypeface(face);
        }
        dayLabels[0].setText("\u263d");
        dayLabels[1].setText("\u2642");
        dayLabels[2].setText("\u263f");
        dayLabels[3].setText("\u2643");
        dayLabels[4].setText("\u2640");
        dayLabels[5].setText("\u2644");
        dayLabels[6].setText("\u2609");
        dayLabels[7].setText("\u2645");
        dayLabels[8].setText("\u2646");

        //Initialize the date buttons
        for(int i = 0; i < 4; i++) {
            for(int j = 0; j < 7; j++) {
                dateButtons[i][j] = new DateButton(context, j+1);
            }
        }
        dateButtons[0][7] = new DateButton(context, 8);
        dateButtons[0][8] = new DateButton(context, 9);
    }

    public void renderLabels(TableRow row) {

        TableRow.LayoutParams rowParams = null;
        for(int i = 0; i < 9; i++) {
            rowParams = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT, 0);
            rowParams.column = 8+i;
            rowParams.span = 1;
            rowParams.gravity = Gravity.CENTER;
            row.addView(dayLabels[i], rowParams);
        }
    }

    public void renderDateButtonRow(TableRow row, int rowIndex, int columnStartIndex) {
        TableRow.LayoutParams rowParams = null;
        for(int i = 0; i < 7; i++) {
            rowParams = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT, 0);
            rowParams.column = columnStartIndex + i;
            rowParams.gravity = Gravity.CENTER;
            rowParams.width = 50;
            rowParams.leftMargin = 1;
            rowParams.rightMargin = 1;
            rowParams.topMargin = 2;
            rowParams.bottomMargin = 2;
            row.addView(dateButtons[rowIndex][i], rowParams);
        }
    }

    public void renderCaudalDateButtons(TableRow row, int rowIndex, int columnStartIndex) {
        TableRow.LayoutParams rowParams = null;
        for(int i = 0; i < 2; i++) {
            rowParams = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT, 0);
            rowParams.column = columnStartIndex + i;
            rowParams.gravity = Gravity.CENTER;
            rowParams.width = 50;
            rowParams.leftMargin = 1;
            rowParams.rightMargin = 1;
            rowParams.topMargin = 2;
            rowParams.bottomMargin = 2;
            row.addView(dateButtons[0][7+i], rowParams);
        }
    }

    public void setDate(KaCalendar date) {
        if(date.get(KaCalendar.KA_MONTH) == 14) {
            setRegularDaysVisibility(TextView.GONE);
            dayLabels[8].setVisibility(TextView.VISIBLE);
            dateButtons[0][8].setVisibility(TextView.VISIBLE);
            if(date.isLeapYear()) {
                dayLabels[7].setVisibility(TextView.VISIBLE);
                dateButtons[0][7].setVisibility(TextView.VISIBLE);
            }
        } else {
            setRegularDaysVisibility(TextView.VISIBLE);
            dayLabels[7].setVisibility(TextView.GONE);
            dateButtons[0][7].setVisibility(TextView.GONE);
            dayLabels[8].setVisibility(TextView.GONE);
            dateButtons[0][8].setVisibility(TextView.GONE);
        }
    }

    private void setRegularDaysVisibility(int visibility) {
        for(int i = 0; i < 7; i++) {
            dayLabels[i].setVisibility(visibility);
        }
        for(int i = 0; i < 4; i++) {
            for(int j = 0; j < 7; j++) {
                dateButtons[i][j].setVisibility(visibility);
            }
        }
    }
}
