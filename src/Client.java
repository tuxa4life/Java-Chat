import java.io.*;
import java.net.*;

public class Client {
    public static void main(String[] args) {
        try (Socket socket = new Socket("127.0.0.1", 5000)) {
            DataInputStream in = new DataInputStream(socket.getInputStream());
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

            // Thread to receive messages from server
            new Thread(() -> {
                try {
                    while (true) {
                        String msg = in.readUTF();
                        System.out.println(msg);
                    }
                } catch (IOException e) {
                    System.out.println("Disconnected from server.");
                }
            }).start();

            System.out.print("Enter username: ");
            String username = reader.readLine();
            out.writeUTF(username);
            out.flush();

            String text;
            while ((text = reader.readLine()) != null) {
                if (text.equals("quit()")) break;

                out.writeUTF(text);
                out.flush();
            }

            socket.close();
            System.out.println("Client closed.");
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}