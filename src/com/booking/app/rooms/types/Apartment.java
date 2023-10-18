package com.booking.app.rooms.types;

import com.booking.app.rooms.Room;

import java.util.List;
import java.util.Map;

public class Apartment extends Room {

    public Apartment(int number, Map<String, String> datesBooked, List<String> datesCancelled) {
        super(number, datesBooked, datesCancelled);
    }

    @Override
    public double getPrice() {
        return 120;
    }
    @Override
    public double getFee() {
        return 40;
    }
    @Override
    public String getType() {
        return "Apartment";
    }
}
