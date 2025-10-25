import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientHandler implements Runnable {
    private final Socket socket;
    private final DataInputStream in;
    private final DataOutputStream out;
    private String username = "Guest";

    public ClientHandler(Socket socket) throws IOException {
        this.socket = socket;
        this.in = new DataInputStream(socket.getInputStream());
        this.out = new DataOutputStream(socket.getOutputStream());
    }

    public void run() {
        try {
            username = in.readUTF();
            out.writeUTF("Welcome to the chat, " + username + "!");

            // Send all previous messages from the LinkedList
            synchronized (Server.getMessages()) {
                for (Message m : Server.getMessages()) {
                    out.writeUTF(m.getTime() + " | " + m.getAuthor() + ": " + m.getMessage());
                }
            }

            while (true) {
                String msg = in.readUTF();
                if (msg.equalsIgnoreCase("quit()")) break;

                // Don't print or format here - just pass to server
                // The server will handle storing and broadcasting
                Server.broadcast(msg, this);
            }
        } catch (IOException e) {
            System.out.println("Client disconnected: " + getUsername());
        } finally {
            try {
                socket.close();
            } catch (IOException ignored) {
            }
            Server.removeClient(this);
        }
    }

    public void sendMessage(String msg) {
        try {
            out.writeUTF(msg);
            out.flush();
        } catch (IOException ignored) {
        }
    }

    public String getUsername() {
        return this.username;
    }
}