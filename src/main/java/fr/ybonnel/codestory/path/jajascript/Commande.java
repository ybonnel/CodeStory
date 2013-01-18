package fr.ybonnel.codestory.path.jajascript;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

    public void setNomVol(String nomVol) {
        this.nomVol = nomVol;
    }

    public void setHeureDepart(int heureDepart) {
        this.heureDepart = heureDepart;
    }

    public void setTempsVol(int tempsVol) {
        this.tempsVol = tempsVol;
    }

    public void setPrix(int prix) {
        this.prix = prix;
    }
}
