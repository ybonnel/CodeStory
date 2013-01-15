package fr.ybonnel.codestory.path.jajascript;


import java.util.List;

public class JajascriptRequest {

    private List<Commande> commandes;

    public JajascriptRequest(List<Commande> commandes) {
        this.commandes = commandes;
    }

    public List<Commande> getCommandes() {
        return commandes;
    }
}
