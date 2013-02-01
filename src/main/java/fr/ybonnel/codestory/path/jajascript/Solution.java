package fr.ybonnel.codestory.path.jajascript;

import com.google.common.base.Optional;
import com.google.common.primitives.Ints;

import java.util.LinkedList;
import java.util.List;

public class Solution implements Comparable<Solution> {
    public final int price;
    public final int endTime;
    public final Optional<Solution> oldSolution;
    public final Flight newFlight;

    Solution(int price, Optional<Solution> oldSolution, Flight newFlight) {
        this.price = price;
        this.oldSolution = oldSolution;
        this.newFlight = newFlight;
        this.endTime = newFlight.endTime;
    }

    public List<Flight> getFlights() {

        LinkedList<Flight> flights = new LinkedList<Flight>();
        flights.add(newFlight);

        Optional<Solution> currentSolution = oldSolution;
        while (currentSolution.isPresent()) {
            flights.addFirst(currentSolution.get().newFlight);
            currentSolution = currentSolution.get().oldSolution;
        }

        return flights;
    }

    public boolean isBetterThan(Optional<Solution> bestSolutionToAdd) {
        return !bestSolutionToAdd.isPresent() || price > bestSolutionToAdd.get().price;
    }

    @Override
    public int compareTo(Solution o) {
        return Ints.compare(price, o.price);
    }
}
