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
    private FastFifo lastSolutions;


    public NewLegacyJajascriptService(Commande[] commandes) {
        this.commandes = commandes;
        Arrays.sort(this.commandes, new Comparator<Commande>() {
            @Override
            public int compare(Commande commande1, Commande commande2) {
                return Ints.compare(commande1.getHeureDepart(), commande2.getHeureDepart());
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
            if (commande.getHeureDepart() == lastDepart) {
                currentMaxSameDepart++;
            } else {
                if (currentMaxSameDepart > maxSameDepart) {
                    maxSameDepart = currentMaxSameDepart;
                }
                lastDepart = commande.getHeureDepart();
                currentMaxSameDepart = 1;
            }
            starts[indexComand] = commande.getHeureDepart();
            ends[indexComand] = commande.getHeureDepart() + commande.getTempsVol();
            if (commande.getTempsVol() > bigestDuration) {
                bigestDuration = commande.getTempsVol();
            }
            prices[indexComand] =  commande.getPrix();
        }
        if (currentMaxSameDepart > maxSameDepart) {
            maxSameDepart = currentMaxSameDepart;
        }
        int sizeOfFifo = (bigestDuration << 1) + maxSameDepart;
        lastSolutions = new FastFifo(sizeOfFifo);
        for (int i=0; i< sizeOfFifo;i++) {
            lastSolutions.enqueue(new Solution(nbCommands));
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

            boolean[] newAceptedCommands = lastSolutions.getFirst().acceptedCommands;

            for (int indexAccepted = 0; indexAccepted < i;indexAccepted++) {
                newAceptedCommands[indexAccepted] = bestSolutionToAdd.acceptedCommands[indexAccepted];
            }
            newAceptedCommands[i] = true;
            Solution newSolution = new Solution(ends[i], bestSolutionToAdd.prix + prices[i], newAceptedCommands);
            lastSolutions.enqueue(newSolution);
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
                path.add(commandes[i].getNomVol());
            }
        }

        return new JajaScriptResponse(gain, path);
    }

}
