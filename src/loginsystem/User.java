/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package loginsystem;

import static java.lang.Math.min;

/**
 *
 * @author jonat
 */
public class User {

    static final String DELIMITER = ",";
    protected String userName;
    protected String password;
    protected String salt;
    protected String[] securityQuestions;
    protected String[] securityAnswers;
    public String displayName;
    protected String email;

    /**
     * @param username user name
     * @param password password
     */
    public User(String username, String password) {
        this.userName = username;
        this.password = password;
        this.securityQuestions = new String[3];
        this.securityAnswers = new String[3];
    }

    /**
     *
     * @param username user name
     * @param password password
     * @param questions security questions that must be answered to log in
     * @param answers answers to security answers
     */
    public User(String username, String password, String[] questions, String[] answers) {
        this.userName = username;
        this.password = password;
        this.securityQuestions = questions;
        this.securityAnswers = answers;
    }

    /**
     *
     * @param username user name
     * @param displayName name to display
     * @param email email of user
     * @param password password
     * @param salt salt is stored locally unfortunately
     * @param questions security questions that must be answered to log in
     * @param answers answers to security answers
     */
    public User(String username, String displayName, String email, String password, String salt, String[] questions, String[] answers) {
        this.userName = username;
        this.displayName = displayName;
        this.email = email;
        this.password = password;
        this.salt = salt;
        this.securityQuestions = questions;
        this.securityAnswers = answers;
    }

    public String userName() {
        return this.userName;
    }

    public String[] securityQuestions() {
        return this.securityQuestions;
    }

    public void setPassword(String s) {
        this.password = s;
    }

    public void setDisplayName(String name) {
        this.displayName = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String email() {
        return this.email;
    }

    public String salt() {
        return this.salt;
    }

    public Boolean validatePassword(String password) {
        return password.equals(this.password);
    }

    public Boolean validateAnswers(String[] answers) {
        Boolean x = true;
        for (int i = 0; i < min(answers.length, this.securityAnswers.length); i++) {
            x &= answers[i].equals(securityAnswers[i]) || answers[i].equals("");
        }
        return x;
    }

    @Override
    public String toString() {
        String temp = "";
        for (int i = 0; i < 3; i++) {
            temp += this.securityQuestions[i];
            temp += DELIMITER;
            temp += this.securityAnswers[i];
            temp += DELIMITER;

        }
        return this.userName + DELIMITER + this.displayName + DELIMITER + this.email + DELIMITER + this.password + DELIMITER + this.salt + DELIMITER + temp;
    }
}
