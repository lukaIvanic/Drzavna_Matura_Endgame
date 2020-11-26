package com.example.drzavnamatura_endgame;

public class Icons {
    int price;
    String iconPicUrl;
    Boolean isBought;

    public Icons(int price, String iconPicUrl, Boolean isBought) {
        this.price = price;
        this.iconPicUrl = iconPicUrl;
        this.isBought = isBought;
    }

    public int getPrice() {
        return price;
    }

    public String getIconPicUrl() {
        return iconPicUrl;
    }

    public Boolean getBought() {
        return isBought;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setIconPicUrl(String iconPicUrl) {
        this.iconPicUrl = iconPicUrl;
    }

    public void setBought(Boolean bought) {
        isBought = bought;
    }
}
