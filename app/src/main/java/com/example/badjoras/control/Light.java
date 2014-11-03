package com.example.badjoras.control;

import java.io.Serializable;

/**
 * Created by Rafael on 02/11/2014.
 */
public class Light extends Feature implements Serializable {

    private int intensity;

    public Light() {
        super();
        intensity = 0;
    }

    public int getIntensity() {
        return intensity;
    }

    public void changeIntensity(int new_value) {
        if((new_value >= 0) && (new_value <= 100))
            intensity = new_value;
    }

}
