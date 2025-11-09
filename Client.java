import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) {
        Config config = new Config("server_info.dat");
        String host = config.getHost();
        int port = config.getPort();

        try (Socket socket = new Socket(host, port);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             Scanner sc = new Scanner(System.in)) {

            while (true) {
                System.out.print("Enter operation (e.g. ADD 10 20, or EXIT): ");
                String input = sc.nextLine();

                if (input.equalsIgnoreCase("EXIT")) {
                    System.out.println("👋 Client terminated.");
                    break;
                }

                out.println(input); // 서버에 전송
                String response = in.readLine(); // 서버 응답 수신

                // ✅ null 응답 대비
                if (response == null) {
                    System.out.println("⚠️ Server closed the connection or sent an empty response.");
                    break;
                }

                // ✅ 사람이 보기 좋게 출력
                if (response.contains("RESPONSE OK VALUE")) {
                    String[] parts = response.split(" ");
                    String value = parts[parts.length - 2];
                    System.out.println("Answer: " + value);
                } else if (response.contains("RESPONSE ERROR")) {
                    if (response.contains("DIV_ZERO"))
                        System.out.println("Error message: divided by zero");
                    else if (response.contains("INVALID_OP"))
                        System.out.println("Error message: invalid operation");
                    else if (response.contains("ARG_ERROR"))
                        System.out.println("Error message: too many arguments");
                    else if (response.contains("NUM_ERROR"))
                        System.out.println("Error message: invalid number");
                    else
                        System.out.println("Error message: unknown error");
                } else {
                    System.out.println("Error: Invalid response format.");
                }
            }

        } catch (IOException e) {
            System.err.println("❌ Connection error: " + e.getMessage());
        }
    }
}
