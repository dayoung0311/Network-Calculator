import java.io.*;
import java.net.*;
import java.util.concurrent.*;

/**
 * Server.java
 * - Accepts client connections via TCP socket
 * - Uses ThreadPool to handle multiple clients concurrently
 * - Each client handled by CalculatorHandler (Runnable)
 */
public class Server {
    public static void main(String[] args) {
        int port = 1234;
        System.out.println("✅ Server started on port " + port);

        ExecutorService pool = Executors.newFixedThreadPool(5);

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("📡 New client: " + clientSocket.getInetAddress());
                pool.execute(new CalculatorHandler(clientSocket));
            }
        } catch (IOException e) {
            System.err.println("❌ Server error: " + e.getMessage());
        } finally {
            pool.shutdown();
        }
    }
}
