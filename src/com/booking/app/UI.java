package com.booking.app;

import com.booking.app.authentication.User;
import com.booking.app.authentication.Users;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Scanner;

public class UI {
    public static void main(String[] args) throws ParseException {
        Scanner sc = new Scanner(System.in);
        Hotel hotel = new Hotel();
        Users users = new Users();
        User loggedUser = new User("", "", false, false);
        showMenu(loggedUser);
        while (sc.hasNext()) {
            String command = sc.nextLine();
            switch (command) {
                case "0":
                    showMenu(loggedUser);
                    break;
                case "1":
                    hotel.getRooms();
                    break;
                case "2":
                    if (loggedUser.isLogged()) {
                        System.out.print("Please specify date in format /i.e. 30.07.2023/: ");
                        try {
                            String date = new SimpleDateFormat("dd.MM.yyyy").format(new SimpleDateFormat("dd.MM.yyyy").parse(sc.nextLine()));
                            System.out.println("Room Types:");
                            hotel.getTypes().forEach(System.out::println);
                            System.out.print("Please specify room type: ");
                            String type = sc.nextLine();
                            if (!hotel.availableRooms(date, type)) {
                                break;
                            }
                            System.out.print("Please specify room number: ");
                            int numberToReserve = Integer.parseInt(sc.nextLine());
                            hotel.reserveRoom(date, loggedUser.getUsername(), numberToReserve);
                        }
                        catch (ParseException e){
                            System.out.println("The specified date is not in the requested format.");
                            break;
                        }

                    } else {
                        System.out.println("Please log in!");
                    }
                    break;
                case "3":
                    if (loggedUser.isLogged()) {
                        System.out.print("Please specify room number: ");
                        int numberToCancel = Integer.parseInt(sc.nextLine());
                        System.out.print("Please specify date in format /i.e. 30.07.2023/: ");
                        try {
                            String dateToCancel = new SimpleDateFormat("dd.MM.yyyy").format(new SimpleDateFormat("dd.MM.yyyy").parse(sc.nextLine()));
                            hotel.cancelReservation(dateToCancel, numberToCancel,loggedUser);
                        }catch (ParseException e){
                            System.out.println("The specified date is not in the requested format.");
                            break;
                        }
                    } else {
                        System.out.println("Please log in!");
                    }
                    break;
                case "4":
                    if (!loggedUser.isLogged()) {
                        System.out.println("Please enter the following details: ");
                        System.out.print("Username: ");
                        String username = sc.nextLine();
                        System.out.print("Password: ");
                        String password = sc.nextLine();
                        System.out.print("Repeat password: ");
                        String rePassword = sc.nextLine();
                        users.register(username, password, rePassword);
                    } else {
                        System.out.println("You are logged in.");
                    }
                    break;
                case "5":
                    if (!loggedUser.isLogged()) {
                        System.out.println("Please enter the following details: ");
                        System.out.print("Username: ");
                        String loginUsername = sc.nextLine();
                        System.out.print("Password: ");
                        String loginPassword = sc.nextLine();
                        loggedUser = users.login(loginUsername, loginPassword);
                    } else {
                        System.out.println("You are logged in.");
                    }
                    break;
                case "6":
                    if(loggedUser.isLogged()){
                        hotel.getUserBookings(loggedUser);
                    } else {
                        System.out.println("You are not logged in.");
                    }
                    break;
                case "7":
                    if (loggedUser.isLogged()){
                    loggedUser.setLogged(false);
                    System.out.println("Logged Out successful!");}
                    else {
                        System.out.println("You are not logged in.");
                    }
                    break;
                case "8":
                    if(loggedUser.isAdmin()){
                        hotel.getAllBookings();
                    } else {
                        System.out.println("Not logged as admin");
                    }
                    break;
                case "9":
                    if(loggedUser.isAdmin()){
                        hotel.getTotalIncome();
                        hotel.getTotalFee();
                    } else {
                        System.out.println("Not logged as admin");
                    }
                    break;
                case "10":
                    if(loggedUser.isAdmin()){

                    } else {
                        System.out.println("Not logged as admin");
                    }
                    break;
                default:
                    System.out.println("No such command!");
                    break;
            }
        }
    }

    private static void showMenu(User loggedUser) {
        System.out.println("Menu");
        System.out.println("0. View Menu");
        System.out.println("1. View Rooms");
        if (loggedUser.isLogged()) {
            System.out.println("2. Book a Room");
            System.out.println("3. Cancel Booking");
        }
        if (!loggedUser.isLogged()) {
            System.out.println("4. Register");
            System.out.println("5. Login");
        } else {
            System.out.println("6. View Profile");
            System.out.println("7. Logout");
        }
        if (loggedUser.isAdmin()) {
            System.out.println("8. View All Bookings");
            System.out.println("9. View total income and cancellation fees.");
            System.out.println("10. Add or remove or modify room details.");
        }
        System.out.println();
        System.out.println("Please enter the number of the operation you would like to perform.");
    }
}
