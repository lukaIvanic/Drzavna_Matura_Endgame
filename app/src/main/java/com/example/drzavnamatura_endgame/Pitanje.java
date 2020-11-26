package com.example.drzavnamatura_endgame;

import android.widget.RadioButton;

public class Pitanje {

    String pitanje;
    String odg1;
    String odg2;
    String odg3;
    String odg4;
    String url;
    String metadata;
    Boolean isTocno;
    String user_odgovor = "";
    String tocanOdgovorString = "";
    int kojiOdgovor;
    int tocanOdgovor;

    public Pitanje(String pitanje, String user_odgovor, String tocanOdgovorString, String url) {
        this.pitanje = pitanje;
        this.user_odgovor = user_odgovor;
        this.tocanOdgovorString = tocanOdgovorString;
        this.url = url;
        this.metadata = "user_input";
    }

    public Pitanje(String pitanje, String odg1, String odg2, String odg3, String odg4, int tocanOdgovor, int kojiOdgovor, Boolean isTocno, String metadata) {
        this.pitanje = pitanje;
        this.odg1 = odg1;
        this.odg2 = odg2;
        this.odg3 = odg3;
        this.odg4 = odg4;
        this.tocanOdgovorString = "nope";
        this.tocanOdgovor = tocanOdgovor;
        this.isTocno = isTocno;
        this.kojiOdgovor = kojiOdgovor;
        this.metadata = metadata;
    }

    public Pitanje(String pitanje, String odg1, String odg2, String odg3, String odg4, int tocanOdgovor, String url, int kojiOdgovor, Boolean isTocno, String metadata) {
        this.pitanje = pitanje;
        this.odg1 = odg1;
        this.odg2 = odg2;
        this.odg3 = odg3;
        this.odg4 = odg4;
        this.tocanOdgovor = tocanOdgovor;
        this.url = url;
        this.tocanOdgovorString = "nope";
        this.isTocno = isTocno;
        this.kojiOdgovor = kojiOdgovor;
        this.metadata = metadata;
    }

    public void setKojiOdgovor(int kojiOdgovor) {
        this.kojiOdgovor = kojiOdgovor;
    }

    public Boolean getIsTocno() {
        return isTocno;
    }

    public String getUrl() {
        return url;
    }

    public String getPitanje() {
        return pitanje;
    }

    public String getOdg1() {
        return odg1;
    }

    public String getOdg2() {
        return odg2;
    }

    public String getOdg3() {
        return odg3;
    }

    public String getOdg4() {
        return odg4;
    }

    public int getTocanOdgovor() {
        return tocanOdgovor;
    }

    public void setIsTocno(Boolean isTocno) {
        this.isTocno = isTocno;
    }

    public int getKojiOdgovor() {
        return kojiOdgovor;
    }

    public String getMetadata() {
        return metadata;
    }

    public String getUser_odgovor() {
        return user_odgovor;
    }

    public void setUser_odgovor(String user_odgovor) {
        this.user_odgovor = user_odgovor;
    }

    public String getTocanOdgovorString() {
        return tocanOdgovorString;
    }

    public void setTocanOdgovorString(String tocanOdgovorString) {
        this.tocanOdgovorString = tocanOdgovorString;
    }

    public void setPitanje(String pitanje) {
        this.pitanje = pitanje;
    }

    public void setOdg1(String odg1) {
        this.odg1 = odg1;
    }

    public void setOdg2(String odg2) {
        this.odg2 = odg2;
    }

    public void setOdg3(String odg3) {
        this.odg3 = odg3;
    }

    public void setOdg4(String odg4) {
        this.odg4 = odg4;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setMetadata(String metadata) {
        this.metadata = metadata;
    }

    public Boolean getTocno() {
        return isTocno;
    }

    public void setTocno(Boolean tocno) {
        isTocno = tocno;
    }

    public void setTocanOdgovor(int tocanOdgovor) {
        this.tocanOdgovor = tocanOdgovor;
    }
}
