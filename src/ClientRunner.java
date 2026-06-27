import java.io.IOException;
import java.util.Scanner;

public class ClientRunner {

    public static void main(String[] args) throws IOException {
        EchoClient client = new EchoClient();
        client.startConnection("127.0.0.1", 6666);

        Scanner scanner = new Scanner(System.in);

        System.out.println("Connected to server.");
        System.out.println("Enter commands (SET, GET, DEL, EXISTS, PING, etc.).");
        System.out.println("Type '.' to terminate the server connection.");

        while (true) {
            System.out.print("> ");
            String command = scanner.nextLine();

            String response = client.sendMessage(command);
            System.out.println("Server: " + response);

            if (command.equals(".")) {
                break;
            }
        }

        scanner.close();
        client.stopConnection();
    }
}
