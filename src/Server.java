import java.io.*;
import java.net.*;
import java.util.*;

public class Server {
    private static final int PORT = 5000;
    private static final Set<ClientHandler> clients = Collections.synchronizedSet(new HashSet<>());
    private static final List<Message> messages = Collections.synchronizedList(new LinkedList<>());

    public static void main(String[] args) throws InterruptedException {
        try (ServerSocket ss = new ServerSocket(PORT)) {
            System.out.println("Server started on port: " + PORT);

            while (true) {
                Socket socket = ss.accept();
                ClientHandler client = new ClientHandler(socket);
                clients.add(client);
                new Thread(client).start();
                System.out.println(client.getUsername() + " connected.");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void broadcast(String msg, ClientHandler sender) {
        messages.add(new Message(sender.getUsername(), msg));

        synchronized (clients) {
            for (ClientHandler c : clients) {
                if (c != sender) c.sendMessage(msg);
            }
        }
    }

    public static void removeClient(ClientHandler client) {
        clients.remove(client);
    }

    public static List<Message> getMessages () {
        return messages;
    }

    public static void sendMessage (Message message) {
        messages.add(message);
    }
}
