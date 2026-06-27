import java.util.List;

public class GetCommandService implements CommandService {

    private final StorageEngine storageEngine;

    public GetCommandService(StorageEngine storageEngine) {
        this.storageEngine = storageEngine;
    }

    @Override
    public String execute(List<String> tokens) {
        return storageEngine.get(tokens.get(1));
    }

    @Override
    public boolean validate(List<String> tokens) {
        return tokens.size() == 2;
    }
}
