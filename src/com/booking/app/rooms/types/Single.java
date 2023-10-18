package com.booking.app.rooms.types;

import com.booking.app.rooms.Room;

import java.util.List;
import java.util.Map;

public class Single extends Room {


    public Single(int number, Map<String, String> datesBooked, List<String> datesCancelled) {
        super(number, datesBooked, datesCancelled);
    }

    @Override
    public double getPrice() {
        return 40;
    }
    @Override
    public double getFee() {
        return 15;
    }

    @Override
    public String getType() {
        return "Single";
    }
}
