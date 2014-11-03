package com.example.badjoras.control;

import java.io.Serializable;
import java.util.HashMap;

import static com.example.badjoras.smarthome.MainActivity.BEDROOM;
import static com.example.badjoras.smarthome.MainActivity.OUTSIDE_GENERAL;
import static com.example.badjoras.smarthome.MainActivity.KITCHEN;
import static com.example.badjoras.smarthome.MainActivity.LIVING_ROOM;

/**
 * Created by Rafael on 24/10/2014.
 */
public class Home implements Serializable {

    private HashMap<String, Room> rooms;

    public Home() {
        populateMap();
    }

    private void populateMap() {
        rooms = new HashMap<String, Room>();
        rooms.put(BEDROOM, new Room(BEDROOM));
        rooms.put(KITCHEN, new Room(KITCHEN));
        rooms.put(LIVING_ROOM, new Room(LIVING_ROOM));
        rooms.put(OUTSIDE_GENERAL, new Room(OUTSIDE_GENERAL));
    }

    public HashMap getMap() {
        return rooms;
    }

    public void modifyMap(HashMap<String, Room> new_map) {
        this.rooms = new_map;
    }
}
