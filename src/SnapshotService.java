import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class SnapshotService {

    private static final Path SNAPSHOT_FILE = Path.of("./snapshot.txt");

    public void save(Map<String, String> store) throws IOException {
        Path parentDirectory = SNAPSHOT_FILE.toAbsolutePath().getParent();
        Files.createDirectories(parentDirectory);
        System.out.println(parentDirectory);
        Path tempFile = Files.createTempFile(parentDirectory, "snapshot-", ".tmp");

        try {
            try (FileChannel channel = FileChannel.open(tempFile, StandardOpenOption.WRITE);
                 BufferedWriter writer = new BufferedWriter(Channels.newWriter(channel, StandardCharsets.UTF_8))) {
                for (Map.Entry<String, String> entry : store.entrySet()) {
                    writer.write(entry.getKey());
                    writer.write(":");
                    writer.write(entry.getValue());
                    writer.newLine();
                }

                writer.flush();
                channel.force(true);
            }

            Files.move(tempFile, SNAPSHOT_FILE, StandardCopyOption.ATOMIC_MOVE, StandardCopyOption.REPLACE_EXISTING);

            try (FileChannel dirChannel = FileChannel.open(parentDirectory, StandardOpenOption.READ)) {
                dirChannel.force(true);
            } catch (IOException | UnsupportedOperationException ignored) {
                // Not supported in Windows
            }
        } catch (IOException e) {
            Files.deleteIfExists(tempFile);
            throw e;
        }
    }

    public Map<String, String> load() throws IOException {
        if (Files.notExists(SNAPSHOT_FILE)) {
            return Collections.emptyMap();
        }

        Map<String, String> snapshotStore = new HashMap<>();

        for (String line : Files.readAllLines(SNAPSHOT_FILE)) {
            String[] parts = line.split(":", 2);
            if (parts.length == 2) {
                snapshotStore.put(parts[0], parts[1]);
            }
        }

        return snapshotStore;
    }
}
