package com.example.badjoras.control;

import java.io.Serializable;

/**
 * Created by Rafael on 17/11/2014.
 */
public class StoveOven extends Feature implements Serializable {

    //esta variavel tem o INDICE do vector de minutos que está no fragmento,
    //e não o numero de minutos em si
    private int index_minutes_to_go;

    private boolean stove_is_on;
    private int temperature;

    //AVISO!!!
    //A variavel "minutes" contem o indice para o numberpicker
    //Ou seja, se minutes==1, na realidade são 5 minutos
    //AFINAL TALVEZ NAO, ESTA MERDA IA COMPLICAR PA CRL

    public StoveOven() {
        super();
        resetStove();
    }

    public int getTemperature() {
        return this.temperature;
    }

    public int getIndexMinutesToGo() {
        return this.index_minutes_to_go;
    }

    public boolean isStoveOn() {
        return stove_is_on;
    }

    public void setTemperature(int new_temperature) {
        this.temperature = new_temperature;
    }


    public void changeMinutesToGo(int new_index_minutes) {
        this.index_minutes_to_go = new_index_minutes;
    }

    public void turnOnStove() {
        stove_is_on = true;
    }

    public void turnOffStove() {
        resetStove();
    }

    public void resetStove() {
        stove_is_on = false;
        temperature = 0;
        index_minutes_to_go = 0;
    }
}
