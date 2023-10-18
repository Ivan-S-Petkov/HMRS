package com.booking.app.rooms.types;

import com.booking.app.rooms.Room;

import java.util.List;
import java.util.Map;

public class Double extends Room {


    public Double(int number, Map<String, String> datesBooked, List<String> datesCancelled) {
        super(number, datesBooked, datesCancelled);
    }

    @Override
    public double getPrice() {
        return 60;
    }
    @Override
    public double getFee() {
        return 20;
    }
    @Override
    public String getType() {
        return "Double";
    }
}
