import java.io.*;
import java.net.*;
import java.util.StringTokenizer;

/**
 * CalculatorHandler.java
 * - Runnable class for each client connection
 * - Parses arithmetic command and responds using ASCII-based protocol
 */
public class CalculatorHandler implements Runnable {
    private Socket socket;

    // ✅ Error code constants
    private static final String OK = "RESPONSE OK VALUE";
    private static final String ERROR_DIV_ZERO = "RESPONSE ERROR DIV_ZERO END";
    private static final String ERROR_INVALID_OP = "RESPONSE ERROR INVALID_OP END";
    private static final String ERROR_ARG = "RESPONSE ERROR ARG_ERROR END";
    private static final String ERROR_NUM = "RESPONSE ERROR NUM_ERROR END";

    public CalculatorHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try (
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        ) {
            String request;
            // ✅ 지속적으로 클라이언트 요청 처리
            while ((request = in.readLine()) != null) {
                System.out.println("📨 Received: " + request);

                String response = processRequest(request);
                out.println(response); // 반드시 응답 보내기
                System.out.println("📤 Sent: " + response);

                // 클라이언트가 EXIT 명령을 보낼 경우 연결 종료
                if (request.equalsIgnoreCase("EXIT")) break;
            }

        } catch (IOException e) {
            System.err.println("⚠️ I/O error: " + e.getMessage());
        } finally {
            try { socket.close(); } catch (IOException e) {}
        }
    }

    private String processRequest(String request) {
        if (request == null || request.isEmpty()) return "RESPONSE ERROR EMPTY_INPUT END";

        StringTokenizer st = new StringTokenizer(request, " ");
        if (!st.hasMoreTokens()) return ERROR_ARG;

        String cmd = st.nextToken().toUpperCase();

        try {
            double a = Double.parseDouble(st.nextToken());
            double b = Double.parseDouble(st.nextToken());
            return calculate(cmd, a, b);
        } catch (NumberFormatException e) {
            return ERROR_NUM;
        } catch (Exception e) {
            return ERROR_ARG;
        }
    }

    private String calculate(String cmd, double a, double b) {
        switch (cmd) {
            case "ADD": return OK + " " + (a + b) + " END";
            case "SUB": return OK + " " + (a - b) + " END";
            case "MUL": return OK + " " + (a * b) + " END";
            case "DIV":
                // ✅ 이 부분이 핵심 수정
                if (b == 0) {
                    return ERROR_DIV_ZERO;
                } else {
                    return OK + " " + (a / b) + " END";
                }
            default:
                return ERROR_INVALID_OP;
        }
    }
}
