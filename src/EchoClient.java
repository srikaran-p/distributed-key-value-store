import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class EchoClient {

    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;

    public void startConnection(String ip, int port) throws IOException {
        clientSocket = new Socket(ip, port);
        out = new PrintWriter(clientSocket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
    }

    public String sendMessage(String message) throws IOException {
        out.println(message);

        return in.readLine();
    }

    public void stopConnection() throws IOException {
        in.close();
        out.close();
        clientSocket.close();
    }

    public static void main(String[] args) throws IOException {
        EchoClient client = new EchoClient();
        client.startConnection("127.0.0.1", 6666);

        int index = 0;
        while (index++ < 3) {
            String response = client.sendMessage("SET index " + index);
            System.out.println(response);

            response = client.sendMessage("GET index");
            System.out.println(response);
        }

        String pingMessage = client.sendMessage("PING");
        System.out.println(pingMessage);

        String existsMessage = client.sendMessage("EXISTS index");
        System.out.println(existsMessage);

        String deleteMessage = client.sendMessage("DEL index");
        System.out.println(deleteMessage);

        deleteMessage = client.sendMessage("DEL index");
        System.out.println(deleteMessage);

        String terminateMessage = client.sendMessage(".");
        System.out.println(terminateMessage);

        client.stopConnection();
    }
}
