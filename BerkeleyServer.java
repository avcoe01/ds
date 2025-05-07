import java.io.*;
import java.net.*;
import java.util.*;

public class BerkeleyServer {
    private static final int PORT = 12345;  // Port to listen for incoming requests
    private static final int NUM_CLIENTS = 3; // Number of clients in the system

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(PORT);
        System.out.println("Server started. Waiting for clients...");

        List<Socket> clientSockets = new ArrayList<>();
        List<Long> clientTimes = new ArrayList<>();

        // Accept client connections and collect their times
        for (int i = 0; i < NUM_CLIENTS; i++) {
            Socket clientSocket = serverSocket.accept();
            clientSockets.add(clientSocket);
            System.out.println("Client " + (i+1) + " connected.");
        }

        // Receive client times and calculate the average time offset
        long serverTime = System.currentTimeMillis();
        for (Socket clientSocket : clientSockets) {
            DataInputStream in = new DataInputStream(clientSocket.getInputStream());
            try {
                long clientTime = in.readLong();
                clientTimes.add(clientTime);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // Calculate the average time difference
        long totalTime = 0;
        for (long clientTime : clientTimes) {
            totalTime += clientTime;
        }
        long averageTime = totalTime / clientTimes.size();
        long timeOffset = serverTime - averageTime;

        // Send the time offset to each client
        for (int i = 0; i < NUM_CLIENTS; i++) {
            Socket clientSocket = clientSockets.get(i);
            DataOutputStream out = new DataOutputStream(clientSocket.getOutputStream());
            out.writeLong(timeOffset);  // Send time offset to client
            System.out.println("Sent time offset to Client " + (i+1) + ": " + timeOffset);
        }

        // Close client connections
        for (Socket clientSocket : clientSockets) {
            clientSocket.close();
        }

        serverSocket.close();
    }
}
