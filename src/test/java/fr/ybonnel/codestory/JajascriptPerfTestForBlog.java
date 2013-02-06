package fr.ybonnel.codestory;

import com.google.common.base.Throwables;
import fr.ybonnel.codestory.path.jajascript.Flight;
import fr.ybonnel.codestory.path.jajascript.JajascriptService;
import org.junit.Ignore;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class JajascriptPerfTestForBlog {

    private List<String> readAllLinesOfInputStream(InputStream stream) {
        List<String> result = new ArrayList<String>();
        try {
            BufferedReader bufReader = new BufferedReader(new InputStreamReader(stream));
            String ligne;
            while ((ligne = bufReader.readLine()) != null) {
                result.add(ligne);
            }
            stream.close();
        } catch (IOException ioException) {
            Throwables.propagate(ioException);
        }
        return result;
    }

    private List<String> noms = readAllLinesOfInputStream(JajascriptPerfTestForBlog.class.getResourceAsStream("/noun.txt"));
    private List<String> adjectifs = readAllLinesOfInputStream(JajascriptPerfTestForBlog.class.getResourceAsStream("/adjective.txt"));


    private Random rand = new Random();

    private String randomName() {
        return noms.get(rand.nextInt(noms.size())) + "-" + adjectifs.get(rand.nextInt(adjectifs.size())) + "-" + (rand.nextInt(99) + 1);
    }

    private void doGenerate(int seed, List<Flight> trips) {
        trips.add(createFlight(randomName(), seed + rand.nextInt(5), 1 + rand.nextInt(10), 1 + rand.nextInt(30)));
        trips.add(createFlight(randomName(), seed + rand.nextInt(5), 1 + rand.nextInt(10), 1 + rand.nextInt(20) + 3));
        trips.add(createFlight(randomName(), seed + rand.nextInt(5), 1 + rand.nextInt(10), 1 + rand.nextInt(10)));
        trips.add(createFlight(randomName(), seed + rand.nextInt(5), 1 + rand.nextInt(10), 1 + rand.nextInt(10) + 5));
        trips.add(createFlight(randomName(), seed + rand.nextInt(5), 1 + rand.nextInt(20), 1 + rand.nextInt(7)));
    }

    private Flight createFlight(String nom, int startTime, int duration, int price) {
        Flight commande = new Flight();
        commande.setName(nom);
        commande.setStartTime(startTime);
        commande.setDuration(duration);
        commande.setPrice(price);
        return commande;
    }

    private List<Flight> generate(int level) {
        List<Flight> result = new ArrayList<Flight>();
        int seed = 0;
        for (int i = 1; i <= level; i++) {
            doGenerate(seed, result);
            seed += 5;
        }
        return result;
    }

    private long testALevel(int level) {
        JajascriptService service;
        long startTime;
        {
            List<Flight> flights = generate(level);
            System.gc();
            startTime = System.nanoTime();
            service = new JajascriptService(flights);
        }

        service.calculate();

        long elapsedTime = System.nanoTime() - startTime;

        System.gc();
        
        return elapsedTime;
    }

    private static List<Integer> levels = new ArrayList<Integer>() {{
        add(1);
        add(2);
        add(3);
        add(4);
        add(5);
        add(6);
        add(7);
        add(8);
        add(9);
        add(10);
        add(11);
        add(12);
        add(13);
        add(14);
        add(15);
        add(16);
        add(17);
        add(18);
        add(19);
        add(20);
        add(30);
        add(50);
        add(100);
        add(200);
        add(300);
        add(400);
        add(500);
        add(600);
        add(700);
        add(800);
        add(1000);
        add(2000);
        add(10000);
        add(20000);
        add(40000);
        for (int i = 100000; i <= 10000000; i += 100000) {
            add(i);
        }
    }};

    @Test
    @Ignore
    public void testManyLevels() {
        for (int level : levels) {
            long elapsedTime = testALevel(level);
            System.out.println("NbFlights   : " + (level * 5));
            System.out.println("ElapsedTime : " + TimeUnit.NANOSECONDS.toMillis(elapsedTime) + "ms");
            System.out.println();
            if (TimeUnit.NANOSECONDS.toSeconds(elapsedTime) > 10) {
                break;
            }
        }
    }
}
