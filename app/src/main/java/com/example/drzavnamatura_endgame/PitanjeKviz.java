package com.example.drzavnamatura_endgame;

import java.util.ArrayList;

public class PitanjeKviz {
    private String pitanje;
    private String tocanOdgovor;
    private ArrayList<String> netocniOdgovori;

    public PitanjeKviz(String pitanje, String tocanOdgovor, ArrayList<String> netocniOdgovori) {
        this.pitanje = pitanje;
        this.tocanOdgovor = tocanOdgovor;
        this.netocniOdgovori = netocniOdgovori;
    }

    public String getPitanje() {
        return pitanje;
    }

    public void setPitanje(String pitanje) {
        this.pitanje = pitanje;
    }

    public String getTocanOdgovor() {
        return tocanOdgovor;
    }

    public void setTocanOdgovor(String tocanOdgovor) {
        this.tocanOdgovor = tocanOdgovor;
    }

    public ArrayList<String> getNetocniOdgovori() {
        return netocniOdgovori;
    }

    public void setNetocniOdgovori(ArrayList<String> netocniOdgovori) {
        this.netocniOdgovori = netocniOdgovori;
    }
}
