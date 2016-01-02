package com.qualimony.alcal;

import com.qualimony.ka.KaCalendar;

/**
 * Created by petre.popescu on 1/2/2016.
 */
public interface EventGetter {

    public void startGetEvents(KaCalendar startTime, KaCalendar endTime, EventTarget target);

}
