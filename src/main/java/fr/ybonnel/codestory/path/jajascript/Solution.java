package fr.ybonnel.codestory.path.jajascript;

import java.util.Arrays;

public class Solution {
    public int heureFin;
    public int prix;
    public boolean[] acceptedCommands;

    Solution(int nbCommand) {
        heureFin = 0;
        prix = 0;
        acceptedCommands = new boolean[nbCommand];
        Arrays.fill(acceptedCommands, false);
    }

    Solution(int heureFin, int prix, boolean[] acceptedCommands) {
        this.heureFin = heureFin;
        this.prix = prix;
        this.acceptedCommands = acceptedCommands;
    }
}
