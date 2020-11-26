package com.example.drzavnamatura_endgame;

public class Ispit {
    private String razina;
    private String datum;
    private int postotak;
    private int predmet;
    private int id;

    public Ispit(String razina, String datum, int postotak, int predmet, int id) {
        this.razina = razina;
        this.datum = datum;
        this.postotak = postotak;
        this.predmet = predmet;
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRazina() {
        return razina;
    }

    public void setRazina(String razina) {
        this.razina = razina;
    }

    public String getDatum() {
        return datum;
    }

    public void setDatum(String datum) {
        this.datum = datum;
    }

    public int getPostotak() {
        return postotak;
    }

    public void setPostotak(int postotak) {
        this.postotak = postotak;
    }

    public int getPredmet() {
        return predmet;
    }

    public void setPredmet(int predmet) {
        this.predmet = predmet;
    }
}