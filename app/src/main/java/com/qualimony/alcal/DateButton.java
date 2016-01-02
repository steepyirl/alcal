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
    private List<Event> events;

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

    public void setEvents(List<Event> events) {
        this.events = events;
    }

    public List<Event> getEvents() {
        return events;
    }

}
