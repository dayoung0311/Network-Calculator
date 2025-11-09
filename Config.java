import java.io.*;
import java.util.*;

public class Config {
    private String host = "localhost";
    private int port = 1234;

    public Config(String filename) {
        try (Scanner sc = new Scanner(new File(filename))) {
            if (sc.hasNext()) host = sc.next();
            if (sc.hasNextInt()) port = sc.nextInt();
        } catch (FileNotFoundException e) {
            System.out.println("⚠️ Config file not found. Using default (localhost:1234)");
        }
    }

    public String getHost() { return host; }
    public int getPort() { return port; }
}
