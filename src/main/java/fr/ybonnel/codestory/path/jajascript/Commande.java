package fr.ybonnel.codestory.path.jajascript;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Commande {

    @JsonProperty("VOL")
    private String nomVol;
    @JsonProperty("DEPART")
    private int heureDepart;
    @JsonProperty("DUREE")
    private int tempsVol;
    @JsonProperty("PRIX")
    private int prix;

    private int getHeureFin() {
        return heureDepart + tempsVol;
    }

    public String getNomVol() {
        return nomVol;
    }

    public int getHeureDepart() {
        return heureDepart;
    }

    public int getTempsVol() {
        return tempsVol;
    }

    public int getPrix() {
        return prix;
    }
}
