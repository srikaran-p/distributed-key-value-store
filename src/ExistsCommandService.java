import java.util.List;

public class ExistsCommandService implements CommandService {

    private final StorageEngine storageEngine;

    public ExistsCommandService(StorageEngine storageEngine) {
        this.storageEngine = storageEngine;
    }

    @Override
    public String execute(List<String> tokens) {
        final boolean isExists = storageEngine.exists(tokens.get(1));
        return isExists ? "TRUE" : "FALSE";
    }

    @Override
    public boolean validate(List<String> tokens) {
        return tokens.size() == 2;
    }
}
