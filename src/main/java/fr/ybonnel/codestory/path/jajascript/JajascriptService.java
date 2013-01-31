package fr.ybonnel.codestory.path.jajascript;


import com.google.common.base.Function;
import com.google.common.collect.Lists;

import java.util.*;

public class JajascriptService {


    private List<Flight> flights;
    private LinkedList<Solution> lastSolutions = new LinkedList<Solution>();


    public JajascriptService(List<Flight> flights) {
        this.flights = flights;
        Collections.sort(this.flights);
    }

    private Solution calculateSolution() {
        // Iterate on all flights.
        for (Flight flight : flights) {
            // Pre-calculate endTime for future needs.
            flight.endTime = flight.startTime + flight.duration;
            Solution bestSolutionToAdd = null;
            int bestPriceAlreadyFound  = -1;
            // Search the best solution in FIFO we can take for this flight.
            for (Solution solution : lastSolutions) {
                if (flight.startTime >= solution.endTime
                        && solution.isBetterThan(bestSolutionToAdd)) {
                    bestSolutionToAdd = solution;
                }
                if (flight.endTime>= solution.endTime && solution.price > bestPriceAlreadyFound) {
                    bestPriceAlreadyFound = solution.price;
                }
            }

            int newPrice = (bestSolutionToAdd == null ? 0 : bestSolutionToAdd.price) + flight.price;

            if (newPrice > bestPriceAlreadyFound) {
                // Add the new solution to FIFO only if it's better than other solution with lower or equal endTime.
                lastSolutions.addLast(new Solution(newPrice, bestSolutionToAdd, flight));
            }

            if (bestSolutionToAdd != null) {
                Solution oldSolution = lastSolutions.getFirst();
                while (oldSolution.endTime < flight.startTime
                        && oldSolution.price < bestSolutionToAdd.price) {
                    lastSolutions.removeFirst();
                    oldSolution = lastSolutions.getFirst();
                }
            }

        }

        // Search the best solution in FIFO.
        Collections.sort(lastSolutions);
        return lastSolutions.getLast();
    }

    public JajaScriptResponse calculate() {

        Solution solution = calculateSolution();

        // Construct the path with the array of accepted flights.
        List<String> path = Lists.transform(solution.getFlights(), new Function<Flight, String>() {
            @Override
            public String apply(Flight input) {
                return input.getName();
            }
        });

        return new JajaScriptResponse(solution.price, path);
    }

}
