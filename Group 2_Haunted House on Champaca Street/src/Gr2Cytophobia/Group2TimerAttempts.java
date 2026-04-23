package cs.goated;
import java.io.*;

public class Group2TimerAttempts {
    public static long startTime = 0;
    public static int attempts = 1; // Start at Attempt 1
    private static final String FILE_NAME = "fastest_time.txt";

    public static void start() {
        if (startTime == 0) startTime = System.currentTimeMillis();
    }

    public static void addAttempt() {
        attempts++;
    }

    public static long getElapsed() {
        if (startTime == 0) return 0;
        return (System.currentTimeMillis() - startTime) / 1000;
    }

    public static void saveBestTime(long newTime) {
        long currentBest = getLongBest();
        if (currentBest == 0 || newTime < currentBest) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
                writer.write(String.valueOf(newTime));
            } catch (IOException e) { e.printStackTrace(); }
        }
    }

    public static String getBestTimeDisplay() {
        long best = getLongBest();
        return (best == 0) ? "--" : best + "s";
    }

    private static long getLongBest() {
        File f = new File(FILE_NAME);
        if (!f.exists()) return 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(f))) {
            String line = reader.readLine();
            return (line != null) ? Long.parseLong(line.trim()) : 0;
        } catch (Exception e) { return 0; }
    }
}