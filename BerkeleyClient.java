import java.io.*;
import java.net.*;

public class BerkeleyClient {
    private static final String SERVER_ADDRESS = "localhost";  // Server address
    private static final int PORT = 12345;  // Port to communicate with the server

    public static void main(String[] args) throws IOException {
        Socket socket = new Socket(SERVER_ADDRESS, PORT);
        DataOutputStream out = new DataOutputStream(socket.getOutputStream());
        DataInputStream in = new DataInputStream(socket.getInputStream());

        // Send the client's current time to the server
        long clientTime = System.currentTimeMillis();
        out.writeLong(clientTime);
        System.out.println("Client sent time to server: " + clientTime);

        // Receive the time offset from the server
        long timeOffset = in.readLong();
        System.out.println("Client received time offset from server: " + timeOffset);

        // Adjust the client's clock by the time offset
        long correctedTime = clientTime + timeOffset;
        System.out.println("Client's corrected time: " + correctedTime);

        socket.close();
    }
}
