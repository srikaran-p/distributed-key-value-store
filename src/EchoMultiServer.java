import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

public class EchoMultiServer {

    private ServerSocket serverSocket;

    public void start(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        final StorageEngine memoryStorage = new PersistentStorage();
        final CommandServiceFactory commandServiceFactory = new CommandServiceFactory(memoryStorage);

        while (true) {
            new EchoClientHandler(serverSocket.accept(), commandServiceFactory).start();
        }
    }

    public void stop() throws IOException {
        serverSocket.close();
    }

    public static void main(String[] args) throws IOException {
        EchoMultiServer server = new EchoMultiServer();
        server.start(6666);
    }

    private static class EchoClientHandler extends Thread {
        private Socket clientSocket;
        private PrintWriter out;
        private BufferedReader in;
        private CommandServiceFactory commandServiceFactory;

        public EchoClientHandler(Socket socket, CommandServiceFactory commandServiceFactory) {
            this.clientSocket = socket;
            this.commandServiceFactory = commandServiceFactory;
        }

        public void run() {
            try {
                out = new PrintWriter(clientSocket.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    if (".".equals(inputLine)) {
                        out.println("BYE");
                        break;
                    }

                    List<String> tokens = CommandParser.parse(inputLine);
                    if (tokens.isEmpty()) {
                        out.println("Invalid command");
                        break;
                    }

                    final Command command = Command.getCommand(tokens.getFirst());
                    final CommandService commandService = commandServiceFactory.getCommandService(command);
                    if (commandService == null) {
                        out.println("INVALID COMMAND");
                        continue;
                    }

                    if (!commandService.validate(tokens)) {
                        out.println("INCORRECT SYNTAX FOR " + tokens.getFirst());
                        continue;
                    }

                    final String responseMessage = commandService.execute(tokens);
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
