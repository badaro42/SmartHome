package com.example.badjoras.control;

import java.io.Serializable;
import java.util.HashMap;

import static com.example.badjoras.smarthome.MainActivity.BEDROOM;
import static com.example.badjoras.smarthome.MainActivity.OUTSIDE_GENERAL;
import static com.example.badjoras.smarthome.MainActivity.KITCHEN;
import static com.example.badjoras.smarthome.MainActivity.LIVING_ROOM;
import static com.example.badjoras.smarthome.MainActivity.DAY;
import static com.example.badjoras.smarthome.MainActivity.NIGHT;

/**
 * Created by Rafael on 24/10/2014.
 */
public class Home implements Serializable {

    private HashMap<String, Room> rooms;
    private int counter;
    private int current_time_of_day;
    private boolean user_in_house;

    public Home() {
        populateMap();
        counter = 0;
        user_in_house = true;
        current_time_of_day = 1; //começa de dia!
    }

    public void changeUserInHouse(boolean isInHouse) {
        this.user_in_house = isInHouse;
    }

    public boolean getUserInHouse() {
        return user_in_house;
    }

    public void setCounter(int new_count) {
        counter = new_count;
    }

    public int getCounter() {
        return counter;
    }

    public void incrementCounter() {
        counter++;
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

    public void changeTimeOfDay(int new_time) {
        current_time_of_day = new_time;
    }

    public int getCurrentTimeOfDay() {
        return current_time_of_day;
    }
}
