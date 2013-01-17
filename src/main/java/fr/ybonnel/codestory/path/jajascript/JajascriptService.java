package fr.ybonnel.codestory.path.jajascript;


import com.google.common.primitives.Ints;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

public class JajascriptService {

    private List<Commande> commandes;

    private int nbCommands;
    private int[] starts;
    private int[] ends;
    private int[] prices;
    private LinkedList<Solution> lastSolutions;


    private static class Solution {
        private int heureFin;
        private int prix;
        private boolean[] acceptedCommands;

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
        prices = new int[nbCommands];
        int bigestDuration = 0;
        for (int indexComand = 0; indexComand < nbCommands; indexComand++) {
            Commande commande = commandes.get(indexComand);
            starts[indexComand] = commande.getHeureDepart();
            ends[indexComand] = commande.getHeureDepart() + commande.getTempsVol();
            if (commande.getTempsVol() > bigestDuration) {
                bigestDuration = commande.getTempsVol();
            }
            prices[indexComand] =  commande.getPrix();
        }
        lastSolutions = new LinkedList<Solution>();
        for (int i=0; i< bigestDuration *2;i++) {
            lastSolutions.addLast(new Solution(nbCommands));
        }
    }

    private Solution calculateIteratif() {
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

        Solution bestSolution = null;
        for (Solution solution : lastSolutions) {
            if (bestSolution == null || solution.prix > bestSolution.prix) {
                bestSolution = solution;
            }
        }
        return bestSolution;
    }

    public JajaScriptResponse calculate() {
        boolean[] acceptedCommands = new boolean[nbCommands];
        Arrays.fill(acceptedCommands, false);

        Solution solution = calculateIteratif();

        int gain = solution.prix;
        List<String> path = newArrayList();

        for (int i=0; i<nbCommands; i++) {
            if (solution.acceptedCommands[i]) {
                path.add(commandes.get(i).getNomVol());
            }
        }

        return new JajaScriptResponse(gain, path);
    }

}
