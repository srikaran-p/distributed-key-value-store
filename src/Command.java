import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public enum Command {

    GET("GET"),
    SET("SET"),
    PING("PING"),
    DELETE("DEL"),
    EXISTS("EXISTS"),
    SAVE("SAVE");

    private static Map<String, Command> LABEL_TO_COMMAND;

    static {
        final Map<String, Command> labelToCommand = new HashMap<>();

        for (Command command : Command.values()) {
            labelToCommand.put(command.getCommandLabel(), command);
        }

        LABEL_TO_COMMAND = Collections.unmodifiableMap(labelToCommand);
    }

    private String commandLabel;

    Command(String commandLabel) {
        this.commandLabel = commandLabel;
    }

    public String getCommandLabel() {
        return commandLabel;
    }

    public static Command getCommand(String commandLabel) {
        if (!LABEL_TO_COMMAND.containsKey(commandLabel)) {
            return null;
        }

        return LABEL_TO_COMMAND.get(commandLabel);
    }
}
