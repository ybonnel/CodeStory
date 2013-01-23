package fr.ybonnel.codestory.path.jajascript.legacy;


import com.google.common.primitives.Ints;
import fr.ybonnel.codestory.path.jajascript.Flight;
import fr.ybonnel.codestory.path.jajascript.JajaScriptResponse;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.google.common.collect.Lists.newArrayList;

public class LegacyJajascriptService {

    private List<Flight> commandes;

    private int nbCommands;
    private int[] starts;
    private int[] ends;
    private int[] durations;
    private int[] prices;
    private int maxValue;
    private long startTime;


    public LegacyJajascriptService(List<Flight> commandes) {
        this.commandes = commandes;
        Collections.sort(this.commandes, new Comparator<Flight>() {
            @Override
            public int compare(Flight flight1, Flight flight2) {
                return Ints.compare(flight1.getStartTime(), flight2.getStartTime());
            }
        });

        nbCommands = commandes.size();
        starts = new int[nbCommands];
        ends = new int[nbCommands];
        durations = new int[nbCommands];
        prices = new int[nbCommands];
        for (int indexComand = 0; indexComand < nbCommands; indexComand++) {
            Flight flight = commandes.get(indexComand);
            starts[indexComand] = flight.getStartTime();
            ends[indexComand] = flight.getStartTime() + flight.getDuration();
            durations[indexComand] = flight.getDuration();
            prices[indexComand] =  flight.getPrice();
        }
        maxValue = Ints.max(ends);
        pricesByEndTime = new int[maxValue+1];
        Arrays.fill(pricesByEndTime, -1);
    }

    private boolean[] bestAcceptedCommands;
    private int bestPrice;
    private int[] pricesByEndTime;

    private void addToPlanningsIfBetter(boolean[] acceptedCommands, int price) {

        if (price > bestPrice) {
            bestAcceptedCommands = acceptedCommands;
            bestPrice = price;
        }
    }

    private void calculate(int heureFinPlanning, int totalPrice, int totalDuration, boolean[] acceptedCommande, int lastCommandeAdded) {



        // Planing
        // - heure début - heure fin
        // - boolean[] : liste commande

        if (totalPrice <= pricesByEndTime[heureFinPlanning] || TimeUnit.NANOSECONDS.toSeconds(System.nanoTime()-startTime) > 20  ) {
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
            addToPlanningsIfBetter(acceptedCommande, totalPrice);
        }
    }

    public JajaScriptResponse calculate() {
        boolean[] acceptedCommands = new boolean[nbCommands];
        Arrays.fill(acceptedCommands, false);

        startTime = System.nanoTime();

        calculate(0, 0, 0, acceptedCommands, -1);

        int gain = bestPrice;
        List<String> path = newArrayList();

        for (int i=0; i<nbCommands; i++) {
            if (bestAcceptedCommands[i]) {
                path.add(commandes.get(i).getName());
            }
        }

        return new JajaScriptResponse(gain, path);
    }

}
