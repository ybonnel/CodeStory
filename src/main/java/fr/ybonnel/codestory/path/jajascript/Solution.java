package fr.ybonnel.codestory.path.jajascript;

import java.util.BitSet;

public class Solution {
    public int endTime;
    public int price;
    public BitSet acceptedFlights;

    Solution(int nbFlights) {
        endTime = 0;
        price = 0;
        acceptedFlights = new BitSet(nbFlights);
    }

    Solution(int endTime, int price, BitSet acceptedFlights) {
        this.endTime = endTime;
        this.price = price;
        this.acceptedFlights = acceptedFlights;
    }
}
