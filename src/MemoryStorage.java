import java.util.concurrent.ConcurrentHashMap;

public class MemoryStorage implements StorageEngine {

    private final ConcurrentHashMap<String, String> store;

    public MemoryStorage() {
        store = new ConcurrentHashMap<>();
    }

    @Override
    public void set(String key, String value) {
        store.put(key, value);
    }

    @Override
    public String get(String key) {
        return store.getOrDefault(key, null);
    }

    @Override
    public String delete(String key) {
        return store.remove(key);
    }

    @Override
    public boolean exists(String key) {
        return store.containsKey(key);
    }

    @Override
    public void snapshot() {
        throw new UnsupportedOperationException("Cannot take snapshot for Memory Storages");
    }
}
