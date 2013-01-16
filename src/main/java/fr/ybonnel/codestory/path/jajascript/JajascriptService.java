package fr.ybonnel.codestory.path.jajascript;


import com.google.common.primitives.Ints;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Stack;
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
    private long startTime;
    private int bigestDuration = 0;
    private LinkedList<Solution> lastSolutions;


    private static class Solution {
        int heureFin;
        int prix;
        boolean[] acceptedCommands;

        private Solution(int nbCommand) {
            heureFin = 0;
            prix = 0;
            acceptedCommands = new boolean[nbCommand];
            Arrays.fill(acceptedCommands, false);
        }

        private Solution(int heureFin, int prix, boolean[] acceptedCommands) {
            this.heureFin = heureFin;
            this.prix = prix;
            this.acceptedCommands = acceptedCommands;
        }
    }

    public JajascriptService(List<Commande> commandes) {
        this.commandes = commandes;
        Collections.sort(this.commandes, new Comparator<Commande>() {
            @Override
            public int compare(Commande commande1, Commande commande2) {
                return Ints.compare(commande1.getHeureDepart() + commande1.getTempsVol(), commande2.getHeureDepart() + commande2.getTempsVol());
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
            if (commande.getTempsVol() > bigestDuration) {
                bigestDuration = commande.getTempsVol();
            }
            prices[indexComand] =  commande.getPrix();
        }
        lastSolutions = new LinkedList<Solution>();
        for (int i=0; i<bigestDuration*2;i++) {
            lastSolutions.addLast(new Solution(nbCommands));
        }
        maxValue = Ints.max(ends);
        pricesByEndTime = new int[maxValue+1];
        Arrays.fill(pricesByEndTime, -1);
    }

    private boolean[] bestAcceptedCommands = null;
    private int bestPrice = 0;
    private int[] pricesByEndTime;
    private long timeFound = 0;

    private void addToPlanningsIfBetter(boolean[] acceptedCommands, int price) {

        if (price > bestPrice) {
            bestAcceptedCommands = acceptedCommands;
            bestPrice = price;
            timeFound = System.currentTimeMillis();
        }
    }

    private void calculateIteratif() {
        // Parcours de toutes les commandes
        for (int i=0; i<nbCommands;i++) {
            Solution bestSolutionToAdd = null;
            int bestPrice = -1;
            for (Solution solution : lastSolutions) {
                if (starts[i] >= solution.heureFin
                        && solution.prix > bestPrice ) {
                    bestSolutionToAdd = solution;
                    bestPrice = solution.prix;
                }
            }

            lastSolutions.removeFirst();

            boolean[] newAceptedCommands = Arrays.copyOf(bestSolutionToAdd.acceptedCommands, bestSolutionToAdd.acceptedCommands.length);
            newAceptedCommands[i] = true;
            Solution newSolution = new Solution(ends[i], bestSolutionToAdd.prix + prices[i], newAceptedCommands);
            lastSolutions.addLast(newSolution);
        }

        for (Solution solution : lastSolutions) {
            addToPlanningsIfBetter(solution.acceptedCommands, solution.prix);

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

        //calculate(0, 0, 0, acceptedCommands, -1);
        calculateIteratif();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss,SSS");

        System.out.println("Solution found at : " + sdf.format(new Date(timeFound)));
        System.out.println("Actual time : " + sdf.format(new Date()));

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
