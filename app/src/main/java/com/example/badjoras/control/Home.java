package com.example.badjoras.control;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by Rafael on 24/10/2014.
 */
public class Home implements Serializable{

    private HashMap<String, Room> rooms;

    public Home() {
        populateMap();
    }

    private void populateMap() {
        rooms = new HashMap<String, Room>();
        rooms.put("Quarto", new Room());
        rooms.put("Cozinha", new Room());
        rooms.put("Sala", new Room());
        rooms.put("Exterior", new Room());
    }

    public HashMap getMap() {
        return rooms;
    }
}
