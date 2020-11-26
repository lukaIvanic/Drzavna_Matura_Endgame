package com.example.drzavnamatura_endgame;

public class User {
    private String username;
    private String email;
    private String userID;
    private String score;
    private boolean isMale;
    private String profilePicUrl;


    public User() {
    }

    public User(String username, String email, String userID, String score, boolean male, String profilePicUrl) {
        this.username = username;
        this.email = email;
        this.userID = userID;
        this.score = score;
        this.isMale = male;
        this.profilePicUrl = profilePicUrl;
    }

    public String getProfilePicUrl() {
        return profilePicUrl;
    }

    public String getUsername() {
        return username;
    }

    public String getScore() {
        return score;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public void setMale(boolean male) {
        isMale = male;
    }

    public void setProfilePicUrl(String profilePicUrl) {
        this.profilePicUrl = profilePicUrl;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public boolean isMale() {
        return isMale;
    }

}