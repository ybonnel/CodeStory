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
        Collections.sort(request.getCommandes(), new Comparator<Commande>() {
            @Override
            public int compare(Commande commande1, Commande commande2) {
                return Ints.compare(commande1.getHeureDepart(), commande2.getHeureDepart());
            }
        });
    }

    private Planning bestPlanning = null;

    private void addToPlanningsIfBetter(Planning planning) {
        if (bestPlanning == null) {
            bestPlanning = planning;
        } else {
            if (planning.isBetterThan(bestPlanning)) {
                bestPlanning = planning;
            }
        }
    }

    private void calculate(Planning actualPlanning, Collection<Commande> commandesToAdd) {
        if (actualPlanning != null) {
            addToPlanningsIfBetter(actualPlanning);
        }
        Iterator<Commande> itCommande = commandesToAdd.iterator();

        while (itCommande.hasNext()) {
            Commande commandeToAdd = itCommande.next();
            itCommande.remove();
            if (actualPlanning == null || actualPlanning.canAddCommande(commandeToAdd)) {
                calculate(new Planning(actualPlanning).addCommande(commandeToAdd), newArrayList(commandesToAdd));
            }
        }
    }

    public JajaScriptResponse calculate() {

        calculate(null, request.getCommandes());

        Planning planning = bestPlanning;

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
