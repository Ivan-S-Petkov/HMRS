package com.booking.app.authentication;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.*;

public class Users {
    private final List<User> users;

    public Users() {
        this.users = new ArrayList<>();
        readJSON();
    }

    public void register(String username, String password, String rePassword) {
        if (Objects.equals(password, rePassword)) {
            if (!duplicateUsername(username)) {
                try {
                    String hashedPassword = Password.generateStorngPasswordHash(password);
                    writeToJSON(username, hashedPassword);
                    readJSON();
                    System.out.println("Registered successful!");
                } catch (NoSuchAlgorithmException e) {
                    throw new RuntimeException(e);
                } catch (InvalidKeySpecException e) {
                    throw new RuntimeException(e);
                }
            } else {
                System.out.println("Username already exists!");
            }
        } else {
            System.out.println("Password does not match!");
        }
    }

    private void parseUserObject(JSONObject user) {
        String username = String.valueOf(user.get("username"));
        String password = String.valueOf(user.get("password"));
        boolean admin = Boolean.parseBoolean(String.valueOf(user.get("admin")));
        this.users.add(new User(username, password, true, admin));
    }

    private void readJSON() {
        JSONParser jsonParser = new JSONParser();
        try (FileReader reader = new FileReader("users.json")) {
            if (reader.ready()) {
                Object obj = jsonParser.parse(reader);
                JSONArray usersList = (JSONArray) obj;
                this.users.clear();
                usersList.forEach(user -> parseUserObject((JSONObject) user));
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }

    private void writeToJSON(String username, String password) {
        JSONArray usersList = new JSONArray();
        this.users.forEach(user -> usersList.add(user.toJSON()));
        try (FileWriter file = new FileWriter("users.json")) {
            JSONObject user = new JSONObject();
            user.put("username", username);
            //TO DO Hash password
            user.put("password", password);
            user.put("admin", false);
            usersList.add(user);
            file.write(usersList.toJSONString());
            file.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean duplicateUsername(String username) {
        if (users.stream().noneMatch(user -> Objects.equals(user.getUsername(), username))) {
            return false;
        } else {
            return true;
        }
    }

    public User login(String loginUsername, String loginPassword) {
        List<User> userFound = users.stream().filter(user -> {
            boolean b = false;
            try {
                b = Objects.equals(user.getUsername(), loginUsername) && Password.validatePassword(loginPassword, user.getPassword());
            } catch (NoSuchAlgorithmException e) {
                throw new RuntimeException(e);
            } catch (InvalidKeySpecException e) {
                throw new RuntimeException(e);
            }
            return b;
        }).toList();
        if (userFound.isEmpty()) {
            System.out.println("Username or password are incorrect");
            return null;
        } else {
            System.out.println("Logged in successfully!");
            User loggedUser = userFound.get(0);
            loggedUser.setLogged(true);
            return loggedUser;
        }
    }
}
