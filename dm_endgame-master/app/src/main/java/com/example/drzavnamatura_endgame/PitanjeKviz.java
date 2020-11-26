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
}
