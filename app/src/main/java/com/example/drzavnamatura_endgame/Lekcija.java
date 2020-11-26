package com.example.drzavnamatura_endgame;

public class Lekcija {
    String naslov = "", sadrzaj = "";
    String slikaURL = "";
    String visiOdjelPredmet, visiOdjelGradivo;
    String TIP_LEKCIJE = "0";
    String razred = "";
    public static final String SAMO_NASLOV = "0";
    public static final String NASLOV_SADRZAJ = "1";
    public static final String NASLOV_SADRZAJ_SLIKA = "2";

    public Lekcija(String naslov, String visiOdjelPredmet, String visiOdjelGradivo, int razred) {
        this.naslov = naslov;
        this.visiOdjelPredmet = visiOdjelPredmet;
        this.visiOdjelGradivo = visiOdjelGradivo;
        this.razred = razred + ". Razred";
        TIP_LEKCIJE = SAMO_NASLOV;
    }

    public Lekcija(String naslov, String sadrzaj, String visiOdjelPredmet, String visiOdjelGradivo, int razred) {
        this.naslov = naslov;
        this.sadrzaj = sadrzaj;
        this.visiOdjelPredmet = visiOdjelPredmet;
        this.visiOdjelGradivo = visiOdjelGradivo;
        this.razred = razred + ". Razred";
        TIP_LEKCIJE = NASLOV_SADRZAJ;
    }

    public Lekcija(String naslov, String sadrzaj, String slika, String visiOdjelPredmet, String visiOdjelGradivo, int razred) {
        this.naslov = naslov;
        this.sadrzaj = sadrzaj;
        this.slikaURL = slika;
        this.visiOdjelPredmet = visiOdjelPredmet;
        this.visiOdjelGradivo = visiOdjelGradivo;
        this.razred = razred + ". Razred";
        TIP_LEKCIJE = NASLOV_SADRZAJ_SLIKA;
    }

    public String getRazred() {
        return razred;
    }

    public String getVisiOdjelPredmet() {
        return visiOdjelPredmet;
    }

    public String getVisiOdjelGradivo() {
        return visiOdjelGradivo;
    }

    public String getNaslov() {
        return naslov;
    }

    public String getTIP_LEKCIJE() {
        return TIP_LEKCIJE;
    }

    public String getSadrzaj() {
        return sadrzaj;
    }

    public String getSlikaURL() {
        return slikaURL;
    }
}
