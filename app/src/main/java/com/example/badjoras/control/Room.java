package com.example.badjoras.control;

import java.io.Serializable;
import java.util.HashMap;

import static com.example.badjoras.smarthome.MainActivity.*;

/**
 * Created by Rafael on 24/10/2014.
 */
public class Room implements Serializable {

    private HashMap<String, Feature> features;

    public Room(String room) {
        populateMap(room);
    }

    //TODO: refazer esta lista de features!!!
    private void populateMap(String room) {
        features = new HashMap<String, Feature>();

        if (room.equalsIgnoreCase(KITCHEN)) {
            features.put(PANTRY_STOCK, new Feature());
            features.put(AIR_CONDITIONER, new Feature());
            features.put(LIGHTS, new Feature());
            features.put(BLINDS, new Feature());
            features.put(COFFEE_MACHINE, new Feature());
            features.put(STOVE_OVEN, new Feature());
        }
        else if (room.equalsIgnoreCase(BEDROOM)) {
            features.put(AIR_CONDITIONER, new Feature());
            features.put(LIGHTS, new Feature());
            features.put(BLINDS, new Feature());
        }
        else if (room.equalsIgnoreCase(LIVING_ROOM)) {
            features.put(AIR_CONDITIONER, new Feature());
            features.put(LIGHTS, new Feature());
            features.put(BLINDS, new Feature());
        }
        else if (room.equalsIgnoreCase(OUTSIDE_GENERAL)) {
            features.put(SPRINKLER, new Feature());
            features.put(GARAGE_DOOR, new Feature());
            features.put(SURVEILLANCE_CAMERAS, new Feature());
            features.put(POWER_MONITORING, new Feature());
            features.put(SCHEDULED_FUNCTIONS, new Feature());
        }
    }

    public HashMap getMap() {
        return this.features;
    }

    public void modifyMap(HashMap<String, Feature> new_map) {
        this.features = new_map;
    }
}