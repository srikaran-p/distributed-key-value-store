import java.util.List;

public class SaveCommandService implements CommandService {

    private StorageEngine storageEngine;

    public SaveCommandService(StorageEngine storageEngine) {
        this.storageEngine = storageEngine;
    }

    @Override
    public String execute(List<String> tokens) {
        storageEngine.snapshot();
        return "OK";
    }

    @Override
    public boolean validate(List<String> tokens) {
        if (tokens == null) {
            return false;
        }

        return tokens.size() == 1;
    }
}
