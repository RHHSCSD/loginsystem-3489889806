package loginsystem;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import static java.lang.Character.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/**
 *
 * @author jonat
 */
public final class RegistrationSystem {

    final static String DEFAULTFILELOCATION = "userbase.dsv";
    final static int PASSWORDMINIMUMLENGTH = 4;
    //Array to store the users.
    private ArrayList<User> users;
    //Easily find user ID in the file
    private HashMap<String, Integer> userIDs;
    private int userCount = 0;
    private String fileLocation = DEFAULTFILELOCATION;
    private File Storage = new File(fileLocation);

    /**
     * Adds a user to the login/registration
     *
     * @param u the user to add to the registration system.
     * @return if the user is added, true if username hasn't been taken (and
     * thus user has been added), or false if the username has been taken(and
     * thus user has not been added)
     */
    private int addUser(User u) {
        //check if the username is unique
        if (!isUnique(u.userName())) {
            return 1;
        }
        //add the user into the arraylist
        users.add(u);
        //add the user into usernames list
        userIDs.put(u.userName(), userCount);
        userCount++;
        //write the user to file
        saveUser(u);
        return 0;
    }

    private Boolean isUnique(String userName) {
        return !userIDs.containsKey(userName);
    }

    private Boolean ContainsDelimiter(String field) {
        return (field.contains(User.DELIMITER));
    }

    private String encrypt(String password, String salt) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            digest.update((password + salt).getBytes());
            byte data[] = digest.digest();
            String encrypted = "";
            for (byte b : data) {
                encrypted += (Integer.toHexString((b & 0XFF) | 0x100).substring(1, 3));
            }
            return encrypted;
        } catch (NoSuchAlgorithmException ex) {
            System.out.println("SHA-256 NOT FOUND");
            return null;
        }
    }

    private String generateSalt() {
        Random r = new Random();
        String salt = "";
        final String alphabets = "1234567890QWERTYUIOPASDFGHJKLZXCVBNMqwertyuiopasfghjklzxcvbnm";
        for (int i = 0; i < 10 + r.nextInt(10); i++) {
            salt += alphabets.charAt(r.nextInt(alphabets.length()));
        }
        return salt;
    }

    /**
     * @param username identifier of user
     * @param displayName name shown publicly
     * @param email email of user
     * @param password password(unhashed) of user
     * @param questions security questions
     * @param answers security answers
     * @return 0 if good, 1 if username has been taken, 2 if needs uppercase, 3
     * if needs lower case, 4 if needs number, 5 if needs special character, 6
     * if it's a common password 7 if the password has delimiter, 8 if password
     * is too short
     */
    public int register(String username, String displayName, String email, String password, String[] questions, String[] answers) {
        int passwordStatus = validatePassword(password);
        if (passwordStatus != 0) {
            return passwordStatus;
        }
        if (ContainsDelimiter(username)) {
            return 7;
        }
        String salt = generateSalt();
        password = encrypt(password, salt);
        User temp = new User(username, displayName, email, password, salt, questions, answers);
        //do some salting
        return addUser(temp);
    }

    /**
     * Validates credentials of the user.
     *
     * @param username
     * @param password
     * @return true if successful, false if not.
     */
    public Boolean login(String username, String password) {
        if (!userIDs.containsKey(username)) {
            return false;
        }
        User temp = users.get(userIDs.get(username));
        return temp.validatePassword(encrypt(password, temp.salt()));
    }

    /**
     *
     * @param pwd
     * @return 0 if good, 2 if needs uppercase, 3 if needs lower case, 4 if
     * needs number, 5 if needs special character, 6 if it's a common password
     */
    private int validatePassword(String pwd) {
        Boolean hasLower = false, hasUpper = false, hasNumber = false, hasSpecial = false;
        if (pwd.length() < PASSWORDMINIMUMLENGTH) {
            return 8;
        }
        for (int i = 0; i < pwd.length(); i++) {
            char x = pwd.charAt(i);
            if (isUpperCase(x)) {
                hasUpper = true;
                continue;
            }
            if (isLowerCase(x)) {
                hasLower = true;
                continue;
            }
            if (isDigit(x)) {
                hasNumber = true;
                continue;
            }
            if (!((isDigit(x)) || isAlphabetic(x))) {
                hasSpecial = true;
            }
        }
        if (!hasUpper) {
            return 2;
        }
        if (!hasLower) {
            return 3;
        }
        if (!hasNumber) {
            return 4;
        }
        if (!hasSpecial) {
            return 5;
        }
        File passwordsDict = new File("dictbadpass.txt");
        try {
            Scanner s = new Scanner(passwordsDict);
            while (s.hasNext()) {
                if (s.nextLine().equals(pwd)) {
                    return 6;
                }
            }
        } catch (FileNotFoundException e) {
        }
        return 0;

    }

    private void saveUser(User u) {
        try {
            FileWriter writer = new FileWriter(Storage, true);
            writer.write(u.toString());
            writer.close();
        } catch (IOException e) {
            System.out.println("Cannot write to file.");
        }
    }

    private void clearMemory() {
        this.users = new ArrayList<>();
        this.userCount = 0;
        this.userIDs = new HashMap<>();
    }

    /**
     * Loads a user base given a file location.
     *
     * @param fileLocation
     */
    public void loadUsers(String fileLocation) {
        this.fileLocation = fileLocation;
        this.Storage = new File(this.fileLocation);
        try {
            Scanner s = new Scanner(Storage);
            s.useDelimiter(User.DELIMITER);
            clearMemory();
            while (s.hasNextLine()) {
                String username = s.next();
                String displayName = s.next();
                String email = s.next();
                String password = s.next();
                String salt = s.next();
                String[] questions = new String[3];
                String[] answers = new String[3];
                for (int i = 0; i < 3; i++) {
                    questions[i] = s.next();
                    answers[i] = s.next();
                }
                User temp = new User(username, displayName, email, password, salt, questions, answers);
                addUser(temp);
            }
        } catch (FileNotFoundException e) {
            System.out.println(e);
        }

    }

    /**
     * Creates login based on default storage file location
     */
    public RegistrationSystem() {
        loadUsers(DEFAULTFILELOCATION);
    }

    /**
     *
     * Creates login based on default storage file location
     *
     * @param FileLocation location of the file
     */
    public RegistrationSystem(String FileLocation) {
        loadUsers(fileLocation);
    }

    /**
     *
     * Creates login based on list of users
     *
     * @param userbase users to initialize loginSystem to. Only name uniqueness
     * is checked but any issues will not be returned.
     */
    public RegistrationSystem(User[] userbase) {
        clearMemory();
        for (User u : userbase) {
            addUser(u);
        }
    }
}
