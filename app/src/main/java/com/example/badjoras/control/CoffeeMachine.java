package com.example.badjoras.control;

import java.io.Serializable;

/**
 * Created by Rafael on 13/11/2014.
 */
public class CoffeeMachine extends Feature implements Serializable {

    private int scheduled_hour;
    private int scheduled_minute;
    private boolean isScheduled;

    public CoffeeMachine() {
        super();
        resetSchedule();
    }

    public void setSchedule(int hour, int minute) {
        this.scheduled_hour = hour;
        this.scheduled_minute = minute;
        this.isScheduled = true;
    }

    //minutos e horas definidos a -1 pois se fosse 0 equivaleria à meia noite
    public void resetSchedule() {
        this.scheduled_hour = -1;
        this.scheduled_minute = -1;
        this.isScheduled = false;
    }

    public boolean isScheduled() {
        return isScheduled;
    }

    public int getScheduledHour() {
        return scheduled_hour;
    }

    public int getScheduledMinute() {
        return scheduled_minute;
    }
}
