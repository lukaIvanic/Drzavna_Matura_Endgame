package com.example.drzavnamatura_endgame;

public class Room {
    private String roomName;
    private int maxPlayers;
    private int currentPlayers;
    private boolean isPlayClicked = false;
    private boolean full;
    public boolean fromRandom = false;


    public Room(String roomName, int maxPlayers, int currentPlayers, boolean full) {
        this.roomName = roomName;
        this.maxPlayers = maxPlayers;
        this.currentPlayers = currentPlayers;
        this.full = full;
    }

    public boolean isFromRandom() {
        return fromRandom;
    }

    public void setFromRandom(boolean fromRandom) {
        this.fromRandom = fromRandom;
    }

    public boolean isFull() {
        return full;
    }

    public void setFull(boolean full) {
        this.full = full;
    }

    public boolean isPlayClicked() {
        return isPlayClicked;
    }

    public void setPlayClicked(boolean playClicked) {
        isPlayClicked = playClicked;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public void setMaxPlayers(int maxPlayers) {
        this.maxPlayers = maxPlayers;
    }

    public int getCurrentPlayers() {
        return currentPlayers;
    }

    public void setCurrentPlayers(int currentPlayers) {
        this.currentPlayers = currentPlayers;
    }
}