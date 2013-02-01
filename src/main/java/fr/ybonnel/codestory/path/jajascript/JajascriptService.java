package fr.ybonnel.codestory.path.jajascript;


import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.collect.Lists;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class JajascriptService {

    /**
     * Flights to optimize.
     */
    private List<Flight> flights;
    /**
     * Last solutions found.
     */
    private LinkedList<Solution> lastSolutions = new LinkedList<Solution>();


    public JajascriptService(List<Flight> flights) {
        this.flights = flights;
        Collections.sort(this.flights);
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

    private static class BestSolutions {
        Optional<Solution> bestCompatibleSolution = Optional.absent();
        Optional<Solution> bestSolutionWithEquivalentEndTime = Optional.absent();

        int getPriceOfCompatibleSolution() {
            return getPriceOfAnOptionalSolution(bestCompatibleSolution);
        }

        int getPriceOfEquivalentEndTimeSolution() {
            return getPriceOfAnOptionalSolution(bestSolutionWithEquivalentEndTime);
        }

        private static int getPriceOfAnOptionalSolution(Optional<Solution> optionalSolution) {
            return optionalSolution.isPresent() ? optionalSolution.get().price : 0;
        }
    }

    /**
     * @return an optimal solution.
     */
    private Solution calculateSolution() {
        // Iterate on all flights.
        for (Flight flight : flights) {
            // Pre-calculate endTime for future needs.
            flight.calculateEndTime();

            BestSolutions bestSolutions = getBestSolutionsForAFlight(flight);

            int newPrice = flight.price + bestSolutions.getPriceOfCompatibleSolution();

            if (newPrice > bestSolutions.getPriceOfEquivalentEndTimeSolution()) {
                // Add the new solution to FIFO only if it's better than other solution with lower or equal endTime.
                lastSolutions.addLast(new Solution(newPrice, bestSolutions.bestCompatibleSolution, flight));
            }

            if (bestSolutions.bestCompatibleSolution.isPresent()) {
                // If we found a compatible solution, we remove all solution with endTime lower than last flight startTime and lower price.
                removeOldSolutions(flight.startTime, bestSolutions.bestCompatibleSolution.get().price);
            }
        }

        // Search the best solution in FIFO.
        Collections.sort(lastSolutions);
        return lastSolutions.getLast();
    }

    /**
     * Remove all solution with lower or equal endTime than lastStartTime and lower price than priceOfCompatibleSolution.
     */
    private void removeOldSolutions(int lastStartTime, int priceOfCompatibleSolution) {
        Iterator<Solution> lastSolutionIterator= lastSolutions.iterator();

        while (lastSolutionIterator.hasNext()) {
            Solution oldSolution = lastSolutionIterator.next();
            if (oldSolution.endTime <= lastStartTime
                    && oldSolution.price < priceOfCompatibleSolution) {
                lastSolutionIterator.remove();
            }
        }
    }

    /**
     * Get the best solution in {@link JajascriptService#lastSolutions} for a flight.
     */
    private BestSolutions getBestSolutionsForAFlight(Flight flight) {
        BestSolutions bestSolutions = new BestSolutions();
        // Search the best solution in FIFO we can take for this flight.
        for (Solution solution : lastSolutions) {
            if (flight.startTime >= solution.endTime && solution.isBetterThan(bestSolutions.bestCompatibleSolution)) {
                bestSolutions.bestCompatibleSolution = Optional.of(solution);
            }
            if (flight.endTime >= solution.endTime && solution.isBetterThan(bestSolutions.bestSolutionWithEquivalentEndTime)) {
                bestSolutions.bestSolutionWithEquivalentEndTime = Optional.of(solution);
            }
        }
        return bestSolutions;
    }

}
