package com.example.drzavnamatura_endgame;

public class Komentar {
    String komentar, autor;
    int seconds;



    public Komentar(String komentar, String autor, int seconds) {
        this.komentar = komentar;
        this.autor = autor;
        this.seconds = seconds;
    }
    public int getSeconds() {
        return seconds;
    }

    public void setSeconds(int seconds) {
        this.seconds = seconds;
    }

    public String getKomentar() {
        return komentar;
    }

    public void setKomentar(String komentar) {
        this.komentar = komentar;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }
}