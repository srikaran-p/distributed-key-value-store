import java.util.List;

public class SetCommandService implements CommandService {

    private final StorageEngine storageEngine;

    public SetCommandService(StorageEngine storageEngine) {
        this.storageEngine = storageEngine;
    }

    @Override
    public String execute(List<String> tokens) {
        storageEngine.set(tokens.get(1), tokens.get(2));
        return "OK";
    }

    @Override
    public boolean validate(List<String> tokens) {
        return tokens.size() == 3;
    }
}
