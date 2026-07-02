import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class WriteAheadLog {

    private static final Path LOG_FILE = Path.of("./database.txt");

    public void append(LogEntry entry) throws IOException {
        if (entry == null || entry.getCommand() == null) {
            return;
        }

        final String command = entry.getCommand().getCommandLabel();
        final String key = entry.getKey();
        final String value = entry.getValue();
        if (value == null) {
            Files.writeString(LOG_FILE, command + " " + key + System.lineSeparator(), StandardOpenOption.APPEND);
        } else {
            Files.writeString(LOG_FILE, command + " " + key + " " + value + System.lineSeparator(), StandardOpenOption.APPEND);
        }
    }

    public void clear() throws IOException {
        Files.newBufferedWriter(LOG_FILE).close();
    }

    public List<LogEntry> loadEntries() throws IOException {
        if (Files.notExists(LOG_FILE)) {
            Files.createFile(LOG_FILE);
            return Collections.emptyList();
        }

        List<LogEntry> entries = new ArrayList<>();

        try (BufferedReader reader = Files.newBufferedReader(LOG_FILE)) {
            String line;

            while ((line = reader.readLine()) != null) {
                LogEntry entry = replay(line);
                if (entry != null) {
                    entries.add(entry);
                }
            }
        }

        return entries;
    }

    private LogEntry replay(String line) {
        if (line == null) {
            return null;
        }

        List<String> tokens = CommandParser.parse(line);
        if (tokens.isEmpty()) {
            return null;
        }

        String commandToken = tokens.getFirst();
        final Command command = Command.getCommand(commandToken);
        if (command == null) {
            return null;
        }

        return switch (command) {
            case Command.SET -> {
                if (tokens.size() != 3) {
                    yield null;
                }
                yield new LogEntry(command, tokens.get(1), tokens.get(2));
            }
            case Command.DELETE -> {
                if (tokens.size() != 2) {
                    yield null;
                }
                yield new LogEntry(command, tokens.get(1));
            }
            default -> null;
        };
    }
}
