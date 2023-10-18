package com.booking.app;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

import com.booking.app.authentication.User;
import com.booking.app.rooms.*;
import com.booking.app.rooms.types.Apartment;
import com.booking.app.rooms.types.Deluxe;
import com.booking.app.rooms.types.Double;
import com.booking.app.rooms.types.Single;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.*;

public class Hotel {

    private List<Room> rooms;
    private Set<String> roomTypes;

    public Hotel() {
        this.rooms = new ArrayList<>();
        readJSON();
        roomTypes = new HashSet<>();
        this.rooms.forEach(room -> this.roomTypes.add(room.getType()));
    }


    private void readJSON() {
        JSONParser jsonParser = new JSONParser();
        try (FileReader reader = new FileReader("rooms.json")) {
            //Read JSON file
            Object obj = jsonParser.parse(reader);

            JSONArray roomsList = (JSONArray) obj;

            //Iterate over rooms array
            roomsList.forEach(room -> parseRoomObject((JSONObject) room));

        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }

    private void writeToJSON() {
        JSONArray roomList = new JSONArray();
        this.rooms.forEach(room -> roomList.add(room.toJSON()));
        try (FileWriter file = new FileWriter("rooms.json")) {
            file.write(roomList.toJSONString());
            file.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void parseRoomObject(JSONObject room) {
        int number = Integer.parseInt(String.valueOf(room.get("number")));
        String type = String.valueOf(room.get("type"));
        Map<String, String> datesBooked = new HashMap<>();
        JSONObject datesBookedObject = (JSONObject) room.get("datesBooked");
        datesBookedObject.keySet().forEach(key -> datesBooked.put(key.toString(), String.valueOf(datesBookedObject.get(key))));
        List<String> datesCancelled = new ArrayList<>();
        JSONArray datesCancelledArray = (JSONArray) room.get("datesCancelled");
        datesCancelledArray.forEach(item-> datesCancelled.add(item.toString()));
        if (Objects.equals(type, "Single")) {
            this.rooms.add(new Single(number, datesBooked, datesCancelled ));
        }
        if (Objects.equals(type, "Double")) {
            this.rooms.add(new Double(number, datesBooked, datesCancelled));
        }
        if (Objects.equals(type, "Deluxe")) {
            this.rooms.add(new Deluxe(number, datesBooked, datesCancelled));
        }
        if (Objects.equals(type, "Apartment")) {
            this.rooms.add(new Apartment(number, datesBooked, datesCancelled));
        }
    }

    public void getRooms() {
        this.rooms.forEach(room -> System.out.printf("Number: %d - Type: %s - Price: %.2f per night %n", room.getNumber(), room.getType(), room.getPrice()));
    }

    public void setRooms(List<Room> rooms) {
        this.rooms = rooms;
    }

    public Set<String> getTypes() {
        return this.roomTypes;
    }

    public void reserveRoom(String date, String username, int number) {
        Room selectedRoom = null;
        for (Room room :
                this.rooms) {
            if (room.getNumber() == number) {
                selectedRoom = room;
            }
        }
        if (selectedRoom != null) {
            if (selectedRoom.isBooked(date)) {
                System.out.println("The room is already booked!");
            } else {
                selectedRoom.setDatesBooked(date, username);
                writeToJSON();
                System.out.printf("Room %d reserved! %n", selectedRoom.getNumber());
            }
        } else {
            System.out.println("The room does not exists!");
        }

    }


    public void cancelReservation(String date, int number, User user) {
        Room selectedRoom = null;
        for (Room room :
                this.rooms) {
            if (room.getNumber() == number) {
                selectedRoom = room;
            }
        }
        if (selectedRoom != null) {
            if (!selectedRoom.isBooked(date)) {
                System.out.println("The room is not booked!");
            } else {
                if (selectedRoom.getBookedUsername(date).equals(user.getUsername())) {
                    System.out.printf("Cancellation successful! The fee %.2f has to be paid. %n", selectedRoom.getFee());;
                    selectedRoom.removeBooking(date);
                    selectedRoom.setCancelledByUsers(user.getUsername());
                    writeToJSON();
                } else {
                    System.out.println("The room has been booked by somebody else.");
                }
            }
        } else {
            System.out.println("The room does not exists!");
        }
    }

    public boolean availableRooms(String date, String type) {
        List<Room> availableRooms = new ArrayList<>();
        this.rooms.forEach(room -> {
            if (!room.isBooked(date) && room.getType().equalsIgnoreCase(type)) {
                System.out.println(room.getDatesBooked());
                System.out.println(room.getType());
                availableRooms.add(room);
            }
        });

        if (!availableRooms.isEmpty()) {
            availableRooms.forEach(room -> {
                System.out.printf("Room Number: %d %n", room.getNumber());
            });
        } else {
            System.out.println("No available rooms!");
            return false;
        }
        return true;
    }

    public void getUserBookings(User loggedUser) {
        this.rooms.forEach(room -> {
            room.getDatesBooked().keySet().forEach( date -> {
                if (room.getBookedUsername(date).equals(loggedUser.getUsername())) {
                    System.out.printf("%s number %s booked for %s %n", room.getType(), room.getNumber(), date);
                }
            });
        });
    }

    public void getAllBookings() {
        this.rooms.forEach(room -> {
            if (!room.getDatesBooked().isEmpty()){
                System.out.printf("Room %s bookings: %n", room.getNumber());
            }
            room.getDatesBooked().keySet().forEach( date -> {
                    System.out.printf(" - %s booked by %s %n",  date, room.getDatesBooked().get(date));
            });
        });
    }

    public void getTotalIncome() {
        final double[] totalIncome = {0};
        this.rooms.forEach(room -> {
            if(!room.getDatesBooked().isEmpty()){
                totalIncome[0] = totalIncome[0] + room.getDatesBooked().size() * room.getPrice();
            }
        });
        System.out.printf("Total income from bookings - %.2f", totalIncome[0]);
    }

    public void getTotalFee() {
    }
}


