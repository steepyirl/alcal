package com.qualimony.alcal;

import android.content.Context;
import android.widget.Button;

import com.google.api.services.calendar.model.Event;

import java.util.List;

/**
 * Created by petre.popescu on 12/29/2015.
 */
public class DateButton extends Button {

    private int week;
    private int day;

    private int cycle;
    private int year;
    private int month;
    private List<Event> events;
    MonthView monthView;
    boolean current = false;

    public DateButton(Context context, MonthView monthView, int week, int day) {
        super(context);
        this.monthView = monthView;
        this.week = week;
        this.day = day;
    }

    public MonthView getMonthView() {
        return monthView;
    }

    public int getCycle() {
        return cycle;
    }

    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }

    public int getWeek() {
        return week;
    }

    public int getDay() {
        return day;
    }

    public void setEvents(List<Event> events) {
        this.events = events;
    }

    public void setMonth(int cycle, int year, int month) {
        this.cycle = cycle;
        this.year = year;
        this.month = month;
    }

    public List<Event> getEvents() {
        return events;
    }

    public void setCurrent(boolean current) {
        this.current = current;
        if(current)
            setBackgroundColor(0xff37e8b7);
        else {
            if(day <= 5)
                setBackgroundColor(0xffd6d6d6);
            else
                setBackgroundColor(0xffffb9b9);
        }
    }

    public boolean isCurrent() {
        return current;
    }

    public void setSelected(boolean selected) {
        if(!current) {
            if (day <= 5)
                setBackgroundColor(selected ? 0xff858585 : 0xffd6d6d6);
            else
                setBackgroundColor(selected ? 0xffff4848 : 0xffffb9b9);
        }
    }

}
