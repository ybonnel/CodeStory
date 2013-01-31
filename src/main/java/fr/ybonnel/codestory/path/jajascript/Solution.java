package fr.ybonnel.codestory.path.jajascript;

import java.util.*;

public class Solution {
    public final int endTime;
    public final int price;
    public final Solution oldSolution;
    public final Flight newFlight;

    Solution(int endTime, int price, Solution oldSolution, Flight newFlight) {
        this.endTime = endTime;
        this.price = price;
        this.oldSolution = oldSolution;
        this.newFlight = newFlight;
    }

    public List<Flight> getFlights() {

        LinkedList<Flight> flights = new LinkedList<Flight>();
        flights.add(newFlight);

        Solution currentSolution = oldSolution;
        while (currentSolution != null) {
            flights.add(currentSolution.newFlight);
            currentSolution = currentSolution.oldSolution;
        }

        Collections.reverse(flights);

        return flights;
    }
}
