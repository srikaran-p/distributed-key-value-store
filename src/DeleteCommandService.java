import java.util.List;
import java.util.Objects;

public class DeleteCommandService implements CommandService {

    private final StorageEngine storageEngine;

    public DeleteCommandService(StorageEngine storageEngine) {
        this.storageEngine = storageEngine;
    }

    @Override
    public String execute(List<String> tokens) {
        final String value = storageEngine.delete(tokens.get(1));
        return Objects.requireNonNullElse(value, "KEY NOT PRESENT");
    }

    @Override
    public boolean validate(List<String> tokens) {
        return tokens.size() == 2;
    }
}
