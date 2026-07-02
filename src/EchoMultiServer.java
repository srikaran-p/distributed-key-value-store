import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

public class EchoMultiServer {

    private ServerSocket serverSocket;
    private Node node;

    public void start(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        final StorageEngine memoryStorage = new PersistentStorage();
        final CommandServiceFactory commandServiceFactory = new CommandServiceFactory(memoryStorage);
        node = new Node(commandServiceFactory);

        while (true) {
            new EchoClientHandler(serverSocket.accept(), node).start();
        }
    }

    public void stop() throws IOException {
        serverSocket.close();
    }

    private static class EchoClientHandler extends Thread {
        private Socket clientSocket;
        private PrintWriter out;
        private BufferedReader in;
        private Node node;

        public EchoClientHandler(Socket socket, Node node) {
            this.clientSocket = socket;
            this.node = node;
        }

        public void run() {
            try {
                out = new PrintWriter(clientSocket.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    String responseMessage = node.handleRequest(inputLine);
                    out.println(responseMessage);
                }

                in.close();
                out.close();
                clientSocket.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
