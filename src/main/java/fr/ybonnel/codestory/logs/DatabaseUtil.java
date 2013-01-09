package fr.ybonnel.codestory.logs;

public class DatabaseUtil {

    private static boolean test = false;

    public static String getUrl() {
        if (test) {
            return "jdbc:h2:mem:codestory";
        }
        return "jdbc:h2:./codestory";
    }

    public static void goInTestMode() {
        test = true;
    }
}
