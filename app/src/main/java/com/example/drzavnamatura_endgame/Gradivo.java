package com.example.drzavnamatura_endgame;

public class Gradivo {
    String naslov;
    String razred;
    String visiOdjelPredmet;
    int vrsta;
    public Gradivo(){

    }

    public Gradivo(String naslov, String razred, String visiOdjelPredmet) {
        this.naslov = naslov;
        this.razred = razred;
        this.visiOdjelPredmet = visiOdjelPredmet;
    }

    public Gradivo(String naslov, String razred, String visiOdjelPredmet, int vrsta) {
        this.naslov = naslov;
        this.razred = razred;
        this.visiOdjelPredmet = visiOdjelPredmet;
        this.vrsta = vrsta;
    }

    public int getVrsta() {
        return vrsta;
    }

    public void setVrsta(int vrsta) {
        this.vrsta = vrsta;
    }

    public String getVisiOdjelPredmet() {
        return visiOdjelPredmet;
    }

    public String getNaslov() {
        return naslov;
    }

    public String getRazred() {
        return razred;
    }
}
