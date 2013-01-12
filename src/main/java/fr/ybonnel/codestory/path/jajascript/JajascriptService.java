package fr.ybonnel.codestory.path.jajascript;


import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

public class JajascriptService {

    private JajascriptRequest request;

    public Planning getBetterPlanning() {
        Collections.sort(plannings, new Comparator<Planning>() {
            @Override
            public int compare(Planning planning, Planning planning1) {
                int y = planning.getTotalPrice();
                int x = planning1.getTotalPrice();
                return (x < y) ? -1 : ((x == y) ? 0 : 1);
            }
        });
        return Iterables.getFirst(plannings, null);
    }

    public JajascriptService(JajascriptRequest request) {
        this.request = request;
    }

    private List<Planning> plannings = newArrayList();

    private void calculate(Planning actualPlanning, Collection<Commande> commandesToAdd) {
        if (actualPlanning != null) {
            addToPlanningsIfBetter(actualPlanning);
        }
        for (Commande commandeToAdd : commandesToAdd) {
            if (actualPlanning == null || actualPlanning.canAddCommande(commandeToAdd)) {
                Planning newPlanning = new Planning(actualPlanning);
                newPlanning.addCommande(commandeToAdd);
                Collection<Commande> newCommandesToAdd = Collections2.filter(commandesToAdd, new FilterCommande(commandeToAdd));
                calculate(newPlanning, newCommandesToAdd);
            }
        }
    }

    private void addToPlanningsIfBetter(Planning planning) {
        for (Planning myPlanning : plannings) {
            if (myPlanning.getTotalPrice() > planning.getTotalPrice()) {
                return;
            }
        }
        plannings.add(planning);
    }

    public JajaScriptResponse calculate() {

        calculate(null, request.getCommandes());

        Planning planning = getBetterPlanning();

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
