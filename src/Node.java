import java.util.List;

public class Node {

    private CommandServiceFactory commandServiceFactory;

    public Node(CommandServiceFactory commandServiceFactory) {
        this.commandServiceFactory = commandServiceFactory;
    }

    public String handleRequest(String inputLine) {
        if (".".equals(inputLine)) {
            return "BYE";
        }

        List<String> tokens = CommandParser.parse(inputLine);
        if (tokens.isEmpty()) {
            return "INVALID COMMAND";
        }

        final Command command = Command.getCommand(tokens.getFirst());
        final CommandService commandService = commandServiceFactory.getCommandService(command);
        if (commandService == null) {
            return "INVALID COMMAND";
        }

        if (!commandService.validate(tokens)) {
            return ("INCORRECT SYNTAX FOR " + tokens.getFirst());
        }

        return commandService.execute(tokens);
    }
}
