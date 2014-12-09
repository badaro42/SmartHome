package com.example.badjoras.control;

import java.io.Serializable;

/**
 * Created by Rafael on 13/11/2014.
 */
public class Blinds extends Feature implements Serializable {

    //true -> desbloqueado; false -> bloqueado
    private boolean unlocked;

    //0 -> totalmente aberto; 100 -> totalmente fechado
    private int opening;

    public Blinds() {
        super();
        unlocked = false;
        opening = 0;
    }

    public void changeUnlockingStatus(boolean status) {
        unlocked = status;
    }

    public boolean getUnlockingStatus() {
        return unlocked;
    }

    public int getOpening() {
        return opening;
    }

    public void changeOpening(int new_opening) {
        opening = new_opening;
    }
}
