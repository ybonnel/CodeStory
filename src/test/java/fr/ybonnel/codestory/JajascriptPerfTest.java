package fr.ybonnel.codestory;

import com.google.common.primitives.Longs;
import fr.ybonnel.codestory.path.jajascript.Flight;
import fr.ybonnel.codestory.path.jajascript.JajascriptService;
import org.junit.Ignore;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * Created with IntelliJ IDEA.
 * User: ybonnel
 * Date: 31/01/13
 * Time: 06:36
 * To change this template use File | Settings | File Templates.
 */
public class JajascriptPerfTest {


    private Random rand = new Random();

    private Flight[] randFilghts(int nbFlights) {
        List<Flight> flights = new ArrayList<Flight>();

        for (int i=0; i<nbFlights; i++) {
            Flight flight = new Flight();
            flight.setName(UUID.randomUUID().toString());
            flight.setStartTime(rand.nextInt(nbFlights/2));
            flight.setDuration(rand.nextInt(10) + 1);
            flight.setPrice(rand.nextInt(100) + 1);
            flights.add(flight);
        }
        return flights.toArray(new Flight[nbFlights]);
    }

    private void testALevel(int nbFlights) {
        int nbOccurToTest = 5;

        long totalElapsedTime = 0;
        long minElapsedTime = Long.MAX_VALUE;
        long maxElapsedTime = 0;

        for (int i = 0; i <= nbOccurToTest; i++) {

            JajascriptService service;
            long startTime;
            {
                Flight[] flights = randFilghts(nbFlights);
                startTime = System.nanoTime();
                service = new JajascriptService(flights);
            }

            service.calculate();

            long elapsedTime = System.nanoTime() - startTime;

            if (i != 0) {
                totalElapsedTime += elapsedTime;
                minElapsedTime = Longs.min(minElapsedTime, elapsedTime);
                maxElapsedTime = Longs.max(maxElapsedTime, elapsedTime);
            }

            System.out.println("Test " + i + " : " + TimeUnit.NANOSECONDS.toMillis(elapsedTime));
        }
        System.out.println("NbFlights " + nbFlights + " :");
        System.out.println("\tMin : " + TimeUnit.NANOSECONDS.toMillis(minElapsedTime));
        System.out.println("\tMax : " + TimeUnit.NANOSECONDS.toMillis(maxElapsedTime));
        System.out.println("\tMoy : " + TimeUnit.NANOSECONDS.toMillis(totalElapsedTime / nbOccurToTest));
    }

    @Test
    @Ignore
    public void testManyLevels() {
        testALevel(50000);
        testALevel(100000);
        testALevel(200000);
        testALevel(500000);
        testALevel(1000000);

    }
}
