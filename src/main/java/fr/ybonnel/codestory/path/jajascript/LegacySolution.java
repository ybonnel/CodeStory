package fr.ybonnel.codestory.path.jajascript;

import java.util.BitSet;

public class LegacySolution {
    public int heureFin;
    public int prix;
    public BitSet acceptedCommands;

    LegacySolution(int nbCommand) {
        heureFin = 0;
        prix = 0;
        acceptedCommands = new BitSet(nbCommand);
    }

    LegacySolution(int heureFin, int prix, BitSet acceptedCommands) {
        this.heureFin = heureFin;
        this.prix = prix;
        this.acceptedCommands = acceptedCommands;
    }
}
