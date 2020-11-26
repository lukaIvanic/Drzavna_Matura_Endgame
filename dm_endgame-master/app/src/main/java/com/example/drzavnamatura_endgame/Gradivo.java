package com.example.drzavnamatura_endgame;

public class Gradivo {
    String naslov;
    String razred;
    String visiOdjelPredmet;
    String vrsta;
    public Gradivo(){

    }

    public Gradivo(String naslov, String razred, String visiOdjelPredmet) {
        this.naslov = naslov;
        this.razred = razred;
        this.visiOdjelPredmet = visiOdjelPredmet;
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
