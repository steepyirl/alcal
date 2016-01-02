package com.qualimony.alcal;

import android.content.Context;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.api.services.calendar.model.Event;
import com.qualimony.ka.KaCalendar;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by petre.popescu on 1/1/2016.
 */
public class MonthView implements EventTarget {
    private TextView[] dayLabels = new TextView[9];
    private DateButton[][] dateButtons = new DateButton[4][9];
    private KaCalendar date;

    public MonthView(Context context, Typeface face, View.OnClickListener dateButtonClickListener) {
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
                dateButtons[i][j] = new DateButton(context, i+1, j+1);
                dateButtons[i][j].setOnClickListener(dateButtonClickListener);
            }
        }
        for(int j = 7; j < 9; j++) {
            dateButtons[0][j] = new DateButton(context, 1, j+1);
            dateButtons[0][j].setOnClickListener(dateButtonClickListener);
        }
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

    public void setDate(KaCalendar date, EventGetter eventGetter) {
        this.date = date;
        KaCalendar today = new KaCalendar();
        KaCalendar eventStartDate = new KaCalendar();
        eventStartDate.set(KaCalendar.KA_GEN, date.get(KaCalendar.KA_GEN));
        eventStartDate.set(KaCalendar.KA_YEAR, date.get(KaCalendar.KA_YEAR));
        eventStartDate.set(KaCalendar.KA_MONTH, date.get(KaCalendar.KA_MONTH));
        eventStartDate.set(KaCalendar.KA_WEEK, 1);
        KaCalendar eventEndDate = new KaCalendar();
        eventEndDate.set(KaCalendar.KA_GEN, date.get(KaCalendar.KA_GEN));
        eventEndDate.set(KaCalendar.KA_YEAR, date.get(KaCalendar.KA_YEAR));
        eventEndDate.set(KaCalendar.KA_MONTH, date.get(KaCalendar.KA_MONTH));
        Calendar cal = Calendar.getInstance();
        today.setTimeInMillis(cal.getTimeInMillis());
        if(date.get(KaCalendar.KA_MONTH) == 14) {
            setRegularDaysVisibility(TextView.GONE);
            dayLabels[8].setVisibility(TextView.VISIBLE);
            dateButtons[0][8].setVisibility(TextView.VISIBLE);
            setCurrentDateStatus(today, date, dateButtons[0][8]);
            eventEndDate.set(KaCalendar.KA_WEEK, 1);
            eventEndDate.set(KaCalendar.KA_DAY, 9);
            if(date.isLeapYear()) {
                dayLabels[7].setVisibility(TextView.VISIBLE);
                dateButtons[0][7].setVisibility(TextView.VISIBLE);
                setCurrentDateStatus(today, date, dateButtons[0][7]);
                eventStartDate.set(KaCalendar.KA_DAY, 8);
            } else {
                eventStartDate.set(KaCalendar.KA_DAY, 9);
            }
        } else {
            setRegularDaysVisibility(TextView.VISIBLE);
            setRegularDaysCurrentDateStatus(today, date);
            dayLabels[7].setVisibility(TextView.GONE);
            dateButtons[0][7].setVisibility(TextView.GONE);
            dayLabels[8].setVisibility(TextView.GONE);
            dateButtons[0][8].setVisibility(TextView.GONE);
            eventStartDate.set(KaCalendar.KA_DAY, 1);
            eventEndDate.set(KaCalendar.KA_WEEK, 4);
            eventEndDate.set(KaCalendar.KA_DAY, 7);
            eventEndDate.add(KaCalendar.KA_DAY, 1);
        }
        eventGetter.startGetEvents(eventStartDate, eventEndDate, this);
    }

    private void setRegularDaysCurrentDateStatus(KaCalendar today, KaCalendar dateSet) {
        for(int i = 0; i < 4; i++) {
            for(int j = 0; j < 7; j++) {
                setCurrentDateStatus(today, dateSet, dateButtons[i][j]);
            }
        }
    }

    private void setCurrentDateStatus(KaCalendar today, KaCalendar dateSet, DateButton button) {
        if(today.get(KaCalendar.KA_GEN) == dateSet.get(KaCalendar.KA_GEN)
                && today.get(KaCalendar.KA_YEAR) == dateSet.get(KaCalendar.KA_YEAR)
                && today.get(KaCalendar.KA_MONTH) == dateSet.get(KaCalendar.KA_MONTH)
                && today.get(KaCalendar.KA_WEEK) == button.getWeek()
                && today.get(KaCalendar.KA_DAY) == button.getDay()) {
            //This button represents the current date
            button.setBackgroundColor(0xff37e8b7);
        } else if(button.getDay() <= 5) {
            //Set weekday color
            button.setBackgroundColor(0xffd6d6d6);
        } else {
            //Set weekend color
            button.setBackgroundColor(0xffffb9b9);
        }
        //Highlight colors:
        //0xff858585 (weekdays)
        //0xffff4848 (weekends)
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

    @Override
    public void setEvents(List<Event> events) {
        //Now populate the buttons
        if(14 == date.get(KaCalendar.KA_MONTH)) {
            if(date.isLeapYear()) {
                setEventsOnDateButton(events, dateButtons[0][7]);
            }
            setEventsOnDateButton(events, dateButtons[0][8]);
        } else {
            for(int i = 0; i < 4; i++) {
                for(int j = 0; j < 7; j++) {
                    setEventsOnDateButton(events, dateButtons[i][j]);
                }
            }
        }
    }

    private void setEventsOnDateButton(List<Event> events, DateButton button) {
        KaCalendar dayStart = new KaCalendar();
        KaCalendar dayEnd = new KaCalendar();
        dayStart.setTimeInMillis(date.getTimeInMillis());
        dayStart.set(KaCalendar.KA_WEEK, button.getWeek());
        dayStart.set(KaCalendar.KA_DAY, button.getDay());
        dayStart.set(KaCalendar.HOUR, 0);
        dayStart.set(KaCalendar.MINUTE, 0);
        dayStart.set(KaCalendar.SECOND, 0);
        long dayStartTime = dayStart.getTimeInMillis();
        long dayEndTime = dayStartTime + 86400000L;

        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        List<Event> dayEvents = new ArrayList<Event>();
        for(Event event : events) {
            long eventStartTime, eventEndTime;
            if(event.getStart().getDateTime() == null)
                eventStartTime = event.getStart().getDate().getValue();
            else
                eventStartTime = event.getStart().getDateTime().getValue();
            if(event.getEnd().getDateTime() == null)
                eventEndTime = event.getEnd().getDate().getValue() + 86400000L;
            else
                eventEndTime = event.getEnd().getDateTime().getValue();
            if((eventStartTime >= dayStartTime && eventStartTime < dayEndTime) || (eventEndTime > dayStartTime && eventEndTime < dayEndTime) || (eventStartTime < dayStartTime && eventEndTime > dayEndTime)) {
                dayEvents.add(event);
            }
        }
        button.setEvents(dayEvents);
        if(dayEvents.isEmpty())
            button.setText("");
        else
            button.setText("...");
    }
}
