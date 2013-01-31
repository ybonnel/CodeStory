package fr.ybonnel.codestory.path.jajascript;

import com.google.common.primitives.Ints;

import java.util.LinkedList;
import java.util.List;

public class Solution implements Comparable<Solution> {
    public final int price;
    public final int endTime;
    public final Solution oldSolution;
    public final Flight newFlight;

    Solution(int price, Solution oldSolution, Flight newFlight) {
        this.price = price;
        this.oldSolution = oldSolution;
        this.newFlight = newFlight;
        this.endTime = newFlight.endTime;
    }

    public List<Flight> getFlights() {

        LinkedList<Flight> flights = new LinkedList<Flight>();
        flights.add(newFlight);

        Solution currentSolution = oldSolution;
        while (currentSolution != null) {
            flights.addFirst(currentSolution.newFlight);
            currentSolution = currentSolution.oldSolution;
        }

        return flights;
    }

    public boolean isBetterThan(Solution bestSolutionToAdd) {
        return bestSolutionToAdd == null || price > bestSolutionToAdd.price;
    }

    @Override
    public int compareTo(Solution o) {
        return Ints.compare(price, o.price);
    }
}
