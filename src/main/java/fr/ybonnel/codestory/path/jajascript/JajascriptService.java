package fr.ybonnel.codestory.path.jajascript;


import com.google.common.primitives.Ints;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.google.common.collect.Lists.newArrayList;

public class JajascriptService {

    private List<Commande> commandes;

    private int nbCommands;
    private int[] starts;
    private int[] ends;
    private int[] durations;
    private int[] prices;
    private int maxValue;

    public JajascriptService(List<Commande> commandes) {
        this.commandes = commandes;
        Collections.sort(this.commandes, new Comparator<Commande>() {
            @Override
            public int compare(Commande commande1, Commande commande2) {
                return Ints.compare(commande1.getHeureDepart(), commande2.getHeureDepart());
            }
        });

        nbCommands = commandes.size();
        starts = new int[nbCommands];
        ends = new int[nbCommands];
        durations = new int[nbCommands];
        prices = new int[nbCommands];
        for (int indexComand = 0; indexComand < nbCommands; indexComand++) {
            Commande commande = commandes.get(indexComand);
            starts[indexComand] = commande.getHeureDepart();
            ends[indexComand] = commande.getHeureDepart() + commande.getTempsVol();
            durations[indexComand] = commande.getTempsVol();
            prices[indexComand] =  commande.getPrix();
        }
        maxValue = Ints.max(ends);
        pricesByEndTime = new int[maxValue+1];
        Arrays.fill(pricesByEndTime, 0);
    }

    private boolean[] bestAcceptedCommands = null;
    private int bestPrice = 0;
    private int bestDuration = Integer.MAX_VALUE;
    private int[] pricesByEndTime;

    private void addToPlanningsIfBetter(boolean[] acceptedCommands, int price, int duration) {

        if (price > bestPrice || price == bestPrice && duration < bestDuration) {
            bestAcceptedCommands = acceptedCommands;
            bestPrice = price;
            bestDuration = duration;
        }
    }

    private void calculate(int heureFinPlanning, int totalPrice, int totalDuration, boolean[] acceptedCommande, int lastCommandeAdded) {
        // Planing
        // - heure début - heure fin
        // - boolean[] : liste commande

        if (totalPrice < pricesByEndTime[heureFinPlanning]) {
            return;
        }
        pricesByEndTime[heureFinPlanning]= totalPrice;

        boolean mustAdd = true;
        int endFirstAccepted = maxValue;
        for (int i =lastCommandeAdded+1; i<nbCommands && starts[i]<endFirstAccepted;i++) {
            if (starts[i] >= heureFinPlanning) {
                if (mustAdd) {
                    endFirstAccepted = ends[i];
                }
                mustAdd = false;
                boolean[] newAceptedCommands = Arrays.copyOf(acceptedCommande, acceptedCommande.length);
                newAceptedCommands[i] = true;
                calculate(ends[i], totalPrice + prices[i], totalDuration + durations[i], newAceptedCommands, i);
            }
        }

        if (mustAdd && totalPrice > 0) {
            addToPlanningsIfBetter(acceptedCommande, totalPrice, totalDuration);
        }
    }

    public JajaScriptResponse calculate() {
        boolean[] acceptedCommands = new boolean[nbCommands];
        Arrays.fill(acceptedCommands, false);

        calculate(0, 0, 0, acceptedCommands, -1);

        int gain = bestPrice;
        List<String> path = newArrayList();

        for (int i=0; i<nbCommands; i++) {
            if (bestAcceptedCommands[i]) {
                path.add(commandes.get(i).getNomVol());
            }
        }


        return new JajaScriptResponse(gain, path);
    }

}
