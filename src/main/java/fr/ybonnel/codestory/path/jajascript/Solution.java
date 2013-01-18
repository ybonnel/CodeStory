package fr.ybonnel.codestory.path.jajascript;

import java.util.BitSet;

public class Solution {
    public int heureFin;
    public int prix;
    public BitSet acceptedCommands;

    Solution(int nbCommand) {
        heureFin = 0;
        prix = 0;
        acceptedCommands = new BitSet(nbCommand);
    }

    Solution(int heureFin, int prix, BitSet acceptedCommands) {
        this.heureFin = heureFin;
        this.prix = prix;
        this.acceptedCommands = acceptedCommands;
    }
}
