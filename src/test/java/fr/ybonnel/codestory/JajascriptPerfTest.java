package fr.ybonnel.codestory;

import fr.ybonnel.codestory.path.jajascript.Flight;
import fr.ybonnel.codestory.path.jajascript.JajascriptService;
import org.junit.Ignore;
import org.junit.Test;

import java.util.*;
import java.util.concurrent.TimeUnit;

public class JajascriptPerfTest {


    private Random rand = new Random();

    private List<Flight> randFilghts(int nbFlights) {
        List<Flight> flights = new ArrayList<Flight>();

        for (int i=0; i<nbFlights; i++) {
            Flight flight = new Flight();
            flight.setName(UUID.randomUUID().toString());
            flight.setStartTime(rand.nextInt(nbFlights/2));
            flight.setDuration(rand.nextInt(10) + 1);
            flight.setPrice(rand.nextInt(100) + 1);
            flights.add(flight);
        }
        return flights;
    }

    private void testALevel(int nbFlights) {
        int nbOccurToTest = 20;

        LinkedList<Long> elapsedTimes = new LinkedList<Long>();

        for (int i = 1; i <= nbOccurToTest; i++) {

            JajascriptService service;
            long startTime;
            {
                List<Flight> flights = randFilghts(nbFlights);
                startTime = System.nanoTime();
                service = new JajascriptService(flights);
            }

            service.calculate();

            elapsedTimes.add(System.nanoTime() - startTime);

        }

        Collections.sort(elapsedTimes);

        // Remove 5 smaller and 5 bigger times
        for (int i=0;i<5;i++) {
            elapsedTimes.removeFirst();
            elapsedTimes.removeLast();
        }

        long minElapsedTime = elapsedTimes.getFirst();
        long maxElapsedTime = elapsedTimes.getLast();
        long totalElapsedTime = 0;
        for (long elapsedTime : elapsedTimes) {
            totalElapsedTime+=elapsedTime;
        }

        System.out.println(nbFlights + ";" + TimeUnit.NANOSECONDS.toMillis(minElapsedTime)
            + ";" + TimeUnit.NANOSECONDS.toMillis(maxElapsedTime)
            + ";" + TimeUnit.NANOSECONDS.toMillis(totalElapsedTime / elapsedTimes.size()));
    }

    @Test
    @Ignore
    public void testManyLevels() {
        System.out.println("NbFlights;Min(ms);Max(ms);Moy(ms)");
        for (int nbFlight = 10000; nbFlight <= 1000000; nbFlight +=10000) {
            testALevel(nbFlight);
        }
    }

    @Test
    @Ignore
    public void testDetails() {

        int nbFlights = 1000000;

        // Warmup
        new JajascriptService(randFilghts(nbFlights)).calculate();
        new JajascriptService(randFilghts(nbFlights)).calculate();
        new JajascriptService(randFilghts(nbFlights)).calculate();

        testDetailPerfOnce(nbFlights);
        testDetailPerfOnce(nbFlights);
        testDetailPerfOnce(nbFlights);
        testDetailPerfOnce(nbFlights);

    }

    private void testDetailPerfOnce(int nbFlights) {
        JajascriptService service;
        long startTime;
        long elapsedTimeConstruct;
        {
            List<Flight> flights = randFilghts(nbFlights);
            startTime = System.nanoTime();
            service = new JajascriptService(flights);
            elapsedTimeConstruct = System.nanoTime() - startTime;
        }

        long startTimeCalculate = System.nanoTime();
        service.calculate();
        long endTime = System.nanoTime();
        System.out.println("Construct time : " + TimeUnit.NANOSECONDS.toMillis(elapsedTimeConstruct));
        System.out.println("Calculate time : " + TimeUnit.NANOSECONDS.toMillis(endTime - startTimeCalculate));
        System.out.println("Total time : " + TimeUnit.NANOSECONDS.toMillis(endTime - startTime));
    }
}
