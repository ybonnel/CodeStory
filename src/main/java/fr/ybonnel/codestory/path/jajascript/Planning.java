package fr.ybonnel.codestory.path.jajascript;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

public class Planning {

    private List<Commande> commandes;

    private int maxHeureFin = -1;
    private int totalPrice = 0;

    public boolean canAddCommande(Commande commande) {
        return commande.getHeureDepart() >= maxHeureFin;
    }

    public Planning addCommande(Commande commande) {
        maxHeureFin = commande.getHeureDepart() + commande.getTempsVol();
        commandes.add(commande);
        totalPrice += commande.getPrix();
        return this;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public int getTempsVol() {
        int tempsVol = 0;
        for (Commande commande : commandes) {
            tempsVol+= commande.getTempsVol();
        }
        return tempsVol;
    }

    Planning(Planning planning) {
        if (planning == null) {
            this.commandes = newArrayList();
        } else {
            this.commandes = newArrayList(planning.commandes);
            this.maxHeureFin = planning.maxHeureFin;
            this.totalPrice = planning.totalPrice;
        }
    }

    public List<Commande> getCommandes() {
        return commandes;
    }

    public boolean isBetterThan(Planning planning) {
        if (this.getTotalPrice() > planning.getTotalPrice()) {
            return true;
        } else if (this.getTotalPrice() < planning.getTotalPrice()) {
            return false;
        }
        return this.getTempsVol() < planning.getTempsVol();
    }
}
