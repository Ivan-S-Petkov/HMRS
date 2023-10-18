package com.booking.app.rooms;

import org.json.simple.JSONObject;

import java.util.List;
import java.util.Map;

public class Room {
    private final int number;
    private final Map<String, String> datesBooked;
    private final List<String> cancelledByUsers;


    public Room(int number, Map<String, String> datesBooked, List<String> cancelledByUsers) {
        this.number = number;
        this.datesBooked = datesBooked;
        this.cancelledByUsers = cancelledByUsers;
    }

    public int getNumber() {
        return number;
    }

    public boolean isBooked(String date) {
        return this.datesBooked.get(date) != null;
    }

    public String getBookedUsername(String date) {
        return this.datesBooked.get(date);
    }

    public Map<String, String> getDatesBooked() {
        return this.datesBooked;
    }

    public void setDatesBooked(String date, String username) {
        datesBooked.put(date, username);
    }

    public void removeBooking(String date){
        this.datesBooked.remove(date);
    }

    public JSONObject toJSON() {
        JSONObject roomDetails = new JSONObject();
        roomDetails.put("number", this.getNumber());
        roomDetails.put("type", this.getType());
        roomDetails.put("datesBooked", this.getDatesBooked());
        roomDetails.put("cancelledByUsers", this.getCancelledByUsers());
        return roomDetails;
    }

    public String getType(){
        return "";
    }

    public double getPrice(){
        return 0;
    }

    public double getFee(){
        return 0;
    }

    public List<String> getCancelledByUsers() {
        return cancelledByUsers;
    }

    public void setCancelledByUsers(String username) {
        this.cancelledByUsers.add(username);
    }
}
