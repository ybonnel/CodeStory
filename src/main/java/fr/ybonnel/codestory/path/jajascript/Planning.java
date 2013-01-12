package fr.ybonnel.codestory.path.jajascript;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

public class Planning {

    private List<Commande> commandes;

    public boolean canAddCommande(Commande commande) {
        for (Commande actualCommande : commandes) {
            if (!commande.compatibleWith(actualCommande)) {
                return false;
            }
        }
        return true;
    }

    public Planning addCommande(Commande commande) {
        commandes.add(commande);
        return this;
    }

    public int getTotalPrice() {
        int totalPrice = 0;
        for (Commande commande : commandes) {
            totalPrice+= commande.getPrix();
        }
        return totalPrice;
    }

    Planning(Planning planning) {
        if (planning == null) {
            this.commandes = newArrayList();
        } else {
            this.commandes = newArrayList(planning.commandes);
        }
    }

    public List<Commande> getCommandes() {
        return commandes;
    }
}
