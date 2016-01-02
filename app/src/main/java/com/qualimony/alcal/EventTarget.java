package com.qualimony.alcal;

import com.google.api.services.calendar.model.Event;

import java.util.List;

/**
 * Created by petre.popescu on 1/2/2016.
 */
public interface EventTarget {

    public void setEvents(List<Event> events);

}
