package fr.ybonnel.codestory.database;

public class DatabaseUtil {

    private static boolean test = false;

    public static String getUrl() {
        if (test) {
            return "jdbc:h2:mem:codestory;DB_CLOSE_DELAY=-1";
        }
        return "jdbc:h2:./codestory";
    }

    public static void goInTestMode() {
        test = true;
    }
}
