package fr.ybonnel.codestory.path.jajascript;

import com.google.common.base.Predicate;


public class FilterCommande implements Predicate<Commande> {

    private Commande commandeToFilter;

    FilterCommande(Commande commandeToFilter) {
        this.commandeToFilter = commandeToFilter;
    }

    @Override
    public boolean apply(Commande commande) {
        return !commandeToFilter.equals(commande);
    }
}
