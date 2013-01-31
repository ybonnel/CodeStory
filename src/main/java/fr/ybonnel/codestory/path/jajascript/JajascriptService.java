package fr.ybonnel.codestory.path.jajascript;


import com.google.common.primitives.Ints;

import java.util.*;

import static com.google.common.collect.Lists.newArrayList;

public class JajascriptService {


    private Flight[] flights;

    private int nbFlights;
    private int[] starts;
    private int[] ends;
    private int[] prices;
    private LinkedList<Solution> lastSolutions;


    public JajascriptService(Flight[] flights) {
        this.flights = flights;
        // Sort flight order by start time.
        Arrays.sort(this.flights, new Comparator<Flight>() {
            @Override
            public int compare(Flight flight1, Flight flight2) {
                return Ints.compare(flight1.startTime, flight2.startTime);
            }
        });

        nbFlights = flights.length;
        starts = new int[nbFlights];
        ends = new int[nbFlights];
        prices = new int[nbFlights];
        int sizeOfFifo = completeArraysAndReturnFifoSize(flights);

        // Fill FIFO with empty solution.
        lastSolutions = new LinkedList<Solution>();
    }

    /**
     * Complete all arrays (starts, ends and prices) and calculate size of FIFO.
     */
    private int completeArraysAndReturnFifoSize(Flight[] flights) {
        int bigestDuration = 0;
        int maxSameStartTime = 1;
        int currentMaxSameStartTimeFound = 0;
        int lastStartTime = -1;
        for (int flightNumber = 0; flightNumber < nbFlights; flightNumber++) {
            Flight flight = flights[flightNumber];

            // If startTime if equal to the precedent,
            // increment the number of same startTime.
            if (flight.startTime == lastStartTime) {
                currentMaxSameStartTimeFound++;
            } else {
                if (currentMaxSameStartTimeFound > maxSameStartTime) {
                    maxSameStartTime = currentMaxSameStartTimeFound;
                }
                lastStartTime = flight.startTime;
                currentMaxSameStartTimeFound = 1;
            }

            starts[flightNumber] = flight.startTime;
            ends[flightNumber] = flight.startTime + flight.duration;
            prices[flightNumber] = flight.price;

            if (flight.getDuration() > bigestDuration) {
                bigestDuration = flight.duration;
            }
        }
        // The max of same startTime can have be found in the last iteration.
        if (currentMaxSameStartTimeFound > maxSameStartTime) {
            maxSameStartTime = currentMaxSameStartTimeFound;
        }

        // Size of FIFO needed =
        // Max(duration) * 2 + (Max of same startTime).
        // This is the max number of flight between two accepted flight.
        return (bigestDuration << 1) + maxSameStartTime;
    }

    private Solution calculateSolution() {
        // Iterate on all flight.
        for (int flightNumber = 0; flightNumber < nbFlights; flightNumber++) {
            Solution bestSolutionToAdd = null;
            int bestPrice = -1;
            int bestPriceAlreadyFound  = -1;
            // Search the best solution in FIFO we can take for this slght.
            for (Solution solution : lastSolutions) {
                if (starts[flightNumber] >= solution.endTime
                        && solution.price > bestPrice) {
                    bestSolutionToAdd = solution;
                    bestPrice = solution.price;
                }
                if (ends[flightNumber] >= solution.endTime && solution.price > bestPriceAlreadyFound) {
                    bestPriceAlreadyFound = solution.price;
                }
            }

            int newPrice = (bestSolutionToAdd == null ? 0 : bestSolutionToAdd.price) + prices[flightNumber];

            if (newPrice > bestPriceAlreadyFound) {
                // Add the current flight to the solution.
                Solution newSolution = new Solution(ends[flightNumber], newPrice, bestSolutionToAdd, flights[flightNumber]);
                // Add the new solution to FIFO.
                lastSolutions.addLast(newSolution);
            }

            if (bestSolutionToAdd != null) {
                Solution oldSolution = lastSolutions.getFirst();
                while (oldSolution.endTime <= starts[flightNumber]
                        && oldSolution.price < bestSolutionToAdd.price) {
                    lastSolutions.removeFirst();
                    oldSolution = lastSolutions.getFirst();
                }
            }

        }

        // Search the best solution in FIFO.
        Solution bestSolution = null;
        for (Solution solution : lastSolutions) {
            if (bestSolution == null || solution.price > bestSolution.price) {
                bestSolution = solution;
            }
        }
        return bestSolution;
    }

    public JajaScriptResponse calculate() {

        Solution solution = calculateSolution();

        int gain = solution.price;

        List<String> path = newArrayList();
        // Construct the path with the array of accepted flights.
        for (Flight flight : solution.getFlights()) {
            path.add(flight.getName());
        }

        return new JajaScriptResponse(gain, path);
    }

}
