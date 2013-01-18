package fr.ybonnel.codestory.path.jajascript;


import com.google.common.primitives.Ints;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

public class NewLegacyJajascriptService {

    private Commande[] commandes;

    private int nbCommands;
    private int[] starts;
    private int[] ends;
    private int[] prices;
    private LegacyFastFifo lastSolutions;


    public NewLegacyJajascriptService(Commande[] commandes) {
        this.commandes = commandes;
        Arrays.sort(this.commandes, new Comparator<Commande>() {
            @Override
            public int compare(Commande commande1, Commande commande2) {
                return Ints.compare(commande1.heureDepart, commande2.heureDepart);
            }
        });

        nbCommands = commandes.length;
        starts = new int[nbCommands];
        ends = new int[nbCommands];
        prices = new int[nbCommands];
        int bigestDuration = 0;
        int maxSameDepart = 1;
        int currentMaxSameDepart = 0;
        int lastDepart = -1;
        for (int indexComand = 0; indexComand < nbCommands; indexComand++) {
            Commande commande = commandes[indexComand];
            if (commande.heureDepart == lastDepart) {
                currentMaxSameDepart++;
            } else {
                if (currentMaxSameDepart > maxSameDepart) {
                    maxSameDepart = currentMaxSameDepart;
                }
                lastDepart = commande.heureDepart;
                currentMaxSameDepart = 1;
            }
            starts[indexComand] = commande.heureDepart;
            ends[indexComand] = commande.heureDepart + commande.tempsVol;
            if (commande.getTempsVol() > bigestDuration) {
                bigestDuration = commande.tempsVol;
            }
            prices[indexComand] =  commande.prix;
        }
        if (currentMaxSameDepart > maxSameDepart) {
            maxSameDepart = currentMaxSameDepart;
        }
        int sizeOfFifo = (bigestDuration << 1) + maxSameDepart;
        lastSolutions = new LegacyFastFifo(sizeOfFifo);
        for (int i=0; i< sizeOfFifo;i++) {
            lastSolutions.enqueue(new LegacySolution(nbCommands));
        }
    }

    private LegacySolution calculateIteratif() {
        // Parcours de toutes les commandes
        for (int i=0; i<nbCommands;i++) {
            LegacySolution bestSolutionToAdd = null;
            int bestPrice = -1;
            for (LegacySolution solution : lastSolutions) {
                if (starts[i] >= solution.heureFin
                        && solution.prix > bestPrice ) {
                    bestSolutionToAdd = solution;
                    bestPrice = solution.prix;
                }
            }

            boolean[] newAceptedCommands = lastSolutions.getFirst().acceptedCommands;

            System.arraycopy(bestSolutionToAdd.acceptedCommands, 0,  newAceptedCommands, 0, i);
            newAceptedCommands[i] = true;
            LegacySolution newSolution = new LegacySolution(ends[i], bestSolutionToAdd.prix + prices[i], newAceptedCommands);
            lastSolutions.enqueue(newSolution);
        }

        LegacySolution bestSolution = null;
        for (LegacySolution solution : lastSolutions) {
            if (bestSolution == null || solution.prix > bestSolution.prix) {
                bestSolution = solution;
            }
        }
        return bestSolution;
    }

    public JajaScriptResponse calculate() {

        LegacySolution solution = calculateIteratif();


        int gain = solution.prix;
        List<String> path = newArrayList();

        for (int i=0; i<nbCommands; i++) {
            if (solution.acceptedCommands[i]) {
                path.add(commandes[i].nomVol);
            }
        }

        return new JajaScriptResponse(gain, path);
    }

}
