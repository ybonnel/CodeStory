package fr.ybonnel.codestory.path.jajascript;


import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import com.google.common.collect.Ordering;
import com.google.common.primitives.Ints;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

public class JajascriptService {

    private JajascriptRequest request;

    public JajascriptService(JajascriptRequest request) {
        this.request = request;
    }

    private List<Planning> plannings = newArrayList();

    public Planning getBestPlanning() {
        Ordering<Planning> byPriceDescAndTempsVolAsc = new Ordering<Planning>() {
            @Override
            public int compare(Planning planning1, Planning planning2) {
                int result = Ints.compare(planning2.getTotalPrice(), planning1.getTotalPrice());
                if (result == 0) {
                    result = Ints.compare(planning1.getTempsVol(), planning2.getTempsVol());
                }
                return result;
            }
        };
        return byPriceDescAndTempsVolAsc.min(plannings);
    }

    private void addToPlanningsIfBetter(Planning planning) {
        for (Planning myPlanning : plannings) {
            if (myPlanning.getTotalPrice() > planning.getTotalPrice()) {
                return;
            }
        }
        plannings.add(planning);
    }

    private void calculate(Planning actualPlanning, Collection<Commande> commandesToAdd) {
        if (actualPlanning != null) {
            addToPlanningsIfBetter(actualPlanning);
        }
        List<Commande> tmpCommandes = new ArrayList<Commande>(commandesToAdd);
        Iterator<Commande> itCommande = tmpCommandes.iterator();

        while (itCommande.hasNext()) {
            Commande commandeToAdd = itCommande.next();
            itCommande.remove();
            if (actualPlanning == null || actualPlanning.canAddCommande(commandeToAdd)) {
                Planning newPlanning = new Planning(actualPlanning);
                newPlanning.addCommande(commandeToAdd);
                Collection<Commande> newCommandesToAdd = new ArrayList<Commande>(tmpCommandes);
                calculate(newPlanning, newCommandesToAdd);
            }
        }
    }

    public JajaScriptResponse calculate() {

        calculate(null, request.getCommandes());

        Planning planning = getBestPlanning();

        int gain = -1;
        List<String> path = null;

        if (planning != null) {
            gain = planning.getTotalPrice();
            List<Commande> commandes = newArrayList(planning.getCommandes());
            Collections.sort(commandes, new Comparator<Commande>() {
                @Override
                public int compare(Commande commande, Commande commande1) {
                    int x = commande.getHeureDepart();
                    int y = commande1.getHeureDepart();
                    return (x < y) ? -1 : ((x == y) ? 0 : 1);
                }
            });
            path = Lists.transform(commandes, new Function<Commande, String>() {
                @Override
                public String apply(Commande commande) {
                    return commande.getNomVol();
                }
            });
        }


        return new JajaScriptResponse(gain, path);
    }

}
