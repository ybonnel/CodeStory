package fr.ybonnel.codestory.path.jajascript;


import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.google.common.collect.Lists.newArrayList;

public class JajascriptRequest {

    private List<Commande> commandes = newArrayList();

    private static final Pattern patternArray = Pattern.compile("\\[(.*)\\]");
    private static final Pattern patternCommande = Pattern.compile("\\{ ?\"(\\w+)\" ?, ?(\\d+) ?, ?(\\d+) ?, ?(\\d+) ?\\}");

    public static JajascriptRequest fromPayLoad(String payLoad) {

        Matcher matcher = patternArray.matcher(payLoad);

        JajascriptRequest request = new JajascriptRequest();

        if (matcher.matches()) {

            String commandesPayLoad = matcher.group(1);

            Matcher matcherCommande = patternCommande.matcher(commandesPayLoad);

            while (matcherCommande.find()) {

                System.out.println(matcherCommande.group(0));

                request.commandes.add(new Commande(matcherCommande.group(1),
                        Integer.parseInt(matcherCommande.group(2)),
                        Integer.parseInt(matcherCommande.group(3)),
                        Integer.parseInt(matcherCommande.group(4))));

            }
        }
        return request;
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
}
