import java.util.HashMap;
import java.util.Map;

public class CommandServiceFactory {

    private final Map<Command, CommandService> commandServiceMap;

    public CommandServiceFactory(StorageEngine storageEngine) {
        commandServiceMap = new HashMap<>();

        commandServiceMap.put(Command.GET, new GetCommandService(storageEngine));
        commandServiceMap.put(Command.SET, new SetCommandService(storageEngine));
        commandServiceMap.put(Command.DELETE, new DeleteCommandService(storageEngine));
        commandServiceMap.put(Command.EXISTS, new ExistsCommandService(storageEngine));
        commandServiceMap.put(Command.PING, new PingCommandService());
    }

    public CommandService getCommandService(Command command) {
        if (!commandServiceMap.containsKey(command)) {
            return null;
        }

        return commandServiceMap.get(command);
    }
}
