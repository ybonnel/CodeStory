package fr.ybonnel.codestory.path.jajascript;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.List;

public class JajascriptRequest {

    private List<Commande> commandes;

    public JajascriptRequest(List<Commande> commandes) {
        this.commandes = commandes;
    }

    @Override
    public String toString() {
        return "JajascriptRequest{" +
                "commandes=" + commandes +
                '}';
    }

    public List<Commande> getCommandes() {
        return commandes;
    }

    private final static ObjectMapper mapper = new ObjectMapper().setSerializationInclusion(JsonInclude.Include.NON_NULL);

    public static JajascriptRequest fromPayLoad(String payLoad) throws IOException {
        List<Commande> commandes = mapper.readValue(payLoad, new TypeReference<List<Commande>>(){});
        return new JajascriptRequest(commandes);
    }
}
