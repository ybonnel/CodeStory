package fr.ybonnel.codestory.path.jajascript;


import java.util.List;

public class JajascriptRequest {

    private List<Flight> commandes;

    public JajascriptRequest(List<Flight> commandes) {
        this.commandes = commandes;
    }

    public List<Flight> getCommandes() {
        return commandes;
    }
}
