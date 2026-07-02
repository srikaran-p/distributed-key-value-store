import java.io.IOException;

public class ServerRunner {

    public static void main(String[] args) throws IOException {
        EchoMultiServer server = new EchoMultiServer();
        server.start(6666);
    }
}
